package cz.uruba.ets2mpcompanion.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.uruba.ets2mpcompanion.constants.Numeric;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.interfaces.AbstractFetchJSONTask;

public class FetchServerTimeTask extends AbstractFetchJSONTask<Date> {

    public FetchServerTimeTask(DataReceiverJSON<Date> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    @Override
    protected Date processHTTPStream(String stream) throws JSONException {
        JSONObject jsonObject = new JSONObject(stream);

        long numMinutes = jsonObject.getLong("game_time");

        return new Date(numMinutes * 60 * 1000 + Numeric.gameTimeStartingPointEpoch * 1000);
    }
}
