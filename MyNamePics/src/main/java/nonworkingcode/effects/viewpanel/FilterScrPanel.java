package nonworkingcode.effects.viewpanel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import nonworkingcode.effects.filters.GPUImageFilterTools;

public class FilterScrPanel extends BaseScrPanel<Bitmap> {
    private static FilterScrPanel _effectBar;
    private GPUImageFilter filter;
    private FilterSeekPanel.IOnHideListener li;
    private GPUImageView mGPUImageView;
    private EditorNames editorItems;
    private int mode = 0;

    public FilterScrPanel(Context context, AttributeSet attrs, EditorNames items, int mode) {
        super(context, attrs);
        this.filter = new GPUImageFilter();
        editorItems = items;
        this.mode = mode;
    }

    public FilterScrPanel(Context context, EditorNames items, int mode) {
        super(context);
        this.filter = new GPUImageFilter();
        editorItems = items;
        this.mode = mode;
    }

    public static FilterScrPanel getInstance() {
        return _effectBar;
    }

    public static FilterScrPanel init(Activity activity, GPUImageView imageView, List<Bitmap> bitmaps,
                                      EditorNames items, int mode, FilterSeekPanel.IOnHideListener li) {
        if (_effectBar != null) {
            _effectBar.onDeAttach();
        }
        _effectBar = new FilterScrPanel(activity, items, mode);
        _effectBar.initView(activity, imageView, bitmaps);
        _effectBar.setOnHideListener(li);
        return _effectBar;
    }

    private void initView(Activity activity, GPUImageView imageView, List<Bitmap> bitmaps) {
        super.initView(activity, bitmaps);
        this.mGPUImageView = imageView;
    }

    public void onApplied() {
        if (this.li != null) {
            this.li.onApplyEffect(this.filter);
        }
    }

    public void onDiscarded() {
        this.filter = new GPUImageFilter();
        this.mGPUImageView.setFilter(this.filter);
        if (this.li != null) {
            this.li.onHide();
        }
    }

    private void setOnHideListener(FilterSeekPanel.IOnHideListener l) {
        this.li = l;
    }

    public void onDeAttach() {
        if (_effectBar != null) {
            _effectBar.removeAllViews();
        }
        _effectBar = null;
    }

    public void onItemSelected(int position) {
        if (position == 0) {
            filter = new GPUImageFilter();
            if (filter != null) {
                mGPUImageView.setFilter(filter);
            }
            return;
        }
        if (editorItems == EditorNames.Filters) {
            this.filter = GPUImageFilterTools.createFilterForType(getContext(),
                    GPUImageFilterTools.filters_filter.get(position));

        } else if (editorItems == EditorNames.Effects) {
            this.filter = GPUImageFilterTools.createFilterForType(getContext(),
                    GPUImageFilterTools.filters_effects.get(position));
        } else if (editorItems == EditorNames.Overlay) {
            filter = getFilter(position);
        } else if (editorItems == EditorNames.Texture) {
            filter = getFilter(position);
        }

        if (filter != null) {
            mGPUImageView.setFilter(filter);
        } else {
            Toast.makeText(getContext(), "filter null", Toast.LENGTH_SHORT).show();
        }

    }

    private GPUImageFilter getFilter(int position) {
        if (position == 0) {
            this.filter = new GPUImageFilter();
            return filter;
        }
        String resName = "imagess/overlay/overlay_%d.jpg";
        if (position == 2) {
            resName = "imagess/overlay/overlay_%d.png";
        }
        int mode = 3;
        if (this.mode == 1) {
            resName = "imagess/texture/texture_%d.jpg";
            mode = GPUImageFilterTools.textureModes[position];
        }
        Log.e("name", String.format(resName, new Object[]{Integer.valueOf(position)}));

        GPUImageFilter filter = GPUImageFilterTools.getFilter(getContext(),
                String.format(resName, new Object[]{Integer.valueOf(position)}), mode);
        return filter;
    }

    public boolean onBackPressed() {
        this.filter = new GPUImageFilter();
        this.mGPUImageView.setFilter(this.filter);
        return super.onBackPressed();
    }

    public String getTitle() {
        return editorItems.name();
    }

    String getName(int i) {
        if (editorItems == EditorNames.Filters) {
            return GPUImageFilterTools.filterNames[i];
        } else if (editorItems == EditorNames.Effects) {
            return GPUImageFilterTools.effectsName[i];
        } else if (editorItems == EditorNames.Overlay) {
            return getTitle() + "_" + (i + 1);
        } else if (editorItems == EditorNames.Texture) {
            return getTitle() + "_" + (i + 1);
        }
        return "Not Found";
    }
}
