package com.editor.panel;

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
import com.editor.activity.EditorActivity;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AppUtils;
import com.text.TextMainPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import nonworkingcode.effects.custuminterface.IOnBackStateChangeListener;
import nonworkingcode.effects.filters.GPUImageFilterTools;
import nonworkingcode.effects.util.EffectsUtils;
import nonworkingcode.effects.viewpanel.BaseOnlyScrollPanel;
import nonworkingcode.effects.viewpanel.BasicPanel;
import nonworkingcode.effects.viewpanel.EditorNames;
import nonworkingcode.effects.viewpanel.FilterScrPanel;
import nonworkingcode.effects.viewpanel.FilterSeekPanel;
import nonworkingcode.effects.viewpanel.LoadThumbAsync;
import nonworkingcode.grid.panel.BaseFramePanel;

/**
 * Created by Caliber Fashion on 12/2/2016.
 */

public class EditorPanel extends BaseOnlyScrollPanel<Integer[]> {
    private static List<GPUImageFilterTools.FilterType> filters = new ArrayList<>();
    private int[] names;
    private int[] resIds;
    private BasicPanel mBasePanel;
    //private FilterPanel mFilterPanel;
    private Activity activity;
    private FilterSeekPanel mFilterSeekPanel;
    private GPUImageView mGpuImageView;
    //private Bitmap sourceBitmap;
    private FrameLayout mFrame;
    private IOnBackStateChangeListener listener;
    private int selected = -1;

    public EditorPanel(Context context) {
        super(context);
        init(context);
    }

    public EditorPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        names = new int[]{R.string.text, R.string.frame, R.string.stickers, R.string.crop,
                /*R.string.ef_filter, R.string.ef_effects, R.string.ef_overlay, R.string.ef_texture,*/
                R.string.brightness, R.string.contrast, R.string.sharpness,
                R.string.balance, R.string.saturation, R.string.highlights, R.string.shadow2, R.string.temperature};
        resIds = new int[]{R.mipmap.ic_text, R.mipmap.ic_frame, R.mipmap.ic_sticker, R.mipmap.ic_crop,
                /*R.mipmap.ic_fx, R.mipmap.ic_filters, R.mipmap.ic_overlay_light, R.mipmap.ic_texture,*/
                R.mipmap.ic_brightness,
                R.mipmap.ic_contrast, R.mipmap.ic_sharpness, R.mipmap.ic_balance, R.mipmap.ic_saturation,
                R.mipmap.ic_highlight, R.mipmap.ic_shadow, R.mipmap.ic_temperature};
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

    public void setGPUImageView(final EditorActivity activity, GPUImageView gpuImageView) {
        this.activity = activity;
        this.mGpuImageView = gpuImageView;
        resetParams();
    }

    private void resetParams() {
        filters.clear();
        filters.add(GPUImageFilterTools.FilterType.BRIGHTNESS);
        filters.add(GPUImageFilterTools.FilterType.CONTRAST);
        filters.add(GPUImageFilterTools.FilterType.SHARPEN);
        filters.add(GPUImageFilterTools.FilterType.WHITE_BALANCE);
        filters.add(GPUImageFilterTools.FilterType.SATURATION);
        filters.add(GPUImageFilterTools.FilterType.HIGHLIGHT);
        filters.add(GPUImageFilterTools.FilterType.SHADOW);
        filters.add(GPUImageFilterTools.FilterType.COLOR_BALANCE);
        try {
            //sourceBitmap = EffectsUtils.getInstance().getBitmap(true, false).copy(Bitmap.Config.ARGB_8888, true);
        }catch (OutOfMemoryError e){

        }
    }

