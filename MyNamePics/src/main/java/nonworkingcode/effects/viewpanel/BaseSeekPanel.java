package nonworkingcode.effects.viewpanel;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;

/**
 * Created by Caliber Fashion on 12/2/2016.
 */

public abstract class BaseSeekPanel extends BaseFilterPanel {
    private int mProgress;
    private TextView tvProgress;
    private TextView tvTitle;
    private SeekBar seekBar;

    public BaseSeekPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSeekPanel(Context context) {
        super(context);
    }

    public abstract String getTitle();

    public abstract void onDeAttach();

    public abstract void onSeekChange(int i);

    protected void setProgress(int progress) {
        if (this.mProgress != progress && progress >= 0 && progress <= maxValue) {
            this.mProgress = progress;
            this.tvProgress.setText(progress*factor + "");
            onSeekChange(progress);
        }
    }

    protected void onScrollEnd(int progress) {
        setProgress(progress);
    }

    int maxValue=100,factor=1;
    protected void initView(Activity activity) {
        View rootView = activity.getLayoutInflater().inflate(R.layout.baseseekpanel, this, true);
        this.tvProgress = (TextView) rootView.findViewById(R.id.tv_progress);
        this.tvTitle = (TextView) rootView.findViewById(R.id.tv_sp_filter_name);
        this.tvTitle.setText(getTitle());
        this.seekBar = (SeekBar) rootView.findViewById(R.id.wheel_panel_filter);
        this.seekBar.setMax(maxValue);
        this.seekBar.setProgress(this.mProgress);
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    onScrollEnd(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        ImageButton ibBack = (ImageButton) rootView.findViewById(R.id.ib_sp_discard);
        ImageButton ibDone = (ImageButton) rootView.findViewById(R.id.ib_sp_apply);
        AppUtils.setImage(ibBack, R.mipmap.ic_cross);
        AppUtils.setImage(ibDone, R.mipmap.ic_done);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDiscarded();
            }
        });
        ibDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onApplied();
            }
        });
    }

    //this is helper method for PIPBlur,this is case specific
    public void setSeekBarInitialValue(int max,int progress){
        maxValue=max;
        factor=100/max;
        if(seekBar!=null){
            seekBar.setMax(maxValue);
            updateProgress(progress);
        }
    }

    protected void updateProgress(int progress) {
        mProgress = progress;
        seekBar.setVisibility(VISIBLE);
        seekBar.setProgress(this.mProgress);
        tvProgress.setText(progress*factor + "");
    }

    public void show() {
        super.show();
        this.tvTitle.setText(getTitle());
    }
}
