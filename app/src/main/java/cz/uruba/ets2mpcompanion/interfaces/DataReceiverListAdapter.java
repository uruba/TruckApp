package cz.uruba.ets2mpcompanion.interfaces;

import android.content.Context;
import android.support.v4.widget.Space;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.adapters.viewholders.EmptyViewHolder;
import cz.uruba.ets2mpcompanion.adapters.viewholders.LastUpdatedViewHolder;

public abstract class DataReceiverListAdapter<T extends List> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_DATA_ENTRY = 0;
    protected static final int TYPE_LAST_UPDATED = 1;
    protected static final int TYPE_FOOTER = 2;

    protected Context context;

    protected DataReceiver<?> callbackDataReceiver;

    protected T dataCollection;

    public DataReceiverListAdapter(T dataCollection, DataReceiver<?> callbackDataReceiver) {
        this.dataCollection = dataCollection;
        this.callbackDataReceiver = callbackDataReceiver;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View itemView;

        switch (viewType) {
            case TYPE_LAST_UPDATED:
                itemView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.block_lastupdated, parent, false);

                return new LastUpdatedViewHolder(itemView);

            case TYPE_FOOTER:
                Space emptyView = new Space(context);
                emptyView.setLayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 84, context.getResources().getDisplayMetrics())
                        )
                );

                return new EmptyViewHolder(emptyView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_LAST_UPDATED:
                LastUpdatedViewHolder lastUpdatedViewHolder = (LastUpdatedViewHolder) holder;
                lastUpdatedViewHolder.lastUpdated.setTime(callbackDataReceiver.getLastUpdated());
                break;
        }
    }

    public T getDataCollection() {
        return dataCollection;
    }

    public void setDataCollection(T newCollection) {
        dataCollection = newCollection;
        notifyDataSetChanged();
    }

    public int getDataCollectionSize() {
        return dataCollection.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_LAST_UPDATED;
        } else if (position == getDataCollectionSize() + 1) {
            return TYPE_FOOTER;
        }

        return TYPE_DATA_ENTRY;
    }

    @Override
    public int getItemCount() {
        return getDataCollectionSize() + 2;
    }
}
