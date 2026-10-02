package com.smartpantry.manager.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.smartpantry.manager.R;

// Settings tab - saves the user's choices with SharedPreferences
// so they are remembered after the app is closed
public class SettingsFragment extends Fragment {

    private SwitchCompat switchExpiryAlerts;
    private SwitchCompat switchDarkMode;
    private Spinner spinnerUnitPref;
    private TextView tvAbout;

    // Names used to save settings (public so other screens can read them)
    public static final String PREFS_NAME = "smart_pantry_prefs";
    public static final String KEY_EXPIRY_ALERTS = "expiry_alerts"; // highlight + pop-up for items close to expiry
    public static final String KEY_DARK_MODE = "dark_mode";         // high contrast dark mode on or off
    public static final String KEY_UNIT_PREF = "unit_pref";         // Metric or Imperial default unit

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        switchExpiryAlerts = view.findViewById(R.id.switch_expiry_alerts);
        switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        spinnerUnitPref = view.findViewById(R.id.spinner_unit_pref);
        tvAbout = view.findViewById(R.id.tv_about);

        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Expiry Alerts switch - load saved value and save when changed
        switchExpiryAlerts.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, true));
        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply();
        });

        // Dark Mode switch - saves the choice and changes the theme straight away
        switchDarkMode.setChecked(prefs.getBoolean(KEY_DARK_MODE, false));
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO);
        });

        // Unit preference dropdown
        String[] unitOptions = {"Metric", "Imperial"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, unitOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnitPref.setAdapter(adapter);

        // Select the saved option
        String savedUnit = prefs.getString(KEY_UNIT_PREF, "Metric");
        if ("Imperial".equals(savedUnit)) {
            spinnerUnitPref.setSelection(1);
        } else {
            spinnerUnitPref.setSelection(0);
        }

        // Unit preference isn't working yet, so the dropdown is greyed out (screen shows "Coming soon")
        spinnerUnitPref.setEnabled(false);

        // Save the new choice when the user picks one
        spinnerUnitPref.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putString(KEY_UNIT_PREF, unitOptions[position]).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }
}
