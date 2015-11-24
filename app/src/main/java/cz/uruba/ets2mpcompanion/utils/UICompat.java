package cz.uruba.ets2mpcompanion.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.widget.ProgressBar;

import cz.uruba.ets2mpcompanion.R;

public class UICompat {
    public static final int DEFAULT_COLOUR = R.color.AppTheme_colorPrimary;

    public static int getThemeColour(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    public static void setOverscrollEffectColour(Context context) {
        UICompat.setOverscrollEffectColour(context, UICompat.getThemeColour(context, R.attr.colorPrimary));
    }

    @SuppressWarnings("deprecation")
    public static void setOverscrollEffectColour(Context context, int colour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        Resources resources = context.getResources();

        int glowDrawableId = resources.getIdentifier("overscroll_glow", "drawable", "android");
        Drawable androidGlow = resources.getDrawable(glowDrawableId);
        assert androidGlow != null;
        androidGlow.setColorFilter(colour, PorterDuff.Mode.SRC_IN);

        int edgeDrawableId = resources.getIdentifier("overscroll_edge", "drawable", "android");
        Drawable androidEdge = resources.getDrawable(edgeDrawableId);
        assert androidEdge != null;
        androidEdge.setColorFilter(colour, PorterDuff.Mode.SRC_IN);
    }

    public static void setProgressBarColour(Context context, ProgressBar progressBar) {
        UICompat.setProgressBarColour(progressBar, UICompat.getThemeColour(context, R.attr.colorPrimary));
    }

    @SuppressWarnings("deprecation")
    public static void setProgressBarColour(ProgressBar progressBar, int colour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        progressBar.getProgressDrawable().setColorFilter(colour, PorterDuff.Mode.SRC_IN);
    }
}
