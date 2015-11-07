package cz.uruba.ets2mpcompanion.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.adapters.MeetupListAdapter;
import cz.uruba.ets2mpcompanion.interfaces.JsoupDataReceiver;
import cz.uruba.ets2mpcompanion.model.MeetupInfo;
import cz.uruba.ets2mpcompanion.tasks.FetchJsoupDataTask;

public class MeetupListFragment extends Fragment implements JsoupDataReceiver, SearchView.OnQueryTextListener {
    @Bind(R.id.recyclerview_meetuplist) RecyclerView meetupList;
    @Bind(R.id.fab) FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetuplist, container, false);

        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchMeetupList();
            }
        });

        fetchMeetupList();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_meetup_search, menu);

        MenuItem item = menu.findItem(R.id.action_meetup_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    private void fetchMeetupList() {
        new FetchJsoupDataTask(this, "http://ets2c.com/").execute();
    }

    @Override
    public void processData(Document data) {
        if (data == null) {
            return;
        }

        ArrayList<MeetupInfo> meetupList = new ArrayList<>();

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
            meetupList.add(meetupInfo);
        }

        MeetupListAdapter meetupListAdapter = new MeetupListAdapter(meetupList);
        this.meetupList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        this.meetupList.setAdapter(meetupListAdapter);
    }

    @Override
    public void handleIOException(IOException e) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
