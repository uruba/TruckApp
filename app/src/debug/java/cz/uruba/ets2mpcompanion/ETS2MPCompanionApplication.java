package cz.uruba.ets2mpcompanion;

import com.google.android.gms.analytics.GoogleAnalytics;

import cz.uruba.ets2mpcompanion.interfaces.AbstractETS2MPCompanionApplication;

public class ETS2MPCompanionApplication extends AbstractETS2MPCompanionApplication {

    @Override
    public void initAnalytics(GoogleAnalytics analytics) {
        analytics.setDryRun(true);
    }
}
