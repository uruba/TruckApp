package cz.uruba.ets2mpcompanion.model;

import android.support.v4.util.Pair;

import java.util.Date;

public class ServerTime {
    private Date serverTime, serverTimeFreshToDate;

    public ServerTime(Date serverTime) {
        setServerTime(serverTime);
    }

    private void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
        this.serverTimeFreshToDate = new Date();
    }

    public Pair<Date, Date> getServerTime() {
        return new Pair<>(serverTime, serverTimeFreshToDate);
    }
}
