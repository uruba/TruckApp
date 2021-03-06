package cz.uruba.ets2mpcompanion.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.adapters.MeetupListAdapter;
import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.interfaces.fragments.AbstractDataReceiverFragment;
import cz.uruba.ets2mpcompanion.interfaces.model.MeetupInfo;
import cz.uruba.ets2mpcompanion.model.general.DataSet;
import cz.uruba.ets2mpcompanion.tasks.FetchMeetupListTask;

public class MeetupListFragment extends AbstractDataReceiverFragment<MeetupInfo, MeetupListAdapter> implements SearchView.OnQueryTextListener {
    @Bind(R.id.recyclerview_meetuplist) RecyclerView meetupList;

    private static final int MEETUP_FIELD_LOCATION = 1;
    private static final int MEETUP_FIELD_SERVER = 1 << 1;
    private static final int MEETUP_FIELD_ORGANISER = 1 << 2;
    private static final int MEETUP_FIELD_LANGUAGE = 1 << 3;
    private String[] serverLiterals;

    private static final String PREF_SERVER_FILTER_SETTING = "preference_server_filter_setting";

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetuplist, container, false);

        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataList(true);
                submitOnRefreshAnalytics("Meetup list");
            }
        });

        meetupList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        listAdapter = new MeetupListAdapter(getContext(), new ArrayList<MeetupInfo>(), this);
        meetupList.setAdapter(listAdapter);

        serverLiterals = getResources().getStringArray(R.array.server_names);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_meetup_search, menu);

        MenuItem menuSearchItem = menu.findItem(R.id.action_meetup_search);
        menuItems.add(menuSearchItem);
        menuItems.add(menu.findItem(R.id.action_meetup_filter));
        showMenuItems();

        searchView = (SearchView) MenuItemCompat.getActionView(menuSearchItem);
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
                resetMeetupList();
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

    @Override
    protected void fetchDataList(boolean notifyUser) {
        showLoadingOverlay();

        new FetchMeetupListTask(this, URL.MEETUP_LIST, notifyUser).execute();
    }

    public void resetMeetupList() {
        if(listAdapter != null) {
            listAdapter.setDataCollection(dataSet.getCollection());
            filterByServer(true);
        }
    }

    @Override
    public void handleReceivedData(DataSet<MeetupInfo> data, boolean notifyUser) {
        dataSet = data;

        listAdapter.setDataCollection(new ArrayList<>(dataSet.getCollection()));

        filterByServer();

        if (notifyUser) {
            Snackbar.make(fragmentWrapper, this.getResources().getString(R.string.meetup_list_refreshed), Snackbar.LENGTH_SHORT).show();
        }
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
        filterByText(newText, MEETUP_FIELD_LOCATION | MEETUP_FIELD_ORGANISER | MEETUP_FIELD_LANGUAGE, filterByServer());
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
                            }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    private List<MeetupInfo> filterByText(String newText) {
        return filterByText(newText, MEETUP_FIELD_LOCATION | MEETUP_FIELD_ORGANISER | MEETUP_FIELD_LANGUAGE);
    }

    private List<MeetupInfo> filterByText(String newText, int fieldsFlag) {
        return filterByText(newText, fieldsFlag, dataSet.getCollection());
    }

    private List<MeetupInfo> filterByText(String newText, int fieldsFlag, List<MeetupInfo> inputMeetups) {
        if (dataSet.getCollection().size() < 1) {
            return null;
        }

        newText = newText.toLowerCase();

        List<MeetupInfo> filteredMeetups = new ArrayList<>();
        for (MeetupInfo meetup : inputMeetups) {

            List<String> fields = new ArrayList<>();
            if ((fieldsFlag & MEETUP_FIELD_LOCATION) == MEETUP_FIELD_LOCATION) {
                fields.add(meetup.getLocation().toLowerCase());
            }
            if ((fieldsFlag & MEETUP_FIELD_SERVER) == MEETUP_FIELD_SERVER) {
                fields.add(meetup.getServer().toLowerCase().replaceAll("\\s+", ""));
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

        listAdapter.setDataCollection(filteredMeetups);
        meetupList.scrollToPosition(0);

        if (listAdapter.getDataCollectionSize() == 0) {
            textEmptyList.setText(getString(R.string.no_meetups_found));
            textEmptyList.setVisibility(View.VISIBLE);
        } else {
            textEmptyList.setVisibility(View.GONE);
        }

        return filteredMeetups;
    }

    private List<MeetupInfo> filterByServer() {
        return filterByServer(false);
    }

    private List<MeetupInfo> filterByServer(boolean ignoreQuery) {
        int which = sharedPref.getInt(PREF_SERVER_FILTER_SETTING, 0);

        String serverLiteral = which == 0 ? "" : serverLiterals[which];

        if (searchView != null && (!ignoreQuery && searchView.getQuery().length() > 0)) {
            filterByText(serverLiteral.replaceAll("\\s+",""), MEETUP_FIELD_SERVER, filterByText(searchView.getQuery().toString().replaceAll("\\s+","")));
        } else {
            filterByText(serverLiteral.replaceAll("\\s+",""), MEETUP_FIELD_SERVER);
        }

        if (!TextUtils.isEmpty(serverLiteral)) {
            listAdapter.setFilteringMessage(String.format(getString(R.string.filtering_status), serverLiteral));
        } else {
            listAdapter.setFilteringMessage();
        }

        return listAdapter.getDataCollection();
    }
}
