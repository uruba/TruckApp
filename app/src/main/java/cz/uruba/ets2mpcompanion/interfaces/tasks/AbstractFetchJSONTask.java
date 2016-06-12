package cz.uruba.ets2mpcompanion.interfaces.tasks;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

public abstract class AbstractFetchJSONTask<T> extends AbstractFetchTask<T> {

    protected AbstractFetchJSONTask(DataReceiverJSON<T> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    protected abstract T processHTTPStream(String stream) throws JSONException;

    @Override
    protected void handleExceptionPostExecute(Exception e) {
        if (e instanceof IOException) {
            callbackObject.handleIOException((IOException) e);
        }
        if (e instanceof JSONException) {
            ((DataReceiverJSON) callbackObject).handleJSONException((JSONException) e);
        }
    }

    @Override
    protected AsyncTaskResult<T> doInBackground(Void... params) {
        InputStream inputStream;

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            inputStream = connection.getInputStream();

            // Convert the InputStream into a string
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder contentStringBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                contentStringBuilder.append(inputLine);
            }

            inputStream.close();

            T result = processHTTPStream(contentStringBuilder.toString());
            return new AsyncTaskResult<>(result);
        } catch (IOException e) {
            return new AsyncTaskResult<>(e);
        } catch (JSONException e) {
            return new AsyncTaskResult<>(e);
        }
    }
}
