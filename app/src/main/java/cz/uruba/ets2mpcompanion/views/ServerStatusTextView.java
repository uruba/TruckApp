package cz.uruba.ets2mpcompanion.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import cz.uruba.ets2mpcompanion.R;

public class ServerStatusTextView extends TextView {
    static final CharSequence TEXT_OFFLINE = "offline";
    static final CharSequence TEXT_ONLINE = "online";

    int textColourOnline = -1;

    public ServerStatusTextView(Context context) {
        super(context);
    }

    public ServerStatusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ServerStatusTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTextColourOnline(int colour) {
        textColourOnline = colour;
    }

    public void setStatus(boolean online) {
        if (online) {
            processAndSetText(TEXT_ONLINE, textColourOnline == -1 ? ContextCompat.getColor(getContext(), R.color.colorPrimaryDark) : textColourOnline);
        } else {
            processAndSetText(TEXT_OFFLINE, android.R.color.darker_gray);
        }
    }

    private void processAndSetText(CharSequence text, int colour) {
        setTextColor(colour);

        text = text.toString().toUpperCase();
        setText(text);
    }

}
