package cz.uruba.ets2mpcompanion.views;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.util.AttributeSet;

import java.util.Date;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.interfaces.views.AbstractAutoUpdatedTextView;

public class LastUpdatedTextView extends AbstractAutoUpdatedTextView {
    private String LAST_UPDATED;
    private String AGO;
    private String DAY;
    private String HOUR;
    private String MINUTE;
    private String JUST_NOW;

    private Date time;

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

    public void setTime(Date time) {
        this.time = time;
        restartAutoRefresh();
    }

    public Date getTime() {
        return time;
    }

    @Override
    public boolean startAutoRefresh() {
        return time != null && super.startAutoRefresh();
    }

    @Override
    protected long processAndDisplay() {
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
}