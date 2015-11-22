package cz.uruba.ets2mpcompanion.views;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Date;

import cz.uruba.ets2mpcompanion.R;

public class LastUpdatedTextView extends TextView {
    String LAST_UPDATED;
    String AGO;
    String DAY;
    String HOUR;
    String MINUTE;
    String JUST_NOW;

    Date time;

    Handler handler = new Handler();
    UpdateText updateTextTask = new UpdateText();
    boolean isUpdateTextTaskRunning = false;


    public LastUpdatedTextView(Context context) {
        this(context, null);
    }

    public LastUpdatedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, Resources.getSystem().getIdentifier("textViewStyle", "attr", "android"));
    }

    public LastUpdatedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initStrings(context);
    }

    private void initStrings(Context context) {
        LAST_UPDATED = context.getString(R.string.LastUpdatedTextView_LAST_UPDATED);
        AGO = context.getString(R.string.LastUpdatedTextView_AGO);
        DAY = context.getString(R.string.LastUpdatedTextView_DAY);
        HOUR = context.getString(R.string.LastUpdatedTextView_HOUR);
        MINUTE = context.getString(R.string.LastUpdatedTextView_MINUTE);
        JUST_NOW = context.getString(R.string.LastUpdatedTextView_JUST_NOW);
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

    public void setTime(Date time) {
        this.time = time;
        restartAutoRefresh();
    }

    public Date getTime() {
        return time;
    }

    public boolean startAutoRefresh() {
        if (!isUpdateTextTaskRunning && time != null) {
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

    private long processAndDisplayTime() {
        String builtString;

        long timeDifference = getTimeDifferenceFromNow();
        long updateInterval;

        updateInterval = DateUtils.MINUTE_IN_MILLIS;

        if (timeDifference > DateUtils.MINUTE_IN_MILLIS) {
            String timeUnit;
            long divisor;

            if (timeDifference > DateUtils.DAY_IN_MILLIS) {
                timeUnit = DAY;
                divisor = DateUtils.DAY_IN_MILLIS;
            } else if (timeDifference > DateUtils.HOUR_IN_MILLIS) {
                timeUnit = HOUR;
                divisor = DateUtils.HOUR_IN_MILLIS;
            } else {
                timeUnit = MINUTE;
                divisor = DateUtils.MINUTE_IN_MILLIS;
            }

            if (updateInterval != divisor) {
                updateInterval = divisor;
            }

            long timeInUnits = timeDivide(timeDifference, divisor);
            builtString = String.format(
                    AGO,
                    timeInUnits,
                    timeUnit + (timeInUnits > 1 ? "s" : "")
            );

        } else {
            builtString = JUST_NOW;
        }

        builtString = String.format(LAST_UPDATED, builtString);

        this.setText(builtString);

        return updateInterval;
    }

    private int timeDivide(long timeDifference, long divisor) {
        return (int) Math.floor(timeDifference / divisor);
    }

    private long getTimeDifferenceFromNow() {
        return Math.abs(System.currentTimeMillis() - time.getTime());
    }

    private class UpdateText implements Runnable {
        @Override
        public void run() {
            long newUpdateInterval = processAndDisplayTime();
            handler.postDelayed(this, newUpdateInterval);
        }
    }
}
