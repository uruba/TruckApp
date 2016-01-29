package cz.uruba.ets2mpcompanion.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;

public class LastUpdatedWithFilterInfoViewHolder extends LastUpdatedViewHolder {
    @Bind(R.id.filtering_status) public TextView filteringStatus;

    public LastUpdatedWithFilterInfoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
