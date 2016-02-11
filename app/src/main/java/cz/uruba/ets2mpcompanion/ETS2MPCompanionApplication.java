package cz.uruba.ets2mpcompanion;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class ETS2MPCompanionApplication extends Application {
    private Tracker analyticsTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        analyticsTracker = analytics.newTracker(R.xml.global_tracker);
        analyticsTracker.enableAutoActivityTracking(true);
    }

    synchronized public Tracker getAnalyticsTracker() {
        return analyticsTracker;
    }
}
