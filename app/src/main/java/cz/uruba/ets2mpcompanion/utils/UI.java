package cz.uruba.ets2mpcompanion.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ProgressBar;

import cz.uruba.ets2mpcompanion.R;

public class UI {
    public static final int DEFAULT_COLOUR = R.color.colorPrimary;

    public static void setOverscrollEffectColour(Context context) {
        UI.setOverscrollEffectColour(context, UI.DEFAULT_COLOUR);
    }

    @SuppressWarnings("deprecation")
    public static void setOverscrollEffectColour(Context context, int colour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        Resources resources = context.getResources();
        int colourFromRes = resources.getColor(colour);

        int glowDrawableId = resources.getIdentifier("overscroll_glow", "drawable", "android");
        Drawable androidGlow = resources.getDrawable(glowDrawableId);
        assert androidGlow != null;
        androidGlow.setColorFilter(colourFromRes, PorterDuff.Mode.SRC_IN);

        int edgeDrawableId = resources.getIdentifier("overscroll_edge", "drawable", "android");
        Drawable androidEdge = resources.getDrawable(edgeDrawableId);
        assert androidEdge != null;
        androidEdge.setColorFilter(colourFromRes, PorterDuff.Mode.SRC_IN);
    }

    public static void setProgressBarColour(Context context, ProgressBar progressBar) {
        UI.setProgressBarColour(context, progressBar, UI.DEFAULT_COLOUR);
    }

    @SuppressWarnings("deprecation")
    public static void setProgressBarColour(Context context, ProgressBar progressBar, int colour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        progressBar.getProgressDrawable().setColorFilter(context.getResources().getColor(colour), PorterDuff.Mode.SRC_IN);
    }
}
