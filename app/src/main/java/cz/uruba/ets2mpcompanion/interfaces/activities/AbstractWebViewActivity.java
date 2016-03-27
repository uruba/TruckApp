package cz.uruba.ets2mpcompanion.interfaces.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;

public abstract class AbstractWebViewActivity extends AbstractThemedActivity {
    public static final String INTENT_EXTRA_URL = "intentURL";

    protected String targetURL;

    @Bind(R.id.toolbar) protected Toolbar toolbar;
    @Bind(R.id.loading_progress) protected ProgressBar loadingProgressIndicator;
    @Bind(R.id.webview) protected WebView webView;

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException ignored) {
        }

        initWebView();
    }

    protected void initWebView() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            targetURL = extras.getString(INTENT_EXTRA_URL);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    // show the "loading" spinner
                    loadingProgressIndicator.setVisibility(ProgressBar.VISIBLE);
                    AbstractWebViewActivity.this.onPageStarted(view, url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // hide the "loading" spinner
                    loadingProgressIndicator.setVisibility(ProgressBar.GONE);
                    AbstractWebViewActivity.this.onPageFinished(view, url);
                }
            });

            // we need to enable JavaScript in our WebView
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);


            webView.loadUrl(targetURL);
        } else {
            finish();
        }
    }

    protected abstract void onPageStarted(WebView view, String url);
    protected abstract void onPageFinished(WebView view, String url);

    @Override
    public void onBackPressed() {
        if (webView.canGoBack() && !webView.getUrl().equals(targetURL)){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}