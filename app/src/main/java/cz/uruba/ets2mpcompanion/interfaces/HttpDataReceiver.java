package cz.uruba.ets2mpcompanion.interfaces;

import java.io.IOException;

public interface HttpDataReceiver {
    void processData(String data);
    void handleIOException(IOException e);
}
