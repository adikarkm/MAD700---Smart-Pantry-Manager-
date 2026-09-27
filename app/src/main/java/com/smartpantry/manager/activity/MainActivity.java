package com.smartpantry.manager.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartpantry.manager.R;
import com.smartpantry.manager.fragment.PantryFragment;
import com.smartpantry.manager.fragment.SettingsFragment;
import com.smartpantry.manager.fragment.SuggestedRecipesFragment;

// Main screen - hosts the three tabs (Pantry, Recipes, Settings)
// and swaps between them using the bottom navigation bar.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        }
    }
}
