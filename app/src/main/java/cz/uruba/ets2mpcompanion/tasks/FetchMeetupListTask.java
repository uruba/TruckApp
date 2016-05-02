package cz.uruba.ets2mpcompanion.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.uruba.ets2mpcompanion.interfaces.tasks.AbstractFetchJSONTask;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.model.MeetupInfo;
import cz.uruba.ets2mpcompanion.model.general.DataSet;

public class FetchMeetupListTask extends AbstractFetchJSONTask<DataSet<MeetupInfo>> {

    public FetchMeetupListTask(DataReceiverJSON<DataSet<MeetupInfo>> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    @Override
    protected DataSet<MeetupInfo> processHTTPStream(String stream) throws JSONException {
        ArrayList<MeetupInfo> meetupList = new ArrayList<>();
        JSONArray ets2cArray;

        JSONObject jsonObject = new JSONObject(stream);
        jsonObject = jsonObject.getJSONObject("items");

        ets2cArray = jsonObject.getJSONArray("ets2c");

        processJSONArray(ets2cArray, meetupList);

        return new DataSet<>(meetupList, new Date());
    }

    private void processJSONArray(JSONArray array, ArrayList<MeetupInfo> meetupListToAddTo) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);

            String server = item.getString("server");
            String time = item.getString("time");
            String location = item.getString("location");
            String organiser = item.getString("organiser");
            String language = item.getString("language");
            String participants = item.getString("participants").trim();
            String relativeURL = item.getString("relativeURL");

            MeetupInfo meetupInfo = new MeetupInfo(server, time, location, organiser, language, participants, relativeURL);
            meetupListToAddTo.add(meetupInfo);
        }
    }
}
