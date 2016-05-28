package cz.uruba.ets2mpcompanion.interfaces.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;

import cz.uruba.ets2mpcompanion.model.general.DataSet;

public abstract class Filter<T extends Serializable> {
    protected Context context;
    protected DataSet<T> data;
    protected FilterCallback<T> callback;

    protected SharedPreferences sharedPref;

    public Filter(Context context, DataSet<T> data, FilterCallback<T> callback) {
        this.data = data;
        this.callback = callback;

        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }
}
