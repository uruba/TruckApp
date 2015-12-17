package cz.uruba.ets2mpcompanion.tasks;

import java.util.Date;

import cz.uruba.ets2mpcompanion.interfaces.DataReceiverJSON;
import cz.uruba.ets2mpcompanion.interfaces.FetchTask;
import cz.uruba.ets2mpcompanion.tasks.result.AsyncTaskResult;

public class FetchServerTimeTask extends FetchTask<Void, Void, Date, DataReceiverJSON<Date>> {

    public FetchServerTimeTask(DataReceiverJSON<Date> callbackObject, String requestURL, boolean notifyUser) {
        super(callbackObject, requestURL, notifyUser);
    }

    @Override
    protected void handleExceptionPostExecute(Exception e) {

    }

    @Override
    protected AsyncTaskResult<Date> doInBackground(Void... params) {
        return null;
    }
}
