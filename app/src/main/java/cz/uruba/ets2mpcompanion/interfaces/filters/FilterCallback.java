package cz.uruba.ets2mpcompanion.interfaces.filters;

import java.io.Serializable;

import cz.uruba.ets2mpcompanion.model.general.DataSet;

public interface FilterCallback<T extends Serializable> {
    void dataFiltered(DataSet<T> data);
}
