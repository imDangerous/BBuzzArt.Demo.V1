package com.bbuzzart.demo.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by imDangerous on 2016-10-13.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
