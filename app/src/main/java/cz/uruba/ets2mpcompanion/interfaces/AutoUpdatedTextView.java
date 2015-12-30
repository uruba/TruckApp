package cz.uruba.ets2mpcompanion.interfaces;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public abstract class AutoUpdatedTextView extends TextView {
    protected Handler handler = new Handler();
    protected UpdateText updateTextTask = new UpdateText();
    protected boolean isUpdateTextTaskRunning = false;

    public AutoUpdatedTextView(Context context) {
        super(context);
    }

    public AutoUpdatedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoUpdatedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public boolean startAutoRefresh() {
        if (!isUpdateTextTaskRunning) {
            handler.post(updateTextTask);
            isUpdateTextTaskRunning = true;
            return true;
        }

        return false;
    }

    public boolean stopAutoRefresh() {
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

    protected class UpdateText implements Runnable {
        @Override
        public void run() {
            long newUpdateInterval = processAndDisplay();
            handler.postDelayed(this, newUpdateInterval);
        }
    }
}