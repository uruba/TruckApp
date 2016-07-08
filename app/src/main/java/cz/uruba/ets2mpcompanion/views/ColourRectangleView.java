package cz.uruba.ets2mpcompanion.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

@SuppressLint("ViewConstructor")
public class ColourRectangleView extends View {
    private Paint paint;
    private Rect rectangle;

    public ColourRectangleView(Context context, int colour, int sideLength) {
        super(context);

        paint = new Paint();
        paint.setColor(colour);

        rectangle = new Rect(0, 0, sideLength, sideLength);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(rectangle, paint);
    }
}
