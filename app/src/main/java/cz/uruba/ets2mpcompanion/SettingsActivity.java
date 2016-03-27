package cz.uruba.ets2mpcompanion;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.fragments.SettingsFragment;
import cz.uruba.ets2mpcompanion.interfaces.activities.AbstractThemedActivity;

public class SettingsActivity extends AbstractThemedActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;
    // implicitly, we want to show an exit animation
    boolean animateExit = true;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException ignored) {
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, new SettingsFragment())
                .commit();
    }

    @Override
    public void onPause() {
        super.onPause();

        // check if the flag indicates that we should not animate the activity exit
        if (!animateExit) {
            animateExit = true;
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void finishWithoutExitAnimation() {
        animateExit = false;
        finish();
    }
}
