package cz.uruba.ets2mpcompanion.interfaces;

import android.os.AsyncTask;

import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

public abstract class FetchTask<T> extends AsyncTask<Void, Void, AsyncTaskResult<T>> {
    protected DataReceiver<T> callbackObject;
    protected String requestURL;
    protected boolean notifyUser;

    public FetchTask(DataReceiver<T> callbackObject, String requestURL, boolean notifyUser) {
        super();
        this.callbackObject = callbackObject;
        this.requestURL = requestURL;
        this.notifyUser = notifyUser;
    }

    protected abstract void handleExceptionPostExecute(Exception e);

    @Override
    protected final void onPostExecute(AsyncTaskResult<T> result) {
        if (result.isException()) {
            handleExceptionPostExecute(result.getException());
        } else {
            callbackObject.processData(result.getResult(), notifyUser);
        }
    }
}
