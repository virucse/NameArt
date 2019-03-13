package com.text;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.downloader.FileUtils;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.TypefacesUtils;
import com.widgets.MyButton;
import com.widgets.MyImageButton;
import com.widgets.OnMyButtonClickListener;

import java.io.File;

import nonworkingcode.effects.viewpanel.BasicPanel;
import nonworkingcode.grid.util.CollageUtil;

/**
 * Created by caliber fashion on 9/19/2017.
 */

public class TextSubPanel extends BasicPanel {
    private OnMyButtonClickListener mClickListener;
    private Context mContext;
    private HorizontalScrollView mHsr;
    private ImageButton mIbRotate;
    private LinearLayout mLnContainer;
    private LinearLayout mLnSeekRoot;
    private int mMode;
    private SeekBar mSbGradient;
    private int mScreenHeight;
    private View mBack;
    private TextBackgroundPanel mTextBackgroundPanel;
    private LinearLayout back_ib_scroll_container;
    private FontDownloadPanel mFontDownloadPanel;
    private int mSelectedPosition;
    private String filePath;
    private TextItemPanelListner mListener;

    public TextSubPanel(Context context, int modeR, int screenHeight) {
        super(context);
        init(context, modeR, screenHeight);
    }

    public TextSubPanel(Context context, int modeR, int screenHeight, int filePos) {
        super(context);
        setFontDownloadFilePosition(filePos);
        init(context, modeR, screenHeight);
    }

