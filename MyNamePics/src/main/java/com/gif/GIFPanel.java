package com.gif;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.downloader.DownloadListener;
import com.downloader.Downloader;
import com.downloader.FileUtils;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AppUtils;
import com.text.TextMainPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.effects.viewpanel.BasicPanel;
import nonworkingcode.effects.viewpanel.LoadThumbAsync;
import nonworkingcode.grid.panel.BaseFramePanel;


/**
 * Created by ROSHAN on 11/21/2017.
 */

public class GIFPanel  extends BaseOnlyScrollPanel<Integer[]> {

    private int[] names;
    private int[] resIds;
    private BasicPanel mBasePanel;
    private Activity activity;
    private FrameLayout mFrame;
    private int selected = -1;
    public static int REQ_FOR_FILTER= 111;


    public GIFPanel(Context context) {
        super(context);
        init(context);
    }

    public GIFPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }



    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        if (selected == indexClicked&&indexClicked!=0) {
            selected = indexClicked;
            return;
        }else {
            selected = indexClicked;
        } if (this.mBasePanel != null) {
            removeView(this.mBasePanel);
            this.mBasePanel = null;
        }if (mFrame != null) {
            mFrame.removeView(mBasePanel);
            mFrame.removeAllViews();
        }
        if (mFrame == null) {
            mFrame = (FrameLayout) findViewById(R.id.frame_top);
        }if (indexClicked==0){
            ((GIFActivity) activity).changeSpeedOfGif();

        }
        else if (indexClicked == 1) {
          mBasePanel = TextMainPanel.init(activity);
            mFrame.addView(mBasePanel);
            mBasePanel.setVisibility(View.GONE);
            mBasePanel.show();
            return;
        } else if (indexClicked == 2) {
            File rootFile = FileUtils.getDataDir(getContext(), AppUtils.BORDERFOLDERNAME);
            if (rootFile.exists() && rootFile.list().length > 0) {
                mBasePanel = new FramePanel(getContext());
                mFrame.addView(this.mBasePanel);
                mBasePanel.setVisibility(View.GONE);
                mBasePanel.show();
            } else {
                downloadFrame(rootFile);
            }
            return;
        } else if (indexClicked == 3) {
            ((GIFActivity) activity).showSticker();
            return;
        }else if (indexClicked == 4){
            ((GIFActivity)activity).onFilterButtonClick();
            resetLinSelector();
            selected=-1;

        }

    }

    private void init(Context context) {
        names = new int[]{R.string.delay,R.string.text, R.string.frame, R.string.stickers,  R.string.filter};
        resIds = new int[]{R.mipmap.delay,R.mipmap.ic_text, R.mipmap.ic_frame, R.mipmap.ic_sticker, R.mipmap.ic_fx };
        mBasePanel = null;
        if (context instanceof Activity) {
            initView((Activity) context);
        }
    }

    private void initView(Activity activity) {
        this.activity = activity;
        List<Integer[]> supers = new ArrayList();
        for (int i = 0; i < this.names.length; i++) {
            supers.add(new Integer[]{this.resIds[i], this.names[i]});
        }
        super.initView(activity, supers);
        mFrame = (FrameLayout) findViewById(R.id.frame_top);
    }

    private void downloadFrame(File rootFile) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        String downUrl = AppUtils.getBorderDownloadRootUrl()+ "border.zip";
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
                if(mFrame!=null&&getContext()!=null&&!((GIFActivity)getContext()).isFinishing()){
                    mBasePanel = new FramePanel(getContext());
                    mFrame.addView(mBasePanel);
                    mBasePanel.setVisibility(View.GONE);
                    mBasePanel.show();
                }

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

    @Override
    public boolean onBackPressed() {
        selected = -1;

        if (this.mBasePanel == null) {
            return false;
        } else {
            if (mBasePanel.onBackPressed()) {
                return true;
            }
            mBasePanel.hide(new OnHideListener() {
                @Override
                public void onHideFinished_BasicPanel() {
                    mBasePanel.removeAllViews();
                    removeView(mBasePanel);
                    LoadThumbAsync.clear();
                    if (mFrame != null) {
                        mFrame.removeView(mBasePanel);
                        mFrame.removeAllViews();
                        mFrame = null;
                    }
                    mBasePanel = null;
                }
            });
            resetLinSelector();
            return true;
        }
    }

    class FramePanel extends BaseFramePanel {
        public FramePanel(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public FramePanel(Context context) {
            super(context);
        }

        @Override
        public int getSelected() {
            return 0;
        }

        @Override
        public boolean isSelectable() {
            return false;
        }

        @Override
        public void onItemClicked(int indexClicked) {
            if (indexClicked == 0) {
                ((GIFActivity) activity).addBorder(null);
                return;
            }
            try {
                String  path = getFramePath(indexClicked).getAbsolutePath();
                Bitmap bit = BitmapFactory.decodeFile(path);
                Drawable d = new BitmapDrawable(getResources(), bit);
                ((GIFActivity) activity).addBorder(d);
            } catch (OutOfMemoryError e) {
                System.gc();
            }

        }
    }
}
