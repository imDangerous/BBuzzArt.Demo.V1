package com.bbuzzart.demo.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bbuzzart.demo.R;

public class ItemVerticalMarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public ItemVerticalMarginDecoration(Context context) {
        margin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            // int left, int top, int right, int bottom
            if (i == 0) {
                params.setMargins(0, 0, 0, margin / 2);
            } else {
                params.setMargins(0, 0, 0, margin / 2);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();


            if (i == 0) {
                params.setMargins(0, 0, 0, margin / 2);
            } else {
                params.setMargins(0, 0, 0, margin / 2);
            }
        }

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (i == 0) {
                params.setMargins(0, 0, 0, margin / 2);
            } else {
                params.setMargins(0, 0, 0, margin / 2);
            }
        }

    }
}