package cz.uruba.ets2mpcompanion.interfaces.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public abstract class AbstractAutoUpdatedTextView extends TextView {
    private final Handler handler = new Handler();
    private final UpdateText updateTextTask = new UpdateText();
    private boolean isUpdateTextTaskRunning = false;

    public AbstractAutoUpdatedTextView(Context context) {
        super(context);
    }

    public AbstractAutoUpdatedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractAutoUpdatedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoRefresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoRefresh();
    }

    protected boolean startAutoRefresh() {
        if (!isUpdateTextTaskRunning) {
            handler.post(updateTextTask);
            isUpdateTextTaskRunning = true;
            return true;
        }

        return false;
    }

    private boolean stopAutoRefresh() {
        if (isUpdateTextTaskRunning) {
            handler.removeCallbacks(updateTextTask);
            isUpdateTextTaskRunning = false;
            return true;
        }

        return false;
    }

    public boolean restartAutoRefresh() {
        stopAutoRefresh();
        return startAutoRefresh();
    }

    protected abstract long processAndDisplay();

    private class UpdateText implements Runnable {
        @Override
        public void run() {
            long newUpdateInterval = processAndDisplay();
            handler.postDelayed(this, newUpdateInterval);
        }
    }
}