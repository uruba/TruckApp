package cz.uruba.ets2mpcompanion.tasks.result;

// Taken from here: http://stackoverflow.com/questions/1739515/asynctask-and-error-handling-on-android
public class AsyncTaskResult<T> {
    private T result;
    private Exception exception;

    public boolean isException() {
        return exception != null;
    }

    public T getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }

    public AsyncTaskResult(T result) {
        super();
        this.result = result;
    }

    public AsyncTaskResult(Exception exception) {
        super();
        this.exception = exception;
    }
}
