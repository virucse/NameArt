package nonworkingcode.effects.viewpanel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;

import java.util.List;

import nonworkingcode.effects.viewpanel.views.PanelItemView;

public abstract class BaseScrPanel<T> extends BaseFilterPanel {
    private OnClickListener effectClick;
    private LinearLayout linContainer;
    private TextView tvTitle;

    public BaseScrPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseScrPanel(Context context) {
        super(context);
        init();
    }

    abstract String getName(int i);

    public abstract String getTitle();

    public abstract void onDeAttach();

    abstract void onItemSelected(int i);

    private void init() {
        effectClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ((Integer) v.getTag()).intValue();
                BaseScrPanel.this.resetLinSelector(position);
                BaseScrPanel.this.onItemSelected(position);
            }
        };
    }

    protected void initView(Activity activity, List<T> items) {
        View rootView = activity.getLayoutInflater().inflate(R.layout.base_scr_panel, this, true);
        this.tvTitle = (TextView) findViewById(R.id.tv_sp_filter_name);
        this.tvTitle.setText(getTitle());
        this.linContainer = (LinearLayout) rootView.findViewById(R.id.ln_pn_filter_con);
        if (this.linContainer != null) {
            this.linContainer.removeAllViews();
            int size = AppUtils.screenHeight / 9;
            int maring = (int) AppUtils.dpToPx(activity, 2.0f);
            int pSizeX = AppUtils.screenHeight / 10;
            int pSizeY = AppUtils.screenHeight / 10;
            if (items.get(0) instanceof Bitmap) {
                float f;
                float factor;
                pSizeX = ((Bitmap) items.get(0)).getWidth();
                pSizeY = ((Bitmap) items.get(0)).getHeight();
                if (pSizeX > pSizeY) {
                    f = (float) pSizeX;
                    factor = (float) ((1.0 * ((float) pSizeY)) / pSizeX);
                } else {
                    f = (float) pSizeX;
                    factor = (float) ((1.0 * ((float) pSizeY)) / pSizeX);
                }
                pSizeX = size;
                pSizeY = (int) (((float) size) * factor);
                if (!getName(0).isEmpty()) {
                    f = (float) pSizeX;
                    pSizeX = size;
                    float f2 = (float) size;
                    pSizeY = (int) ((((1.0 * ((float) pSizeY)))) + AppUtils.dpToPx(activity, 25.0f));
                }
            }
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(pSizeX, pSizeY);
            param.leftMargin = maring;
            param.rightMargin = maring;
            for (int i = 0; i < items.size(); i++) {
                PanelItemView panelItemView = new PanelItemView(activity);
                T item = items.get(i);
                if (item instanceof Bitmap) {
                    panelItemView.setData((Bitmap) item);
                    panelItemView.setName(getName(i));
                } else if (item instanceof Integer) {
                    panelItemView.setData(((Integer) item).intValue());
                }
                panelItemView.setTag(Integer.valueOf(i));
                panelItemView.setLayoutParams(param);
                panelItemView.setOnClickListener(this.effectClick);
                this.linContainer.addView(panelItemView);
            }
            final HorizontalScrollView hsr = (HorizontalScrollView) rootView.findViewById(R.id.hsv_pn_filter);
            hsr.post(new Runnable() {
                @Override
                public void run() {
                    hsr.scrollTo(0, 0);
                }
            });
        }
        ImageButton ibBack = (ImageButton) rootView.findViewById(R.id.ib_sp_discard);
        ImageButton ibDone = (ImageButton) rootView.findViewById(R.id.ib_sp_apply);
        AppUtils.setImage(ibBack, R.mipmap.ic_cross);
        AppUtils.setImage(ibDone, R.mipmap.ic_done);
        ibBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDiscarded();
            }
        });
        ibDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onApplied();
            }
        });
    }

    protected void resetLinSelector() {
        if (this.linContainer.getChildCount() > 0) {
            for (int i = 0; i < this.linContainer.getChildCount(); i++) {
                View v = this.linContainer.getChildAt(i);
                if (v != null && (v instanceof PanelItemView)) {
                    ((PanelItemView) v).setSelected(false);
                }
            }
        }
    }

    protected void resetLinSelector(int inde) {
        if (this.linContainer.getChildCount() > 0) {
            int i = 0;
            while (i < this.linContainer.getChildCount()) {
                View v = this.linContainer.getChildAt(i);
                if (v != null && (v instanceof PanelItemView)) {
                    ((PanelItemView) v).setSelected(inde == i);
                }
                i++;
            }
        }
    }
}
