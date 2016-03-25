package cz.uruba.ets2mpcompanion.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.views.ThemedNumberPicker;

public class AutoRefreshIntervalPreference extends DialogPreference {
    @Bind(R.id.hour_picker) ThemedNumberPicker hourPicker;
    @Bind(R.id.minute_picker) ThemedNumberPicker minutePicker;
    private int intervalLengthMinutes;

    public AutoRefreshIntervalPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AutoRefreshIntervalPreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.dialogPreferenceStyle);
    }

    public int getIntervalLengthMinutes() {
        return intervalLengthMinutes;
    }

    public void setIntervalLengthMinutes(int intervalLengthMinutes) {
        this.intervalLengthMinutes = intervalLengthMinutes;
        persistInt(intervalLengthMinutes);
    }

    @Override
    protected View onCreateDialogView() {
        View view = View.inflate(getContext(), R.layout.dialog_auto_refresh_interval_preference, null);

        ButterKnife.bind(this, view);

        setDividerColor();

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setTypeFaceBold();

        return view;
    }

    private void setDividerColor() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        hourPicker.setSeparatorColor(color);
        minutePicker.setSeparatorColor(color);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, this);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        hourPicker.setValue(getIntervalLengthMinutes() / 60);
        minutePicker.setValue(getIntervalLengthMinutes() % 60);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            int value = hourPicker.getValue() * 60 + minutePicker.getValue();
            if (callChangeListener(value)) {
                setIntervalLengthMinutes(value);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, intervalLengthMinutes);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setIntervalLengthMinutes(restoreValue ? getPersistedInt(intervalLengthMinutes) : (int) defaultValue);
    }
}
