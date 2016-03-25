package cz.uruba.ets2mpcompanion.views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * Inspired by https://github.com/KasualBusiness/MaterialNumberPicker
 */
public class ThemedNumberPicker extends NumberPicker {

    public ThemedNumberPicker(Context context) {
        super(context);
        initView();
    }

    public ThemedNumberPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    /**
     * Init number picker by disabling focusability of edit text embedded inside the number picker
     * We also override the edit text filter private attribute by using reflection as the formatter is still buggy while attempting to display the default value
     * This is still an open Google @see <a href="https://code.google.com/p/android/issues/detail?id=35482#c9">issue</a> from 2012
     */
    private void initView() {
        setFocusability(false);

        try {
            Field f = NumberPicker.class.getDeclaredField("mInputText");
            f.setAccessible(true);
            EditText inputText = (EditText)f.get(this);
            inputText.setFilters(new InputFilter[0]);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uses reflection to access divider private attribute and override its color
     * Use Color.Transparent if you wish to hide them
     */
    public void setSeparatorColor(int separatorColor) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(this, new ColorDrawable(separatorColor));
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void setTypeFaceBold() {
        for (int i = 0; i < ThemedNumberPicker.this.getChildCount(); i++){
            View child = ThemedNumberPicker.this.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = NumberPicker.class.getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);

                    Typeface boldTypeFace = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                    ((Paint)selectorWheelPaintField.get(this)).setTypeface(boldTypeFace);

                    ((EditText) child).setTypeface(boldTypeFace);

                    ThemedNumberPicker.this.invalidate();
                    break;
                }
                catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setFocusability(boolean isFocusable) {
        setDescendantFocusability(isFocusable ? FOCUS_AFTER_DESCENDANTS : FOCUS_BLOCK_DESCENDANTS);
    }
}
