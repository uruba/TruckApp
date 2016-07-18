package cz.uruba.ets2mpcompanion.interfaces.tasks;

import android.os.AsyncTask;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;
import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

abstract class AbstractFetchTask<T> extends AsyncTask<Void, Void, AsyncTaskResult<T>> {
    final DataReceiver<T> callbackObject;
    final String requestURL;
    private final boolean notifyUser;

    AbstractFetchTask(DataReceiver<T> callbackObject, String requestURL, boolean notifyUser) {
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
