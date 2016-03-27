package cz.uruba.ets2mpcompanion.model.general;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DataSet<T extends Serializable> implements Serializable {
    private ArrayList<T> collection;
    private Date lastUpdated;

    public DataSet(ArrayList<T> collection, Date lastUpdated) {
        this.collection = collection;
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<T> getCollection() {
        return collection;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
