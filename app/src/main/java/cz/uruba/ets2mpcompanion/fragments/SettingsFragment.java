package cz.uruba.ets2mpcompanion.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import java.util.Arrays;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.SettingsActivity;
import cz.uruba.ets2mpcompanion.preferences.AutoRefreshIntervalPreference;
import cz.uruba.ets2mpcompanion.preferences.ColourChooserPreference;
import cz.uruba.ets2mpcompanion.preferences.CustomEditTextPreference;
import cz.uruba.ets2mpcompanion.preferences.FormattedEditTextPreference;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String PREF_CUSTOM_THEME_ENABLED = "preference_custom_theme";
    public static final String PREF_THEME_COLOUR = "preference_theme_colour";
    public static final String PREF_WIDGET_TOAST_ENABLED = "preference_widget_toast";
    public static final String PREF_MEETUP_REMINDERS_DEFAULT_TITLE = "preference_meetup_reminders_default_title";
    public static final String PREF_MEETUP_REMINDERS_DEFAULT_DESCRIPTION = "preference_meetup_reminders_default_description";
    public static final String PREF_AUTO_REFRESH_INTERVAL = "preference_auto_refresh_interval";

    public static final String[] preferencesSummaryUpdatedFor =
            {
                PREF_THEME_COLOUR,
                PREF_MEETUP_REMINDERS_DEFAULT_TITLE,
                PREF_MEETUP_REMINDERS_DEFAULT_DESCRIPTION,
                PREF_AUTO_REFRESH_INTERVAL
            };

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

        for (String preferenceKey : preferencesSummaryUpdatedFor) {
            updateSummaryAsCurrentValue(findPreference(preferenceKey));
        }
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

        if (Arrays.asList(preferencesSummaryUpdatedFor).contains(key)) {
            updateSummaryAsCurrentValue(findPreference(key));
        }
    }

    private void updateSummaryAsCurrentValue(Preference preference) {
        String currentValue;

        if (preference instanceof EditTextPreference) {
            currentValue = ((EditTextPreference) preference).getText();
        } else if (preference instanceof ColourChooserPreference) {
            if(TextUtils.isEmpty(((ColourChooserPreference) preference).getValue())) {
                return;
            }

            currentValue = ((ColourChooserPreference) preference).getValueThemeColour();
        } else if (preference instanceof FormattedEditTextPreference) {
            currentValue = ((FormattedEditTextPreference) preference).getText();
        } else if (preference instanceof CustomEditTextPreference) {
            currentValue = ((CustomEditTextPreference) preference).getText();
        } else if (preference instanceof AutoRefreshIntervalPreference){
            long millisTotal = ((AutoRefreshIntervalPreference) preference).getIntervalLengthMillis();
            long minutesTotal = millisTotal / (60 * 1000);
            currentValue = minutesTotal > 0 ?
                    String.format(getString(R.string.settings_summary_auto_refresh_interval), minutesTotal / 60, minutesTotal % 60) :
                    getString(R.string.settings_summary_auto_refresh_interval_never);
        } else {
            return;
        }

        preference.setSummary(currentValue);
    }
}
