package nonworkingcode.grid.panel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.formationapps.nameart.R;

import nonworkingcode.effects.viewpanel.BasicPanel;
import nonworkingcode.grid.util.CollageConst;
import nonworkingcode.grid.util.CollageUtil;

/**
 * Created by Caliber Fashion on 12/11/2016.
 */

public class CollageRatioPanel extends BasicPanel {
    public static final String[] ratio_type;
    private static CollageRatioPanel _ration_view;

    static {
        ratio_type = new String[]{"1:1", "3:2", "2:3", "4:3", "3:4", "2:1", "1:2"};
    }

    private LinearLayout mLinContainer;
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((Integer) v.getTag()).intValue();
            CollageRatioPanel.this.resetLinSelector(position);
            CollageRatioPanel.this.onItemSelected(position);
        }
    };

    public CollageRatioPanel(Context context) {
        super(context);
    }

    public CollageRatioPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static CollageRatioPanel getInstance() {
        return _ration_view;
    }

    public static CollageRatioPanel init(Activity activity) {
        if (_ration_view != null) {
            _ration_view.onDeAttach();
        }
        _ration_view = new CollageRatioPanel(activity);
        _ration_view.initView(activity);
        return _ration_view;
    }

    protected void initView(Activity activity) {
        View rootView = activity.getLayoutInflater().inflate(R.layout.panel_scroll_layout, this, true);
        final HorizontalScrollView hsr = (HorizontalScrollView) rootView.findViewById(R.id.hsv_pn_filter);
        this.mLinContainer = (LinearLayout) rootView.findViewById(R.id.ln_pn_filter_con);
        if (this.mLinContainer != null) {
            this.mLinContainer.removeAllViews();
            final int size = CollageUtil.screenHeight / 9;
            int maring = (int) CollageUtil.dpToPx(1.0f);
            LayoutParams param = new LayoutParams(size, size);
            param.leftMargin = maring;
            param.rightMargin = maring;
            int textColor = CollageUtil.getColor(activity, R.attr.titleTextColor);
            for (int i = 0; i < ratio_type.length; i++) {
                Button view = new Button(activity);
                view.setTextColor(textColor);
                view.setSingleLine(true);
                view.setText(ratio_type[i]);
                view.setTypeface(Typeface.DEFAULT_BOLD);
                view.setTextColor(Color.WHITE);
                view.setBackgroundColor(0);
                view.setTag(Integer.valueOf(i));
                view.setLayoutParams(param);
                view.setOnClickListener(mClickListener);
                this.mLinContainer.addView(view);
            }
            hsr.post(new Runnable() {
                @Override
                public void run() {
                    hsr.scrollTo((int) (((float) (CollageConst.ratioIndex * size)) * 1.01f), 0);
                }
            });
        }
        resetLinSelector(CollageConst.ratioIndex);
    }

    public void onDeAttach() {
        _ration_view = null;
    }

    protected void resetLinSelector(int inde) {
        int selectedColor = CollageUtil.getColor(getContext(), R.attr.colorPrimary);
        if (this.mLinContainer.getChildCount() > 0) {
            for (int i = 0; i < this.mLinContainer.getChildCount(); i++) {
                View v = this.mLinContainer.getChildAt(i);
                if (v != null) {
                    if (inde == i) {
                        v.setBackgroundColor(selectedColor);
                    } else {
                        v.setBackgroundColor(0);
                    }
                }
            }
        }
    }

    public void onItemSelected(int index) {
        CollageConst.ratioIndex = index;
        switch (CollageConst.ratioIndex) {
            case 0 /*0*/:
                CollageConst.collageView.updateRatio(CollageConst.collageBaseWidth, CollageConst.collageBaseWidth);
                break;
            case 1 /*1*/:
                CollageConst.collageView.updateRatio(CollageConst.collageBaseWidth, (int) ((((float) CollageConst.collageBaseWidth) * 2.0f) / 3.0f));
                break;
            case 2 /*2*/:
                CollageConst.collageView.updateRatio((int) ((((float) CollageConst.collageBaseWidth) * 2.0f) / 3.0f), CollageConst.collageBaseWidth);
                break;
            case 3 /*3*/:
                CollageConst.collageView.updateRatio(CollageConst.collageBaseWidth, (int) ((((float) CollageConst.collageBaseWidth) * 3.0f) / 4.0f));
                break;
            case 4/*4*/:
                CollageConst.collageView.updateRatio((int) ((((float) CollageConst.collageBaseWidth) * 3.0f) / 4.0f), CollageConst.collageBaseWidth);
                break;
            case 5 /*5*/:
                CollageConst.collageView.updateRatio(CollageConst.collageBaseWidth, (int) (((float) CollageConst.collageBaseWidth) / 2.0f));
                break;
            case 6/*6*/:
                CollageConst.collageView.updateRatio((int) (((float) CollageConst.collageBaseWidth) / 2.0f), CollageConst.collageBaseWidth);
                break;
            default:
        }
    }
}
