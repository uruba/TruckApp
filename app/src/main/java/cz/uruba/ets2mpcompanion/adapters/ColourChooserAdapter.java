package cz.uruba.ets2mpcompanion.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.views.ColourRectangleView;

public class ColourChooserAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> themeList = new ArrayList<>();

    public ColourChooserAdapter(Context context, Map<String, Integer> themeMap) {
        this.context = context;
        for (Map.Entry<String, Integer> themeStyle : themeMap.entrySet()) {
            themeList.add(themeStyle.getValue());
        }
    }

    @Override
    public int getCount() {
        return themeList.size();
    }

    @Override
    public Object getItem(int position) {
        return themeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int[] attr = {R.attr.colorPrimary};
        TypedArray typedValue = context.obtainStyledAttributes(themeList.get(position), attr);

        int colour = typedValue.getColor(0, Color.BLACK);

        typedValue.recycle();

        return new ColourRectangleView(context, colour, 25);
    }
}
