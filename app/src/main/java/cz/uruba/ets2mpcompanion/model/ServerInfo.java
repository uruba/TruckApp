package cz.uruba.ets2mpcompanion.model;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;

import cz.uruba.ets2mpcompanion.R;

public class ServerInfo implements Comparable<ServerInfo>, Serializable {
    private final boolean online;
    private final String gameName;
    private final String serverName;
    private final int playerCountCurrent;
    private final int playerCountCapacity;
    private final int playerCountWaitQueue;

    public ServerInfo(boolean online, String gameName, String serverName, int playerCountCurrent, int playerCountCapacity, int playerCountWaitQueue) {
        this.online = online;
        this.gameName = gameName;
        this.serverName = serverName;
        this.playerCountCurrent = playerCountCurrent;
        this.playerCountCapacity = playerCountCapacity;
        this.playerCountWaitQueue = playerCountWaitQueue;
    }

    public boolean isOnline() {
        return online;
    }

    public String getGameName() {
        return gameName;
    }

    public String getServerName() {
        return serverName;
    }

    public int getPlayerCountCurrent() {
        return playerCountCurrent;
    }

    public int getPlayerCountCapacity() {
        return playerCountCapacity;
    }

    public int getPlayerCountWaitQueue() {
        return playerCountWaitQueue;
    }

    @Override
    public int compareTo(@NonNull ServerInfo another) {
        return this.playerCountCurrent - another.playerCountCurrent;
    }

    public String getFormattedPlayerCountString(Context context) {
        StringBuilder playerCountStringBuilder = new StringBuilder();

        playerCountStringBuilder.append(String.format(
                context
                        .getResources()
                        .getString(R.string.player_count),
                playerCountCurrent,
                playerCountCapacity
        ));

        if (this.playerCountWaitQueue > 0) {
            playerCountStringBuilder.append(String.format(
                    " (%1$s)",
                    String.format(
                            context
                                    .getResources()
                                    .getString(R.string.player_count_queue),
                            playerCountWaitQueue
                    )
            ));
        }

        return playerCountStringBuilder.toString();
    }

    public float getPlayerCountRatio() {
        return ((float) playerCountCurrent / (float) playerCountCapacity) * 100;
    }
}
