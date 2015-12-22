package cz.uruba.ets2mpcompanion.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import cz.uruba.ets2mpcompanion.tasks.FetchServerTimeTask;

public class ServerListFragment extends DataReceiverFragment<ArrayList<ServerInfo>, ServerListAdapter> implements DataReceiverJSON<ArrayList<ServerInfo>> {
    @Bind(R.id.recyclerview_serverlist) RecyclerView serverList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serverlist, container, false);

        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchServerList(true);
                fetchServerTime();
            }
        });

        serverList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        listAdapter = new ServerListAdapter(getContext(), new ArrayList<ServerInfo>(), this);
        serverList.setAdapter(listAdapter);

        fetchServerList();
        fetchServerTime();

        return view;
    }

    private void fetchServerList() {
        fetchServerList(false);
    }

    private void fetchServerList(boolean notifyUser) {
        showLoadingOverlay();

        new FetchServerListTask(this, URL.SERVER_LIST, notifyUser).execute();
    }

    private void fetchServerTime() {
        new FetchServerTimeTask(new DataReceiverJSON<Date>() {
            @Override
            public void processData(Date data, boolean notifyUser) {
                Toast.makeText(getContext(), data.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleIOException(IOException e) {

            }

            @Override
            public void handleJSONException(JSONException e) {

            }

            @Override
            public Date getLastUpdated() {
                return null;
            }
        }, URL.GAME_TIME, false).execute();
    }

    @Override
    public void processData(ArrayList<ServerInfo> serverList, boolean notifyUser) {
        if (serverList == null || serverList.isEmpty()) {
            handleIOException(null);
            return;
        }

        listAdapter.notifyDataSetChanged();

        lastUpdated = new Date();

        Collections.sort(serverList, Collections.reverseOrder());
        listAdapter.setDataCollection(serverList);

        hideLoadingOverlay();

        if (notifyUser) {
            Snackbar.make(fragmentWrapper, this.getResources().getString(R.string.server_list_refreshed), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void handleIOException(IOException e) {
        hideLoadingOverlay();

        Snackbar.make(fragmentWrapper, this.getResources().getString(R.string.download_error_IOException), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void handleJSONException(JSONException e) {
        hideLoadingOverlay();

        Snackbar.make(fragmentWrapper, this.getResources().getString(R.string.json_error), Snackbar.LENGTH_SHORT).show();
    }
}