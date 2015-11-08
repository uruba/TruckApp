package cz.uruba.ets2mpcompanion.interfaces;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface DataReceiver<T> {
    void processData(T data, boolean notifyUser);
    void handleIOException(IOException e);
}
