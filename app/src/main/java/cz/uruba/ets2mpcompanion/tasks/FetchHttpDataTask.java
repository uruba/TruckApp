package cz.uruba.ets2mpcompanion.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.uruba.ets2mpcompanion.interfaces.HttpDataReceiver;

public class FetchHttpDataTask extends AsyncTask<Void, Void, String> {
    private HttpDataReceiver callbackObject;
    private String requestURL;

    public FetchHttpDataTask(HttpDataReceiver callbackObject, String requestURL) {
        super();
        this.callbackObject = callbackObject;
        this.requestURL = requestURL;
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    protected String doInBackground(Void... params) {
        InputStream is;

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

            return contentAsString;
        } catch (IOException e) {
            callbackObject.handleIOException(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        callbackObject.processData(result);
    }

}
