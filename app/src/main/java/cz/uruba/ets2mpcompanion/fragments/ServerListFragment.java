package cz.uruba.ets2mpcompanion.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.adapters.ServerListAdapter;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.tasks.FetchServersInfoTask;

public class ServerListFragment extends Fragment {
    @Bind(R.id.recyclerview_serverlist) RecyclerView serverList;
    @Bind(R.id.fab) FloatingActionButton fab;


    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serverlist, container, false);

        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchServerList();
            }
        });

        fetchServerList();

        return view;
    }

    private void fetchServerList() {
        new FetchServersInfoTask(this).execute();
    }

    public void populateServerList(String jsonSource) {
        if (jsonSource == null) {
            Snackbar.make(this.serverList, this.getResources().getText(R.string.download_error), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            return;
        }

        ArrayList<ServerInfo> serverList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonSource);
            JSONArray responseArray = jsonObject.getJSONArray("response");

            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject item = responseArray.getJSONObject(i);

                String name = item.getString("name");
                int playerCountCurrent = item.getInt("players");
                int playerCountCapacity = item.getInt("maxplayers");

                ServerInfo serverInfo = new ServerInfo(name, playerCountCurrent, playerCountCapacity);
                serverList.add(serverInfo);
            }
        } catch(JSONException e) {
            Snackbar.make(this.serverList, this.getResources().getText(R.string.json_error), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        Collections.sort(serverList, Collections.reverseOrder());

        ServerListAdapter serverListAdapter = new ServerListAdapter(serverList);
        this.serverList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        this.serverList.setAdapter(serverListAdapter);
    }
}
