package cz.uruba.ets2mpcompanion.tasks;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;
import cz.uruba.ets2mpcompanion.interfaces.FetchTask;
import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

public class FetchJsoupDataTask extends FetchTask<Void, Void, Document, DataReceiver<Document>> {


    public FetchJsoupDataTask(DataReceiver<Document> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    @Override
    protected AsyncTaskResult<Document> doInBackground(Void... params) {
        try {
            Document document = Jsoup.connect(requestURL).get();
            return new AsyncTaskResult<>(document);
        } catch (IOException e) {
            return new AsyncTaskResult<>(e);
        }
    }

    @Override
    protected void handleExceptionPostExecute(Exception e) {
        if (e instanceof  IOException) {
            callbackObject.handleIOException((IOException) e);
        }
    }
}