    private void init(Context context, int modeR, int screenHeight) {
        //this.mIsInitBg = false;
        this.mScreenHeight = 1187;
        this.mMode = 0;
        //this.mSeleted = "";
       /* this.mClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };*/
        mClickListener=new OnMyButtonClickListener(){
            @Override
            public void onClick(View view, Object object) {
                super.onClick(view, object);
                onRatioSelected((String) object);
                resetLinSelector();
                setSelected(view, true);
            }
        };
        this.mScreenHeight = screenHeight;
        this.mContext = context;
        this.mMode = modeR;
        ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.collage_bg_panel, this, true);
        findViewById(R.id.lin_container).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        back_ib_scroll_container = (LinearLayout) findViewById(R.id.backib_scroll_container);
        this.mLnContainer = (LinearLayout) findViewById(R.id.ln_panel_bg_container);
        this.mLnSeekRoot = (LinearLayout) findViewById(R.id.ln_sb_conainer);
        this.mHsr = (HorizontalScrollView) findViewById(R.id.scr_panel_bg);
        this.mBack = findViewById(R.id.ib_panel_bg_back);
        this.mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dispose();
                if (mListener != null) {
                    mListener.onBackClick();
                }
            }
        });
        this.mSbGradient = (SeekBar) findViewById(R.id.sb_panel_bg_grd);
        CollageUtil.setSeekBarThumb(this.mSbGradient);
        this.mSbGradient.setMax(360);
        this.mSbGradient.setProgress(180);
        this.mIbRotate = (ImageButton) findViewById(R.id.ib_panel_bg);
        mIbRotate.setVisibility(GONE);
        this.mSbGradient.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mMode == TextPanelUtils.SHADOW_BUTTON) {
                        float rd = (float) ((float) progress / 9.0f);
                        TextColorRes colorRes = (TextColorRes) mTextBackgroundPanel.getResList().get(mSelectedPosition);
                        if (mListener != null) {
                            mListener.onShadowClick(colorRes, rd);
                        }
                    } else if (mMode == TextPanelUtils.TEXTSIZE_BUTTON) {
                        if (mListener != null) {
                            mListener.onTextSizeChange(progress);
                        }
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

        if (modeR == TextPanelUtils.SHADOW_BUTTON) {
            mTextBackgroundPanel = new TextBackgroundPanel(mContext, mMode);
            addItems();
        } else if (mMode == TextPanelUtils.TEXTSIZE_BUTTON) {
            back_ib_scroll_container.setVisibility(GONE);
            mSbGradient.setMax(100);
            mSbGradient.setProgress(20);
        } else if (mMode == TextPanelUtils.FONTS_BUTTON) {
            addFonts();
        } else if (mMode == TextPanelUtils.DOWNLOAD_FONT_BUTTON) {
            /*this block from when TextPanelUtils.FONTS_BUTTON (addFonts run)
            run and then click on plus*/
            File rootFile = FileUtils.getDataDir(getContext(), filePath);
            addFonts(rootFile);
        } else if (mMode == TextPanelUtils.TEXTURE_BUTTON) {
            addTextures();
        }
        System.gc();
    }

    private void addFonts(File rootFile) {
        System.gc();
        if (this.mLnContainer != null) {
            this.mLnContainer.removeAllViews();
            int size = this.mScreenHeight / 10;
            LayoutParams param = new LayoutParams(size, size);
            //param.topMargin=size/3;
            param.gravity = Gravity.CENTER;
            String[] files = rootFile.list();
            for (int i = 0; i < files.length; i++) {
                File file = new File(rootFile.getAbsolutePath() + "/" + files[i]);
                MyButton btn = new MyButton(getContext());
                btn.setText("Name");
                try {
                    btn.setTypeface(Typeface.createFromFile(file));
                } catch (Exception e) {

                }
                btn.setTextColor(Color.WHITE);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sample_text_font_size));
                btn.setLayoutParams(param);
                btn.setBtnObject(i + "");
                btn.setOnBtnClickListener(mClickListener);
                this.mLnContainer.addView(btn);
                setSelected(btn, false);
            }
            this.mHsr.scrollTo(0, 0);
            this.mLnSeekRoot.setVisibility(View.GONE);
        }
    }

    private void addTextures() {

        if (this.mLnContainer != null) {
            this.mLnContainer.removeAllViews();
            int size = this.mScreenHeight / 10;
            LayoutParams param = new LayoutParams(size, size);
            for (int i = 0; i < BaseActivity.TEXTURE.length; i++) {
                MyImageButton btn = new MyImageButton(getContext());
                btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                //btn.setImageDrawable(BaseActivity.TEXTURE[i]);
                //btn.setImageResource(BaseActivity.TEXTURE[i]);
                Glide.with(getContext()).load(BaseActivity.TEXTURE[i]).into(btn);

                btn.setLayoutParams(param);
                btn.setBtnObject(i + "");
                btn.setOnBtnClickListener(this.mClickListener);
                this.mLnContainer.addView(btn);
                setSelected(btn, false);
            }
            this.mHsr.scrollTo(0, 0);
            this.mLnSeekRoot.setVisibility(View.GONE);
        }
    }

    private void addFonts() {
        if (this.mLnContainer != null) {
            this.mLnContainer.removeAllViews();
            int size = this.mScreenHeight / 10;
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(size,
                    size);
            param.leftMargin = AppUtils.dpToPx(getContext(), 5.0f);
            param.rightMargin = AppUtils.dpToPx(getContext(), 5.0f);

            MyButton btnPlus = new MyButton(getContext());
            btnPlus.setText("+");
            btnPlus.setTextColor(Color.WHITE);
            btnPlus.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.plus_text_size));
            btnPlus.setLayoutParams(param);
            btnPlus.setBtnObject("+");
            btnPlus.setOnBtnClickListener(this.mClickListener);
            mLnContainer.addView(btnPlus);
            setSelected(btnPlus, false);

            for (int i = 0; i < BaseActivity.fonts.length; i++) {
                String font = BaseActivity.fonts[i];
                MyButton btn = new MyButton(getContext());
                btn.setText("Name");
                btn.setTypeface(TypefacesUtils.get(getContext(), font));
                btn.setTextColor(Color.WHITE);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sample_text_font_size));
                btn.setLayoutParams(param);
                btn.setBtnObject(i + "");
                btn.setOnBtnClickListener(this.mClickListener);
                mLnContainer.addView(btn);
                setSelected(btn, false);
            }
            this.mHsr.scrollTo(0, 0);
            this.mLnSeekRoot.setVisibility(View.GONE);
        }
    }

    private void addItems() {
        if (this.mLnContainer != null) {
            this.mLnContainer.removeAllViews();
            int size = this.mScreenHeight / 10;
            LayoutParams param = new LayoutParams(size, size);
            for (TextRes res : mTextBackgroundPanel.getResList()) {
                MyImageButton btn = new MyImageButton(getContext());
                btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                if (res instanceof TextColorRes) {
                    btn.setImageDrawable(new ColorDrawable((res.getColorValue())));
                }
                btn.setLayoutParams(param);
                btn.setBtnObject(res.getPosition() + "");
                btn.setOnBtnClickListener(this.mClickListener);
                this.mLnContainer.addView(btn);
                setSelected(btn, false);
            }
            this.mHsr.scrollTo(0, 0);
            this.mLnSeekRoot.setVisibility(View.GONE);
        }
    }

    private void onRatioSelected(String tag) {
        if (tag.equals("+")) {
            showFontDownloadPanel((Activity) getContext());
            return;
        }
        mSelectedPosition = Integer.parseInt(tag);

        if (mMode == TextPanelUtils.SHADOW_BUTTON) {
            if (mTextBackgroundPanel != null) {
                mLnSeekRoot.setVisibility(VISIBLE);
                TextColorRes colorRes = (TextColorRes) mTextBackgroundPanel.getResList().get(mSelectedPosition);
                if (mListener != null) {
                    mListener.onShadowClick(colorRes, 3.0f);
                }
            }
        } else if (mMode == TextPanelUtils.FONTS_BUTTON) {
            if (mListener != null) {
                mListener.onFontClick(BaseActivity.fonts[mSelectedPosition]);
            }
        } else if (mMode == TextPanelUtils.DOWNLOAD_FONT_BUTTON) {
            File rootFile = FileUtils.getDataDir(getContext(), filePath);
            String rootPath = rootFile.getAbsolutePath();
            if (rootFile.exists() && rootFile.list().length > 1) {
                String file[] = rootFile.list();
                if (mListener != null) {
                    File fil = new File(rootPath + "/" + file[mSelectedPosition]);
                    mListener.onDownloadedFontClick(fil);
                }
            }
        } else if (mMode == TextPanelUtils.TEXTURE_BUTTON) {
            if (mListener != null) {
                mListener.onTextureClick(BaseActivity.TEXTURE[mSelectedPosition]);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mFontDownloadPanel != null) {
            if (mFontDownloadPanel.onBackPressed()) {
                return true;
            }
            mFontDownloadPanel.hide(new OnHideListener() {
                @Override
                public void onHideFinished_BasicPanel() {
                    removeView(mFontDownloadPanel);
                    resetLinSelector();
                    mFontDownloadPanel = null;
                }
            });
            return true;
        }
        return false;
    }

    private void setFontDownloadFilePosition(int pos) {
        switch (pos) {
            case 0:
                filePath = AppUtils.FONTHINDI;
                break;
            case 1:
                filePath = AppUtils.FONTPACK1;
                break;
            case 2:
                filePath = AppUtils.FONTPACK2;
                break;
            case 3:
                filePath = AppUtils.FONTPACK3;
                break;
            case 4:
                filePath = AppUtils.FONTPACK4;
                break;
            case 5:
                filePath = AppUtils.FONTPACK5;
                break;
            case 6:
                filePath = AppUtils.FONTPACK6;
                break;
            case 7:
                filePath = AppUtils.FONTPACK7;
                break;

        }
    }

    private void showFontDownloadPanel(Activity activity) {
        mFontDownloadPanel = FontDownloadPanel.initPanel(activity)
                .setDownloadedFileClickListener(mListener);
        addView(mFontDownloadPanel);
        mFontDownloadPanel.setVisibility(View.GONE);
        mFontDownloadPanel.show();
    }

    public void setTextItemPanelListener(TextItemPanelListner listener) {
        mListener = listener;
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
