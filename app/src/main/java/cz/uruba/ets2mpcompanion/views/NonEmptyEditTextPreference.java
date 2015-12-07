package cz.uruba.ets2mpcompanion.views;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class NonEmptyEditTextPreference extends EditTextPreference {

    public NonEmptyEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NonEmptyEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonEmptyEditTextPreference(Context context) {
        super(context);
    }

    @Override
    public void setText(String text) {
        String newText = text.trim();

        if (!newText.isEmpty()) {
            super.setText(newText);
        }
    }
}
