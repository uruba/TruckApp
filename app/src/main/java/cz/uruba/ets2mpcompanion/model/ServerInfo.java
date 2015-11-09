package cz.uruba.ets2mpcompanion.model;

import android.support.annotation.NonNull;

public class ServerInfo implements Comparable<ServerInfo> {
    private boolean online;
    private String serverName;
    private int playerCountCurrent;
    private int playerCountCapacity;

    public ServerInfo(boolean online, String serverName, int playerCountCurrent, int playerCountCapacity) {
        this.online = online;
        this.serverName = serverName;
        this.playerCountCurrent = playerCountCurrent;
        this.playerCountCapacity = playerCountCapacity;
    }

    public boolean isOnline() {
        return online;
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
}
