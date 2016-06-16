package cz.uruba.ets2mpcompanion.interfaces;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import cz.uruba.ets2mpcompanion.R;

public abstract class AbstractETS2MPCompanionApplication extends Application {
    private Tracker analyticsTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        analyticsTracker = analytics.newTracker(R.xml.global_tracker);
        analyticsTracker.enableAutoActivityTracking(true);
        initAnalytics(analytics);
    }

    protected abstract void initAnalytics(GoogleAnalytics analytics);

    synchronized public Tracker getAnalyticsTracker() {
        return analyticsTracker;
    }
}
