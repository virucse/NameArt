package nonworkingcode.effects.viewpanel;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import nonworkingcode.effects.filters.GPUImageFilterTools;

/**
 * Created by Caliber Fashion on 12/2/2016.
 */

public class FilterSeekPanel extends BaseSeekPanel {
    private GPUImageFilter filter;
    private IOnHideListener li;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private GPUImageView mGpuImageView;
    private GPUImageFilterTools.FilterType mType;
    private String title;

    public FilterSeekPanel(Context context) {
        super(context);
        this.mType = GPUImageFilterTools.FilterType.NORMAL;
        this.filter = new GPUImageFilter();
        this.title = "";
    }

    public FilterSeekPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mType = GPUImageFilterTools.FilterType.NORMAL;
        this.filter = new GPUImageFilter();
        this.title = "";
    }

    public static FilterSeekPanel init(Activity activity, GPUImageView imageView) {
        FilterSeekPanel _panel = new FilterSeekPanel(activity);
        _panel.initView(activity, imageView);
        return _panel;
    }

    public void initView(Activity activity, GPUImageView mGpuImageView) {
        this.mGpuImageView = mGpuImageView;
        super.initView(activity);
    }

    public void switchFilterTo(GPUImageFilterTools.FilterType type, EditorNames editornames) {
        if (type != GPUImageFilterTools.FilterType.NORMAL && this.mType != type) {
            this.mType = type;
            GPUImageFilter mFilter = GPUImageFilterTools.createFilterForType(getContext(), type);
            setGPUImageFilter(mFilter);
            if (type == GPUImageFilterTools.FilterType.RGBG) {
                this.mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter, 1);
            } else if (type == GPUImageFilterTools.FilterType.RGBB) {
                this.mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter, 2);
            } else {
                this.mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
            }
            if (this.mFilterAdjuster.canAdjust()) {
                int progress = (int) getInitialProgressByEditorName(editornames); //this.mFilterAdjuster.getParcent();
                if (progress >= 0) {
                    updateProgress(progress);
                }
                show();
            }
        } else if (this.mType != type) {
            this.mType = type;
            setGPUImageFilter(new GPUImageFilter());
            hide();
        }
    }

    private int getInitialProgressByEditorName(EditorNames editornames) {
        if (editornames == EditorNames.Brightness) {
            return 50;//-1,1,0.0
        } else if (editornames == EditorNames.Contrast) {
            return 50;//0.0 to 4.0, with 1.0
        } else if (editornames == EditorNames.Sharpness) {
            return 50;//from -4.0 to 4.0, with 0.0
        } else if (editornames == EditorNames.Balance) {
            return 80;
        } else if (editornames == EditorNames.Saturation) {
            return 55;
        } else if (editornames == EditorNames.Highlight) {
            return 25;
        } else if (editornames == EditorNames.Shadow) {
            return 20;
        } else if (editornames == EditorNames.Temperature) {
            return 12;
        }
        return 0;
    }

    private void setGPUImageFilter(GPUImageFilter filter) {
        if (filter != null) {
            this.mGpuImageView.setFilter(filter);
        }
        this.filter = filter;
    }

    public void setGPUImageView(GPUImageView image) {
        this.mGpuImageView = image;
    }

    public void onDeAttach() {
    }

    public void onSeekChange(int progress) {
        if (this.mFilterAdjuster != null) {
            this.mFilterAdjuster.adjust(progress);
        }
        if (this.mGpuImageView != null) {
            this.mGpuImageView.requestRender();
        }
    }

    public void onApplied() {
        if (this.li != null) {
            this.li.onApplyEffect(this.filter);
        }
        hide();
        this.mType = GPUImageFilterTools.FilterType.NORMAL;
    }

    public void onDiscarded() {
        this.mType = GPUImageFilterTools.FilterType.NORMAL;
        this.filter = new GPUImageFilter();
        this.mGpuImageView.setFilter(this.filter);
        hide();
        if (this.li != null) {
            this.li.onHide();
        }
    }

    public void setOnHideListener(IOnHideListener l) {
        this.li = l;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean onBackPressed() {
        this.mType = GPUImageFilterTools.FilterType.NORMAL;
        this.filter = new GPUImageFilter();
        this.mGpuImageView.setFilter(this.filter);
        hide();
        return true;
    }

    public interface IOnHideListener {
        void onApplyEffect(GPUImageFilter gPUImageFilter);

        void onHide();
    }
}
