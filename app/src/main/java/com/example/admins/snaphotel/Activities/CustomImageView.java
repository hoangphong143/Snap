package com.example.admins.snaphotel.Activities;

import android.content.Context;

/**
 * Created by Admins on 4/6/2018.
 */

class CustomImageView extends android.support.v7.widget.AppCompatImageView {
    public CustomImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(100,100);
    }
}
