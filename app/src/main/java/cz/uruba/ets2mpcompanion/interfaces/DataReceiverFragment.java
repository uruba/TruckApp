package cz.uruba.ets2mpcompanion.interfaces;

import android.support.v4.app.Fragment;

import java.util.Date;

public abstract class DataReceiverFragment<T> extends Fragment implements DataReceiver<T> {
    protected Date lastUpdated;

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
