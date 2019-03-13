package com.formationapps.artpanel;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.ArtActivity;
import com.text.TextMainPanel;

import java.util.ArrayList;
import java.util.List;

import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.effects.viewpanel.BasicPanel;

/**
 * Created by caliber fashion on 9/20/2017.
 */

public class NameArtPanel extends BaseOnlyScrollPanel<Integer[]> {
    private int[] names;
    private int pos;
    private int[] resIds;
    private BasicPanel mBasePanel;
    private FrameLayout mFrame;
    private BrushPanel mBrushPanel;
    private ArtbgListener mArtbgListener;

    public NameArtPanel(Context context) {
        super(context);
        init(context);
    }

    public NameArtPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.pos = -1;
        this.names = new int[]{R.string.text, R.string.symbol, R.string.photo, R.string.background, R.string.magic_brush, R.string.stickers};
        this.resIds = new int[]{R.mipmap.ic_text, R.mipmap.symbol,
                R.mipmap.head, R.mipmap.ic_background, R.mipmap.magic_brush, R.mipmap.ic_sticker};
        this.mBasePanel = null;
        if (context instanceof Activity) {
            initView((Activity) context);
        }
    }

    private void initView(Activity activity) {
        List<Integer[]> supers = new ArrayList();
        for (int i = 0; i < this.names.length; i++) {
            supers.add(new Integer[]{Integer.valueOf(this.resIds[i]), Integer.valueOf(this.names[i])});
        }
        super.initView(activity, supers);
        mFrame = (FrameLayout) findViewById(R.id.frame_top);
    }

    public boolean onBackPressed() {
        pos = -1;
        if (mBasePanel != null) {
            if (mBasePanel.onBackPressed()) {
                return true;
            }
            if (this.mFrame != null) {
                this.mBasePanel.hide(new OnHideListener() {
                    @Override
                    public void onHideFinished_BasicPanel() {
                        mFrame.removeView(mBasePanel);
                        mBasePanel = null;
                        pos = -1;
                    }
                });
            }
            resetLinSelector();
            return true;
        }
        return false;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    public void setArtBgListener(ArtbgListener listener) {
        mArtbgListener = listener;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        if (pos != indexClicked) {
            pos = indexClicked;
            Activity activity = null;
            if (getContext() instanceof Activity) {
                activity = (Activity) getContext();
            }
            if (mBasePanel != null) {
                if (mFrame != null) {
                    mFrame.removeView(this.mBasePanel);
                }
                mBasePanel = null;
            }
            if (mFrame == null) {
                mFrame = (FrameLayout) findViewById(R.id.frame_top);
            }
            switch (indexClicked) {
                case 0:
                    //this.pos = -1;
                    mBasePanel = new TextMainPanel(activity);
                    ((TextMainPanel) mBasePanel).initView(activity);
                    mFrame.addView(mBasePanel);
                    break;
                case 1:
                    ((ArtActivity) activity).showSymbol();
                    pos = -1;
                    break;
                case 2:
                    ((ArtActivity) activity).onPhotoBtnClick();
                    this.pos = -1;
                    break;
                case 3:
                    mBasePanel = ArtBackgroundPanel.init(activity)
                            .setOnArtBgListener(mArtbgListener);
                    mFrame.addView(this.mBasePanel);
                    break;
                case 4:
                    mBasePanel = BrushPanel.init(activity)
                            .setBrushClickListener(new BrushPanel.BrushClickListener() {
                                @Override
                                public void onBrushClick(int position) {
                                    ((ArtActivity) getContext()).onBrushItemClick(position);
                                }
                            });
                    //mFrame.addView(mBasePanel);
                    addView(mBasePanel);
                    break;
                case 5:
                    ((ArtActivity) activity).showSticker();
                    pos = -1;
                    return;
            }
            if (this.mBasePanel != null) {
                this.mBasePanel.setVisibility(View.GONE);
                this.mBasePanel.show();
            }
        }
    }
}
