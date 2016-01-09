package cz.uruba.ets2mpcompanion.interfaces;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

public abstract class FetchJsoupDataTask<T> extends FetchTask<Void, Void, T, DataReceiver<T>> {
    private Map<String, String> cookies = null;

    public FetchJsoupDataTask(DataReceiver<T> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    public FetchJsoupDataTask(DataReceiver<T> callbackObject, String requestURL, Map<String, String> cookies, boolean notifyUser) {
        this(callbackObject, requestURL, notifyUser);
        this.cookies = cookies;
    }

    protected abstract T processJsoupData(Document document);

    @Override
    protected AsyncTaskResult<T> doInBackground(Void... params) {
        try {
            Document document = cookies == null ? Jsoup.connect(requestURL).get() : Jsoup.connect(requestURL).cookies(cookies).get();

            T result = processJsoupData(document);
            return new AsyncTaskResult<>(result);
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
