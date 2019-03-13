package nonworkingcode.grid.panel;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.formationapps.nameart.R;

import nonworkingcode.effects.viewpanel.BasicPanel;
import nonworkingcode.grid.background.BackgroundManager;
import nonworkingcode.grid.background.ColorResource;
import nonworkingcode.grid.background.GradientResource;
import nonworkingcode.grid.background.IBgResource;
import nonworkingcode.grid.background.PattenResource;
import nonworkingcode.grid.custominterface.IOnBackgroundChangeListener;
import nonworkingcode.grid.util.CollageUtil;

/**
 * Created by Caliber Fashion on 12/11/2016.
 */

public class CollageBGPanel extends BasicPanel {
    private View mBack;
    private BackgroundManager mBackgroundManager;
    private OnClickListener mClickListener;
    private Context mContext;
    private HorizontalScrollView mHsr;
    private ImageButton mIbRotate;
    private boolean mIsInitBg;
    private IOnBackgroundChangeListener mListener;
    private LinearLayout mLnContainer;
    private LinearLayout mLnSeekRoot;
    private int mMode;
    private SeekBar mSbGradient;
    private int mScreenHeight;
    private String mSeleted;

    public CollageBGPanel(Context context, int modeR, int screenHeight) {
        super(context);
        init(context, modeR, screenHeight);
    }

    public CollageBGPanel(Context context, int modeR, AttributeSet attrs, int screenHeight) {
        super(context, attrs);
        init(context, modeR, screenHeight);
    }

