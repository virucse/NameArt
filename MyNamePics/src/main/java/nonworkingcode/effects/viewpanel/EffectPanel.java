package nonworkingcode.effects.viewpanel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.Toast;

import com.formationapps.nameart.R;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import nonworkingcode.effects.activity.EffectActivity;
import nonworkingcode.effects.custuminterface.IOnBackStateChangeListener;
import nonworkingcode.effects.filters.GPUImageFilterTools.FilterType;
import nonworkingcode.effects.util.EffectsUtils;

/**
 * Created by Caliber Fashion on 12/2/2016.
 */

public class EffectPanel extends BaseOnlyScrollPanel<Integer[]> {
    private static List<FilterType> filters = new ArrayList<>();
    private int[] names;
    private int[] resIds;
    private BasicPanel mBasePanel;
    //private FilterPanel mFilterPanel;
    private Activity activity;
    private FilterSeekPanel mFilterSeekPanel;
    private GPUImageView mGpuImageView;
    private Bitmap sourceBitmap;
    private IOnBackStateChangeListener listener;

    public EffectPanel(Context context) {
        super(context);
        init(context);
    }

    public EffectPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        names = new int[]{/*R.string.ef_filter, R.string.ef_effects, R.string.ef_overlay, R.string.ef_texture,*/ R.string.brightness, R.string.contrast, R.string.sharpness, R.string.balance, R.string.saturation, R.string.highlights, R.string.shadow2, R.string.temperature};
        resIds = new int[]{/*R.mipmap.ic_fx, R.mipmap.ic_filters, R.mipmap.ic_overlay_light, R.mipmap.ic_texture,*/ R.mipmap.ic_brightness, R.mipmap.ic_contrast, R.mipmap.ic_sharpness, R.mipmap.ic_balance, R.mipmap.ic_saturation, R.mipmap.ic_highlight, R.mipmap.ic_shadow, R.mipmap.ic_temperature};
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
    }

    public void setGPUImageView(final Activity activity, GPUImageView gpuImageView) {
        this.activity = activity;
        this.mGpuImageView = gpuImageView;
        resetParams();
    }

    private void resetParams() {
        filters.clear();
        filters.add(FilterType.BRIGHTNESS);
        filters.add(FilterType.CONTRAST);
        filters.add(FilterType.SHARPEN);
        filters.add(FilterType.WHITE_BALANCE);
        filters.add(FilterType.SATURATION);
        filters.add(FilterType.HIGHLIGHT);
        filters.add(FilterType.SHADOW);
        filters.add(FilterType.COLOR_BALANCE);
        sourceBitmap = EffectsUtils.getInstance().getBitmap(true, false).copy(Bitmap.Config.ARGB_8888, true);
    }

    public void setIOnBackStateChangeListener(IOnBackStateChangeListener l) {
        listener = l;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public void onItemClicked(final int indexClicked) {
        if (this.mBasePanel != null) {
            removeView(this.mBasePanel);
            this.mBasePanel = null;
        }
        if (mFilterSeekPanel != null) {
            removeView(mFilterSeekPanel);
            mFilterSeekPanel = null;
        }
        /*if (indexClicked < 2) {
            List list = null;
            Bitmap bitmap = this.sourceBitmap;
            if (indexClicked == 1) {
                GPUImageFilterTools.init();
                list = GPUImageFilterTools.filters_effects;
            } else if (indexClicked == 0) {
                GPUImageFilterTools.init();
                list = GPUImageFilterTools.filters_filter;
            }
            LoadThumbAsync.loadThumbnails(bitmap, list, new IOnThumbListener() {
                @Override
                public Activity getActivity() {
                    return activity;
                }

                @Override
                public void onThumbLoaded(List<Bitmap> list) {
                    EffectPanel.this.onThumbLoaded(list, indexClicked, 0);
                }
            });
        } else if (indexClicked < 4) {
            final int mode = indexClicked - 2;
            LoadThumbAsync.loadThumbnails(sourceBitmap, mode, indexClicked == 2 ? 26 : 22, new IOnThumbListener() {
                @Override
                public Activity getActivity() {
                    return activity;
                }

                @Override
                public void onThumbLoaded(List<Bitmap> list) {
                    EffectPanel.this.onThumbLoaded(list, indexClicked, mode);
                }
            });
        }*/ //else {
            int pos = indexClicked - 0;//4;
            if (mFilterSeekPanel == null) {
                mFilterSeekPanel = FilterSeekPanel.init(activity, mGpuImageView);
                addView(mFilterSeekPanel);
                mFilterSeekPanel.setVisibility(GONE);
                mFilterSeekPanel.setOnHideListener(new FilterSeekPanel.IOnHideListener() {
                    @Override
                    public void onApplyEffect(GPUImageFilter gPUImageFilter) {
                        ((EffectActivity) activity).onApplyEffect(gPUImageFilter);
                        EffectPanel.this.onBackPressed();
                        EffectPanel.this.listener.canBack(true);
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

        //}
        if (this.listener != null)
            this.listener.canBack(false);
    }

    private void onThumbLoaded(List<Bitmap> bitmapList, int index, int mode) {
        if (bitmapList == null || bitmapList.size() == 0) {
            Toast.makeText(activity, "Loaded Filter Is Null", Toast.LENGTH_SHORT).show();
        }
        EditorNames editornames = getEditorNames(index);
        mBasePanel = FilterScrPanel.init(activity, mGpuImageView, bitmapList, editornames, mode,
                new FilterSeekPanel.IOnHideListener() {
                    @Override
                    public void onApplyEffect(GPUImageFilter gPUImageFilter) {
                        ((EffectActivity) activity).onApplyEffect(gPUImageFilter);
                        EffectPanel.this.onBackPressed();
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
        if (EffectPanel.this.listener != null) {
            EffectPanel.this.listener.canBack(false);
        }
        return;
    }

    public boolean onBackPressed() {
        if (mFilterSeekPanel != null && mFilterSeekPanel.getVisibility() == VISIBLE) {
            this.mFilterSeekPanel.onBackPressed();
            this.listener.canBack(true);
            return true;
        } else if (this.mBasePanel == null) {
            return false;
        } else {
            this.listener.canBack(true);
            if (this.mBasePanel.getVisibility() == VISIBLE && (this.mBasePanel instanceof BaseScrPanel)) {
                this.mBasePanel.onBackPressed();
            }
            mBasePanel.hide(new OnHideListener() {
                @Override
                public void onHideFinished_BasicPanel() {
                    mBasePanel.removeAllViews();
                    EffectPanel.this.removeView(mBasePanel);
                    LoadThumbAsync.clear();
                    mBasePanel = null;
                }
            });
            resetLinSelector();
            return true;
        }
    }

    private EditorNames getEditorNames(int index) {
        EditorNames editornames = null;
        index=index+4;
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
}
