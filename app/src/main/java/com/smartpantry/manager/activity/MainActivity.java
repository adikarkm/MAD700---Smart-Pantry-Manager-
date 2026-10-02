package com.smartpantry.manager.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartpantry.manager.R;
import com.smartpantry.manager.adapter.PantryAdapter;
import com.smartpantry.manager.database.AppDatabase;
import com.smartpantry.manager.fragment.PantryFragment;
import com.smartpantry.manager.fragment.SettingsFragment;
import com.smartpantry.manager.fragment.SuggestedRecipesFragment;
import com.smartpantry.manager.model.PantryItem;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

// Main screen - hosts the three tabs (Pantry, Recipes, Settings)
// and swaps between them using the bottom navigation bar.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences(SettingsFragment.PREFS_NAME, MODE_PRIVATE);

        // Apply the Dark Mode setting before the screen is drawn
        boolean darkMode = prefs.getBoolean(SettingsFragment.KEY_DARK_MODE, false);
        AppCompatDelegate.setDefaultNightMode(darkMode
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom navigation bar at the bottom of the screen
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // When a tab is tapped, show the matching fragment
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_pantry) {
                selectedFragment = new PantryFragment();
            } else if (itemId == R.id.nav_recipes) {
                selectedFragment = new SuggestedRecipesFragment();
            } else if (itemId == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });

        // Show the Pantry tab first when the app opens
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PantryFragment()).commit();
            bottomNav.setSelectedItemId(R.id.nav_pantry);

            // Expiry pop-up - only when Expiry Alerts is on in Settings.
            // savedInstanceState is only null on a fresh open, so the pop-up
            // does not show again when the screen is rebuilt (e.g. rotating the phone).
            if (prefs.getBoolean(SettingsFragment.KEY_EXPIRY_ALERTS, true)) {
                showExpiryPopup();
            }
        }
    }

    // Lists items that have expired or expire within 3 days in a pop-up.
    // If nothing is expiring, no pop-up is shown.
    private void showExpiryPopup() {
        // Database work runs on a background thread so the screen does not freeze
        Executors.newSingleThreadExecutor().execute(() -> {
            List<PantryItem> items = AppDatabase.getInstance(this).pantryDao().getAllItems();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            long now = System.currentTimeMillis();
            StringBuilder list = new StringBuilder();

            for (PantryItem item : items) {
                Long expiry = item.getExpiryDate();
                if (expiry == null) {
                    continue; // no expiry date set
                }
                String date = sdf.format(new Date(expiry));
                if (expiry < now) {
                    list.append(getString(R.string.expiry_popup_expired, item.getName(), date)).append("\n");
                } else if (expiry - now <= PantryAdapter.THREE_DAYS_MS) {
                    list.append(getString(R.string.expiry_popup_soon, item.getName(), date)).append("\n");
                }
            }

            if (list.length() == 0) {
                return; // nothing is expiring
            }

            // Pop-ups must be shown on the main (UI) thread
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) {
                    return; // the app was closed while we were checking
                }
                new AlertDialog.Builder(this)
                        .setTitle(R.string.expiry_popup_title)
                        .setMessage(list.toString().trim())
                        .setPositiveButton(R.string.ok, null)
                        .show();
            });
        });
    }
}
