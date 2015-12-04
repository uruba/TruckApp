package cz.uruba.ets2mpcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.MeetupDetailActivity;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiver;
import cz.uruba.ets2mpcompanion.interfaces.DataReceiverListAdapter;
import cz.uruba.ets2mpcompanion.model.MeetupInfo;

public class MeetupListAdapter extends DataReceiverListAdapter<List<MeetupInfo>> {

    public MeetupListAdapter(Context context, List<MeetupInfo> dataCollection, DataReceiver<?> callbackDataReceiver) {
        super(context, dataCollection, callbackDataReceiver);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case TYPE_DATA_ENTRY:
                itemView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.cardview_meetupinfo, parent, false);

                return new MeetupInfoViewHolder(itemView);
        }

        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_DATA_ENTRY:
                final MeetupInfo meetupInfo = dataCollection.get(position - 1);

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
                meetupInfoViewHolder.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(context, v);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.show_meetup_detail:
                                        Intent meetupDetailIntent = new Intent(context, MeetupDetailActivity.class);
                                        meetupDetailIntent.putExtra(MeetupDetailActivity.INTENT_EXTRA_URL, meetupInfo.getAbsoluteURL());
                                        context.startActivity(meetupDetailIntent);
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.menu_meetup_entry, popup.getMenu());
                        popup.show();
                    }
                });
                break;
        }

        super.onBindViewHolder(holder, position);
    }

    public void refreshAdapter(List<MeetupInfo> newMeetupList) {
        for (int i = dataCollection.size() - 1; i >= 0; i--) {
            MeetupInfo meetup = dataCollection.get(i);
            if (!newMeetupList.contains(meetup)) {
                removeItem(i);
            }
        }

        for (int i = 0, count = newMeetupList.size(); i < count; i++) {
            MeetupInfo meetup = newMeetupList.get(i);
            if (!dataCollection.contains(meetup)) {
                addItem(i, meetup);
            }
        }
    }

    public MeetupInfo removeItem(int position) {
        MeetupInfo meetup = dataCollection.remove(position);
        notifyItemRemoved(position + 1);
        return meetup;
    }

    public void addItem(int position, MeetupInfo meetup) {
        dataCollection.add(position, meetup);
        notifyItemInserted(position + 1);
    }

    public static class MeetupInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.time) TextView time;
        @Bind(R.id.location) TextView location;
        @Bind(R.id.organiser) TextView organiser;
        @Bind(R.id.language) TextView language;
        @Bind(R.id.participants) TextView participants;
        @Bind(R.id.more) ImageView more;

        public MeetupInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
