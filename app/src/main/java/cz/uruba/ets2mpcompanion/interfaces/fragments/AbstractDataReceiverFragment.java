package cz.uruba.ets2mpcompanion.interfaces.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import cz.uruba.ets2mpcompanion.ETS2MPCompanionApplication;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.constants.GoogleAnalytics;
import cz.uruba.ets2mpcompanion.fragments.SettingsFragment;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.interfaces.adapters.AbstractDataReceiverListAdapter;
import cz.uruba.ets2mpcompanion.model.general.DataSet;

public abstract class AbstractDataReceiverFragment<T extends Serializable, U extends AbstractDataReceiverListAdapter<T>> extends Fragment implements DataReceiverJSON<DataSet<T>> {
    protected DataSet<T> dataSet = new DataSet<>(new ArrayList<T>(), null);

    protected FABStateChangeListener fabStateChangeListener;
    @Bind(R.id.loading_overlay) protected FrameLayout loadingOverlay;
    @Bind(R.id.fab) protected FloatingActionButton fab;
    @Bind(R.id.fragment_wrapper) protected FrameLayout fragmentWrapper;
    @Bind(R.id.text_empty_list) protected TextView textEmptyList;

    protected Handler handler = new Handler();
    protected Runnable runTask = new Runnable() {
        @Override
        public void run() {
            DataSet<T> persistedDataSet = retrievePersistedDataSet();

            if (persistedDataSet == null
                    || persistedDataSet.getLastUpdated() == null) {
                fetchDataList(true);
                return;
            }

            long autoRefreshInterval = sharedPref.getLong(SettingsFragment.PREF_AUTO_REFRESH_INTERVAL, 0);
            long sinceLastRefresh = System.currentTimeMillis() - persistedDataSet.getLastUpdated().getTime();

            if (sinceLastRefresh >= autoRefreshInterval) {
                fetchDataList(true);
            } else {
                handler.postDelayed(this, autoRefreshInterval - sinceLastRefresh);
            }
        }
    };

    protected U listAdapter;

    protected List<MenuItem> menuItems = new ArrayList<>();

    protected SharedPreferences sharedPref;

    protected Tracker analyticsTracker;

    public AbstractDataReceiverFragment() {
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

        attachHandlers();

        if (listAdapter != null) {
            listAdapter.restartLastUpdatedTextView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        handler.removeCallbacks(runTask);
    }

    @Override
    public Date getLastUpdated() {
        return dataSet.getLastUpdated();
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
        if (listAdapter != null && dataSet.getCollection().size() == 0) {
            showEmptyView();
        }
        showMenuItems();
    }

    protected void showMenuItems() {
        if (dataSet.getCollection().size() > 0) {
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
        textEmptyList.setText(getString(R.string.data_not_loaded));
        textEmptyList.setVisibility(View.VISIBLE);
    }

    protected void hideEmptyView() {
        textEmptyList.setVisibility(View.GONE);
    }

    protected void submitOnRefreshAnalytics(String actionName) {
        analyticsTracker.send(new HitBuilders.EventBuilder()
                .setCategory(GoogleAnalytics.EVENT_CATEGORY_REFRESH)
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

    protected void attachHandlers(boolean noRestore) {
        handler.removeCallbacks(runTask);

        if (!noRestore) {
            restorePersistedDataSet();
        }

        long autoRefreshInterval = sharedPref.getLong(SettingsFragment.PREF_AUTO_REFRESH_INTERVAL, 0);

        if (sharedPref.getBoolean(SettingsFragment.PREF_AUTO_REFRESH_ENABLED, false) || autoRefreshInterval != 0) {
            handler.post(runTask);
        }
    }

    protected void attachHandlers() {
        attachHandlers(false);
    }

    protected DataSet<T> retrievePersistedDataSet() {
        try {
            FileInputStream fis = getContext().openFileInput(getClass().getName() + ".persisted");
            ObjectInputStream is = new ObjectInputStream(fis);
            Object readObject = is.readObject();
            is.close();

            if(readObject != null) {
                //noinspection unchecked
                return (DataSet<T>) readObject;
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
        }

        return dataSet;
    }

    protected void restorePersistedDataSet() {
        showLoadingOverlay();

        this.dataSet = retrievePersistedDataSet();

        if (!this.dataSet.getCollection().isEmpty()) {
            listAdapter.resetDataCollection(new ArrayList<>(dataSet.getCollection()));
            Snackbar.make(fragmentWrapper, R.string.persisted_data_retrieved, Snackbar.LENGTH_LONG);
        }

        hideLoadingOverlay();
    }

    protected void persistDataSet() {
        try {
            FileOutputStream fos = getContext().openFileOutput(getClass().getName() + ".persisted", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dataSet);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void fetchDataList(boolean notifyUser);

    protected void fetchDataList() {
        fetchDataList(false);
    }

    protected abstract void handleReceivedData(DataSet<T> data, boolean notifyUser);

    @Override
    public final void processData(DataSet<T> data, boolean notifyUser) {
        handleReceivedData(data, notifyUser);

        persistDataSet();

        attachHandlers(true);
    }

    @Override
    public void handleIOException(IOException e) {
        restorePersistedDataSet();

        hideLoadingOverlay();

        Snackbar.make(fragmentWrapper, this.getResources().getString(R.string.download_error_IOException), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void handleJSONException(JSONException e) {
        restorePersistedDataSet();

        hideLoadingOverlay();

        Snackbar.make(fragmentWrapper, this.getResources().getString(R.string.json_error), Snackbar.LENGTH_SHORT).show();
    }

    public U getListAdapter() {
        return listAdapter;
    }

    public int getDataSetSize() {
        return dataSet.getCollection().size();
    }
}
