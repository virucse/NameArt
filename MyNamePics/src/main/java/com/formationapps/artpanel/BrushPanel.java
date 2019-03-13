package com.formationapps.artpanel;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;

/**
 * Created by caliber fashion on 9/20/2017.
 */

public class BrushPanel extends BaseOnlyScrollPanel<Integer> {
    private static BrushPanel instanse;
    private BrushClickListener mListener;

    public BrushPanel(Context context) {
        super(context);
    }

    public static BrushPanel init(Activity activity) {
        if (instanse != null) {
            instanse.onDeAttach();
        }
        instanse = new BrushPanel(activity);
        instanse.initView(activity);
        return instanse;
    }

    private void initView(Activity activity) {
        List<Integer> values = new ArrayList<>();
        values.clear();
        // values.add(R.mipmap.ic_brightness);
        for (int id : BaseActivity.brushThumb) {
            values.add(id);
        }
        super.initView(activity, values);

        setResourceToPanelLeftIcon(R.mipmap.ic_brightness);
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        if (indexClicked >= 0) {
            if (mListener != null) {
                mListener.onBrushClick(indexClicked);
            }
        }
    }

    @Override
    public void onPanelLeftIconClick(View view) {
        if (mListener != null) {
            mListener.onBrushClick(555);
        }
    }

    @Override
    public void setResourceToPanelLeftIcon(int resId) {
        super.setResourceToPanelLeftIcon(resId);
    }

    @Override
    public void onDeAttach() {
        instanse = null;
    }

    public BrushPanel setBrushClickListener(BrushClickListener listener) {
        mListener = listener;
        return this;
    }

    public interface BrushClickListener {
        public void onBrushClick(int position);
    }
}
