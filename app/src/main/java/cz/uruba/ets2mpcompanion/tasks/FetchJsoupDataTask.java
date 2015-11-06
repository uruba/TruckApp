package cz.uruba.ets2mpcompanion.tasks;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import cz.uruba.ets2mpcompanion.interfaces.JsoupDataReceiver;

public class FetchJsoupDataTask extends AsyncTask<Void, Void, Document> {
    private JsoupDataReceiver callbackObject;
    private String requestURL;

    public FetchJsoupDataTask(JsoupDataReceiver callbackObject, String requestURL) {
        super();
        this.callbackObject = callbackObject;
        this.requestURL = requestURL;
    }

    @Override
    protected Document doInBackground(Void... params) {
        try {
            return Jsoup.connect(requestURL).get();
        } catch (IOException e) {
            callbackObject.handleIOException(e);
            return null;
        }
    }

    @Override
    protected  void onPostExecute(Document result) {
        callbackObject.processData(result);
    }
}
