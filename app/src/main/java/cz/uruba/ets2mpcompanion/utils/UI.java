package cz.uruba.ets2mpcompanion.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;

import cz.uruba.ets2mpcompanion.R;

public class UI {

    public static void setOverscrollEffectColour(Context context) {
        UI.setOverscrollEffectColour(context, R.color.colorPrimary);
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
}
