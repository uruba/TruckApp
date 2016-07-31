package cz.uruba.ets2mpcompanion.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
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
    private long intervalLengthMillis;

    public AutoRefreshIntervalPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AutoRefreshIntervalPreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.dialogPreferenceStyle);
    }

    public long getIntervalLengthMillis() {
        return intervalLengthMillis;
    }

    private void setIntervalLengthMillis(long intervalLengthMillis) {
        this.intervalLengthMillis = intervalLengthMillis;
        persistLong(intervalLengthMillis);
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

        hourPicker.setValue((int)(getIntervalLengthMillis() / (60 * 60 * 1000)));
        minutePicker.setValue((int)((getIntervalLengthMillis() % (60 * 60 * 1000)) / (60 * 1000)));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            int value = hourPicker.getValue() * (60 * 60 * 1000) + minutePicker.getValue() * (60 * 1000);
            if (callChangeListener(value)) {
                setIntervalLengthMillis(value);
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setIntervalLengthMillis(restoreValue ? getPersistedLong(intervalLengthMillis) : (long) defaultValue);
    }
}
