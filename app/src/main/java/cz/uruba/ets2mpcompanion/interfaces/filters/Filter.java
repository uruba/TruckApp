package cz.uruba.ets2mpcompanion.interfaces.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cz.uruba.ets2mpcompanion.model.general.DataSet;

public abstract class Filter {
    protected Context context;
    protected DataSet data;
    protected FilterCallback<DataSet> callback;

    protected SharedPreferences sharedPref;

    public Filter(Context context, DataSet data, FilterCallback<DataSet> callback) {
        this.data = data;
        this.callback = callback;

        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }
}
