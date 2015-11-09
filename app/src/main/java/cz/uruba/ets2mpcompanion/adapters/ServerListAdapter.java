package cz.uruba.ets2mpcompanion.adapters;

import android.content.Context;
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
import cz.uruba.ets2mpcompanion.model.ServerInfo;

public class ServerListAdapter extends RecyclerView.Adapter<ServerListAdapter.ServerInfoViewHolder> {
    private Context context;

    private List<ServerInfo> serverList;

    public ServerListAdapter(List<ServerInfo> serverList) {
        this.serverList = serverList;
    }

    @Override
    public ServerInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View itemView = LayoutInflater
                .from(context)
                .inflate(R.layout.cardview_serverinfo, parent, false);

        return new ServerInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ServerInfoViewHolder holder, int position) {
        ServerInfo serverInfo = serverList.get(position);

        int playerCountCurrent = serverInfo.getPlayerCountCurrent();
        int playerCountCapacity = serverInfo.getPlayerCountCapacity();

        holder.serverName.setText(serverInfo.getServerName());
        holder.numberOfPlayers.setText(
                String.format(
                        context
                                .getResources()
                                .getString(R.string.player_count),
                        playerCountCurrent,
                        playerCountCapacity
                )
        );

        int playerCountRatio = (int) (((float) playerCountCurrent / (float) playerCountCapacity) * 100);
        holder.numberOfPlayersProgressBar.setProgress(playerCountRatio);
    }

    @Override
    public int getItemCount() {
        return serverList.size();
    }

    public static class ServerInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.server_name) TextView serverName;
        @Bind(R.id.number_of_players) TextView numberOfPlayers;
        @Bind(R.id.number_of_players_progressbar) ProgressBar numberOfPlayersProgressBar;

        public ServerInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