    private void init(Context context, int modeR, int screenHeight) {
        this.mIsInitBg = false;
        this.mScreenHeight = 1187;
        this.mMode = 0;
        this.mSeleted = "";
        this.mClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                CollageBGPanel.this.onRatioSelected((String) v.getTag());
                CollageBGPanel.this.resetLinSelector();
                CollageBGPanel.this.setSelected(v, true);
            }
        };
        this.mScreenHeight = screenHeight;
        this.mContext = context;
        this.mMode = modeR;
        ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.collage_bg_panel, this, true);
        this.mBackgroundManager = new BackgroundManager(context, this.mMode);
        findViewById(R.id.lin_container).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        this.mLnContainer = (LinearLayout) findViewById(R.id.ln_panel_bg_container);
        this.mLnSeekRoot = (LinearLayout) findViewById(R.id.ln_sb_conainer);
        this.mHsr = (HorizontalScrollView) findViewById(R.id.scr_panel_bg);
        this.mBack = findViewById(R.id.ib_panel_bg_back);
        this.mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CollageBGPanel.this.dispose();
                if (CollageBGPanel.this.mListener != null) {
                    CollageBGPanel.this.mListener.onBgBackOnClick(CollageBGPanel.this.mMode);
                }
            }
        });
        this.mSbGradient = (SeekBar) findViewById(R.id.sb_panel_bg_grd);
        CollageUtil.setSeekBarThumb(this.mSbGradient);
        this.mSbGradient.setMax(360);
        this.mSbGradient.setProgress(180);
        this.mIbRotate = (ImageButton) findViewById(R.id.ib_panel_bg);
        this.mSbGradient.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mListener == null) {
                        return;
                    }
                    if (mIsInitBg) {
                        mIsInitBg = false;
                    } else {
                        mListener.onBgSeekbarChanged(mMode, (float) (progress - 180));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        CollageUtil.setImage(this.mIbRotate, R.drawable.ic_rotate_gradient);
        this.mIbRotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ibRotateClicked(v);
            }
        });
        addItems();
    }

    private void ibRotateClicked(View v) {
        if (CollageBGPanel.this.mBackgroundManager == null) {
            CollageBGPanel.this.mBackgroundManager = new BackgroundManager(CollageBGPanel.this.mContext, CollageBGPanel.this.mMode);
        }
        IBgResource res = CollageBGPanel.this.mBackgroundManager.getRes(CollageBGPanel.this.mSeleted);
        if (res != null) {
            GradientResource grRes = (GradientResource) res;
            GradientDrawable.Orientation oldOri = grRes.getOrientation();
            GradientDrawable.Orientation newOri = oldOri;
            if (oldOri == GradientDrawable.Orientation.TR_BL) {
                newOri = GradientDrawable.Orientation.TOP_BOTTOM;
            } else if (oldOri == GradientDrawable.Orientation.TOP_BOTTOM) {
                newOri = GradientDrawable.Orientation.TL_BR;
            } else if (oldOri == GradientDrawable.Orientation.TL_BR) {
                newOri = GradientDrawable.Orientation.LEFT_RIGHT;
            } else if (oldOri == GradientDrawable.Orientation.LEFT_RIGHT) {
                newOri = GradientDrawable.Orientation.BL_TR;
            } else if (oldOri == GradientDrawable.Orientation.BL_TR) {
                newOri = GradientDrawable.Orientation.BOTTOM_TOP;
            } else if (oldOri == GradientDrawable.Orientation.BOTTOM_TOP) {
                newOri = GradientDrawable.Orientation.BR_TL;
            } else if (oldOri == GradientDrawable.Orientation.BR_TL) {
                newOri = GradientDrawable.Orientation.RIGHT_LEFT;
            } else if (oldOri == GradientDrawable.Orientation.RIGHT_LEFT) {
                newOri = GradientDrawable.Orientation.TR_BL;
            }
            grRes.setOrientation(newOri);
            if (CollageBGPanel.this.mListener != null) {
                CollageBGPanel.this.mListener.onBgResourceChanged(CollageBGPanel.this.mMode, res);
            }
        }
    }

    private void addItems() {
        if (this.mLnContainer != null) {
            this.mLnContainer.removeAllViews();
            int size = this.mScreenHeight / 10;
            LayoutParams param = new LayoutParams(size, size);
            for (IBgResource res : this.mBackgroundManager.getList()) {
                ImageButton btn = new ImageButton(getContext());
                btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                if (res instanceof GradientResource) {
                    btn.setImageDrawable(((GradientResource) res).getGradientDrawable());
                } else if (res instanceof ColorResource) {
                    btn.setImageDrawable(new ColorDrawable(((ColorResource) res).getColorValue()));
                } else if (res instanceof PattenResource) {
                    btn.setImageBitmap(((PattenResource) res).getIcon());
                }
                btn.setLayoutParams(param);
                btn.setTag(res.getName());
                btn.setOnClickListener(this.mClickListener);
                this.mLnContainer.addView(btn);
                setSelected(btn, false);
            }
            this.mHsr.scrollTo(0, 0);
            this.mLnSeekRoot.setVisibility(View.GONE);
        }
    }

    public void setOnBgChangedListener(IOnBackgroundChangeListener listener) {
        this.mListener = listener;
    }

    private void onRatioSelected(String tag) {
        this.mSeleted = tag;
        IBgResource res = this.mBackgroundManager.getRes(tag);
        if (res == null) {
            return;
        }
        if (this.mMode == BackgroundManager.MODE_GRADIENT) {
            GradientResource gRes = (GradientResource) res;
            gRes.setOrientation(gRes.getDefaultOrientation());
            this.mLnSeekRoot.setVisibility(VISIBLE);
            if (this.mListener != null) {
                this.mIsInitBg = true;
                this.mSbGradient.setProgress(180);
                this.mListener.onBgResourceChangedByPressItem(this.mMode, res);
            }
        } else if (this.mMode == BackgroundManager.MODE_COLOR) {
            ColorResource cRes = (ColorResource) res;
            this.mLnSeekRoot.setVisibility(GONE);
            if (this.mListener != null) {
                this.mIsInitBg = true;
                this.mListener.onBgResourceChangedByPressItem(this.mMode, cRes);
            }
        } else if (this.mMode >= BackgroundManager.MODE_PATTERN) {
            PattenResource pRes = (PattenResource) res;
            this.mLnSeekRoot.setVisibility(GONE);
            if (this.mListener != null) {
                this.mIsInitBg = true;
                this.mListener.onBgResourceChangedByPressItem(this.mMode, pRes);
            }
        }
    }

    private void resetLinSelector() {
        if (this.mLnContainer.getChildCount() > 0) {
            for (int i = 0; i < this.mLnContainer.getChildCount(); i++) {
                View v = this.mLnContainer.getChildAt(i);
                if (v != null) {
                    setSelected(v, false);
                }
            }
        }
    }

    public void setSelected(View v, boolean bl2) {
        int selectedColor = CollageUtil.getColor(this.mContext, R.attr.colorPrimaryDark);
        if (!bl2) {
            selectedColor = 0;
        }
        v.setBackgroundColor(selectedColor);
    }

    public void dispose() {
        if (this.mLnContainer != null) {
            this.mLnContainer.removeAllViews();
        }
    }
}
