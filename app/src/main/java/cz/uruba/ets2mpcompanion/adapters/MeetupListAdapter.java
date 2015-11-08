package cz.uruba.ets2mpcompanion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;
import cz.uruba.ets2mpcompanion.model.MeetupInfo;

public class MeetupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_LAST_UPDATED = 1;
    private static final int TYPE_MEETUP_ENTRY = 2;

    private Context context;

    private List<MeetupInfo> meetupList;
    DataReceiver<?> callbackDataReceiver;

    public MeetupListAdapter(List<MeetupInfo> meetupList, DataReceiver<?> callbackDataReceiver) {
        this.meetupList = new ArrayList<>(meetupList);
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

            case TYPE_MEETUP_ENTRY:
                itemView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.cardview_meetupinfo, parent, false);

                return new MeetupInfoViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_LAST_UPDATED:
                LastUpdatedViewHolder lastUpdatedViewHolder = (LastUpdatedViewHolder) holder;

                lastUpdatedViewHolder.lastUpdated.setText(
                        String.format(
                                context.getResources().getString(R.string.last_updated),
                                callbackDataReceiver.getLastUpdated()
                        )
                );
                break;
            case TYPE_MEETUP_ENTRY:
                MeetupInfo meetupInfo = meetupList.get(position - 1);

                MeetupInfoViewHolder meetupInfoViewHolder = (MeetupInfoViewHolder) holder;

                meetupInfoViewHolder.time.setText(meetupInfo.getWhen());
                meetupInfoViewHolder.location.setText(meetupInfo.getLocation());
                meetupInfoViewHolder.organiser.setText(meetupInfo.getOrganiser());
                meetupInfoViewHolder.language.setText(meetupInfo.getLanguage());
                meetupInfoViewHolder.participants.setText(
                        String.format(
                                context
                                        .getResources()
                                        .getString(
                                                meetupInfo.getParticipants().equals("1") ?
                                                        R.string.participant_count_singular :
                                                        R.string.participant_count_plural),
                                meetupInfo.getParticipants()
                        )
                );
                break;
        }
    }

    @Override
    public int getItemViewType (int position) {
        if (position == 0) {
            return TYPE_LAST_UPDATED;
        }

        return TYPE_MEETUP_ENTRY;
    }

    @Override
    public int getItemCount() {
        return meetupList.size() + 1;
    }

    public void refreshAdapter(List<MeetupInfo> newMeetupList) {
        for (int i = meetupList.size() - 1; i >= 0; i--) {
            MeetupInfo meetup = meetupList.get(i);
            if (!newMeetupList.contains(meetup)) {
                removeItem(i);
            }
        }

        for (int i = 0, count = newMeetupList.size(); i < count; i++) {
            MeetupInfo meetup = newMeetupList.get(i);
            if (!meetupList.contains(meetup)) {
                addItem(i, meetup);
            }
        }

        for (int toPosition = newMeetupList.size() - 1; toPosition >= 0; toPosition--) {
            MeetupInfo meetup = newMeetupList.get(toPosition);
            final int fromPosition = meetupList.indexOf(meetup);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public MeetupInfo removeItem(int position) {
        MeetupInfo meetup = meetupList.remove(position);
        notifyItemRemoved(++position);
        return meetup;
    }

    public void addItem(int position, MeetupInfo meetup) {
        meetupList.add(position, meetup);
        notifyItemInserted(++position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        MeetupInfo meetup = meetupList.remove(fromPosition);
        meetupList.add(toPosition, meetup);
        notifyItemMoved(++fromPosition, ++toPosition);
    }

    public static class MeetupInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.time) TextView time;
        @Bind(R.id.location) TextView location;
        @Bind(R.id.organiser) TextView organiser;
        @Bind(R.id.language) TextView language;
        @Bind(R.id.participants) TextView participants;

        public MeetupInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class LastUpdatedViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.last_updated) TextView lastUpdated;

        public LastUpdatedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
