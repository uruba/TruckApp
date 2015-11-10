package cz.uruba.ets2mpcompanion.interfaces;

import org.json.JSONException;

public interface DataReceiverJSON<T> extends DataReceiver<T> {
    void handleJSONException(JSONException e);
}
