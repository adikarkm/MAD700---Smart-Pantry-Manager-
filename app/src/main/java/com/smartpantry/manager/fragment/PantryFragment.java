package com.smartpantry.manager.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartpantry.manager.R;
import com.smartpantry.manager.activity.AddEditIngredientActivity;
import com.smartpantry.manager.adapter.PantryAdapter;
import com.smartpantry.manager.database.AppDatabase;
import com.smartpantry.manager.model.PantryItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

// Pantry tab - shows all pantry items in a list and lets the user add, edit or delete them
public class PantryFragment extends Fragment implements PantryAdapter.OnItemClickListener {

    private RecyclerView rvPantry;
    private TextView tvEmptyPantry;
    private PantryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        rvPantry = view.findViewById(R.id.rv_pantry);
        tvEmptyPantry = view.findViewById(R.id.tv_empty_pantry);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add);

        // Set up the RecyclerView with an empty list for now
        rvPantry.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PantryAdapter(new ArrayList<>(), this);
        rvPantry.setAdapter(adapter);

        // + button opens the Add Ingredient screen
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEditIngredientActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // Reload every time the screen is shown, so new or edited items appear
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    // Loads the pantry from the database on a background thread,
    // then updates the list on the main (UI) thread
    private void loadData() {
        // Apply the Expiry Alerts setting
        if (getContext() != null) {
            SharedPreferences prefs = getContext().getSharedPreferences(SettingsFragment.PREFS_NAME, Context.MODE_PRIVATE);
            adapter.setShowExpiryWarnings(prefs.getBoolean(SettingsFragment.KEY_EXPIRY_ALERTS, true));
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            List<PantryItem> items = AppDatabase.getInstance(getContext()).pantryDao().getAllItems();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.updateData(items);
                    // Show a message when the pantry is empty
                    if (items.isEmpty()) {
                        tvEmptyPantry.setVisibility(View.VISIBLE);
                        rvPantry.setVisibility(View.GONE);
                    } else {
                        tvEmptyPantry.setVisibility(View.GONE);
                        rvPantry.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    // Tapping an item opens it in edit mode (item ID sent through the Intent)
    @Override
    public void onItemClick(PantryItem item) {
        Intent intent = new Intent(getContext(), AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(PantryItem item, int position) {
        showDeleteConfirmation(item);
    }

    @Override
    public void onDeleteClick(PantryItem item, int position) {
        showDeleteConfirmation(item);
    }

    // Asks the user to confirm before deleting an item
    private void showDeleteConfirmation(PantryItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete " + item.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        AppDatabase.getInstance(getContext()).pantryDao().delete(item);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(this::loadData);
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
