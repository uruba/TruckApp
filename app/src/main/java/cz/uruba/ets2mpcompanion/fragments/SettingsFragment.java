package cz.uruba.ets2mpcompanion.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.SettingsActivity;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String PREF_CUSTOM_THEME_ENABLED = "preference_custom_theme";
    public static final String PREF_THEME_COLOUR = "preference_theme_colour";
    public static final String PREF_WIDGET_TOAST_ENABLED = "preference_widget_toast";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        switch(key) {
            case PREF_CUSTOM_THEME_ENABLED:
            case PREF_THEME_COLOUR:
                SettingsActivity parentActivity = (SettingsActivity) getActivity();
                Intent intent = parentActivity.getIntent();
                parentActivity.finishWithoutExitAnimation();
                startActivity(intent);
        }
    }

}
