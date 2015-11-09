package cz.uruba.ets2mpcompanion.adapters.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.uruba.ets2mpcompanion.R;

public class LastUpdatedViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.last_updated) public TextView lastUpdated;

    public LastUpdatedViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setTextLastUpdated(Context context, Date date) {
        lastUpdated.setText(
                String.format(
                        context.getResources().getString(R.string.last_updated),
                        date
                )
        );
    }
}
