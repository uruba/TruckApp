package cz.uruba.ets2mpcompanion.interfaces;

import android.os.AsyncTask;

import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

public abstract class FetchTask<T, U, V, W extends DataReceiver<V>> extends AsyncTask<T, U, AsyncTaskResult<V>> {
    protected W callbackObject;
    protected String requestURL;
    protected boolean notifyUser;

    public FetchTask(W callbackObject, String requestURL, boolean notifyUser) {
        super();
        this.callbackObject = callbackObject;
        this.requestURL = requestURL;
        this.notifyUser = notifyUser;
    }

    protected abstract void handleExceptionPostExecute(Exception e);

    @Override
    protected final void onPostExecute(AsyncTaskResult<V> result) {
        if (result.isException()) {
            handleExceptionPostExecute(result.getException());
        } else {
            callbackObject.processData(result.getResult(), notifyUser);
        }
    }
}
