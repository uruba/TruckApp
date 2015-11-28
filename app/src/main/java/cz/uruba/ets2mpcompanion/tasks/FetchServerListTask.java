package cz.uruba.ets2mpcompanion.tasks;

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

import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.interfaces.FetchTask;
import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

public class FetchServerListTask extends FetchTask<Void, Void, ArrayList<ServerInfo>, DataReceiverJSON<ArrayList<ServerInfo>>> {

    public FetchServerListTask(DataReceiverJSON<ArrayList<ServerInfo>> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    protected AsyncTaskResult<ArrayList<ServerInfo>> doInBackground(Void... params) {
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
            String contentAsString = readIt(is, 3000);

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
            return new AsyncTaskResult<>(e);
        } catch (JSONException e) {
            return new AsyncTaskResult<>(e);
        }

        return new AsyncTaskResult<>(serverList);
    }

    @Override
    protected void handleExceptionPostExecute(Exception e) {
        if (e instanceof  IOException) {
            callbackObject.handleIOException((IOException) e);
        }
        if (e instanceof JSONException) {
            callbackObject.handleJSONException((JSONException) e);
        }
    }
}
