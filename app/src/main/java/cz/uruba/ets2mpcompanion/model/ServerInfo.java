package cz.uruba.ets2mpcompanion.model;

import android.content.Context;
import android.support.annotation.NonNull;

import cz.uruba.ets2mpcompanion.R;

public class ServerInfo implements Comparable<ServerInfo> {
    private boolean online;
    private String gameName;
    private String serverName;
    private int playerCountCurrent;
    private int playerCountCapacity;

    public ServerInfo(boolean online, String gameName, String serverName, int playerCountCurrent, int playerCountCapacity) {
        this.online = online;
        this.gameName = gameName;
        this.serverName = serverName;
        this.playerCountCurrent = playerCountCurrent;
        this.playerCountCapacity = playerCountCapacity;
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

    @Override
    public int compareTo(@NonNull ServerInfo another) {
        return this.playerCountCurrent - another.playerCountCurrent;
    }

    public String getFormattedPlayerCountString(Context context) {
        return String.format(
                context
                        .getResources()
                        .getString(R.string.player_count),
                playerCountCurrent,
                playerCountCapacity
        );
    }

    public float getPlayerCountRatio() {
        return ((float) playerCountCurrent / (float) playerCountCapacity) * 100;
    }
}
