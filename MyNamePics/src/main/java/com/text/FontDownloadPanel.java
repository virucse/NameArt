package com.text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.downloader.DownloadListener;
import com.downloader.Downloader;
import com.downloader.FileUtils;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;

/**
 * Created by caliber fashion on 9/20/2017.
 */

public class FontDownloadPanel extends BaseOnlyScrollPanel<Integer[]> {
    public static int[] names;
    private static int[] icons;
    private static FontDownloadPanel instance;
    private TextItemPanelListner mListener;
    private TextSubPanel mBackgroundPanel;

    private FontDownloadPanel(Context context) {
        super(context);
        init();
    }

    private FontDownloadPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public static FontDownloadPanel initPanel(Activity activity) {
        if (instance != null) {
            instance.onDeAttach();
        }
        instance = new FontDownloadPanel(activity);
        instance.initView(activity);
        return instance;
    }

    void init() {
        icons = new int[]{R.mipmap.ic_save, R.mipmap.ic_save, R.mipmap.ic_save, R.mipmap.ic_save, R.mipmap.ic_save, R.mipmap.ic_save, R.mipmap.ic_save, R.mipmap.ic_save};
        names = new int[]{R.string.hindi_font, R.string.stylish_pack1, R.string.stylish_pack2, R.string.stylish_pack3, R.string.stylish_pack4, R.string.stylish_pack5, R.string.stylish_pack6, R.string.stylish_pack7};
    }

    protected void initView(Activity activity) {
        List<Integer[]> items = new ArrayList();
        for (int i = 0; i < icons.length; i++) {
            items.add(new Integer[]{icons[i], names[i]});
        }
        super.initView(activity, items);
    }

    public void onDeAttach() {
        //mBackgroundPanel = null;
        instance = null;
    }

    public FontDownloadPanel setDownloadedFileClickListener(TextItemPanelListner listener) {
        mListener = listener;
        return this;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        if (indexClicked <= -1) {
            indexClicked = 0;
        }
        File rootFile = null;
        switch (indexClicked) {
            case 0:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.FONTHINDI);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBackgroundView(0);
                } else {
                    downloadFonts(rootFile, "hindifont.zip", 0);
                }
                break;
            case 1:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.FONTPACK1);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBackgroundView(1);
                } else {
                    downloadFonts(rootFile, "pack1.zip", 1);
                }
                break;
            case 2:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.FONTPACK2);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBackgroundView(2);
                } else {
                    downloadFonts(rootFile, "pack2.zip", 2);
                }
                break;
            case 3:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.FONTPACK3);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBackgroundView(3);
                } else {
                    downloadFonts(rootFile, "pack3.zip", 3);
                }
                break;
            case 4:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.FONTPACK4);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBackgroundView(4);
                } else {
                    downloadFonts(rootFile, "pack4.zip", 4);
                }
                break;
            case 5:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.FONTPACK5);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBackgroundView(5);
                } else {
                    downloadFonts(rootFile, "pack5.zip", 5);
                }
                break;
            case 6:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.FONTPACK6);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBackgroundView(6);
                } else {
                    downloadFonts(rootFile, "pack6.zip", 6);
                }
                break;
            case 7:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.FONTPACK7);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBackgroundView(7);
                } else {
                    downloadFonts(rootFile, "pack7.zip", 7);
                }
                break;
        }
    }

    private void addBackgroundView(int position) {
        if(getContext()!=null){
            if (this.mBackgroundPanel != null) {
                removeView(this.mBackgroundPanel);
                mBackgroundPanel = null;
            }
            mBackgroundPanel = new TextSubPanel(getContext(),
                    TextPanelUtils.DOWNLOAD_FONT_BUTTON, AppUtils.screenHeight, position);
            mBackgroundPanel.setTextItemPanelListener(mListener);
            addView(mBackgroundPanel);
            this.mBackgroundPanel.setVisibility(View.GONE);
            this.mBackgroundPanel.show();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mBackgroundPanel != null) {
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
        }
        return false;
    }

    private void downloadFonts(File rootFile, String filename, final int position) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        String downUrl = AppUtils.getFontDownloadRootUrl() + filename;
        Downloader.download(getContext(), rootFile.getAbsolutePath(), downUrl, new DownloadListener() {
            @Override
            public void onDownloadStarted() {
                try {
                    BaseActivity baseActivity= (BaseActivity) getContext();
                    if(baseActivity!=null&&!baseActivity.isNetworkConnected()){
                        BaseActivity.showNetworkErrorMessage(baseActivity);
                    }else{
                        pd.show();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onDownloadCompleted() {
                try {
                    pd.dismiss();
                } catch (Exception e) {

                }
                addBackgroundView(position);
            }

            @Override
            public void onDownloadFailed() {
                try {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } catch (Exception e) {

                }
            }

            @Override
            public void onDownloadProgress(int progress) {
                try {
                    pd.setMessage("Please wait..." + progress);
                } catch (Exception e) {

                }
            }
        });
    }
}
