package cz.uruba.ets2mpcompanion.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.adapters.MeetupListAdapter;
import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverFragment;
import cz.uruba.ets2mpcompanion.model.MeetupInfo;
import cz.uruba.ets2mpcompanion.tasks.FetchMeetupListTask;

public class MeetupListFragment extends DataReceiverFragment<ArrayList<MeetupInfo>, MeetupListAdapter> implements SearchView.OnQueryTextListener {
    @Bind(R.id.recyclerview_meetuplist) RecyclerView meetupList;

    private List<MeetupInfo> meetups = new ArrayList<>();

    private List<MenuItem> menuItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetuplist, container, false);

        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchMeetupList(true);
            }
        });

        meetupList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        listAdapter = new MeetupListAdapter(getContext(), new ArrayList<MeetupInfo>(), this);
        meetupList.setAdapter(listAdapter);

        fetchMeetupList();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_meetup_search, menu);

        MenuItem menuSearchItem = menu.findItem(R.id.action_meetup_search);
        menuItems.add(menuSearchItem);
        menuItems.add(menu.findItem(R.id.action_meetup_filter));
        showMenuItems();

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuSearchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (meetups.size() > 0) {
                    listAdapter.refreshAdapter(meetups);
                }
                return false;
            }
        });
    }

    private void fetchMeetupList() {
        fetchMeetupList(false);
    }

    private void fetchMeetupList(boolean notifyUser) {
        showLoadingOverlay();

        new FetchMeetupListTask(this, URL.MEETUP_LIST, notifyUser).execute();
    }

    public void resetMeetupList() {
        if(listAdapter != null) {
            listAdapter.refreshAdapter(meetups);
        }
    }

    @Override
    public void processData(ArrayList<MeetupInfo> data, boolean notifyUser) {
        meetups = data;

        lastUpdated = new Date();

        listAdapter.setDataCollection(new ArrayList<>(meetups));

        hideLoadingOverlay();

        if (notifyUser) {
            Snackbar.make(fragmentWrapper, this.getResources().getString(R.string.meetup_list_refreshed), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleIOException(IOException e) {
        hideLoadingOverlay();

        Snackbar.make(fragmentWrapper, this.getResources().getString(R.string.download_error_IOException), Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void showLoadingOverlay() {
        hideMenuItems();
        super.showLoadingOverlay();
    }

    @Override
    protected void hideLoadingOverlay() {
        showMenuItems();
        super.hideLoadingOverlay();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    
    @Override
    public boolean onQueryTextChange(String newText) {
        if (meetups.size() < 1) {
            return true;
        }

        newText = newText.toLowerCase();

        List<MeetupInfo> filteredMeetups = new ArrayList<>();
        for (MeetupInfo meetup : meetups) {
            String [] fields = new String[3];

            fields[0] = meetup.getLocation().toLowerCase();
            fields[1] = meetup.getOrganiser().toLowerCase();
            fields[2] = meetup.getLanguage().toLowerCase();

            for (String field : fields) {
                if (field.contains(newText)) {
                    filteredMeetups.add(meetup);
                    break;
                }
            }
        }

        listAdapter.refreshAdapter(filteredMeetups);
        meetupList.scrollToPosition(0);

        return true;
    }

    private void showMenuItems() {
        if (meetups.size() > 0) {
            for (MenuItem menuItem : menuItems) {
                menuItem.setVisible(true);
            }
        }
    }

    private void hideMenuItems() {
        for (MenuItem menuItem : menuItems) {
            menuItem.setVisible(false);
        }
    }
}
