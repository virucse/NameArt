package nonworkingcode.grid.panel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.gallery.activity.ImageSelectAcivity;
import com.gallery.utils.GalleryUtil;
import com.gallery.utils.ImageUtils;
import com.text.TextMainPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nonworkingcode.effects.activity.EffectActivity;
import nonworkingcode.effects.util.EffectsUtils;
import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.effects.viewpanel.BasicPanel;
import nonworkingcode.grid.activity.CollageActivity;
import nonworkingcode.grid.custominterface.IOnColageItemClickListener;
import nonworkingcode.grid.util.CollageConst;
import nonworkingcode.grid.util.CollageUtil;

/**
 * Created by Caliber Fashion on 12/9/2016.
 */

public class CollagePanel extends BaseOnlyScrollPanel<Integer[]> {
    CollageOnBackStateChangeListener listener;
    private int[] names;
    private int pos;
    private int[] resIds;
    private BasicPanel mBasePanel;
    private FrameLayout mFrame;
    private CollageItemEditPanel mCollageItemEditPanel;

    public CollagePanel(Context context) {
        super(context);
        init(context);
    }

    public CollagePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.pos = -1;
        this.names = new int[]{R.string.cl_layout, R.string.cl_adjust, R.string.cl_ratio, R.string.cl_bg, R.string.cl_frame, R.string.cl_text, R.string.cl_sticker};
        this.resIds = new int[]{R.mipmap.ic_collage_grid, R.mipmap.ic_tune,
                R.mipmap.ic_ratio, R.mipmap.ic_background, R.mipmap.ic_frame, R.mipmap.ic_text, R.mipmap.ic_sticker};
        this.mBasePanel = null;
        if (context instanceof Activity) {
            initView((Activity) context);
        }
    }

    private void initView(Activity activity) {
        List<Integer[]> supers = new ArrayList();
        for (int i = 0; i < this.names.length; i++) {
            supers.add(new Integer[]{resIds[i], names[i]});
        }
        super.initView(activity, supers);
        mFrame = (FrameLayout) findViewById(R.id.frame_top);
    }

    public boolean onBackPressed() {
        this.listener.canBack(true);
        if (mCollageItemEditPanel != null) {
            this.listener.canBack(true);
            mCollageItemEditPanel.hide(new OnHideListener() {
                @Override
                public void onHideFinished_BasicPanel() {
                    removeView(CollagePanel.this.mCollageItemEditPanel);
                    mCollageItemEditPanel = null;
                }
            });
            return true;
        } else if (this.mBasePanel == null) {
            this.listener.canBack(true);
            return false;
        } else {
            this.listener.canBack(true);
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
    }

    @Override
    public boolean isSelectable() {
        return false;
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
                    mBasePanel = CollageLayoutContainer.init(activity, CollageConst.getImageSize());
                    mFrame.addView(mBasePanel);
                    break;
                case 1:
                    mBasePanel = CollageAdjustPanel.init(activity);
                    mFrame.addView(mBasePanel);
                    break;
                case 2:
                    mBasePanel = CollageRatioPanel.init(activity);
                    mFrame.addView(mBasePanel);
                    break;
                case 3:
                    mBasePanel = CollageBgContainerPanel.init(activity);
                    mFrame.addView(this.mBasePanel);
                    break;
                case 4:
                    //frame click
                    File rootFile = FileUtils.getDataDir(getContext(), AppUtils.BORDERFOLDERNAME);
                    if (rootFile.exists() && rootFile.list().length > 0) {
                        mBasePanel = new FramePanel(activity);
                        mFrame.addView(mBasePanel);
                    } else {
                        downloadFrame(rootFile);
                    }

                    break;
                case 5:
                    this.pos = -1;
                    mBasePanel = new TextMainPanel(activity);
                    ((TextMainPanel) mBasePanel).initView(activity);
                    mFrame.addView(mBasePanel);
                    break;
                case 6:
                    ((CollageActivity) activity).showSticker();
                    this.listener.canBack(false);
                    this.pos = -1;
                    return;
            }
            if (this.mBasePanel != null) {
                this.mBasePanel.setVisibility(View.GONE);
                this.mBasePanel.show();
            }
        }
    }

    public void setIOnBackStateChangeListener(CollageOnBackStateChangeListener l) {
        this.listener = l;
    }

    public void onCollageEditAt(int collageIndex) {
        if (this.mCollageItemEditPanel == null) {
            this.listener.canBack(false);
            mCollageItemEditPanel = CollageItemEditPanel.init((Activity) getContext(), collageIndex,
                    new IOnColageItemClickListener() {
                        @Override
                        public void onChange(int index) {
                            onChangeImage(index);
                        }

                        @Override
                        public void onEdit(int index) {
                            onEditImage(index);
                        }

                        @Override
                        public void onReset(int index) {
                            onResetImage(index);
                        }
                    });
            addView(this.mCollageItemEditPanel);
            mCollageItemEditPanel.setVisibility(View.GONE);
            mCollageItemEditPanel.show();
        }
    }

    private void onChangeImage(int index) {
        Intent intent = new Intent((getContext()), ImageSelectAcivity.class);
        intent.putExtra(GalleryUtil.EXTRA_INDEX, index);
        intent.putExtra(GalleryUtil.EXTRA_TYPE, GalleryUtil.APP_TYPE_COLLAGE);
        Activity act = ((Activity) getContext());
        act.startActivityForResult(intent, CollageUtil.REQ_REPLACE_IMAGE);
        CollageConst.collageView.setSelectedAtPosition(false, -1);
        CollagePanel.this.onBackPressed();
    }

    private void onEditImage(int index) {
        EffectsUtils.getInstance().setBitmap(CollageConst.collageBitmaps[index], true, true);
        Intent intent = new Intent(getContext(), EffectActivity.class);
        intent.putExtra(EffectsUtils.EXTRA_INDEX, index);
        intent.putExtra(EffectsUtils.EXTRA_TYPE, EffectsUtils.APP_TYPE_COLLAGE);

        Activity act = ((Activity) getContext());
        act.startActivityForResult(intent, CollageUtil.REQ_EDIT_IMAGE);

        CollagePanel.this.pos = -1;
        CollageConst.collageView.setSelectedAtPosition(false, -1);
        CollagePanel.this.onBackPressed();
    }

    public void onResetImage(int index) {
        Uri url = CollageConst.collageIds[index];
        if (CollageConst.effectApplied[index].booleanValue()) {
            CollageConst.collageView.update(ImageUtils.getinstance().loadBitmap(getContext(),
                    url, CollageConst.getImageSize()), index);
            CollageConst.effectApplied[index] = Boolean.valueOf(false);
        }
    }

    private void downloadFrame(File rootFile) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        String downUrl = AppUtils.getBorderDownloadRootUrl() + "border.zip";
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
                if(getContext()!=null&&!(((CollageActivity)getContext()).isFinishing())){
                    mBasePanel = new FramePanel(getContext());
                    mFrame.addView(mBasePanel);
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

    public interface CollageOnBackStateChangeListener {
        public void canBack(boolean canBack);
    }
}
