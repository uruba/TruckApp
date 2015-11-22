package cz.uruba.ets2mpcompanion.interfaces;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Date;

import butterknife.Bind;
import cz.uruba.ets2mpcompanion.R;

public abstract class DataReceiverFragment<T, U extends DataReceiverListAdapter> extends Fragment implements DataReceiver<T> {
    protected Date lastUpdated;
    protected FABStateChangeListener fabStateChangeListener;
    @Bind(R.id.loading_overlay) protected FrameLayout loadingOverlay;
    @Bind(R.id.fab) protected FloatingActionButton fab;
    @Bind(R.id.fragment_wrapper) protected FrameLayout fragmentWrapper;
    @Bind(R.id.text_empty_list) protected TextView textEmptyList;

    protected U listAdapter;

    public DataReceiverFragment() {
        fabStateChangeListener = new FABStateChangeListener();
    }

    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    protected void showLoadingOverlay() {
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0f, 1f);
        fadeInAnimation.setDuration(200);
        loadingOverlay.setAnimation(fadeInAnimation);
        loadingOverlay.setVisibility(View.VISIBLE);
        fab.hide(fabStateChangeListener.loadingOverlayShown());
        hideEmptyView();
    }

    protected void hideLoadingOverlay() {
        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1f, 0f);
        fadeOutAnimation.setDuration(200);
        loadingOverlay.setAnimation(fadeOutAnimation);
        loadingOverlay.setVisibility(View.GONE);
        fab.show(fabStateChangeListener.loadingOverlayHidden());
        if (listAdapter.getDataCollectionSize() == 0) {
            showEmptyView();
        }
    }

    protected void showEmptyView() {
        textEmptyList.setVisibility(View.VISIBLE);
    }

    protected void hideEmptyView() {
        textEmptyList.setVisibility(View.GONE);
    }

    protected static class FABStateChangeListener extends FloatingActionButton.OnVisibilityChangedListener {
        private boolean isLoadingOverlayShown = false;

        @Override
        public void onShown(FloatingActionButton fab) {
            if(isLoadingOverlayShown) {
                fab.hide();
            }
        }

        @Override
        public void onHidden(FloatingActionButton fab) {
            if(!isLoadingOverlayShown) {
                fab.show();
            }
        }

        public FABStateChangeListener loadingOverlayShown() {
            isLoadingOverlayShown = true;
            return this;
        }

        public FABStateChangeListener loadingOverlayHidden() {
            isLoadingOverlayShown = false;
            return this;
        }
    }
}
