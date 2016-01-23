package cz.uruba.ets2mpcompanion.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private List<MeetupInfo> filteredMeetups = new ArrayList<>();
    public static final int MEETUP_FIELD_LOCATION = 1;
    public static final int MEETUP_FIELD_ORGANISER = 1 << 1;
    public static final int MEETUP_FIELD_LANGUAGE = 1 << 2;
    private CharSequence[] serverLiterals;

    private List<MenuItem> menuItems = new ArrayList<>();

    private SharedPreferences sharedPref;
    public static final String PREF_SERVER_FILTER_SETTING = "preference_server_filter_setting";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetuplist, container, false);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

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

        serverLiterals = new CharSequence[] {
            getString(R.string.server_name_all),
            getString(R.string.server_name_eu1),
            getString(R.string.server_name_eu2),
            getString(R.string.server_name_eu3),
            getString(R.string.server_name_us1)
        };

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_meetup_filter:
                    showFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
            filterByServer();
        }
    }

    @Override
    public void processData(ArrayList<MeetupInfo> data, boolean notifyUser) {
        meetups = data;

        lastUpdated = new Date();

        listAdapter.setDataCollection(new ArrayList<>(meetups));

        filterByServer();

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
        filterByText(newText, MEETUP_FIELD_LOCATION | MEETUP_FIELD_ORGANISER | MEETUP_FIELD_LANGUAGE);

        return true;
    }

    private void showFilterDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.filter_servers))
                .setSingleChoiceItems(
                        serverLiterals,
                        sharedPref.getInt(PREF_SERVER_FILTER_SETTING, 0),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPref.edit().putInt(PREF_SERVER_FILTER_SETTING, which).apply();
                                filterByServer();
                                dialog.dismiss();
                                Toast.makeText(getContext(), serverLiterals[which], Toast.LENGTH_LONG).show();
                            }
                })
                .create()
                .show();
    }

    private void filterByText(String newText, int fieldsFlag) {
        if (meetups.size() < 1) {
            return;
        }

        newText = newText.toLowerCase();

        filteredMeetups = new ArrayList<>();
        for (MeetupInfo meetup : meetups) {

            List<String> fields = new ArrayList<>();
            if ((fieldsFlag & MEETUP_FIELD_LOCATION) == MEETUP_FIELD_LOCATION) {
                fields.add(meetup.getLocation().toLowerCase());
            }
            if ((fieldsFlag & MEETUP_FIELD_ORGANISER) == MEETUP_FIELD_ORGANISER) {
                fields.add(meetup.getOrganiser().toLowerCase());
            }
            if ((fieldsFlag & MEETUP_FIELD_LANGUAGE) == MEETUP_FIELD_LANGUAGE) {
                fields.add(meetup.getLanguage().toLowerCase());
            }

            for (String field : fields) {
                if (field.contains(newText)) {
                    filteredMeetups.add(meetup);
                    break;
                }
            }
        }

        listAdapter.refreshAdapter(filteredMeetups);
        meetupList.scrollToPosition(0);
    }

    private void filterByServer() {
        int which = sharedPref.getInt(PREF_SERVER_FILTER_SETTING, 0);
        filterByText(which == 0 ? "" : serverLiterals[which].toString(), MEETUP_FIELD_LOCATION);
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
