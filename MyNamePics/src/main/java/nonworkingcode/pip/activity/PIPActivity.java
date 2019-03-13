package nonworkingcode.pip.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.downloader.DownloadListener;
import com.downloader.Downloader;
import com.downloader.FileUtils;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
import com.gallery.utils.ImageUtils;

import java.io.File;

import formationapps.helper.stickers.StickerFragment;
import nonworkingcode.effects.util.EffectsUtils;
import nonworkingcode.grid.util.CollageUtil;
import nonworkingcode.pip.panel.PIPPanel;
import nonworkingcode.pip.util.PIPFileModel;
import nonworkingcode.pip.util.PIPUtil;
import nonworkingcode.pip.util.PipManager;
import nonworkingcode.pip.views.PIPView;

/**
 * Created by Caliber Fashion on 12/15/2016.
 */

public class PIPActivity extends BaseActivity {
    private static final String TAG = PIPActivity.class.getSimpleName();
    private static final String FRAGMENT_TAG = "fgtag";
    private PIPPanel mPanel;
    private FrameLayout sizeFrame;
    private PIPView mPIPView;
    private ImageButton ibFlipY, ibFlipX, ibSave;
    private Uri imageUri;
    private Bitmap sourceBitmap;
    private Thread mThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pip_activity);
        logAnalyticEvent("PIP_ACTIVITY");

        PIPUtil.onCreate(this);
        //PipManager.saveData(this);
        AdsHelper.loadAdmobBanner(this, (LinearLayout) findViewById(R.id.ad_container));

        mPanel = (PIPPanel) findViewById(R.id.panel_edmenu);

        sizeFrame = (FrameLayout) findViewById(R.id.rel_ed_work_view);
        mPIPView = (PIPView) findViewById(R.id.pipView);

        mPanel.setIOnBackStateChangeListener(new PIPPanel.OnBackStateChangeListener() {
            @Override
            public void onBackPressed(boolean visible) {
                PIPUtil.showAndHideView(findViewById(R.id.top_bar_ac), visible);
            }
        });
        if (getIntent().getData() != null) {
            handleImage(getIntent().getData());

            this.ibFlipX = (ImageButton) findViewById(R.id.ib_edtr_flipX);
            AppUtils.setImage(ibFlipX, R.mipmap.ic_flip_vertical, Color.WHITE);
            ibFlipX.setVisibility(View.INVISIBLE);

            this.ibFlipY = (ImageButton) findViewById(R.id.ib_edtr_flipY);
            AppUtils.setImage(ibFlipY, R.mipmap.ic_flip_horizontal, Color.WHITE);
            ibFlipY.setVisibility(View.INVISIBLE);

            this.ibSave = (ImageButton) findViewById(R.id.ib_edtr_save);
            //this.ibPopup = (ImageButton) findViewById(C0174R.id.ib_edtr_menu);
            AppUtils.setImage(this.ibSave, R.mipmap.ic_save, Color.WHITE);

            this.ibFlipX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPIPView.setRotationX(mPIPView.getRotationX() == -180f ? 0f : -180f);
                    mPIPView.invalidate();
                }
            });
            ibFlipY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPIPView.setRotationY(mPIPView.getRotationY() == -180.0f ? 0.0f : -180.0f);
                    mPIPView.invalidate();
                }
            });
            this.ibSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //StickerConst.invalidateStickers();
                    //stickerView.setLocked(true);
                    saveButtonClicked(mPIPView.getView());
                }
            });
            //this.ibPopup.setOnClickListener(this.listener);
            return;
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PIPUtil.REQ_FOR_FILTER:
                if (resultCode == RESULT_OK) {
                    mPIPView.setImageBitmap(EffectsUtils.getInstance().getBitmap(true, true), true);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment frag = getSupportFragmentManager().findFragmentByTag("text");
        if (frag != null) {
            getSupportFragmentManager().beginTransaction().remove(frag).commit();
            //CollageUtil.hideAndShow(findViewById(R.id.top_bar_ac),true);
            //StickerConst.invalidateStickers();
            this.mPanel.resetLinSelector();
            return;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            CollageUtil.hideAndShow(findViewById(R.id.top_bar_ac), true);
            //StickerConst.invalidateStickers();
            this.mPanel.resetLinSelector();
            return;
        }
        //StickerConst.invalidateStickers();

        if (!this.mPanel.onBackPressed()) {
            //super.onBackPressed();
            CollageUtil.hideAndShow(findViewById(R.id.top_bar_ac), true);
            showBackpressedDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stickerView != null) {
            stickerView.setLocked(false);
        }
    }

    private void handleImage(Uri path) {
        if (path != null) {
            this.imageUri = path;
        } else {
            Toast.makeText(this, "Uri:" + path, Toast.LENGTH_SHORT).show();
            finish();
        }
        //LoadingDialog.show(this);
        int size = AppUtils.screenWidth;
        sizeFrame.setLayoutParams(new FrameLayout.LayoutParams(size, size));
        mThread = new ImageLoadThread();
        if (checkPIP()) {
            mThread.run();
        }
    }

    public void showText() {
        CollageUtil.onCreate(this);
        //getSupportFragmentManager().beginTransaction().add(R.id.rel_content, new TextFragment(), FRAGMENT_TAG).commit();
    }

    public void showSticker() {
        CollageUtil.onCreate(this);
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content, new StickerFragment(), FRAGMENT_TAG).commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        mPanel.onDeAttach();
        super.onDestroy();
        CollageUtil.onDestroy();
    }

    private boolean checkPIP() {
        String path = FileUtils.getDataDir(PIPActivity.this, AppUtils.PIP + "").getAbsolutePath();
        File pipFile = new File(path);
        if (pipFile.exists() && pipFile.list().length > 5) {
            return true;
        } else {
            //download
            final ProgressDialog pd = new ProgressDialog(PIPActivity.this);
            pd.setMessage("Please wait...");
            String downpath = AppUtils.getPIPDownloadRootUrl() + "pip.zip";
            Downloader.download(PIPActivity.this, path, downpath, new DownloadListener() {
                @Override
                public void onDownloadStarted() {
                    try {
                        BaseActivity baseActivity= PIPActivity.this;
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
                    mThread.run();
                }

                @Override
                public void onDownloadFailed() {
                    try {
                        Toast.makeText(PIPActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
        return false;
    }

    class ImageLoadThread extends Thread {
        @Override
        public void run() {
            try {
                if (!(sourceBitmap == null || sourceBitmap.isRecycled())) {
                    sourceBitmap.recycle();
                }
                sourceBitmap = null;
                System.gc();
                sourceBitmap = ImageUtils.getinstance().loadImage(PIPActivity.this, imageUri,
                        AppUtils.screenWidth, AppUtils.screenWidth);
                PIPActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing() && sourceBitmap != null) {
                            mPanel.initView(PIPActivity.this, mPIPView);
                            mPIPView.setPictureImageBitmap(sourceBitmap);
                            mPanel.setBitmap(sourceBitmap);
                            mPIPView.onPipSelected((PIPFileModel) PipManager.getFileModelList().get(0));
                            setStickerOperation(mPIPView.getStickerView());
                            //LoadingDialog.dismiss();
                        }
                    }
                });
            } catch (Exception e) {
                Log.d(TAG, "run: " + e.getMessage());
            }
        }
    }
}