    public void setIOnBackStateChangeListener(IOnBackStateChangeListener l) {
        listener = l;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public void onItemClicked(int indexClicked) {
        if (selected == indexClicked) {
            selected = indexClicked;
            return;
        } else {
            selected = indexClicked;
        }
        if (this.mBasePanel != null) {
            removeView(this.mBasePanel);
            this.mBasePanel = null;
        }
        if (mFrame != null) {
            mFrame.removeView(mBasePanel);
            mFrame.removeAllViews();
        }
        if (mFrame == null) {
            mFrame = (FrameLayout) findViewById(R.id.frame_top);
        }
        if (mFilterSeekPanel != null) {
            removeView(mFilterSeekPanel);
            mFilterSeekPanel = null;
        }
        if (indexClicked == 0) {
            mBasePanel = TextMainPanel.init(activity);
            mFrame.addView(mBasePanel);
            mBasePanel.setVisibility(View.GONE);
            mBasePanel.show();
            return;
        } else if (indexClicked == 1) {
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
        } else if (indexClicked == 2) {
            ((EditorActivity) activity).showSticker();
            return;
        } else if (indexClicked == 3) {
            ((EditorActivity) activity).onCropImage();
            return;
        }
        // uncomment if you want to add filoter,effects,overlay,texture
        /*indexClicked = indexClicked - 4;
        if (indexClicked < 2) {
            List list = null;
            Bitmap bitmap = this.sourceBitmap;
            if (indexClicked == 1) {
                GPUImageFilterTools.init();
                list = GPUImageFilterTools.filters_effects;
            } else if (indexClicked == 0) {
                GPUImageFilterTools.init();
                list = GPUImageFilterTools.filters_filter;
            }
            final int index = indexClicked;
            LoadThumbAsync.loadThumbnails(bitmap, list, new IOnThumbListener() {
                @Override
                public Activity getActivity() {
                    return activity;
                }

                @Override
                public void onThumbLoaded(List<Bitmap> list) {
                    loadThumb(list, index, 0);
                }
            });
        } else if (indexClicked < 4) {
            final int index = indexClicked;
            final int mode = indexClicked - 2;
            LoadThumbAsync.loadThumbnails(sourceBitmap, mode, indexClicked == 2 ? 26 : 22,
                    new IOnThumbListener() {
                        @Override
                        public Activity getActivity() {
                            return activity;
                        }

                        @Override
                        public void onThumbLoaded(List<Bitmap> list) {
                            loadThumb(list, index, mode);
                        }
                    });
        }*/ else {
            int pos = indexClicked - 4;
            if (mFilterSeekPanel == null) {
                mFilterSeekPanel = FilterSeekPanel.init(activity, mGpuImageView);
                addView(mFilterSeekPanel);
                mFilterSeekPanel.setVisibility(GONE);
                mFilterSeekPanel.setOnHideListener(new FilterSeekPanel.IOnHideListener() {
                    @Override
                    public void onApplyEffect(GPUImageFilter gPUImageFilter) {
                        ((EditorActivity) activity).onApplyEffect(gPUImageFilter);
                        onBackPressed();
                        listener.canBack(true);
                    }

                    @Override
                    public void onHide() {
                        resetLinSelector();
                        onBackPressed();
                    }
                });
            } else {
                this.mFilterSeekPanel.setGPUImageView(this.mGpuImageView);
            }
            mFilterSeekPanel.setTitle(activity.getString(names[indexClicked]));
            EditorNames editornames = getEditorNames(indexClicked);
            mFilterSeekPanel.switchFilterTo(filters.get(pos), editornames);

        }
        if (this.listener != null)
            this.listener.canBack(false);
    }

    private void loadThumb(List<Bitmap> bitmapList, int index, int mode) {
        if (bitmapList == null || bitmapList.size() == 0) {
            Toast.makeText(activity, "Loaded Filter Is Null", Toast.LENGTH_SHORT).show();
        }
        EditorNames editornames = getEditorNames(index);
        mBasePanel = FilterScrPanel.init(activity, mGpuImageView, bitmapList, editornames, mode,
                new FilterSeekPanel.IOnHideListener() {
                    @Override
                    public void onApplyEffect(GPUImageFilter gPUImageFilter) {
                        ((EditorActivity) activity).onApplyEffect(gPUImageFilter);
                        onBackPressed();
                    }

                    @Override
                    public void onHide() {
                        resetLinSelector();
                        onBackPressed();
                    }
                });
        addView(mBasePanel);
        mBasePanel.setVisibility(GONE);
        mBasePanel.show();
        if (listener != null) {
            listener.canBack(false);
        }
        return;
    }

    @Override
    public boolean onBackPressed() {
        selected = -1;
        if (mFilterSeekPanel != null && mFilterSeekPanel.getVisibility() == VISIBLE) {
            this.mFilterSeekPanel.onBackPressed();
            this.listener.canBack(true);
            return true;
        } else if (this.mBasePanel == null) {
            return false;
        } else {
            this.listener.canBack(true);
            if (mBasePanel.onBackPressed()) {
                return true;
            }
            /*if (mBasePanel.getVisibility() == VISIBLE && (this.mBasePanel instanceof BaseScrPanel)) {
                mBasePanel.onBackPressed();
            }*/
            /*if (this.mFrame != null) {
                this.mBasePanel.hide(new OnHideListener() {
                    @Override
                    public void onHideFinished_BasicPanel() {
                        mFrame.removeView(mBasePanel);
                        mBasePanel = null;
                        pos = -1;
                    }
                });
            }*/
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

    private EditorNames getEditorNames(int index) {
        EditorNames editornames = null;
        if (index == 0) {
            editornames = EditorNames.Filters;
        } else if (index == 1) {
            editornames = EditorNames.Effects;
        } else if (index == 2) {
            editornames = EditorNames.Overlay;
        } else if (index == 3) {
            editornames = EditorNames.Texture;
        } else if (index == 4) {
            editornames = EditorNames.Brightness;
        } else if (index == 5) {
            editornames = EditorNames.Contrast;
        } else if (index == 6) {
            editornames = EditorNames.Sharpness;
        } else if (index == 7) {
            editornames = EditorNames.Balance;
        } else if (index == 8) {
            editornames = EditorNames.Saturation;
        } else if (index == 9) {
            editornames = EditorNames.Highlight;
        } else if (index == 10) {
            editornames = EditorNames.Shadow;
        } else if (index == 11) {
            editornames = EditorNames.Temperature;
        }
        return editornames;
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
                if(mFrame!=null&&getContext()!=null&&!((EditorActivity)getContext()).isFinishing()){
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
                ((EditorActivity) activity).addBorder(null);
                return;
            }
            try {
                String path = getFramePath(indexClicked).getAbsolutePath();
                Bitmap bit = BitmapFactory.decodeFile(path);
                Drawable d = new BitmapDrawable(getResources(), bit);
                ((EditorActivity) activity).addBorder(d);
            } catch (OutOfMemoryError e) {
                System.gc();
            }

        }
    }
}
