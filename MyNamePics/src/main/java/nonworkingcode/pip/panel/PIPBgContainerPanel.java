package nonworkingcode.pip.panel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.AttributeSet;
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
import nonworkingcode.pip.activity.PIPActivity;
import nonworkingcode.pip.custominterface.PIPInterface;
import nonworkingcode.pip.util.PIPUtil;

public class PIPBgContainerPanel extends BaseOnlyScrollPanel<Integer[]> {
    public static int[] names;
    private static PIPBgContainerPanel _ration_view;
    private static int[] bgs;

    static {
        bgs = new int[]{R.mipmap.ic_colors, R.mipmap.ic_gradient, R.mipmap.tx_3, R.mipmap.tx_1, R.mipmap.tx_2, R.mipmap.tx_4, R.mipmap.photo_library};
        names = new int[]{R.string.bg_color, R.string.bg_gradient, R.string.bg_material, R.string.bg_deco, R.string.bg_texture, R.string.bg_pattern, R.string.gallery};
    }

    private CollageBGPanel mBackgroundPanel;
    private PIPInterface pipView;

    public PIPBgContainerPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PIPBgContainerPanel(Context context) {
        super(context);
    }

    public static PIPBgContainerPanel getInstance() {
        return _ration_view;
    }

    @Override
    public boolean isSelectable() {
        return canSelectable();
    }

    @Override
    public void onItemClicked(int indexClicked) {
        onItemSelected(indexClicked);
    }

    protected void initView(Activity activity, PIPInterface pipView) {
        this.pipView = pipView;
        List<Integer[]> items = new ArrayList();
        for (int i = 0; i < bgs.length; i++) {
            items.add(new Integer[]{Integer.valueOf(bgs[i]), Integer.valueOf(names[i])});
        }
        super.initView(activity, items);
    }

    private void addBgView(int mode) {
        if (this.pipView != null) {
            if (this.mBackgroundPanel != null) {
                removeView(this.mBackgroundPanel);
                this.mBackgroundPanel = null;
            }
            this.mBackgroundPanel = new CollageBGPanel(getContext(), mode, PIPUtil.screenHeight);
            this.mBackgroundPanel.setOnBgChangedListener(new OnBackgroundChange());
            addView(this.mBackgroundPanel);
            this.mBackgroundPanel.setVisibility(GONE);
            this.mBackgroundPanel.show();
        }
    }

    public void show() {
        super.show();
    }

    public void onItemSelected(int index) {
        if (index == -1) {
            index = 0;
        }
        File rootFile = null;
        switch (index) {
            case 0 /*0*/:
                addBgView(BackgroundManager.MODE_COLOR);
                return;
            case 1 /*1*/:
                addBgView(BackgroundManager.MODE_GRADIENT);
                return;
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
                        ((PIPActivity) getContext()).onBackPressed();
                        Bitmap bitmap = ArtActivity.uriToBitmap(getContext(), uri);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            pipView.setSquareBackground(bitmap);
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
                return;
        }
        //addBgView(BackgroundManager.MODE_PATTERN);
    }

    public boolean onBackPressed() {
        if (this.mBackgroundPanel == null) {
            return false;
        }
        this.mBackgroundPanel.hide(new OnHideListener() {
            @Override
            public void onHideFinished_BasicPanel() {
                mBackgroundPanel.dispose();
                removeView(PIPBgContainerPanel.this.mBackgroundPanel);
                resetLinSelector();
                mBackgroundPanel = null;
            }
        });
        return true;
    }

    public boolean canSelectable() {
        return true;
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

    class OnBackgroundChange implements IOnBackgroundChangeListener {

        @Override
        public void onBgSeekbarChanged(int mode, float var1) {
            if (mode == BackgroundManager.MODE_GRADIENT) {
                pipView.setHueValue(var1);
                pipView.handleImage();
            }
        }

        @Override
        public void onBgResourceChangedByPressItem(int mode, IBgResource res) {
            if (mode == BackgroundManager.MODE_GRADIENT) {
                pipView.setSquareBackground(((GradientResource) res).getGradientDrawable());
            } else if (mode == BackgroundManager.MODE_COLOR) {
                pipView.setSquareBackground(new ColorDrawable(((ColorResource) res).getColorValue()));
            } else if (mode >= BackgroundManager.MODE_PATTERN) {
                pipView.setSquareBackground(((PattenResource) res).getDrawable());
            }
        }

        @Override
        public void onBgResourceChanged(int mode, IBgResource res) {
            if (mode == BackgroundManager.MODE_GRADIENT) {
                pipView.setHueValue(0.0f);
                pipView.setSquareBackground(((GradientResource) res).getGradientDrawable());
            }
        }

        @Override
        public void onBgBackOnClick(int mode) {
            onBackPressed();
        }
    }
}
