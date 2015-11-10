package cz.uruba.ets2mpcompanion.tasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.model.ServerInfo;

public class FetchServerListTask extends AsyncTask<Void, Void, ArrayList<ServerInfo>> {
    private DataReceiverJSON<ArrayList<ServerInfo>> callbackObject;
    private String requestURL;
    private boolean notifyUser;

    public FetchServerListTask(DataReceiverJSON<ArrayList<ServerInfo>> callbackObject, String requestURL, boolean notifyUser) {
        super();
        this.callbackObject = callbackObject;
        this.requestURL = requestURL;
        this.notifyUser = notifyUser;
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    protected ArrayList<ServerInfo> doInBackground(Void... params) {
        InputStream is;

        ArrayList<ServerInfo> serverList = new ArrayList<>();

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, 800);

            is.close();

            JSONObject jsonObject = new JSONObject(contentAsString);
            JSONArray responseArray = jsonObject.getJSONArray("response");

            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject item = responseArray.getJSONObject(i);

                boolean online = item.getBoolean("online");
                String name = item.getString("name");
                int playerCountCurrent = item.getInt("players");
                int playerCountCapacity = item.getInt("maxplayers");

                ServerInfo serverInfo = new ServerInfo(online, name, playerCountCurrent, playerCountCapacity);
                serverList.add(serverInfo);
            }
        } catch (IOException e) {
            callbackObject.handleIOException(e);
            return null;
        } catch (JSONException e) {
            callbackObject.handleJSONException(e);
            return null;
        }

        return serverList;
    }

    @Override
    protected void onPostExecute(ArrayList<ServerInfo> result) {
        callbackObject.processData(result, notifyUser);
    }

}
