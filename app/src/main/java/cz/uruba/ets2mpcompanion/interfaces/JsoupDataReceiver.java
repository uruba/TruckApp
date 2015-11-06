package cz.uruba.ets2mpcompanion.interfaces;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface JsoupDataReceiver {
    void processData(Document data);
    void handleIOException(IOException e);
}
