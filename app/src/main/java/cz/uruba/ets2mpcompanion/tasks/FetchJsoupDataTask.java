package cz.uruba.ets2mpcompanion.tasks;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;

public class FetchJsoupDataTask extends AsyncTask<Void, Void, Document> {
    private DataReceiver<Document> callbackObject;
    private String requestURL;
    private boolean notifyUser;

    public FetchJsoupDataTask(DataReceiver<Document> callbackObject, String requestURL, boolean notifyUser) {
        super();
        this.callbackObject = callbackObject;
        this.requestURL = requestURL;
        this.notifyUser = notifyUser;
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
        callbackObject.processData(result, notifyUser);
    }
}
