package com.webapi;

import android.view.View;

public class FeedItemClickListener implements View.OnClickListener {
    private View view;
    private int position;
    public FeedItemClickListener(View view,int pos){
        this.view=view;
        position=pos;
    }
    @Override
    public void onClick(View v) {

    }
}
