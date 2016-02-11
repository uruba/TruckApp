package cz.uruba.ets2mpcompanion.interfaces;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import cz.uruba.ets2mpcompanion.ETS2MPCompanionApplication;
import cz.uruba.ets2mpcompanion.R;

public abstract class DataReceiverFragment<T, U extends DataReceiverListAdapter> extends Fragment implements DataReceiver<ArrayList<T>> {
    protected List<T> dataSet = new ArrayList<>();

    protected Date lastUpdated;
    protected FABStateChangeListener fabStateChangeListener;
    @Bind(R.id.loading_overlay) protected FrameLayout loadingOverlay;
    @Bind(R.id.fab) protected FloatingActionButton fab;
    @Bind(R.id.fragment_wrapper) protected FrameLayout fragmentWrapper;
    @Bind(R.id.text_empty_list) protected TextView textEmptyList;

    protected U listAdapter;

    protected List<MenuItem> menuItems = new ArrayList<>();

    protected SharedPreferences sharedPref;

    protected Tracker analyticsTracker;

    public DataReceiverFragment() {
        fabStateChangeListener = new FABStateChangeListener();
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        analyticsTracker = ((ETS2MPCompanionApplication) getActivity().getApplication()).getAnalyticsTracker();

        setHasOptionsMenu(true);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (listAdapter != null) {
            listAdapter.restartLastUpdatedTextView();
        }
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
        hideMenuItems();
    }

    protected void hideLoadingOverlay() {
        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1f, 0f);
        fadeOutAnimation.setDuration(200);
        loadingOverlay.setAnimation(fadeOutAnimation);
        loadingOverlay.setVisibility(View.GONE);
        fab.show(fabStateChangeListener.loadingOverlayHidden());
        if (listAdapter != null && listAdapter.getDataCollectionSize() == 0) {
            showEmptyView();
        }
        showMenuItems();
    }

    protected void showMenuItems() {
        if (dataSet.size() > 0) {
            for (MenuItem menuItem : menuItems) {
                menuItem.setVisible(true);
            }
        }
    }

    protected void hideMenuItems() {
        for (MenuItem menuItem : menuItems) {
            menuItem.setVisible(false);
        }
    }

    protected void showEmptyView() {
        textEmptyList.setVisibility(View.VISIBLE);
    }

    protected void hideEmptyView() {
        textEmptyList.setVisibility(View.GONE);
    }

    protected void submitOnRefreshAnalytics(String actionName) {
        analyticsTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Refresh")
                .setAction(actionName)
                .build());
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

    @Override
    public abstract void processData(ArrayList<T> data, boolean notifyUser);

    @Override
    public abstract void handleIOException(IOException e);

    public U getListAdapter() {
        return listAdapter;
    }
}
