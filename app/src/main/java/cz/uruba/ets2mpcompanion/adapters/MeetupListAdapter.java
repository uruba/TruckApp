package cz.uruba.ets2mpcompanion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.model.MeetupInfo;

public class MeetupListAdapter extends RecyclerView.Adapter<MeetupListAdapter.MeetupInfoViewHolder> {
    private Context context;

    private List<MeetupInfo> meetupList;

    public MeetupListAdapter(List<MeetupInfo> meetupList) {
        this.meetupList = meetupList;
    }

    @Override
    public MeetupInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View itemView = LayoutInflater
                .from(context)
                .inflate(R.layout.cardview_meetupinfo, parent, false);

        return new MeetupInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeetupInfoViewHolder holder, int position) {
        MeetupInfo meetupInfo = meetupList.get(position);

        holder.time.setText(meetupInfo.getWhen());
        holder.location.setText(meetupInfo.getLocation());
        holder.organiser.setText(meetupInfo.getOrganiser());
        holder.language.setText(meetupInfo.getLanguage());
        holder.participants.setText(meetupInfo.getParticipants());
    }

    @Override
    public int getItemCount() {
        return meetupList.size();
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
}
