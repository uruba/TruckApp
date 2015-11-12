package cz.uruba.ets2mpcompanion.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.adapters.ServerListAdapter;
import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverFragment;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.tasks.FetchServerListTask;

public class ServerListFragment extends DataReceiverFragment<ArrayList<ServerInfo>> implements DataReceiverJSON<ArrayList<ServerInfo>> {
    @Bind(R.id.recyclerview_serverlist) RecyclerView serverList;
    @Bind(R.id.fab) FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serverlist, container, false);

        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchServerList(true);
            }
        });

        fetchServerList();

        return view;
    }

    private void fetchServerList() {
        fetchServerList(false);
    }

    private void fetchServerList(boolean notifyUser) {
        new FetchServerListTask(this, URL.SERVER_LIST, notifyUser).execute();
    }

    @Override
    public void processData(ArrayList<ServerInfo> serverList, boolean notifyUser) {
        if (serverList.isEmpty()) {
            return;
        }

        lastUpdated = new Date();

        Collections.sort(serverList, Collections.reverseOrder());

        ServerListAdapter serverListAdapter = new ServerListAdapter(serverList, this);
        this.serverList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        this.serverList.setAdapter(serverListAdapter);

        if (notifyUser) {
            Snackbar.make(this.serverList, this.getResources().getString(R.string.server_list_refreshed), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void handleIOException(IOException e) {
        Snackbar.make(this.serverList, this.getResources().getString(R.string.download_error_IOException), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void handleJSONException(JSONException e) {
        Snackbar.make(this.serverList, this.getResources().getString(R.string.json_error), Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
