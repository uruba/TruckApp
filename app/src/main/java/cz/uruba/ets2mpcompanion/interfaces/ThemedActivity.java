package cz.uruba.ets2mpcompanion.interfaces;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import cz.uruba.ets2mpcompanion.constants.Themes;
import cz.uruba.ets2mpcompanion.fragments.SettingsFragment;

public abstract class ThemedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.contains(SettingsFragment.PREF_CUSTOM_THEME_ENABLED) && sharedPref.contains(SettingsFragment.PREF_THEME_COLOUR)) {
            if (sharedPref.getBoolean(SettingsFragment.PREF_CUSTOM_THEME_ENABLED, false)) {
                setTheme(Themes.getThemeStyle(sharedPref.getString(SettingsFragment.PREF_THEME_COLOUR, "")));
            }
        }
    }
}
