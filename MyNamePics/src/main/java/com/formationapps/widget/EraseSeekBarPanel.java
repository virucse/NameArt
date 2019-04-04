package com.formationapps.widget;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.formationapps.nameart.R;
import com.removebg.BgEraserView;

import nonworkingcode.effects.viewpanel.BasicPanel;

public class EraseSeekBarPanel extends BasicPanel {
    public EraseSeekBarPanel(Context context, BgEraserView bgEraserView) {
        super(context);
        initView((Activity) context, bgEraserView);
    }
    private LinearLayout seekBarGroupContainer;
    private SeekBar offsetSeekBar1,thresholdSeekBar;
    private BgEraserView mEraseView;
    private void initView(Activity activity, BgEraserView bgEraserView){
        mEraseView= bgEraserView;
        View rootView=activity.getLayoutInflater().inflate(R.layout.seekbar_erase_layout,this,true);
        seekBarGroupContainer=rootView.findViewById(R.id.lay_threshold_seek);
        offsetSeekBar1=rootView.findViewById(R.id.offset_seekbar1);
        thresholdSeekBar=rootView.findViewById(R.id.threshold_seekbar);

        if(offsetSeekBar1!=null){
            offsetSeekBar1.setProgress(mEraseView.offsetGet() + 150);
            offsetSeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mEraseView != null) {
                        mEraseView.offset(progress - 150);
                        mEraseView.invalidate();
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            });
        }
        if(thresholdSeekBar!=null){
            thresholdSeekBar.setProgress(20);
            thresholdSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mEraseView!= null) {
                        mEraseView.threshold(seekBar.getProgress() + 10);
                        mEraseView.updateThreshHold();
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            });
        }

    }
}
