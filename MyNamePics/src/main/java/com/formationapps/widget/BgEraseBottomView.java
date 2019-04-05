package com.formationapps.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BgRemoverActivity;
import com.removebg.BgEraserView;

import java.util.ArrayList;
import java.util.List;


import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.effects.viewpanel.BasicPanel;


public class BgEraseBottomView extends BaseOnlyScrollPanel<Integer[]> {
    private int[] name,resId;
    private BasicPanel mBasePanel;
    private Activity activity=null;
    private FrameLayout mFrame;
    private BgEraserView mEraseView;
    public BgEraseBottomView(Context context) {
        super(context);
        init(context);
    }
    public BgEraseBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BgEraseBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        name=new int[]{R.string.text_magic,R.string.manual,R.string.freehand, R.string.text_repair,R.string.text_zoom};
        resId=new int[]{R.mipmap.ic_auto,R.mipmap.ic_erase,R.mipmap.ic_freehand,R.mipmap.ic_repair,R.mipmap.ic_zoom};
        if(context instanceof Activity){
            initView((Activity) context);
        }
    }
    private void initView(Activity activity) {
        this.activity = activity;
        List<Integer[]> supers = new ArrayList();
        for (int i = 0; i < this.name.length; i++) {
            supers.add(new Integer[]{this.resId[i], this.name[i]});
        }
        super.initView(activity, supers);
        mFrame = (FrameLayout) findViewById(R.id.framelayout);
    }
    public void setEraseView(BgEraserView eraseView){
        mEraseView=eraseView;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        resetBaseFrame();

        switch (indexClicked){
            case 0:
                //auto or magic view
                mBasePanel=new EraseSeekBarPanel(activity,mEraseView);
                mFrame.addView(mBasePanel);
                ((BgRemoverActivity)activity).onSelectedMode(BgRemoverActivity.MODEOPTIONS.AUTO);
                break;
            case 1:
                mBasePanel=new MagicSeekBarPanel(activity,mEraseView);
                mFrame.addView(mBasePanel);
                ((BgRemoverActivity)activity).onSelectedMode(BgRemoverActivity.MODEOPTIONS.ERASE);
                break;
            case 2:
                ((BgRemoverActivity)activity).onSelectedMode(BgRemoverActivity.MODEOPTIONS.FREEHAND);
                return;
            case 3:
                ((BgRemoverActivity)activity).onSelectedMode(BgRemoverActivity.MODEOPTIONS.RESTORE);
                return;
            case 4:
                ((BgRemoverActivity)activity).onSelectedMode(BgRemoverActivity.MODEOPTIONS.ZOOM);
                return;
        }
        if (mBasePanel != null) {
            mBasePanel.setVisibility(View.GONE);
            mBasePanel.show();
        }
    }
    private void resetBaseFrame(){
        if (getContext() instanceof Activity) {
            activity = (Activity) getContext();
        }
        if (mBasePanel != null) {
            if (mFrame != null) {
                mFrame.removeView(mBasePanel);
            }
            mBasePanel = null;
        }
        if (mFrame == null) {
            mFrame = (FrameLayout) findViewById(R.id.framelayout);
        }
    }
    public void addFreehandEraseOptions(){
        resetBaseFrame();
        mBasePanel=new FreeHandCutPanel(activity);
        mFrame.addView(mBasePanel);
        if (mBasePanel != null) {
            mBasePanel.setVisibility(View.GONE);
            mBasePanel.show();
        }
    }
    public boolean onBackPressed(){
        /*if(mOnBackStateChangeListener!=null){
            mOnBackStateChangeListener.canBack(true);
        }*/
        if(mBasePanel!=null){
            if (mFrame != null) {
                mBasePanel.hide(new OnHideListener() {
                    @Override
                    public void onHideFinished_BasicPanel() {
                        mFrame.removeView(mBasePanel);
                        mBasePanel = null;
                    }
                });
            }
            resetLinSelector();
            return true;
        }
        return false;
    }
}
