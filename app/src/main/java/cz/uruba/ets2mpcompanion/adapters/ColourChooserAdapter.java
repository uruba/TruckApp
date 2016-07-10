package cz.uruba.ets2mpcompanion.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.List;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.views.ColourRectangleView;

public class ColourChooserAdapter extends BaseAdapter {
    private final GridView colourGrid;

    private final Context context;
    private final List<Integer> themeList;

    public ColourChooserAdapter(Context context, List<Integer> themeList, GridView colourGrid) {
        this.context = context;
        this.themeList = themeList;
        this.colourGrid = colourGrid;
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

    // TODO: The colours are off on initially-hidden items when we attempt to use the convertView. Find out why and come up with a code that works best!
    // TODO-NOTE: Probably will have to look into the ColourChooser view which has this adapter bound to it in the ColourChooserPreference
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColourRectangleView colourRectangleView;

        int[] attr = {R.attr.colorPrimary};
        TypedArray typedValue = context.obtainStyledAttributes(themeList.get(position), attr);
        int colour = typedValue.getColor(0, Color.BLACK);

        int rectangleWidth = getColumnWidthCompat();
        colourRectangleView = new ColourRectangleView(context, colour, rectangleWidth);
        colourRectangleView.setLayoutParams(
                new GridView.LayoutParams(
                        rectangleWidth,
                        rectangleWidth)
        );
        typedValue.recycle();

        return colourRectangleView;
    }

    private int getColumnWidthCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return colourGrid.getColumnWidth();
        }

        int spacing = (int) context.getResources().getDimension(R.dimen.colour_grid_spacing);

        return (colourGrid.getWidth() - spacing) / colourGrid.getNumColumns() - spacing;
    }
}
