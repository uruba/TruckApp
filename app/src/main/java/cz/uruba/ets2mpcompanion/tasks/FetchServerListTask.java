package cz.uruba.ets2mpcompanion.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.uruba.ets2mpcompanion.interfaces.tasks.AbstractFetchJSONTask;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.model.general.DataSet;

public class FetchServerListTask extends AbstractFetchJSONTask<DataSet<ServerInfo>> {

    public FetchServerListTask(DataReceiverJSON<DataSet<ServerInfo>> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    @Override
    protected DataSet<ServerInfo> processHTTPStream(String stream) throws JSONException {
        ArrayList<ServerInfo> serverList = new ArrayList<>();
        JSONArray responseArray;

        JSONObject jsonObject = new JSONObject(stream);
        responseArray = jsonObject.getJSONArray("response");

        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject item = responseArray.getJSONObject(i);

            boolean online = item.getBoolean("online");
            String gameName = item.getString("game");
            String serverName = item.getString("name");
            int playerCountCurrent = item.getInt("players");
            int playerCountCapacity = item.getInt("maxplayers");
            int playerCountWaitQueue = item.getInt("queue");

            ServerInfo serverInfo = new ServerInfo(online, gameName, serverName, playerCountCurrent, playerCountCapacity, playerCountWaitQueue);
            serverList.add(serverInfo);
        }

        return new DataSet<>(serverList, new Date());
    }
}
