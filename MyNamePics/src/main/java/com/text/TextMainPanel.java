package com.text;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.View;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.effects.viewpanel.BasicPanel;

/**
 * Created by caliber fashion on 9/19/2017.
 */

public class TextMainPanel extends BaseOnlyScrollPanel<Integer[]> {
    public static int[] names;
    private static int[] icons;
    private static TextMainPanel instanse;
    private Activity mActivity;
    private TextSubPanel mBackgroundPanel;

    private BasicPanel mBasePanel;
    private TextItemPanelListner mListener = new TextItemPanelListner() {
        @Override
        public void onBackClick() {
            onBackPressed();
        }

        @Override
        public void onShadowClick(TextColorRes res, float radius) {
            ((BaseActivity) mActivity).setShadow(radius, 10.0f, 10.0f, res.getColorValue());
        }

        @Override
        public void onTextSizeChange(int progress) {
            ((BaseActivity) mActivity).setTextSize(progress);
        }

        public void onFontClick(String fontPath) {
            ((BaseActivity) mActivity).setFont(fontPath, null);
        }

        @Override
        public void onTextureClick(int resId) {
            ((BaseActivity) mActivity).setTextureOnText(resId);
        }

        public void onDownloadedFontClick(File fontFile) {
            ((BaseActivity) mActivity).setFont(null, fontFile);
        }
    };

    public TextMainPanel(Context context) {
        super(context);
        init();
    }

    public static TextMainPanel init(Activity activity) {
        if (instanse != null) {
            instanse.onDeAttach();
        }
        instanse = new TextMainPanel(activity);
        instanse.initView(activity);
        return instanse;
    }

    void init() {
        icons = new int[]{R.mipmap.add_text_icon, R.mipmap.add_text_icon, R.mipmap.format_bold, R.mipmap.format_italic, R.mipmap.format_underline, R.mipmap.shadow,
                R.mipmap.format_align_left, R.mipmap.format_align_center, R.mipmap.format_align_right, R.mipmap.text_size, R.mipmap.font_icon,
                R.mipmap.color_icon, R.mipmap.texture_icon};
        names = new int[]{R.string.add_new_text, R.string.creative, R.string.bold_format, R.string.italic_format, R.string.underline_format, R.string.shadow,
                R.string.left_align, R.string.center_align, R.string.right_align, R.string.text_size, R.string.font,
                R.string.text_color, R.string.texture};

        mBasePanel = null;
    }

    public void initView(Activity activity) {
        mActivity = activity;
        List<Integer[]> items = new ArrayList();
        for (int i = 0; i < icons.length; i++) {
            items.add(new Integer[]{Integer.valueOf(icons[i]), Integer.valueOf(names[i])});
        }
        super.initView(activity, items);
    }

    public void onDeAttach() {
        mBackgroundPanel = null;
        instanse = null;
    }

    @Override
    public boolean onBackPressed() {
        if (mBackgroundPanel != null) {
            if (mBackgroundPanel.onBackPressed()) {
                return true;
            }
            mBackgroundPanel.hide(new OnHideListener() {
                @Override
                public void onHideFinished_BasicPanel() {
                    mBackgroundPanel.dispose();
                    removeView(mBackgroundPanel);
                    resetLinSelector();
                    mBackgroundPanel = null;
                }
            });
            return true;
        } else if (mBasePanel != null) {
            if (mBasePanel.onBackPressed()) {
                return true;
            }
            mBasePanel.hide(new OnHideListener() {
                @Override
                public void onHideFinished_BasicPanel() {
                    removeView(mBasePanel);
                    resetLinSelector();
                    mBasePanel = null;
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        if (indexClicked == -1) {
            indexClicked = 0;
        }
        Activity activity = null;
        if (getContext() instanceof Activity) {
            activity = (Activity) getContext();
        }
        if (mBasePanel != null) {
            removeView(mBasePanel);
            mBasePanel = null;
        }

        switch (indexClicked) {
            case 0:
                ((BaseActivity) mActivity).addNewText();
                break;
            case 1:
                ((BaseActivity) mActivity).setTextToCreative();
                break;
            case 2:
                ((BaseActivity) mActivity).setTextBold();
                break;
            case 3:
                ((BaseActivity) mActivity).setTextItalic();
                break;
            case 4:
                ((BaseActivity) mActivity).setTextUnderLines();
                break;
            case 5:
                addBackgroundView(TextPanelUtils.SHADOW_BUTTON);
                break;
            case 6:
                ((BaseActivity) mActivity).setTextAlignMent(Layout.Alignment.ALIGN_NORMAL);
                break;
            case 7:
                ((BaseActivity) mActivity).setTextAlignMent(Layout.Alignment.ALIGN_CENTER);
                break;
            case 8:
                ((BaseActivity) mActivity).setTextAlignMent(Layout.Alignment.ALIGN_OPPOSITE);
                break;
            case 9:
                addBackgroundView(TextPanelUtils.TEXTSIZE_BUTTON);
                break;
            case 10:
                addBackgroundView(TextPanelUtils.FONTS_BUTTON);
                break;
            case 11:
                ((BaseActivity) mActivity).setTextColor();
                break;
            case 12:
                addBackgroundView(TextPanelUtils.TEXTURE_BUTTON);
                break;
        }
    }

    private void addBackgroundView(int mode) {
        if (this.mBackgroundPanel != null) {
            removeView(this.mBackgroundPanel);
            mBackgroundPanel = null;
        }
        mBackgroundPanel = new TextSubPanel(getContext(), mode, AppUtils.screenHeight);
        mBackgroundPanel.setTextItemPanelListener(mListener);
        addView(mBackgroundPanel);
        this.mBackgroundPanel.setVisibility(View.GONE);
        this.mBackgroundPanel.show();

    }

    public void showFontDownloadPanel(Activity activity) {
        mBasePanel = FontDownloadPanel.initPanel(activity)
                .setDownloadedFileClickListener(mListener);
        addView(mBasePanel);
        mBasePanel.setVisibility(View.GONE);
        mBasePanel.show();
    }
}