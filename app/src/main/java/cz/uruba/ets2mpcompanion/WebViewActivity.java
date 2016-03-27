package cz.uruba.ets2mpcompanion;

import android.os.Bundle;
import android.webkit.WebView;

import cz.uruba.ets2mpcompanion.interfaces.activities.AbstractWebViewActivity;

public class WebViewActivity extends AbstractWebViewActivity {
    public static final String INTENT_ACTIVITY_TITLE = "intentTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView.getSettings().setBuiltInZoomControls(true);

        String activityTitle = getIntent().getStringExtra(INTENT_ACTIVITY_TITLE);

        if (activityTitle != null) {
            setTitle(activityTitle);
        }
    }

    @Override
    protected void onPageStarted(WebView view, String url) {

    }

    @Override
    protected void onPageFinished(WebView view, String url) {

    }
}
