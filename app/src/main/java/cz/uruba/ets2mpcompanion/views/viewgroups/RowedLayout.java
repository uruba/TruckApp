package cz.uruba.ets2mpcompanion.views.viewgroups;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RowedLayout extends ViewGroup {
    private List<Row> rows;

    public RowedLayout(Context context) {
        super(context);
    }

    public RowedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        rows = new ArrayList<>();
    }

    public void onViewAdded (View child) {

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = right - left - getPaddingRight();

        final int parentTop = getPaddingTop();

        int currentLeft = parentLeft;
        int currentTop = parentTop;
        int rowHeight = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();

                // we've reached the end of current row, so we move on to the next
                if (currentLeft + childWidth > parentRight) {
                    currentLeft = parentLeft;
                    currentTop += rowHeight;
                    rowHeight = 0;
                }

                // update the rowHeight number if this child is the tallest yet (the row is as tall as its tallest element is)
                if (rowHeight < childHeight) {
                    rowHeight = childHeight;
                }

                // now we have all the necessary position figures in place, so we can proceed to layout the child view
                child.layout(currentLeft, currentTop, currentTop + childWidth, currentTop + childHeight);

                // move the currentLeft "pointer" by the element's width
                currentLeft += childWidth;
            }
        }
    }

    public List<Row> getRows() {
        return rows;
    }

    private class Row {
        private List<View> elements = new ArrayList<>();

        public Row() {

        }

        public int getWidth() {
            int width = 0;

            for (View element : elements) {
                width += element.getWidth();
            }

            return width;
        }

        public void addElement(View element) {
            elements.add(element);
        }

        public List<View> getElements() {
            return new ArrayList<>(elements);
        }
    }
}