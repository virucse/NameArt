package com.formationapps.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BgRemoverActivity;
import com.formationapps.nameart.helper.AppUtils;

import nonworkingcode.effects.viewpanel.BasicPanel;

public class FreeHandCutPanel extends BasicPanel {
    public FreeHandCutPanel(Context context) {
        super(context);
        initView((Activity) getContext());
    }
    private Activity activity;
    private void initView(Activity activi){
        activity=activi;
        int[] name=new int[]{R.string.insidecut,R.string.outsidecut};
        init(activity,name);
    }

    private LinearLayout mLinContainer;
    private int pos=0;
    protected void init(Activity activity, int[] texts) {
        View rootView = activity.getLayoutInflater().inflate(R.layout.panel_scroll_layout, this, true);
        final HorizontalScrollView hsr = (HorizontalScrollView) rootView.findViewById(R.id.hsv_pn_filter);
        this.mLinContainer = (LinearLayout) rootView.findViewById(R.id.ln_pn_filter_con);
        if (this.mLinContainer != null) {
            this.mLinContainer.removeAllViews();
            final int size = AppUtils.screenHeight / 9;
            int maring = (int) AppUtils.dpToPx(getContext(),3.0f);
            LayoutParams param =null;
            if(texts.length<=3){
                param = new LayoutParams(AppUtils.screenWidth/texts.length, size);
            }else {
                param = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, size);
            }
            param.leftMargin = maring;
            param.rightMargin = maring;
            int textColor = AppUtils.getColor(activity, R.attr.titleTextColor);
            for (int i = 0; i < texts.length; i++) {
                Button view = new Button(activity);
                view.setSingleLine(true);
                view.setTextColor(textColor);
                view.setSingleLine(true);
                view.setText(texts[i]);
                view.setTypeface(Typeface.DEFAULT_BOLD);
                view.setTextColor(Color.WHITE);
                view.setBackgroundColor(0);
                view.setTag(Integer.valueOf(i));
                view.setLayoutParams(param);
                view.setOnClickListener(mClickListener);
                this.mLinContainer.addView(view);
            }
            hsr.post(new Runnable() {
                @Override
                public void run() {
                    hsr.scrollTo((int) (((float) (pos* size)) * 1.01f), 0);
                }
            });
        }
        resetLinSelector(pos);
    }
    private View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((Integer) v.getTag()).intValue();
            resetLinSelector(position);
            onItemClicked(position);
        }
    };

    private void onItemClicked(int position) {
        pos=position;
        switch (position){
            case 0:
                ((BgRemoverActivity)activity).onSelectedMode(BgRemoverActivity.MODEOPTIONS.FREEHAND_INSIDE);
                //Toast.makeText(getContext(),"inside",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                ((BgRemoverActivity)activity).onSelectedMode(BgRemoverActivity.MODEOPTIONS.FREEHAND_OUTSIDE);
                break;
        }
    }

    protected void resetLinSelector(int inde) {
        int selectedColor = AppUtils.getColor(getContext(), R.attr.colorPrimary);
        if (this.mLinContainer.getChildCount() > 0) {
            for (int i = 0; i < this.mLinContainer.getChildCount(); i++) {
                View v = this.mLinContainer.getChildAt(i);
                if (v != null) {
                    if (inde == i) {
                        v.setBackgroundColor(selectedColor);
                    } else {
                        v.setBackgroundColor(0);
                    }
                }
            }
        }
    }
}
