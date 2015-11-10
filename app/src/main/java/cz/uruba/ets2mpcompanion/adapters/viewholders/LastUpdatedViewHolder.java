package cz.uruba.ets2mpcompanion.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.views.LastUpdatedTextView;

public class LastUpdatedViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.last_updated) public LastUpdatedTextView lastUpdated;

    public LastUpdatedViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
