package cz.uruba.ets2mpcompanion.preferences;

import android.content.Context;
import android.util.AttributeSet;

public class NonEmptyEditTextPreference extends CustomEditTextPreference {

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
