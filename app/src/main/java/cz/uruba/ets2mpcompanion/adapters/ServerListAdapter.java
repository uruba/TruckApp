package cz.uruba.ets2mpcompanion.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.adapters.viewholders.LastUpdatedWithServerTimeViewHolder;
import cz.uruba.ets2mpcompanion.interfaces.fragments.AbstractDataReceiverFragment;
import cz.uruba.ets2mpcompanion.interfaces.adapters.AbstractDataReceiverListAdapter;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.model.ServerTime;
import cz.uruba.ets2mpcompanion.utils.UICompat;
import cz.uruba.ets2mpcompanion.views.LastUpdatedTextView;
import cz.uruba.ets2mpcompanion.views.ServerStatusTextView;

public class ServerListAdapter extends AbstractDataReceiverListAdapter<ServerInfo> implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String PREF_DISPLAY_SERVER_TIME = "preference_display_server_time";

    private int colorPrimaryDark;
    private ColorStateList tint;

    private ServerTime serverTime;

    private SharedPreferences sharedPref;

    public ServerListAdapter(Context context, List<ServerInfo> dataCollection, AbstractDataReceiverFragment<?, ?> callbackDataReceiver) {
        super(context, dataCollection, callbackDataReceiver);
        colorPrimaryDark = UICompat.getThemeColour(R.attr.colorPrimaryDark, context);
        tint = new ColorStateList(
                new int[][]{
                        new int[]{}
                },
                new int[] {
                        colorPrimaryDark
                }
        );

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case TYPE_LAST_UPDATED:
                itemView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.block_lastupdatedwithservertime, parent, false);

                lastUpdatedTextView = (LastUpdatedTextView) itemView.findViewById(R.id.last_updated);

                return new LastUpdatedWithServerTimeViewHolder(itemView);

            case TYPE_DATA_ENTRY:
                itemView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.cardview_serverinfo, parent, false);

                return new ServerInfoViewHolder(itemView, colorPrimaryDark);
        }

        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_LAST_UPDATED:
                LastUpdatedWithServerTimeViewHolder lastUpdatedWithServerTimeViewHolder = (LastUpdatedWithServerTimeViewHolder) holder;

                if (serverTime != null && sharedPref.getBoolean(PREF_DISPLAY_SERVER_TIME, true) && dataCollection.size() > 0) {
                    lastUpdatedWithServerTimeViewHolder.serverTime.setVisibility(View.VISIBLE);
                    lastUpdatedWithServerTimeViewHolder.serverTime.setServerTime(serverTime);
                    lastUpdatedWithServerTimeViewHolder.lastUpdated.setLayoutParams(
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                    );
                } else {
                    lastUpdatedWithServerTimeViewHolder.serverTime.setVisibility(View.GONE);
                    lastUpdatedWithServerTimeViewHolder.lastUpdated.setLayoutParams(
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                    );
                }

                if (!TextUtils.isEmpty(filteringMessage)) {
                    lastUpdatedWithServerTimeViewHolder.filteringStatus.setVisibility(View.VISIBLE);
                    lastUpdatedWithServerTimeViewHolder.filteringStatus.setText(filteringMessage);
                } else {
                    lastUpdatedWithServerTimeViewHolder.filteringStatus.setVisibility(View.GONE);
                }

                break;

            case TYPE_DATA_ENTRY:
                ServerInfo serverInfo = dataCollection.get(position - 1);

                ServerInfoViewHolder serverInfoViewHolder = (ServerInfoViewHolder) holder;

                serverInfoViewHolder.serverStatus.setStatus(serverInfo.isOnline());

                serverInfoViewHolder.serverName.setText(serverInfo.getServerName());
                serverInfoViewHolder.gameName.setText(String.format("(%1$s)", serverInfo.getGameName()));
                serverInfoViewHolder.numberOfPlayers.setText(serverInfo.getFormattedPlayerCountString(context));
                serverInfoViewHolder.numberOfPlayersProgressBar.setProgress((int) serverInfo.getPlayerCountRatio());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    serverInfoViewHolder.numberOfPlayersProgressBar.setProgressTintList(tint);
                } else {
                    UICompat.setProgressBarColour(context, serverInfoViewHolder.numberOfPlayersProgressBar);
                }

                break;
        }

        super.onBindViewHolder(holder, position);
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = new ServerTime(serverTime);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREF_DISPLAY_SERVER_TIME)) {
            notifyDataSetChanged();
        }
    }

    public static class ServerInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.server_name) TextView serverName;
        @Bind(R.id.game_name) TextView gameName;
        @Bind(R.id.number_of_players) TextView numberOfPlayers;
        @Bind(R.id.number_of_players_progressbar) ProgressBar numberOfPlayersProgressBar;
        @Bind(R.id.server_status) ServerStatusTextView serverStatus;

        public ServerInfoViewHolder(View itemView, int textColourOnline) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            serverStatus.setTextColourOnline(textColourOnline);
        }
    }
}
