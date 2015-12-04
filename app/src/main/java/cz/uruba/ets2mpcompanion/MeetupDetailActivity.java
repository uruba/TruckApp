package cz.uruba.ets2mpcompanion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.interfaces.ThemedActivity;

public class MeetupDetailActivity extends ThemedActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.webview) WebView webView;

    public static final String INTENT_EXTRA_URL = "intentURL";

    private String meetupPageURL;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meetup_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException ignored) {
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            meetupPageURL = extras.getString(INTENT_EXTRA_URL);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (url.contains(URL.MEETUP_LIST)) {
                        if (url.equals(meetupPageURL)) {
                            Toast.makeText(getApplicationContext(), "The page is there", Toast.LENGTH_LONG).show();
                        } else if (url.equals(URL.MEETUP_LIST)) {
                            view.loadUrl(meetupPageURL);
                        }

                        view.loadUrl("javascript:$('#chat').toggleClass('hidden');$('.content, .form').width('100%');$('.content').css({'box-sizing': 'border-box', 'padding': '0 20px'});$('.form textarea, #chat').width('95%');$('.row').css('margin-bottom', '48px');$('.row label').css('float', 'none');$('.row small').css('margin-left', '0px');");
                    }
                }
            });
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webView.loadUrl(meetupPageURL);
        } else {
            finish();
        }
    }
}
