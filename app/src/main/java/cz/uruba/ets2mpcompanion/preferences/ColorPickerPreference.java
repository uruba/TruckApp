package cz.uruba.ets2mpcompanion.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import cz.uruba.ets2mpcompanion.R;

public class ColorPickerPreference extends DialogPreference {

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_settings_themecolour);
    }

}
