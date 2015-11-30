package cz.uruba.ets2mpcompanion.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverListAdapter;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.utils.UICompat;
import cz.uruba.ets2mpcompanion.views.ServerStatusTextView;

public class ServerListAdapter extends DataReceiverListAdapter<List<ServerInfo>> {
    int colorPrimaryDark;
    ColorStateList tint;

    public ServerListAdapter(Context context, List<ServerInfo> dataCollection, DataReceiver<?> callbackDataReceiver) {
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
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DATA_ENTRY:
                View itemView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.cardview_serverinfo, parent, false);

                return new ServerInfoViewHolder(itemView, colorPrimaryDark);
        }

        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_DATA_ENTRY:
                ServerInfo serverInfo = dataCollection.get(position - 1);

                ServerInfoViewHolder serverInfoViewHolder = (ServerInfoViewHolder) holder;

                serverInfoViewHolder.serverStatus.setStatus(serverInfo.isOnline());

                serverInfoViewHolder.serverName.setText(serverInfo.getServerName());
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

    public static class ServerInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.server_name) TextView serverName;
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
