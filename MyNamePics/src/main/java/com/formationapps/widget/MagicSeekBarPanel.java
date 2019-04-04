package com.formationapps.widget;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.formationapps.nameart.R;
import com.removebg.BgEraserView;

import nonworkingcode.effects.viewpanel.BasicPanel;

public class MagicSeekBarPanel extends BasicPanel {
    public MagicSeekBarPanel(Context context, BgEraserView bgEraserView) {
        super(context);
        initView((Activity) context, bgEraserView);
    }
    private LinearLayout seekBarGroupContainer;
    private SeekBar offsetSeekBar,radiousSeekBar;
    private BgEraserView mEraseView;
    private void initView(Activity activity, BgEraserView bgEraserView){
        mEraseView= bgEraserView;
        View rootView=activity.getLayoutInflater().inflate(R.layout.seekbar_magic_layout,this,true);
        seekBarGroupContainer=rootView.findViewById(R.id.lay_offset_seek);
        offsetSeekBar=rootView.findViewById(R.id.offset_seekbar);
        radiousSeekBar=rootView.findViewById(R.id.radius_seekbar);

        offsetSeekBar.setProgress(225);
        offsetSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        radiousSeekBar.setProgress(18);
        radiousSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mEraseView != null) {
                    mEraseView.radius(progress + 2);
                    mEraseView.invalidate();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }
}
