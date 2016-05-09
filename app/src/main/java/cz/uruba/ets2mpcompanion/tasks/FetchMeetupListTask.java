package cz.uruba.ets2mpcompanion.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.uruba.ets2mpcompanion.interfaces.tasks.AbstractFetchJSONTask;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.interfaces.model.MeetupInfo;
import cz.uruba.ets2mpcompanion.model.MeetupInfoETS2C;
import cz.uruba.ets2mpcompanion.model.MeetupInfoTRUCKERSEVENTS;
import cz.uruba.ets2mpcompanion.model.general.DataSet;

public class FetchMeetupListTask extends AbstractFetchJSONTask<DataSet<MeetupInfo>> {

    public FetchMeetupListTask(DataReceiverJSON<DataSet<MeetupInfo>> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    @Override
    protected DataSet<MeetupInfo> processHTTPStream(String stream) throws JSONException {
        ArrayList<MeetupInfo> meetupList = new ArrayList<>();
        JSONArray ets2cArray, truckersEventsArray;

        JSONObject jsonObject = new JSONObject(stream);
        jsonObject = jsonObject.getJSONObject("items");

        ets2cArray = jsonObject.getJSONArray("ets2c");
        truckersEventsArray = jsonObject.getJSONArray("truckers.events");

        processJSONArray(ets2cArray, meetupList, MeetupInfoETS2C.class);
        processJSONArray(truckersEventsArray, meetupList, MeetupInfoTRUCKERSEVENTS.class);

        return new DataSet<>(meetupList, new Date());
    }

    private void processJSONArray(JSONArray array, ArrayList<MeetupInfo> meetupListToAddTo, Class<? extends MeetupInfo> meetupSite) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);

            String server = item.getString("server");
            String time = item.getString("time");
            String location = item.getString("location");
            String organiser = item.getString("organiser");
            String language = item.getString("language");
            String participants = item.getString("participants").trim();
            String relativeURL = item.getString("relativeURL");

            MeetupInfo meetupInfo;

            if (meetupSite.equals(MeetupInfoETS2C.class)) {
                meetupInfo = new MeetupInfoETS2C(server, time, location, organiser, language, participants, relativeURL);
            } else
            if (meetupSite.equals(MeetupInfoTRUCKERSEVENTS.class)) {
                meetupInfo = new MeetupInfoTRUCKERSEVENTS(server, time, location, organiser, language, participants, relativeURL);
            } else {
                continue;
            }

            meetupListToAddTo.add(meetupInfo);
        }
    }
}
