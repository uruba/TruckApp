package cz.uruba.ets2mpcompanion.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.interfaces.HttpDataReceiver;
import cz.uruba.ets2mpcompanion.tasks.FetchHttpDataTask;

public class MeetupListFragment extends Fragment implements HttpDataReceiver {
    @Bind(R.id.recyclerview_meetuplist) RecyclerView meetupList;
    @Bind(R.id.fab) FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetuplist, container, false);

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

    private void fetchMeetupList() {
        new FetchHttpDataTask(this, "http://ets2c.com/").execute();
    }

    @Override
    public void processData(String data) {

    }

    @Override
    public void handleIOException(IOException e) {

    }
}
