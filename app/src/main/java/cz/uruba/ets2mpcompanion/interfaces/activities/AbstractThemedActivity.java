package cz.uruba.ets2mpcompanion.interfaces.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.GoogleAnalytics;

import cz.uruba.ets2mpcompanion.constants.Themes;
import cz.uruba.ets2mpcompanion.fragments.SettingsFragment;

public abstract class AbstractThemedActivity extends AppCompatActivity {
    protected SharedPreferences sharedPref;
    protected boolean isCustomThemeEnabled = false;
    protected String prefThemeColour = "";

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(getApplicationContext()).reportActivityStart(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.contains(SettingsFragment.PREF_CUSTOM_THEME_ENABLED) && sharedPref.contains(SettingsFragment.PREF_THEME_COLOUR)) {
            isCustomThemeEnabled = sharedPref.getBoolean(SettingsFragment.PREF_CUSTOM_THEME_ENABLED, false);
            if (isCustomThemeEnabled) {
                prefThemeColour = sharedPref.getString(SettingsFragment.PREF_THEME_COLOUR, "");
                setTheme(Themes.getThemeStyle(prefThemeColour));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isCustomThemeEnabled;
        String prefThemeColour;

        if (sharedPref.contains(SettingsFragment.PREF_CUSTOM_THEME_ENABLED) && sharedPref.contains(SettingsFragment.PREF_THEME_COLOUR)) {
            isCustomThemeEnabled = sharedPref.getBoolean(SettingsFragment.PREF_CUSTOM_THEME_ENABLED, false);
            prefThemeColour = sharedPref.getString(SettingsFragment.PREF_THEME_COLOUR, "");

            if (isCustomThemeEnabled != this.isCustomThemeEnabled || (isCustomThemeEnabled && !prefThemeColour.equals(this.prefThemeColour))) {
                reloadActivity();
            }
        }
    }

    @Override
    protected void onStop() {
        GoogleAnalytics.getInstance(getApplicationContext()).reportActivityStop(this);
        super.onStop();
    }

    protected void reloadActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
