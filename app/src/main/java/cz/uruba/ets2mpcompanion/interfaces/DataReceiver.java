package cz.uruba.ets2mpcompanion.interfaces;

import java.io.IOException;
import java.util.Date;

public interface DataReceiver<T> {
    void processData(T data, boolean notifyUser);
    void handleIOException(IOException e);
    Date getLastUpdated();
}
