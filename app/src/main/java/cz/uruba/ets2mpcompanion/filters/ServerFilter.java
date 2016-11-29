package cz.uruba.ets2mpcompanion.filters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.constants.Preferences;
import cz.uruba.ets2mpcompanion.interfaces.filters.Filter;
import cz.uruba.ets2mpcompanion.interfaces.filters.FilterCallback;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.model.general.DataSet;

/** TODO: Test if this works **/
public class ServerFilter extends Filter<ServerInfo> {
    private final String[] gameLiterals;

    public ServerFilter(Context context, FilterCallback<ServerInfo> callback) {
        super(context, callback);

        gameLiterals = context.getResources().getStringArray(R.array.game_names);
    }

    public void showFilterDialog(final List<ServerInfo> inputServers) {
        String[] choices = new String[gameLiterals.length + 1];

        int i = 0;

        choices[i++] = context.getString(R.string.games_filtering_all);

        for (String gameLiteral : gameLiterals) {
            choices[i++] = String.format(context.getString(R.string.games_filtering_entry), gameLiteral);
        }

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.filter_games))
                .setSingleChoiceItems(
                        choices,
                        sharedPref.getInt(Preferences.PREF_GAME_FILTER_SETTING, 0),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPref.edit().putInt(Preferences.PREF_GAME_FILTER_SETTING, which).apply();
                                filterByGame(inputServers);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    public void filterByGame(List<ServerInfo> inputServers) {
        String gameLiteral = getCurrentGameLiteral();

        ArrayList<ServerInfo> filteredServers = new ArrayList<>();
        for (ServerInfo server : inputServers) {
            if (server.getGameName().contains(gameLiteral)) {
                filteredServers.add(server);
            }
        }

        callback.dataFiltered(new DataSet<>(filteredServers, new Date()));
    }

    public String getCurrentGameLiteral() {
        int which = sharedPref.getInt(Preferences.PREF_GAME_FILTER_SETTING, 0) - 1;

        return which < 0 ? "" : gameLiterals[which];
    }
}
