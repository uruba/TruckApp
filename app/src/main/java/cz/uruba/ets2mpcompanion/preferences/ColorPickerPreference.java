package cz.uruba.ets2mpcompanion.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;

import butterknife.ButterKnife;

public class ColorPickerPreference extends ListPreference {

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onBindDialogView(View view) {
        ButterKnife.bind(this, view);

        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

    }

}
