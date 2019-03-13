package nonworkingcode.grid.panel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import nonworkingcode.grid.activity.CollageActivity;
import nonworkingcode.grid.background.BackgroundManager;
import nonworkingcode.grid.background.ColorResource;
import nonworkingcode.grid.background.GradientResource;
import nonworkingcode.grid.background.IBgResource;
import nonworkingcode.grid.background.PattenResource;
import nonworkingcode.grid.custominterface.IOnBackgroundChangeListener;
import nonworkingcode.grid.util.CollageConst;
import nonworkingcode.grid.util.CollageUtil;

/**
 * Created by Caliber Fashion on 12/11/2016.
 */

public class CollageBgContainerPanel extends BaseOnlyScrollPanel<Integer[]> {
    public static int[] names;
    private static int[] icons;
    private static CollageBgContainerPanel _ration_view;
    private CollageBGPanel mBackgroundPanel;
    private IOnBackgroundChangeListener backgroundChangeListener = new IOnBackgroundChangeListener() {
        @Override
        public void onBgBackOnClick(int i) {
            CollageBgContainerPanel.this.onBackPressed();
        }

        @Override
        public void onBgResourceChanged(int i, IBgResource iBgResource) {
            if (i == BackgroundManager.MODE_GRADIENT) {
                CollageConst.collageView.setHueValue(0.0f);
                CollageConst.collageView.setSquareBackground(((GradientResource) iBgResource).getGradientDrawable());
            }
        }

        @Override
        public void onBgResourceChangedByPressItem(int i, IBgResource iBgResource) {
            if (i == BackgroundManager.MODE_GRADIENT) {
                CollageConst.collageView.setSquareBackground(((GradientResource) iBgResource).getGradientDrawable());
            } else if (i == BackgroundManager.MODE_COLOR) {
                CollageConst.collageView.setSquareBackground(new ColorDrawable(((ColorResource) iBgResource).getColorValue()));
            } else if (i >= BackgroundManager.MODE_PATTERN) {
                CollageConst.collageView.setSquareBackground(((PattenResource) iBgResource).getDrawable());
            }
        }

        @Override
        public void onBgSeekbarChanged(int i, float f) {
            if (i == BackgroundManager.MODE_GRADIENT) {
                CollageConst.collageView.setHueValue(f);
                CollageConst.collageView.handleImage();
            }
        }
    };

    public CollageBgContainerPanel(Context context) {
        super(context);
        init();
    }

    public CollageBgContainerPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public static CollageBgContainerPanel init(Activity activity) {
        if (_ration_view != null) {
            _ration_view.onDeAttach();
        }
        _ration_view = new CollageBgContainerPanel(activity);
        _ration_view.initView(activity);
        return _ration_view;
    }

    void init() {
        icons = new int[]{R.mipmap.ic_colors, R.mipmap.ic_gradient, R.mipmap.tx_1, R.mipmap.tx_2, R.mipmap.tx_3, R.mipmap.tx_4, R.mipmap.photo_library};
        names = new int[]{R.string.bg_colorO, R.string.bg_gradient, R.string.bg_deco, R.string.bg_material, R.string.bg_pattern, R.string.bg_texture, R.string.gallery};
    }

    protected void initView(Activity activity) {
        List<Integer[]> items = new ArrayList();
        for (int i = 0; i < icons.length; i++) {
            items.add(new Integer[]{icons[i], names[i]});
        }
        super.initView(activity, items);
    }

    public void onDeAttach() {
        mBackgroundPanel = null;
        _ration_view = null;
    }

    private void addBgView(int mode) {
        if (CollageConst.collageView != null) {
            if (this.mBackgroundPanel != null) {
                removeView(this.mBackgroundPanel);
                this.mBackgroundPanel = null;
            }
            this.mBackgroundPanel = new CollageBGPanel(getContext(), mode, CollageUtil.screenHeight);
            this.mBackgroundPanel.setOnBgChangedListener(backgroundChangeListener);
            addView(this.mBackgroundPanel);
            this.mBackgroundPanel.setVisibility(View.GONE);
            this.mBackgroundPanel.show();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (this.mBackgroundPanel == null) {
            return false;
        }
        this.mBackgroundPanel.hide(new OnHideListener() {
            @Override
            public void onHideFinished_BasicPanel() {
                CollageBgContainerPanel.this.mBackgroundPanel.dispose();
                CollageBgContainerPanel.this.removeView(CollageBgContainerPanel.this.mBackgroundPanel);
                CollageBgContainerPanel.this.resetLinSelector();
                CollageBgContainerPanel.this.mBackgroundPanel = null;
            }
        });
        return true;
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
        File rootFile = null;
        switch (indexClicked) {
            case 0 :
                addBgView(BackgroundManager.MODE_COLOR);
                break;
            case 1 :
                addBgView(BackgroundManager.MODE_GRADIENT);
                break;
            case 2 :
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
                    String downloadUrl =AppUtils.getBgDownloadRootUrl() + "deco.zip";
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
                    String downloadUrl =AppUtils.getBgDownloadRootUrl() + "pattern.zip";
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
                        ((CollageActivity) getContext()).onBackPressed();
                        Bitmap bitmap = ArtActivity.uriToBitmap(getContext(), uri);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            CollageConst.collageView.setSquareBackground(new BitmapDrawable(getContext().getResources(), bitmap));
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
}
