package com.smartpantry.manager.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.smartpantry.manager.R;
import com.smartpantry.manager.database.AppDatabase;
import com.smartpantry.manager.fragment.SettingsFragment;
import com.smartpantry.manager.model.PantryItem;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

// Screen for adding a new pantry item or editing an existing one.
// If an item ID is passed in the Intent we are editing, otherwise adding.
public class AddEditIngredientActivity extends AppCompatActivity {
    // Key used to pass the item ID in the Intent
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private EditText etIngredientName;
    private EditText etQuantity;
    private Spinner spinnerUnit;
    private Button btnExpiryDate;
    private TextView tvExpiryDisplay;
    private Button btnSave;

    private Long selectedExpiryDate = null; // null means no expiry date
    private int itemId = -1;                // -1 means we are adding a new item

    // Units the user can pick from
    private final String[] units = {"pcs", "g", "kg", "ml", "L", "cups", "tbsp", "tsp"};
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        // Toolbar with a back arrow
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Link the views from the layout
        etIngredientName = findViewById(R.id.et_ingredient_name);
        etQuantity = findViewById(R.id.et_quantity);
        spinnerUnit = findViewById(R.id.spinner_unit);
        btnExpiryDate = findViewById(R.id.btn_expiry_date);
        tvExpiryDisplay = findViewById(R.id.tv_expiry_display);
        btnSave = findViewById(R.id.btn_save);

        // Fill the unit dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);

        // Check if an item ID was sent - if so, we are editing
        itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId > 0) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Ingredient");
            }
            loadItem();
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Add Ingredient");
            }
            selectDefaultUnit();
        }

        btnExpiryDate.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> saveItem());
    }

    // Picks a starting unit for new items based on the Metric/Imperial setting
    private void selectDefaultUnit() {
        SharedPreferences prefs = getSharedPreferences(SettingsFragment.PREFS_NAME, Context.MODE_PRIVATE);
        String unitPref = prefs.getString(SettingsFragment.KEY_UNIT_PREF, "Metric");
        String defaultUnit = "Imperial".equals(unitPref) ? "cups" : "g";

        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(defaultUnit)) {
                spinnerUnit.setSelection(i);
                break;
            }
        }
    }

    // Opens a calendar so the user can choose an expiry date
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (selectedExpiryDate != null) {
            calendar.setTimeInMillis(selectedExpiryDate);
        }

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            selectedExpiryDate = selected.getTimeInMillis();
            updateExpiryDisplay();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Shows the chosen expiry date on screen
    private void updateExpiryDisplay() {
        if (selectedExpiryDate != null) {
            tvExpiryDisplay.setText(dateFormat.format(new Date(selectedExpiryDate)));
        } else {
            tvExpiryDisplay.setText("No expiry set");
        }
    }

    // Loads the item from the database and fills in the form (edit mode).
    // Database work runs on a background thread so the screen does not freeze.
    private void loadItem() {
        Executors.newSingleThreadExecutor().execute(() -> {
            PantryItem item = AppDatabase.getInstance(this).pantryDao().getById(itemId);
            if (item != null) {
                runOnUiThread(() -> {
                    etIngredientName.setText(item.getName());
                    etQuantity.setText(String.valueOf(item.getQuantity()));
                    selectedExpiryDate = item.getExpiryDate();
                    updateExpiryDisplay();

                    // Select the saved unit in the dropdown
                    for (int i = 0; i < units.length; i++) {
                        if (units[i].equals(item.getUnit())) {
                            spinnerUnit.setSelection(i);
                            break;
                        }
                    }
                });
            }
        });
    }

    // Checks the input and saves the item to the database
    private void saveItem() {
        String name = etIngredientName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();

        // Validation: name must not be empty
        if (TextUtils.isEmpty(name)) {
            etIngredientName.setError("Ingredient name is required");
            etIngredientName.requestFocus();
            return;
        }

        // Validation: quantity must not be empty
        if (TextUtils.isEmpty(qtyStr)) {
            etQuantity.setError("Quantity is required");
            etQuantity.requestFocus();
            return;
        }

        // Validation: quantity must be a number bigger than 0
        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
            if (quantity <= 0) {
                etQuantity.setError("Quantity must be greater than 0");
                etQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError("Invalid quantity");
            etQuantity.requestFocus();
            return;
        }

        String unit = spinnerUnit.getSelectedItem().toString();

        Executors.newSingleThreadExecutor().execute(() -> {
            if (itemId > 0) {
                // Update the existing item
                PantryItem item = AppDatabase.getInstance(this).pantryDao().getById(itemId);
                if (item != null) {
                    item.setName(name);
                    item.setQuantity(quantity);
                    item.setUnit(unit);
                    item.setExpiryDate(selectedExpiryDate);
                    AppDatabase.getInstance(this).pantryDao().update(item);
                }
            } else {
                // Insert a new item
                PantryItem item = new PantryItem(name, quantity, unit, selectedExpiryDate);
                AppDatabase.getInstance(this).pantryDao().insert(item);
            }

            // Go back to the pantry list
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        });
    }

    // Back arrow in the toolbar closes this screen
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
