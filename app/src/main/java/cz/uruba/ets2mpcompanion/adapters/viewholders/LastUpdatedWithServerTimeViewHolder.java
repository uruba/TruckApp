package cz.uruba.ets2mpcompanion.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.views.ServerTimeTextView;

public class LastUpdatedWithServerTimeViewHolder extends LastUpdatedViewHolder {
    @Bind(R.id.server_time) public ServerTimeTextView serverTime;

    public LastUpdatedWithServerTimeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
