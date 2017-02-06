package com.bbuzzart.demo.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by imDangerous on 2017-02-05.
 */
public class FixedHorizonMarginDecoration extends RecyclerView.ItemDecoration {

    private static final int fixedVal = 20;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = fixedVal;
        outRect.right = 0;
        outRect.top = 0;
        outRect.bottom = 0;

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 0;
        } else {
            outRect.left = fixedVal;
        }
    }
}
