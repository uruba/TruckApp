package cz.uruba.ets2mpcompanion.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.views.LastUpdatedTextView;

public class LastUpdatedWithServerTimeViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.last_updated) public LastUpdatedTextView lastUpdated;
    @Bind(R.id.server_time) public TextView serverTime;

    public LastUpdatedWithServerTimeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
