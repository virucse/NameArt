package com.webapi.common;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by caliber fashion on 9/17/2017.
 */

public class MyImageView extends AppCompatImageView implements View.OnClickListener {
    private ClickListener mClickListener;
    private int position;

    public MyImageView(Context context) {
        super(context);
        init();
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(this);
    }

    public void setPosition(int pos) {
        position = pos;
    }

    public void setMyClickListener(ClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.onClick(v, position);
        }
    }

    public interface ClickListener {
        public void onClick(View v, int position);
    }
}
