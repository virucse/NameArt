package com.formationapps.artpanel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.downloader.DownloadListener;
import com.downloader.Downloader;
import com.downloader.FileUtils;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.ArtActivity;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.permission.MarseMallowPermission;
import com.gallery.activity.GalleryFragment;
import com.gallery.utils.GalleryUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.grid.background.BackgroundManager;
import nonworkingcode.grid.background.ColorResource;
import nonworkingcode.grid.background.GradientResource;
import nonworkingcode.grid.background.IBgResource;
import nonworkingcode.grid.background.PattenResource;
import nonworkingcode.grid.custominterface.IOnBackgroundChangeListener;
import nonworkingcode.grid.panel.CollageBGPanel;

/**
 * Created by caliber fashion on 9/20/2017.
 */

public class ArtBackgroundPanel extends BaseOnlyScrollPanel<Integer[]> {
    public static int[] names;
    private static int[] icons;
    private static ArtBackgroundPanel instance;
    private CollageBGPanel mBackgroundPanel;
    private ArtbgListener mArtbgListener;
    private IOnBackgroundChangeListener backgroundChangeListener = new IOnBackgroundChangeListener() {
        @Override
        public void onBgBackOnClick(int i) {
            onBackPressed();
        }

        @Override
        public void onBgResourceChanged(int i, IBgResource iBgResource) {
            if (i == BackgroundManager.MODE_GRADIENT) {
                if (mArtbgListener != null) {
                    mArtbgListener.setHueValue(0.0f);
                    mArtbgListener.setSquareBackground(((GradientResource) iBgResource).getGradientDrawable());
                }
            }
        }

        @Override
        public void onBgResourceChangedByPressItem(int i, IBgResource iBgResource) {
            if (mArtbgListener != null) {
                if (i == BackgroundManager.MODE_GRADIENT) {
                    mArtbgListener.setSquareBackground(((GradientResource) iBgResource).getGradientDrawable());
                } else if (i == BackgroundManager.MODE_COLOR) {
                    mArtbgListener.setSquareBackground(new ColorDrawable(((ColorResource) iBgResource).getColorValue()));
                } else if (i >= BackgroundManager.MODE_PATTERN) {
                    mArtbgListener.setSquareBackground(((PattenResource) iBgResource).getDrawable());
                }
            }
        }

        @Override
        public void onBgSeekbarChanged(int i, float f) {
            //Log.i("ARTBG","root");
            if (mArtbgListener != null) {
                // Log.i("ARTBG","!=null");
                if (i == BackgroundManager.MODE_GRADIENT) {
                    // Log.i("ARTBG","modeGrad");
                    mArtbgListener.setHueValue(f);
                    mArtbgListener.handleImage();
                }
            }

        }
    };

    public ArtBackgroundPanel(Context context) {
        super(context);
        init();
    }

    public ArtBackgroundPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public static ArtBackgroundPanel init(Activity activity) {
        if (instance != null) {
            instance.onDeAttach();
        }
        instance = new ArtBackgroundPanel(activity);
        instance.initView(activity);
        return instance;
    }

    void init() {
        icons = new int[]{R.mipmap.ic_colors, R.mipmap.ic_gradient, R.mipmap.tx_1, R.mipmap.tx_2, R.mipmap.tx_3, R.mipmap.tx_4, R.mipmap.photo_library};
        names = new int[]{R.string.bg_colorO, R.string.bg_gradient, R.string.bg_deco, R.string.bg_material, R.string.bg_pattern, R.string.bg_texture, R.string.gallery};
    }

    protected void initView(Activity activity) {
        List<Integer[]> items = new ArrayList();
        for (int i = 0; i < icons.length; i++) {
            items.add(new Integer[]{Integer.valueOf(icons[i]), Integer.valueOf(names[i])});
        }
        super.initView(activity, items);
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        if (indexClicked == -1) {
            indexClicked = 0;
        }
        File rootFile = null;
        switch (indexClicked) {
            case 0 /*0*/:
                addBgView(BackgroundManager.MODE_COLOR);
                break;
            case 1 /*1*/:
                addBgView(BackgroundManager.MODE_GRADIENT);
                break;
            case 2 /*2*/:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.BACKGROUNDMATERIAL);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBgView(BackgroundManager.MODE_MATERIAL);
                } else {
                    String downloadUrl = AppUtils.getBgDownloadRootUrl() + "material.zip";
                    downloadBackGround(rootFile, downloadUrl, BackgroundManager.MODE_MATERIAL);
                }
                break;
            case 3 /*3*/:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.BACKGROUNDDECO);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBgView(BackgroundManager.MODE_DECO);
                } else {
                    String downloadUrl = AppUtils.getBgDownloadRootUrl() + "deco.zip";
                    downloadBackGround(rootFile, downloadUrl, BackgroundManager.MODE_DECO);
                }
                break;
            case 4 /*4*/:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.BACKGROUNDTEXTURE);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBgView(BackgroundManager.MODE_TEXTURE);
                } else {
                    String downloadUrl = AppUtils.getBgDownloadRootUrl() + "texture.zip";
                    downloadBackGround(rootFile, downloadUrl, BackgroundManager.MODE_TEXTURE);
                }
                break;
            case 5 /*5*/:
                rootFile = FileUtils.getDataDir(getContext(), AppUtils.BACKGROUNDPATTERN);
                if (rootFile.exists() && rootFile.list().length > 1) {
                    addBgView(BackgroundManager.MODE_PATTERN);
                } else {
                    String downloadUrl = AppUtils.getBgDownloadRootUrl() + "pattern.zip";
                    downloadBackGround(rootFile, downloadUrl, BackgroundManager.MODE_PATTERN);
                }
                break;
            case 6:
                if (!MarseMallowPermission.storagePermitted((Activity) getContext(), 66)) {
                    return;
                }
                GalleryFragment gf = GalleryFragment.newInstance(new GalleryFragment.OnGalleryFinishListener() {
                    @Override
                    public void onImageChooseListener(Uri uri) {
                        if (mArtbgListener != null) {
                            mArtbgListener.onImageSelectedFromGallery(uri);
                            ((ArtActivity) getContext()).onBackPressed();
                        }
                    }

                    @Override
                    public int getAppType() {
                        return GalleryUtil.APP_TYPE_DEFAULT;
                    }
                });
                ((BaseActivity) getContext()).showGalleryFragment(gf);
                break;
            default:
        }
    }

    private void downloadBackGround(File rootFile, String downloadUrl, final int mode) {
        if (rootFile == null) return;
        //download
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        String unzipPath = rootFile.getAbsolutePath();
        Downloader.download(getContext(), unzipPath, downloadUrl, new DownloadListener() {
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
                addBgView(mode);
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

    private void addBgView(int mode) {
        if (this.mBackgroundPanel != null) {
            removeView(this.mBackgroundPanel);
            this.mBackgroundPanel = null;
        }
        this.mBackgroundPanel = new CollageBGPanel(getContext(), mode, AppUtils.screenHeight);
        this.mBackgroundPanel.setOnBgChangedListener(backgroundChangeListener);
        addView(this.mBackgroundPanel);
        this.mBackgroundPanel.setVisibility(View.GONE);
        this.mBackgroundPanel.show();

    }

    public void onDeAttach() {
        mBackgroundPanel = null;
        instance = null;
    }

    @Override
    public boolean onBackPressed() {
        if (this.mBackgroundPanel == null) {
            return false;
        }
        this.mBackgroundPanel.hide(new OnHideListener() {
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

    public ArtBackgroundPanel setOnArtBgListener(ArtbgListener listener) {
        mArtbgListener = listener;
        return this;
    }
}
