package cz.uruba.ets2mpcompanion.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.model.ServerTime;

public class ServerTimeTextView extends TextView {
    private ServerTime serverTime;
    private Context context;

    public ServerTimeTextView(Context context) {
        super(context);
        setContext(context);
    }

    public ServerTimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setContext(context);
    }

    public ServerTimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContext(context);
    }

    public void setServerTime(ServerTime serverTime) {
        this.serverTime = serverTime;

        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());

        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        setText(String.format(
                context.getString(R.string.server_time),
                dateFormat.format(serverTime.getServerTime().first)));
    }

    private void setContext(Context context) {
        this.context = context;
    }
}
