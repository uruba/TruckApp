package cz.uruba.ets2mpcompanion.tasks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;
import cz.uruba.ets2mpcompanion.interfaces.FetchTask;
import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

public class FetchJsoupDataTask extends FetchTask<Void, Void, Document, DataReceiver<Document>> {
    private Map<String, String> cookies = null;

    public FetchJsoupDataTask(DataReceiver<Document> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    public FetchJsoupDataTask(DataReceiver<Document> callbackObject, String requestURL, Map<String, String> cookies, boolean notifyUser) {
        this(callbackObject, requestURL, notifyUser);
        this.cookies = cookies;
    }

    @Override
    protected AsyncTaskResult<Document> doInBackground(Void... params) {
        try {
            Document document = cookies == null ? Jsoup.connect(requestURL).get() : Jsoup.connect(requestURL).cookies(cookies).get();
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
