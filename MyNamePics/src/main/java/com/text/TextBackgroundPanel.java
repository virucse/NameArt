package com.text;

import android.content.Context;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caliber fashion on 9/19/2017.
 */

public class TextBackgroundPanel {
    private List<TextRes> mList;
    private Context mContext;

    public TextBackgroundPanel(Context context, int mode) {
        mList = new ArrayList<>();
        this.mContext = context;
        if (mode == TextPanelUtils.SHADOW_BUTTON) {
            //shadow button needs color resource
            String[] colorArray = mContext.getResources().getStringArray(R.array.color_array);
            for (int i = 0; i < colorArray.length; i++) {
                mList.add(initColorRes("", i, colorArray[i]));
            }
        } else if (mode == TextPanelUtils.FONTS_BUTTON) {
            //this needs text fonts
            if (BaseActivity.fonts != null && BaseActivity.fonts.length > 2) {

            }
        }

    }

    public List<TextRes> getResList() {
        return mList;
    }

    public TextColorRes initColorRes(String name, int position, String color) {
        TextColorRes tcr = new TextColorRes();
        tcr.setContext(mContext);
        tcr.setPosition(position);
        tcr.setName(name);
        tcr.setValueColor(color);
        return tcr;
    }

    public void dispose() {
        if (mList != null)
            mList.clear();
    }
}
