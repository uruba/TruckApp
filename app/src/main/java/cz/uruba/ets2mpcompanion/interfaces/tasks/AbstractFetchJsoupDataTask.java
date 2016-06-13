package cz.uruba.ets2mpcompanion.interfaces.tasks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;
import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

public abstract class AbstractFetchJsoupDataTask<T> extends AbstractFetchTask<T> {
    private Map<String, String> cookies = null;

    private AbstractFetchJsoupDataTask(DataReceiver<T> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    protected AbstractFetchJsoupDataTask(DataReceiver<T> callbackObject, String requestURL, Map<String, String> cookies, boolean notifyUser) {
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
