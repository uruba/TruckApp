package cz.uruba.ets2mpcompanion.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.utils.UICompat;
import cz.uruba.ets2mpcompanion.views.viewgroups.RowedLayout;

/** This class was adapted from the Android's internal EditTextPreference class
 *  You can browse it here: https://github.com/android/platform_frameworks_base/blob/master/core/java/android/preference/EditTextPreference.java
 */
public class FormattedEditTextPreference extends DialogPreference {
    private ArrayList<CharSequence> formatStrings;

    @Bind(R.id.edit_text) EditText editText;
    @Bind(R.id.container_insert_format_string_buttons) RowedLayout containerButtons;
    private String text;

    public FormattedEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.formated_edit_text_preference);
        try {
            CharSequence[] entries = typedArray.getTextArray(R.styleable.formated_edit_text_preference_android_entries);
            if (entries != null && entries.length > 0) {
                formatStrings = new ArrayList<>(Arrays.asList(entries));
            }
        } finally {
            typedArray.recycle();
        }
    }

    public FormattedEditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormattedEditTextPreference(Context context) {
        this(context, null);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        persistString(text);
    }

    @Override
    protected View onCreateDialogView() {
        View view = View.inflate(getContext(), R.layout.dialog_formatted_edit_text_preference, null);

        ButterKnife.bind(this, view);

        int themeColor = UICompat.getThemeColour(R.attr.colorPrimary, getContext());

        for (CharSequence formatString : formatStrings) {
            TextView textView = new TextView(getContext());
            textView.setText(formatString);

            GradientDrawable backgroundDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.bckg_rounded_corners);
            if (backgroundDrawable != null) {
                backgroundDrawable.setColor(themeColor);
            }

            textView.setTextColor(getContext().getResources().getColor(android.R.color.white));
            textView.setBackground(backgroundDrawable);

            containerButtons.addView(textView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        return view;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        editText.setText(getText());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            String value = editText.getText().toString().trim();
            if (callChangeListener(value)) {
                setText(value);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setText(restoreValue ? getPersistedString(text) : (String) defaultValue);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.text = getText();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setText(myState.text);
    }

    private static class SavedState extends BaseSavedState {
        String text;

        public SavedState(Parcel source) {
            super(source);
            text = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(text);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
