package cz.uruba.ets2mpcompanion.interfaces.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;

import cz.uruba.ets2mpcompanion.model.general.DataSet;

public abstract class Filter<T extends Serializable> {
    protected Context context;
    protected final FilterCallback<T> callback;

    protected final SharedPreferences sharedPref;

    protected Filter(Context context, FilterCallback<T> callback) {
        this.callback = callback;

        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }
}
