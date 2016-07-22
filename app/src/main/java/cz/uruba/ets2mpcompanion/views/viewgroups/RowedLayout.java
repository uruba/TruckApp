package cz.uruba.ets2mpcompanion.views.viewgroups;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.uruba.ets2mpcompanion.R;

public class RowedLayout extends ViewGroup {
    private List<Row> rows;
    private int horizontalSpacing, verticalSpacing;

    public RowedLayout(Context context) {
        this(context, null);
    }

    public RowedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.rowed_layout);
        try {
            horizontalSpacing = typedArray.getDimensionPixelSize(R.styleable.rowed_layout_android_horizontalSpacing, 0);
            verticalSpacing = typedArray.getDimensionPixelSize(R.styleable.rowed_layout_android_verticalSpacing, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int maxWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        final int maxHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        rows = new ArrayList<>();

        Row currentRow = new Row();
        rows.add(currentRow);

        int currentLeft = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            LayoutParams childLayoutParams = child.getLayoutParams();
            int childWidthSpec = createChildMeasureSpec(childLayoutParams.width, maxWidth, widthMode);
            int childHeightSpec = createChildMeasureSpec(childLayoutParams.height, maxHeight, heightMode);
            child.measure(childWidthSpec, childHeightSpec);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            currentLeft += childWidth;

            if (currentLeft > maxWidth) {
                currentRow = new Row();
                rows.add(currentRow);
                currentLeft = 0;
            } else {
                currentLeft += horizontalSpacing;
            }

            currentRow.addElement(child, childWidth, childHeight);
        }

        int longestRowWidth = 0;
        int totalRowHeight = 0;
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            totalRowHeight += row.getHeight();

            if (i < rows.size() - 1) {
                totalRowHeight += verticalSpacing;
            }

            longestRowWidth = Math.max(longestRowWidth, row.getWidth() + (row.getElements().size() - 1) * horizontalSpacing);
        }

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? MeasureSpec.getSize(widthMeasureSpec) : longestRowWidth + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? MeasureSpec.getSize(heightMeasureSpec) : totalRowHeight + getPaddingTop() + getPaddingBottom()
        );
    }

    private int createChildMeasureSpec(int childLayoutParam, int max, int parentMode) {
        int spec;
        if (childLayoutParam == LayoutParams.MATCH_PARENT) {
            spec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
        } else if (childLayoutParam == LayoutParams.WRAP_CONTENT) {
            spec = MeasureSpec.makeMeasureSpec(max, parentMode == MeasureSpec.UNSPECIFIED ? MeasureSpec.UNSPECIFIED
                    : MeasureSpec.AT_MOST);
        } else {
            spec = MeasureSpec.makeMeasureSpec(childLayoutParam, MeasureSpec.EXACTLY);
        }
        return spec;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();

        int currentLeft = parentLeft;
        int currentTop = parentTop;

        for (int i = 0; i < rows.size(); i++) {
            Row currentRow = rows.get(i);
            List<Row.Element> elements = currentRow.getElements();

            for (int j = 0; j < elements.size(); j++) {
                Row.Element element = elements.get(j);

                View elementView = element.getElement();
                elementView.layout(
                        currentLeft,
                        currentTop,
                        currentLeft + element.getPremeasuredWidth(),
                        currentTop + element.getPremeasuredHeight());

                currentLeft += element.getPremeasuredWidth() + horizontalSpacing;
            }

            currentLeft = parentLeft;
            currentTop += currentRow.getHeight() + verticalSpacing;
        }
    }

    public List<Row> getRows() {
        return rows;
    }

    private class Row {
        private final List<Element> elements = new ArrayList<>();
        private int totalWidth, totalHeight;

        public Row() {

        }

        public int getWidth() {
            return totalWidth;
        }

        public int getHeight() {
            return totalHeight;
        }

        public void addElement(View element, int measuredWidth, int measuredHeight) {

            // we increment the total row width by the width of the element being added
            totalWidth += measuredWidth;
            // the row is only as high as its tallest element
            totalHeight = Math.max(totalHeight, measuredHeight);

            elements.add(new Element(element, measuredWidth, measuredHeight));
        }

        public List<Element> getElements() {
            return new ArrayList<>(elements);
        }

        public class Element {
            final View element;
            final int premeasuredWidth;
            final int premeasuredHeight;

            public Element(View element, int premeasuredWidth, int premeasuredHeight) {
                this.element = element;
                this.premeasuredWidth = premeasuredWidth;
                this.premeasuredHeight = premeasuredHeight;
            }

            public View getElement() {
                return element;
            }

            public int getPremeasuredWidth() {
                return premeasuredWidth;
            }

            public int getPremeasuredHeight() {
                return premeasuredHeight;
            }
        }
    }
}