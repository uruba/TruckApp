package cz.uruba.ets2mpcompanion.fragments;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import cz.uruba.ets2mpcompanion.tasks.FetchServerTimeTask;

public class ServerListFragment extends DataReceiverFragment<ServerInfo, ServerListAdapter> implements DataReceiverJSON<ArrayList<ServerInfo>> {
    @Bind(R.id.recyclerview_serverlist) RecyclerView serverList;

    private String[] gameLiterals;

    private SharedPreferences sharedPref;
    public static final String PREF_GAME_FILTER_SETTING = "preference_game_filter_setting";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serverlist, container, false);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

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

        gameLiterals = getResources().getStringArray(R.array.game_names);

        fetchServerList();
        fetchServerTime();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game_filter, menu);

        menuItems.add(menu.findItem(R.id.action_game_filter));
        showMenuItems();
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
            private Date lastUpdated = new Date();

            @Override
            public void processData(Date data, boolean notifyUser) {
                listAdapter.setServerTime(data);
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void handleIOException(IOException e) {

            }

            @Override
            public void handleJSONException(JSONException e) {

            }

            @Override
            public Date getLastUpdated() {
                return lastUpdated;
            }
        }, URL.GAME_TIME, false).execute();
    }

    @Override
    public void processData(ArrayList<ServerInfo> serverList, boolean notifyUser) {
        dataSet = serverList;

        lastUpdated = new Date();

        Collections.sort(dataSet, Collections.reverseOrder());
        listAdapter.setDataCollection(new ArrayList<>(dataSet));

        filterByGame();

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

    private void showFilterDialog() {
        String[] choices = new String[gameLiterals.length + 1];

        int i = 0;

        choices[i++] = getString(R.string.games_filtering_all);

        for (String gameLiteral : gameLiterals) {
            choices[i++] = String.format(getString(R.string.games_filtering_entry), gameLiteral);
        }

        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.filter_servers))
                .setSingleChoiceItems(
                        choices,
                        sharedPref.getInt(PREF_GAME_FILTER_SETTING, 0),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPref.edit().putInt(PREF_GAME_FILTER_SETTING, which).apply();
                                filterByGame();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    private void filterByGame() {

    }
}