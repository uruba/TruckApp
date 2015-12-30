package cz.uruba.ets2mpcompanion.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.interfaces.AutoUpdatedTextView;
import cz.uruba.ets2mpcompanion.model.ServerTime;

public class ServerTimeTextView extends AutoUpdatedTextView {
    // hard-coded interval of 10 seconds = 10000 ms
    private static final long UPDATE_INTERVAL = 10000;

    private ServerTime serverTime;
    private Context context;

    private DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());

    public ServerTimeTextView(Context context) {
        this(context, null);
    }

    public ServerTimeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, Resources.getSystem().getIdentifier("textViewStyle", "attr", "android"));
    }

    public ServerTimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContext(context);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public boolean startAutoRefresh() {
        return serverTime != null && super.startAutoRefresh();
    }

    @Override
    protected long processAndDisplay() {
        Date serverTimeAtRefresh = serverTime.getServerTime().first;
        Date realTimeAtRefresh = serverTime.getServerTime().second;

        long computedServerTime = serverTimeAtRefresh.getTime() + ((System.currentTimeMillis() - realTimeAtRefresh.getTime()) / (10 * 1000)) * 60 * 1000;

        setText(String.format(
                context.getString(R.string.server_time),
                dateFormat.format(new Date(computedServerTime))
                )
        );

        return UPDATE_INTERVAL;
    }

    public void setServerTime(ServerTime serverTime) {
        this.serverTime = serverTime;
        restartAutoRefresh();
    }

    private void setContext(Context context) {
        this.context = context;
    }
}
