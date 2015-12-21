package cz.uruba.ets2mpcompanion.interfaces;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cz.uruba.ets2mpcompanion.model.ServerInfo;
import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

public abstract class FetchHTTPTask<T, U, V, W extends DataReceiverJSON<V>> extends FetchTask<T, U, V, W> {

    public FetchHTTPTask(W callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    protected abstract V processHTTPStream(String stream) throws JSONException;

    @Override
    protected void handleExceptionPostExecute(Exception e) {
        if (e instanceof IOException) {
            callbackObject.handleIOException((IOException) e);
        }
        if (e instanceof JSONException) {
            callbackObject.handleJSONException((JSONException) e);
        }
    }

    @Override
    protected AsyncTaskResult<V> doInBackground(T... params) {
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

            V result = processHTTPStream(contentAsString);
            return new AsyncTaskResult<>(result);
        } catch (IOException e) {
            return new AsyncTaskResult<>(e);
        } catch (JSONException e) {
            return new AsyncTaskResult<>(e);
        }
    }
}
