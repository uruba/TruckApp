package cz.uruba.ets2mpcompanion.model;

public class ServerInfo implements Comparable<ServerInfo> {
    private String serverName;
    private int playerCountCurrent;
    private int playerCountCapacity;

    public ServerInfo(String serverName, int playerCountCurrent, int playerCountCapacity) {
        this.serverName = serverName;
        this.playerCountCurrent = playerCountCurrent;
        this.playerCountCapacity = playerCountCapacity;
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
    public int compareTo(ServerInfo another) {
        return this.playerCountCurrent - another.playerCountCurrent;
    }
}
