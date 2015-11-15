package cz.uruba.ets2mpcompanion.interfaces;

import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import java.util.Date;

import butterknife.Bind;
import cz.uruba.ets2mpcompanion.R;

public abstract class DataReceiverFragment<T> extends Fragment implements DataReceiver<T> {
    protected Date lastUpdated;
    @Bind(R.id.loading_overlay) protected FrameLayout loadingOverlay;
    @Bind(R.id.fab) protected FloatingActionButton fab;
    @Bind(R.id.fragment_wrapper) protected FrameLayout fragmentWrapper;

    public Date getLastUpdated() {
        return lastUpdated;
    }

    protected void showLoadingOverlay() {
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0f, 1f);
        fadeInAnimation.setDuration(200);
        loadingOverlay.setAnimation(fadeInAnimation);
        loadingOverlay.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingOverlay() {
        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1f, 0f);
        fadeOutAnimation.setDuration(200);
        loadingOverlay.setAnimation(fadeOutAnimation);
        loadingOverlay.setVisibility(View.GONE);
    }

    protected void hideLoadingOverlayOnMainLooper() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                hideLoadingOverlay();
            }
        });
    }
}
