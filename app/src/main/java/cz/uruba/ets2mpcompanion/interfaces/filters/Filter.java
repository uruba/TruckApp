package cz.uruba.ets2mpcompanion.interfaces.filters;

import android.content.Context;

public abstract class Filter<T> {
    protected Context context;
    protected T data;
    protected FilterCallback<T> callback;

    public Filter(Context context, T data, FilterCallback<T> callback) {
        this.data = data;
        this.callback = callback;
    }
}
