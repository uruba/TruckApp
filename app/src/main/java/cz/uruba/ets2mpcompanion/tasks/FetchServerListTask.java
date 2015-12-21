package cz.uruba.ets2mpcompanion.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.interfaces.FetchHTTPTask;
import cz.uruba.ets2mpcompanion.model.ServerInfo;

public class FetchServerListTask extends FetchHTTPTask<ArrayList<ServerInfo>, DataReceiverJSON<ArrayList<ServerInfo>>> {

    public FetchServerListTask(DataReceiverJSON<ArrayList<ServerInfo>> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    @Override
    protected ArrayList<ServerInfo> processHTTPStream(String stream) throws JSONException {
        ArrayList<ServerInfo> serverList = new ArrayList<>();
        JSONArray responseArray;

        JSONObject jsonObject = new JSONObject(stream);
        responseArray = jsonObject.getJSONArray("response");

        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject item = responseArray.getJSONObject(i);

            boolean online = item.getBoolean("online");
            String name = item.getString("name");
            int playerCountCurrent = item.getInt("players");
            int playerCountCapacity = item.getInt("maxplayers");

            ServerInfo serverInfo = new ServerInfo(online, name, playerCountCurrent, playerCountCapacity);
            serverList.add(serverInfo);
        }

        return serverList;
    }
}
