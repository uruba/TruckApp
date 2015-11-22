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

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
import cz.uruba.ets2mpcompanion.tasks.FetchJsoupDataTask;

public class MeetupListFragment extends DataReceiverFragment<Document> implements SearchView.OnQueryTextListener {
    @Bind(R.id.recyclerview_meetuplist) RecyclerView meetupList;


    List<MeetupInfo> meetups = new ArrayList<>();
    MeetupListAdapter meetupListAdapter;

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

        fetchMeetupList();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_meetup_search, menu);

        MenuItem item = menu.findItem(R.id.action_meetup_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
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
                    meetupListAdapter.refreshAdapter(meetups);
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

        new FetchJsoupDataTask(this, URL.MEETUP_LIST, notifyUser).execute();
    }

    public void resetMeetupList() {
        if (meetupListAdapter != null) {
            meetupListAdapter.refreshAdapter(meetups);
        }
    }

    @Override
    public void processData(Document data, boolean notifyUser) {
        if (data == null) {
            return;
        }

        meetups = new ArrayList<>();

        Elements elem_table_list = data.select(".table_list .row");
        elem_table_list.remove(0);

        for (Element elem : elem_table_list) {
            Elements elem_data = elem.children();
            int iterCount = 0;
            String time, location, organiser, language, participants;

            time = location = organiser = language = participants = "";

            for (Element data_field : elem_data) {
                iterCount++;

                String elemContent = data_field.text();
                switch (iterCount) {
                    case 1:
                        time = elemContent;
                        break;
                    case 2:
                        location = elemContent;
                        break;
                    case 3:
                        organiser = elemContent;
                        break;
                    case 4:
                        language = elemContent;
                        break;
                    case 5:
                        participants = elemContent;
                        break;
                }
            }

            MeetupInfo meetupInfo = new MeetupInfo(time, location, organiser, language, participants);
            meetups.add(meetupInfo);
        }

        lastUpdated = new Date();

        meetupListAdapter = new MeetupListAdapter(meetups, this);
        this.meetupList.setAdapter(meetupListAdapter);

        hideLoadingOverlay();

        if (notifyUser) {
            Snackbar.make(fragmentWrapper, this.getResources().getString(R.string.meetup_list_refreshed), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void handleIOException(IOException e) {
        hideLoadingOverlay();

        Snackbar.make(fragmentWrapper, this.getResources().getString(R.string.download_error_IOException), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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

        meetupListAdapter.refreshAdapter(filteredMeetups);
        meetupList.scrollToPosition(0);

        return true;
    }
}
