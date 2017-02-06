package com.bbuzzart.demo.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bbuzzart.demo.R;


public class ItemHorizonMarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;
    private int row;

    public ItemHorizonMarginDecoration(Context context, int row) {
        this.margin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);
        this.row = row;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            // int left, int top, int right, int bottom
            if (i % 2 == 0) {
                params.setMargins(0, margin / 2, margin / 4, 0);
            } else {
                params.setMargins(margin / 4, margin / 2, 0, 0);
            }

            if (i == 0) {
                params.setMargins(0, margin / 2, margin / 4, 0);
            } else if (i == 1) {
                params.setMargins(margin / 4, margin / 2, 0, 0);
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

            /*
            if (i % 2 == 0) {
                params.setMargins(margin, margin, margin / 2, 0);
            } else {
                params.setMargins(margin / 2, margin, margin, 0);
            }
            */

            if (i % 2 == 0) {
                params.setMargins(0, margin / 2, margin / 4, 0);
            } else {
                params.setMargins(margin / 4, margin / 2, 0, 0);
            }

            if (i == 0) {
                params.setMargins(0, margin / 2, margin / 4, 0);
            } else if (i == 1) {
                params.setMargins(margin / 4, margin / 2, 0, 0);
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

            /*
            if (i % 2 == 0) {
                params.setMargins(margin, margin, margin / 2, 0);
            } else {
                params.setMargins(margin / 2, margin, margin, 0);
            }
            */


            if (i % 2 == 0) {
                params.setMargins(0, margin / 2, margin / 4, 0);
            } else {
                params.setMargins(margin / 4, margin / 2, 0, 0);
            }

            if (i == 0) {
                params.setMargins(0, margin / 2, margin / 4, 0);
            } else if (i == 1) {
                params.setMargins(margin / 4, margin / 2, 0, 0);
            }
        }

    }
}