package nonworkingcode.effects.viewpanel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;

import java.io.File;
import java.util.List;

import nonworkingcode.effects.filters.GPUImageFilterTools;
import nonworkingcode.effects.viewpanel.views.PanelItemView;

/**
 * Created by Caliber Fashion on 12/1/2016.
 */

public abstract class BaseOnlyScrollPanel<T> extends BasicPanel {
    private OnClickListener viewClickListener;
    private String name;
    private LinearLayout mContainer;
    private ImageView panelLeftIcon;

    public BaseOnlyScrollPanel(Context context) {
        super(context);
        init();
    }

    public BaseOnlyScrollPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseOnlyScrollPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseOnlyScrollPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public abstract boolean isSelectable();

    public abstract void onItemClicked(int indexClicked);

    private void init() {
        viewClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = ((Integer) v.getTag()).intValue();
                if (isSelectable()) {
                    resetLinSelector(index);
                }
                onItemClicked(index);
            }
        };
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void initView(Activity activity, List<T> items) {
        initView(activity, items, 0, 0);
    }

    protected void initView(Activity activity, List<T> items, int sizeX, int sizeY) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(getContext(), "Size: " + items.size(), Toast.LENGTH_SHORT).show();
        }
        if (items.size() <= 0) {
            return;
        }
        View rootView = activity.getLayoutInflater().inflate(R.layout.panel_scroll_layout, this, true);
        final HorizontalScrollView hsr = (HorizontalScrollView) rootView.findViewById(R.id.hsv_pn_filter);
        mContainer = (LinearLayout) rootView.findViewById(R.id.ln_pn_filter_con);
        if (this.mContainer != null) {
            this.mContainer.removeAllViews();
            int size = AppUtils.screenHeight / 9;
            int maring = (int) AppUtils.dpToPx(activity, 10.0f);
            if (sizeX == 0 || sizeY == 0) {
                size = AppUtils.screenHeight / 10;
                sizeX = size;
                sizeY = size;
            } else {
                float factor;
                maring = (int) AppUtils.dpToPx(activity, 2.0f);
                if (sizeX > sizeY) {
                    factor = (1.0f * ((float) sizeY)) / ((float) sizeX);
                } else {
                    factor = (1.0f * ((float) sizeY)) / ((float) sizeX);
                }
                sizeX = size;
                sizeY = (int) ((((float) size) * factor) + AppUtils.dpToPx(activity, 25.0f));
            }
            LayoutParams param = null;
            if (items.size() <= 5) {
                maring = (int) AppUtils.dpToPx(activity, 30.0f);
                param = new LayoutParams(AppUtils.screenWidth / items.size(), sizeY);
                param.leftMargin = maring;
                param.rightMargin = maring;
                // Toast.makeText(getContext(),"X:"+AppUtils.screenWidth/items.size()+" Y:"+sizeY,Toast.LENGTH_LONG).show();
            } else {
                param = new LayoutParams(sizeX, sizeY);
                param.leftMargin = maring;
                param.rightMargin = maring;
            }

            for (int i = 0; i < items.size(); i++) {
                PanelItemView view = new PanelItemView(activity);
                T item = items.get(i);
                if (item instanceof Integer) {
                    view.setData(((Integer) item).intValue());
                } else if (item instanceof String) {
                    view.setData(String.valueOf(item));
                } else if (item instanceof String[]) {
                    String[] is = (String[]) item;
                    view.setData(is[0], is[1]);
                } else if (item instanceof Integer[]) {
                    Integer[] is2 = (Integer[]) item;
                    view.setData(is2[0], is2[1]);
                } else if (item instanceof Bitmap) {
                    view.setData((Bitmap) item);
                    if (this.name != null) {
                        view.setName(GPUImageFilterTools.filterNames[i]);
                    }
                } else if (item instanceof File) {
                    view.setFile((File) item);
                }
                view.setTag(i);
                view.setLayoutParams(param);
                view.setOnClickListener(this.viewClickListener);
                this.mContainer.addView(view);
            }
            hsr.post(new Runnable() {
                @Override
                public void run() {
                    hsr.scrollTo(0, 0);
                }
            });
        }

        panelLeftIcon = (ImageView) rootView.findViewById(R.id.icon_panel_scroll);
        panelLeftIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPanelLeftIconClick(v);
            }
        });
        panelLeftIcon.setVisibility(GONE);
    }

    protected void updateResource(int pos, int resId) {
        if (this.mContainer.getChildCount() > 0) {
            View v = this.mContainer.getChildAt(pos);
            if (v instanceof PanelItemView) {
                ((PanelItemView) v).updateData(resId);
            }
        }
    }

    public void resetLinSelector() {
        if (this.mContainer.getChildCount() > 0) {
            for (int i = 0; i < this.mContainer.getChildCount(); i++) {
                View v = this.mContainer.getChildAt(i);
                if (v != null && (v instanceof PanelItemView)) {
                    ((PanelItemView) v).setSelected(false);
                }
            }
        }
    }

    protected void resetLinSelector(int inde) {
        if (this.mContainer.getChildCount() > 0) {
            int i = 0;
            while (i < this.mContainer.getChildCount()) {
                View v = this.mContainer.getChildAt(i);
                if (v != null && (v instanceof PanelItemView)) {
                    ((PanelItemView) v).setSelected(inde == i);
                }
                i++;
            }
        }
    }

    public void onPanelLeftIconClick(View view) {
    }

    public void setResourceToPanelLeftIcon(int resId) {
        if (panelLeftIcon != null) {
            panelLeftIcon.setVisibility(VISIBLE);
            panelLeftIcon.setImageResource(resId);
        }
    }

    public void onDeAttach() {
        super.onDeAttach();
        if (mContainer != null) {
            mContainer.removeAllViews();
        }
        removeAllViews();
    }
}
