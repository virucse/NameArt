package nonworkingcode.grid.panel;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

import com.formationapps.nameart.R;

import nonworkingcode.effects.viewpanel.BasicPanel;
import nonworkingcode.grid.util.CollageConst;
import nonworkingcode.grid.util.CollageUtil;

/**
 * Created by Caliber Fashion on 12/11/2016.
 */

public class CollageAdjustPanel extends BasicPanel {
    private static CollageAdjustPanel _mPanel;
    private SeekBar mSeekBarCorner;
    private SeekBar mSeekBarShadow;
    private SeekBar mSeekBarSize;
    private View rootView;
    private View seekCornerContainer;
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (seekBar.getId() == R.id.sb_col_shp_size) {
                    if(CollageConst.collageView!=null)CollageConst.collageView.setLineThickness(0.1f * (((float) progress) / 100.0f));
                } else if (seekBar.getId() == R.id.sb_col_shp_shadow) {
                    if(CollageConst.collageView!=null)CollageConst.collageView.setShadowSize(5.0f * (((float) progress) / 100.0f));
                } else if (seekBar.getId() == R.id.sb_col_shp_corner) {
                    if(CollageConst.collageView!=null)CollageConst.collageView.setCornerRadious(50.0f * (((float) progress) / 100.0f));
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    public CollageAdjustPanel(Context context) {
        super(context);
    }

    public CollageAdjustPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static CollageAdjustPanel init(Activity activity) {
        if (_mPanel != null) {
            _mPanel.onDeAttach();
        }
        _mPanel = new CollageAdjustPanel(activity);
        _mPanel.initView(activity);
        return _mPanel;
    }

    public void onDeAttach() {
        if (_mPanel != null) {
            _mPanel.removeAllViews();
        }
        _mPanel = null;
    }

    private void initView(Activity activity) {
        this.rootView = activity.getLayoutInflater().inflate(R.layout.collage_adjust_panel, this, true);
        this.seekCornerContainer = this.rootView.findViewById(R.id.ln_shape_shadow_con);
        this.mSeekBarSize = (SeekBar) this.rootView.findViewById(R.id.sb_col_shp_size);
        this.mSeekBarShadow = (SeekBar) this.rootView.findViewById(R.id.sb_col_shp_shadow);
        this.mSeekBarCorner = (SeekBar) this.rootView.findViewById(R.id.sb_col_shp_corner);
        CollageUtil.setSeekBarThumb(this.mSeekBarSize);
        CollageUtil.setSeekBarThumb(this.mSeekBarShadow);
        CollageUtil.setSeekBarThumb(this.mSeekBarCorner);
        if (CollageConst.gridIndex >= 256) {
            this.seekCornerContainer.setVisibility(GONE);
        } else {
            this.mSeekBarCorner.setProgress((int) ((CollageConst.cornerRadius / 50.0f) * 100.0f));
        }
        this.mSeekBarSize.setProgress((int) ((CollageConst.lineThickness / 0.1f) * 100.0f));
        this.mSeekBarShadow.setProgress((int) ((CollageConst.shadow / 5.0f) * 100.0f));
        this.mSeekBarSize.setOnSeekBarChangeListener(onSeekBarChangeListener);
        this.mSeekBarShadow.setOnSeekBarChangeListener(onSeekBarChangeListener);
        this.mSeekBarCorner.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

}
