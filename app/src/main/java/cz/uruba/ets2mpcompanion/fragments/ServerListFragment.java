package cz.uruba.ets2mpcompanion.fragments;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.adapters.ServerListAdapter;
import cz.uruba.ets2mpcompanion.constants.URL;
import cz.uruba.ets2mpcompanion.interfaces.fragments.AbstractDataReceiverFragment;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.model.general.DataSet;
import cz.uruba.ets2mpcompanion.tasks.FetchServerListTask;
import cz.uruba.ets2mpcompanion.tasks.FetchServerTimeTask;

public class ServerListFragment extends AbstractDataReceiverFragment<ServerInfo, ServerListAdapter> implements DataReceiverJSON<DataSet<ServerInfo>> {
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
                fetchDataList(true);
                fetchServerTime();
                submitOnRefreshAnalytics("Server list");
            }
        });

        serverList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        listAdapter = new ServerListAdapter(getContext(), new ArrayList<ServerInfo>(), this);
        serverList.setAdapter(listAdapter);

        gameLiterals = getResources().getStringArray(R.array.game_names);

        this.fetchDataList();
        fetchServerTime();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game_filter, menu);

        menuItems.add(menu.findItem(R.id.action_game_filter));
        showMenuItems();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_game_filter:
                showFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void fetchDataList(boolean notifyUser) {
        showLoadingOverlay();

        new FetchServerListTask(this, URL.SERVER_LIST, notifyUser).execute();
    }

    protected void fetchServerTime() {
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
    public void processData(DataSet<ServerInfo> serverList, boolean notifyUser) {
        dataSet = serverList;


        Collections.sort(dataSet.getCollection(), Collections.reverseOrder());
        listAdapter.resetDataCollection(new ArrayList<>(dataSet.getCollection()));

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
                .setTitle(getString(R.string.filter_games))
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
        filterByGame(dataSet.getCollection());
    }

    private void filterByGame(List<ServerInfo> inputServers) {
        int which = sharedPref.getInt(PREF_GAME_FILTER_SETTING, 0) - 1;

        String gameLiteral = which < 0 ? "" : gameLiterals[which];

        List<ServerInfo> filteredServers = new ArrayList<>();
        for (ServerInfo server : inputServers) {
            if (server.getGameName().contains(gameLiteral)) {
                filteredServers.add(server);
            }
        }

        listAdapter.setDataCollection(filteredServers);

        if (!TextUtils.isEmpty(gameLiteral)) {
            listAdapter.setFilteringMessage(
                    String.format(
                            getString(R.string.filtering_status),
                            gameLiteral
                    )
            );
        } else {
            listAdapter.setFilteringMessage();
        }

        serverList.scrollToPosition(0);
    }
}