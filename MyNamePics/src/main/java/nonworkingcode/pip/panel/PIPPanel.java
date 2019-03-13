package nonworkingcode.pip.panel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import net.qiujuer.genius.blur.StackBlur;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nonworkingcode.effects.activity.EffectActivity;
import nonworkingcode.effects.util.EffectsUtils;
import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.effects.viewpanel.BaseSeekPanel;
import nonworkingcode.effects.viewpanel.BasicPanel;
import nonworkingcode.grid.panel.BaseFramePanel;
import nonworkingcode.pip.activity.PIPActivity;
import nonworkingcode.pip.custominterface.OnBlurListener;
import nonworkingcode.pip.util.PIPUtil;
import nonworkingcode.pip.views.PIPView;

/**
 * Created by Caliber Fashion on 12/16/2016.
 */

public class PIPPanel extends BaseOnlyScrollPanel<Integer[]> {
    private float blurValue;
    private BasicPanel mBasePanel;
    private int selected;
    private int[] names, resIds;
    private PIPView mPIPView;
    private FrameLayout mFrame;
    private Bitmap sourceBitmap;
    private OnBackStateChangeListener listener;
    private BlurPanel mBlurPanel;

    public PIPPanel(Context context) {
        super(context);
        init();
    }

    public PIPPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        blurValue = 20.0f;
        mBasePanel = null;
        selected = -1;
        names = new int[]{R.string.pip, R.string.blur, R.string.cl_sticker, R.string.filter, R.string.frame,
                R.string.cl_text, R.string.cl_bg};
        resIds = new int[]{R.mipmap.ic_pip, R.mipmap.ic_blur, R.mipmap.ic_sticker, R.mipmap.ic_filters,
                R.mipmap.ic_frame, R.mipmap.ic_text, R.mipmap.ic_background};
    }

    public void initView(Activity activity, PIPView sizeView) {
        mPIPView = sizeView;
        List<Integer[]> supers = new ArrayList();
        for (int i = 0; i < this.names.length; i++) {
            supers.add(new Integer[]{resIds[i], names[i]});
        }
        super.initView(activity, supers);
        mFrame = (FrameLayout) findViewById(R.id.frame_top);
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        if (this.selected != indexClicked) {
            this.selected = indexClicked;
            Activity activity = null;
            if (getContext() instanceof Activity) {
                activity = (Activity) getContext();
            }
            if (!(this.mBasePanel == null || this.mFrame == null)) {
                this.mBasePanel.onDeAttach();
                this.mFrame.removeView(this.mBasePanel);
                this.mBasePanel = null;
            }
            switch (indexClicked) {
                case 0 /*0*/:
                    this.mBasePanel = PipIconPanel.init(activity, this.mPIPView);
                    this.mFrame.addView(this.mBasePanel);
                    break;
                case 1 /*1*/:
                    mBlurPanel = new BlurPanel(getContext());
                    mBlurPanel.setOnBlurListener(new OnBlurListener() {
                        @Override
                        public void onBlurOk(int progress) {
                            blurValue = progress;
                            mPIPView.setSquareBackground(blur(sourceBitmap, blurValue));
                            mBlurPanel = null;
                        }

                        @Override
                        public void onBlurCancle(int progress) {
                            mPIPView.setSquareBackground(blur(sourceBitmap, blurValue));
                            mBlurPanel = null;
                        }
                    });
                    addView(this.mBlurPanel);
                    mBlurPanel.setVisibility(GONE);
                    mBlurPanel.show();
                    resetLinSelector();
                    this.selected = -1;
                    return;
                case 2 /*2*/:
                    this.selected = -1;
                    resetLinSelector();
                    ((PIPActivity) activity).showSticker();
                    return;
                case 3 /*3*/:
                    Bitmap b = mPIPView.getBitmap();
                    if (b != null) {
                        EffectsUtils.getInstance().setBitmap(b, true, true);
                        ((Activity) getContext()).startActivityForResult(
                                new Intent(getContext(), EffectActivity.class), PIPUtil.REQ_FOR_FILTER);
                        this.selected = -1;
                        resetLinSelector();
                    }
                    return;
                case 4 /*4*/:
                    //frame working
                    File rootFile = FileUtils.getDataDir(getContext(), AppUtils.BORDERFOLDERNAME);
                    if (rootFile.exists() && rootFile.list().length > 0) {
                        this.mBasePanel = new FramePanel(getContext());
                        this.mFrame.addView(this.mBasePanel);
                    } else {
                        downloadFrame(rootFile);
                    }

                    break;
                case 5:
                    this.selected = -1;
                    mBasePanel = new TextMainPanel(activity);
                    ((TextMainPanel) mBasePanel).initView(activity);
                    mFrame.addView(mBasePanel);
                    break;
                case 6 /*5*/:
                    mBasePanel = new PIPBgContainerPanel(activity);
                    ((PIPBgContainerPanel) this.mBasePanel).initView(activity, mPIPView);
                    this.mFrame.addView(this.mBasePanel);
                    break;
            }
            if (this.mBasePanel != null) {
                mBasePanel.setVisibility(View.GONE);
                mBasePanel.show();
            }
        }
    }

    public void onDeAttach() {
        super.onDeAttach();
    }

    public boolean onBackPressed() {
        if (this.mBlurPanel != null) {
            this.mBlurPanel.onBackPressed();
            this.listener.onBackPressed(true);
            mBlurPanel = null;
            return true;
        } else if (this.mBasePanel == null) {
            return false;
        } else {
            if (mBasePanel.onBackPressed()) {
                return true;
            }
            this.listener.onBackPressed(true);
            if (mFrame != null) {
                this.mBasePanel.hide(new OnHideListener() {
                    @Override
                    public void onHideFinished_BasicPanel() {
                        selected = -1;
                        mFrame.removeView(mBasePanel);
                        mBasePanel = null;
                    }
                });
            }
            resetLinSelector();
            return true;
        }
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        } else {
            sourceBitmap = bitmap;
            try {
                mPIPView.setSquareBackground(blur(sourceBitmap, blurValue));
            } catch (Exception e) {

            }
        }
    }

    public void setIOnBackStateChangeListener(OnBackStateChangeListener l) {
        listener = l;
    }

    public synchronized Bitmap blur(Bitmap image, float radius) {
        if (image == null || image.isRecycled()) {
            return null;
        }
        /*BlurFactor blurFactor = new BlurFactor();
        blurFactor.width = image.getWidth();
        blurFactor.height = image.getHeight();
        blurFactor.radius = (int) radius;

       return Blur.of(getContext(),image, blurFactor);*/
        Bitmap.Config config = image.getConfig();
        if (config != Bitmap.Config.ARGB_8888 && config != Bitmap.Config.RGB_565) {
            //throw new RuntimeException("Blur bitmap only supported Bitmap.Config.ARGB_8888 and Bitmap.Config.RGB_565.");
            Toast.makeText(getContext(),"This image is not blur supportable",Toast.LENGTH_SHORT).show();
        }

        return StackBlur.blurNatively(image, (int) radius / 2, false);
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
                if (getContext()!= null&&!((PIPActivity)getContext()).isFinishing()) {
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

    public interface OnBackStateChangeListener {
        public void onBackPressed(boolean visible);
    }

    //blur filter
    class BlurPanel extends BaseSeekPanel {
        private int progress;
        private OnBlurListener blurListener;

        public BlurPanel(Context context) {
            super(context);
            super.initView((Activity) context);
          //  setSeekBarInitialValue(10,((int) blurValue)/10);
            updateProgress((int) blurValue);
        }

        void setOnBlurListener(OnBlurListener listener) {
            blurListener = listener;
        }

        @Override
        public String getTitle() {
            return "Blur";
        }

        @Override
        public void onDeAttach() {
        }

        @Override
        public boolean onBackPressed() {
            hide();
            if (blurListener != null)
                blurListener.onBlurCancle(progress);
            return true;
        }

        @Override
        public void onSeekChange(int i) {
            progress = i;
            mPIPView.setSquareBackground(blur(sourceBitmap, progress==0?2:progress));

        }

        @Override
        public void onApplied() {
            if (this.blurListener != null) {
                blurListener.onBlurOk(progress);
            }
            hide();
        }

        @Override
        public void onDiscarded() {
            hide();
            if (blurListener != null) {
                blurListener.onBlurCancle(progress);
            }
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
            return PIPPanel.this.mPIPView.getFrameId();
        }

        @Override
        public boolean isSelectable() {
            return true;
        }

        @Override
        public void onItemClicked(int indexClicked) {
            mPIPView.setCustomBorderId(indexClicked - 1, getFramePath(indexClicked));
        }
    }
}
