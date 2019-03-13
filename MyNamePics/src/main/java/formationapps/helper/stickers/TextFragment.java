package formationapps.helper.stickers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.TypefacesUtils;

import java.io.IOException;

/**
 * Created by Caliber Fashion on 12/12/2016.
 */

public class TextFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = TextFragment.class.getSimpleName();
    private View.OnClickListener clickListener;
    private String defaultText;
    private int height;
    private ImageButton mBackground;
    private ImageButton mColor;
    private View mContentView;
    private DotIndicator mDotIndicator;
    private EditText mEditText;
    private ImageButton mFont;
    private LinearLayout mLinBgContainer;
    private RelativeLayout mRelTextMain;
    private SeekBar mSeekBgOpacity;
    private SeekBar mSeekColor;
    private SeekBar mSeekColorTemp;
    private SeekBar mSeekOpacity;
    private SeekBar mSeekShadoOffset;
    private SeekBar mSeekShadow;
    private SeekBar mSeekShadowColor;
    private TextAndStickerView mTextAndStickerView;
    private ImageButton mType;
    private ViewPager mVpFont;
    private View rootBackground;
    private View rootColor;
    private View rootFont;
    private View selectedView;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (TextFragment.this.getActivity() != null) {
                        View parent = TextFragment.this.getActivity().getWindow().getDecorView();
                        Rect r = new Rect();
                        parent.getWindowVisibleDisplayFrame(r);
                        int screenHeight = parent.getRootView().getHeight();
                        if (Build.VERSION.SDK_INT >= 21) {
                            screenHeight -= TextFragment.this.getResources().getDimensionPixelSize(R.dimen.action_bar_size);
                        }
                        int heightDifference = screenHeight - (r.bottom - r.top);
                        if (TextFragment.this.height == 0 && heightDifference > 100) {
                            TextFragment.this.height = AppUtils.statusBarHeight + heightDifference;
                            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) mContentView.getLayoutParams();
                            param.height = height;
                            Log.d(TAG, "onGlobalLayout: Height " + new StringBuilder(String.valueOf(height)).append(" ").append(screenHeight).toString());
                            mContentView.setLayoutParams(param);
                        }
                    }
                }
            };
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TextFragment.this.mTextAndStickerView.setText(String.valueOf(s.toString()));
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public TextFragment() {

        this.defaultText = "Formationapps";
        this.clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextAndStickerView.setFont(((Integer) ((Button) v).getTag()).intValue());
                if (selectedView != null) {
                    selectedView.setSelected(false);
                }
                selectedView = v;
                selectedView.setSelected(true);
            }
        };
        this.height = 0;
    }

    @SuppressLint({"NewApi"})
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    private void onSelected(int index) {
        this.mType.setSelected(false);
        this.mFont.setSelected(false);
        this.mColor.setSelected(false);
        this.mBackground.setSelected(false);
        this.rootFont.setVisibility(View.GONE);
        this.rootColor.setVisibility(View.GONE);
        this.rootBackground.setVisibility(View.GONE);
        StickerConst.keyboardToggler(getActivity(), this.mEditText, index == 0);
        switch (index) {
            case 0 /*0*/:
                this.mType.setSelected(true);
                this.mEditText.requestFocus();
                break;
            case 1 /*1*/:
                this.mFont.setSelected(true);
                this.rootFont.setVisibility(View.VISIBLE);
                break;
            case 2 /*2*/:
                this.mColor.setSelected(true);
                this.rootColor.setVisibility(View.VISIBLE);
                break;
            case 3 /*3*/:
                this.mBackground.setSelected(true);
                this.rootBackground.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    void loadBackgrounds() {
        int margin = AppUtils.screenHeight / 120;
        int size = AppUtils.screenHeight / 12;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams((int) (((float) (size * 190)) / 94.0f), size);
        param.setMargins(margin * 2, margin, margin * 2, margin);
        for (int i = 0; i < 26; i++) {
            ImageButton ib = new ImageButton(getActivity());
            try {
                setBackground(ib, new BitmapDrawable(getResources(),
                        BitmapFactory.decodeStream(getActivity().getAssets().open("imagess/ground/ground_" + i + ".png"))));
            } catch (IOException var6) {
                var6.printStackTrace();
            }
            this.mLinBgContainer.addView(ib);
            ib.setLayoutParams(param);
            ib.setTag(Integer.valueOf(i));
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextFragment.this.mTextAndStickerView.setTextBackground(((Integer) ((ImageButton) v).getTag()).intValue());
                }
            });
        }
    }

    public boolean onAlphaChanged(int progress, View view) {
        if (view instanceof ViewGroup) {
            for (int var3 = 0; var3 < ((ViewGroup) view).getChildCount(); var3++) {
                onAlphaChanged(progress, ((ViewGroup) view).getChildAt(var3));
                if ((view).getBackground() != null) {
                    (view).getBackground().setAlpha(progress);
                }
            }
        } else if (view instanceof ImageView) {
            if (((ImageView) view).getDrawable() != null) {
                ((ImageView) view).getDrawable().setAlpha(progress);
            }
            if ((view).getBackground() != null) {
                (view).getBackground().setAlpha(progress);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(((TextView) view).getTextColors().withAlpha(progress));
            if ((view).getBackground() != null) {
                (view).getBackground().setAlpha(progress);
            }
        } else if (view instanceof EditText) {
            ((EditText) view).setTextColor(((EditText) view).getTextColors().withAlpha(progress));
            if ((view).getBackground() != null) {
                (view).getBackground().setAlpha(progress);
            }
        }
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_st_keypad /*2131296370*/:
            case R.id.ib_st_font /*2131296371*/:
            case R.id.ib_st_tcolor /*2131296372*/:
            case R.id.ib_st_tbg /*2131296373*/:
                onSelected(((Integer) ((ImageButton) v).getTag()).intValue());
                break;
            case R.id.ib_tf_discard /*2131296378*/:
                StickerConst.keyboardToggler(getActivity(), this.mEditText, false);
                getActivity().onBackPressed();
                break;
            case R.id.ib_tf_apply /*2131296379*/:
                if (StickerConst.selectedSticker != null) {
                    StickerConst.drawingLayout.removeView(StickerConst.selectedSticker);
                }
                StickerConst.keyboardToggler(getActivity(), this.mEditText, false);
                getActivity().onBackPressed();
                if (this.mEditText.getText().toString().length() > 0) {
                    this.mRelTextMain.removeView(this.mTextAndStickerView);
                    StickerConst.drawingLayout.addView(this.mTextAndStickerView);
                    this.mTextAndStickerView.setIsResponse(true);
                    this.mTextAndStickerView.setActive(true);
                }
                break;
            default:
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
        StickerConst.initFont(getActivity());
        View root = layoutInflater.inflate(R.layout.text_fragment, parent, false);
        root.findViewById(R.id.ib_tf_apply).setOnClickListener(this);
        root.findViewById(R.id.ib_tf_discard).setOnClickListener(this);
        this.mType = (ImageButton) root.findViewById(R.id.ib_st_keypad);
        this.mFont = (ImageButton) root.findViewById(R.id.ib_st_font);
        this.mColor = (ImageButton) root.findViewById(R.id.ib_st_tcolor);
        this.mBackground = (ImageButton) root.findViewById(R.id.ib_st_tbg);
        this.rootFont = root.findViewById(R.id.vst_font_view);
        this.rootColor = root.findViewById(R.id.vst_tcolor_view);
        this.rootBackground = root.findViewById(R.id.vts_tbg_view);
        this.mLinBgContainer = (LinearLayout) root.findViewById(R.id.ln_txtbg_con);
        this.mVpFont = (ViewPager) root.findViewById(R.id.vp_st_font);
        this.mDotIndicator = (DotIndicator) root.findViewById(R.id.di_st_font);
        this.mSeekColor = (SeekBar) root.findViewById(R.id.sb_txtcolor);
        this.mSeekOpacity = (SeekBar) root.findViewById(R.id.sb_txtcolor_opacity);
        this.mSeekShadow = (SeekBar) root.findViewById(R.id.sb_txtshadow_color);
        this.mSeekShadoOffset = (SeekBar) root.findViewById(R.id.sb_txtshadow_opacity);
        this.mSeekBgOpacity = (SeekBar) root.findViewById(R.id.sb_txtbg_opacity);
        this.mSeekColorTemp = (SeekBar) root.findViewById(R.id.sb_txtcolor_sec);
        this.mSeekShadowColor = (SeekBar) root.findViewById(R.id.sb_txtshadow_color_sec);
        this.mSeekColorTemp.setMax(StickerConst.progressColors.length - 1);
        this.mSeekShadowColor.setMax(StickerConst.progressColors.length - 1);
        this.mSeekColorTemp.setThumb(ContextCompat.getDrawable(getActivity(), R.drawable.empty_bg));
        this.mSeekShadowColor.setThumb(ContextCompat.getDrawable(getActivity(), R.drawable.empty_bg));
        this.mSeekColor.setMax(StickerConst.progressColors.length - 1);
        this.mSeekShadow.setMax(StickerConst.progressColors.length - 1);
        AppUtils.setSeekBarThumb(this.mSeekBgOpacity);
        AppUtils.setSeekBarThumb(this.mSeekShadoOffset);
        AppUtils.setSeekBarThumb(this.mSeekOpacity);
        this.mSeekColorTemp.setOnSeekBarChangeListener(this);
        this.mSeekShadowColor.setOnSeekBarChangeListener(this);
        this.mSeekColor.setOnSeekBarChangeListener(this);
        this.mSeekColor.setThumb(StickerConst.getThumbDrawable(getResources(), 0));
        this.mSeekOpacity.setProgress(this.mSeekOpacity.getMax());
        this.mSeekOpacity.setOnSeekBarChangeListener(this);
        this.mSeekShadow.setOnSeekBarChangeListener(this);
        this.mSeekShadow.setThumb(StickerConst.getThumbDrawable(getResources(), 0));
        this.mSeekShadoOffset.setProgress(this.mSeekShadoOffset.getMax() / 2);
        this.mSeekShadoOffset.setOnSeekBarChangeListener(this);
        this.mSeekBgOpacity.setProgress(this.mSeekOpacity.getMax());
        this.mSeekBgOpacity.setOnSeekBarChangeListener(this);
        this.mEditText = (EditText) root.findViewById(R.id.edit_text);
        this.mEditText.addTextChangedListener(mTextWatcher);
        this.mEditText.setAlpha(0.0f);
        this.mType.setSelected(true);
        this.mType.setOnClickListener(this);
        this.mFont.setOnClickListener(this);
        this.mColor.setOnClickListener(this);
        this.mBackground.setOnClickListener(this);
        AppUtils.setSelector(this.mType, R.drawable.ic_keyboard_);
        AppUtils.setSelector(this.mFont, R.drawable.ic_text2);
        AppUtils.setSelector(this.mColor, R.drawable.ic_text_color_style);
        AppUtils.setSelector(this.mBackground, R.drawable.ic_text_background);
        this.mType.setTag(Integer.valueOf(0));
        this.mFont.setTag(Integer.valueOf(1));
        this.mColor.setTag(Integer.valueOf(2));
        this.mBackground.setTag(Integer.valueOf(3));
        this.mVpFont.setAdapter(new FontPagerAdapter(getActivity()));
        this.mDotIndicator.setViewPager(this.mVpFont);
        loadBackgrounds();
        this.mRelTextMain = (RelativeLayout) root.findViewById(R.id.rl_st_main_view);
        this.mTextAndStickerView = new TextAndStickerView(getActivity(), "", 0);
        this.mRelTextMain.addView(this.mTextAndStickerView);
        this.mTextAndStickerView.setIsResponse(false);
        if (StickerConst.selectedSticker != null) {
            this.mEditText.setText(StickerConst.selectedSticker.getText());
            this.mTextAndStickerView.setText(StickerConst.selectedSticker.getText());
            this.mTextAndStickerView.setFont(StickerConst.selectedSticker.getFontNum());
            this.mTextAndStickerView.setTextAlpha(StickerConst.selectedSticker.getTextAlpha());
            this.mTextAndStickerView.setAlpha(StickerConst.selectedSticker.getMyAlpha());
            this.mTextAndStickerView.setTextColor(StickerConst.selectedSticker.getTextColor());
            this.mTextAndStickerView.setShadowAlpha(StickerConst.selectedSticker.getShadowAlpha());
            this.mTextAndStickerView.setShadowSize(StickerConst.selectedSticker.getShadowSize());
            this.mTextAndStickerView.setShadowColor(StickerConst.selectedSticker.getShadowColor());
            this.mTextAndStickerView.setTextBackground(StickerConst.selectedSticker.getTextBackground());
        }
        this.height = 0;
        onSelected(0);
        this.mContentView = root.findViewById(R.id.text_menu_content_view);
        root.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        AppUtils.setImage((ImageButton) root.findViewById(R.id.ib_tf_apply), R.mipmap.ic_mark);
        return root;
    }

    public void onPause() {
        super.onPause();
        this.selectedView = null;
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_txtbg_opacity /*2131296400*/:
                onAlphaChanged((int) (((float) progress) / ((float) this.mSeekBgOpacity.getMax())), this.mTextAndStickerView);
                this.mTextAndStickerView.setAlpha(((float) progress) / ((float) this.mSeekBgOpacity.getMax()));
                break;
            case R.id.sb_txtcolor /*2131296402*/:
                this.mTextAndStickerView.setTextColor(StickerConst.progressColors[progress]);
                this.mSeekColor.setThumb(StickerConst.getThumbDrawable(getResources(), progress));
                break;
            case R.id.sb_txtcolor_sec /*2131296403*/:
                if (this.mSeekColor.getProgress() != progress) {
                    this.mSeekColor.setProgress(progress);
                }
                break;
            case R.id.sb_txtcolor_opacity /*2131296404*/:
                this.mTextAndStickerView.setTextAlpha(((float) progress) / ((float) this.mSeekOpacity.getMax()));
                break;
            case R.id.sb_txtshadow_color /*2131296406*/:
                this.mTextAndStickerView.setShadowColor(StickerConst.progressColors[progress]);
                this.mSeekShadow.setThumb(StickerConst.getThumbDrawable(getResources(), progress));
                break;
            case R.id.sb_txtshadow_color_sec /*2131296407*/:
                if (this.mSeekShadow.getProgress() != progress) {
                    this.mSeekShadow.setProgress(progress);
                }
                break;
            case R.id.sb_txtshadow_opacity /*2131296408*/:
                this.mTextAndStickerView.setShadowSize((((((float) progress) / ((float) this.mSeekShadoOffset.getMax())) * 2.0f)
                        - 1.0f) * -1f);
                break;
            default:
        }
    }

    public void onStartTrackingTouch(SeekBar var1) {
    }

    public void onStopTrackingTouch(SeekBar var1) {
    }

    public void onStart() {
        super.onStart();
        if (StickerConst.drawingLayout != null) {
            StickerConst.drawingLayout.bringToFront();
        }
        if (!StickerConst.keyboardShown) {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(1, 0);
            im.toggleSoftInputFromWindow(this.mEditText.getApplicationWindowToken(), 1, 2);
        }
    }

    ////######################end TextStickerFragments########################
    ////**********************************************************************

    private class FontPagerAdapter extends PagerAdapter {
        Context mContext;

        public FontPagerAdapter(Context context) {
            this.mContext = context;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        public int getCount() {
            byte count;
            int pages = StickerConst.fonts.length / 6;
            if (StickerConst.fonts.length % 6 == 0) {
                count = (byte) 0;
            } else {
                count = (byte) 1;
            }
            return count + pages;
        }

        @Override
        public Object instantiateItem(ViewGroup parent, int position) {
            View root = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.font_pager_adapter, null, false);
            Button btn1 = (Button) root.findViewById(R.id.btn_st_font_0);
            Button btn2 = (Button) root.findViewById(R.id.btn_st_font_1);
            Button btn3 = (Button) root.findViewById(R.id.btn_st_font_2);
            Button btn4 = (Button) root.findViewById(R.id.btn_st_font_3);
            Button btn5 = (Button) root.findViewById(R.id.btn_st_font_4);
            Button btn6 = (Button) root.findViewById(R.id.btn_st_font_5);
            btn1.setTextSize(15.0f);
            btn2.setTextSize(15.0f);
            btn3.setTextSize(15.0f);
            btn4.setTextSize(15.0f);
            btn5.setTextSize(15.0f);
            btn6.setTextSize(15.0f);
            position *= 6;
            if (mEditText != null) {
                if (mEditText.getText().toString() != null && mEditText.getText().toString().length() > 0) {
                    defaultText = mEditText.getText().toString();
                }
            }
            if (position < StickerConst.fonts.length) {
                btn1.setTag(Integer.valueOf(position));
                if (position == 0) {
                    btn1.setTypeface(null, 0);
                } else {
                    btn1.setTypeface(TypefacesUtils.get(TextFragment.this.getActivity(), "fonts/" + StickerConst.fonts[position]));
                }
                btn1.setText(TextFragment.this.defaultText);
                btn1.setOnClickListener(TextFragment.this.clickListener);
            }
            if (position + 1 < StickerConst.fonts.length) {
                btn2.setTag(Integer.valueOf(position + 1));
                btn2.setTypeface(TypefacesUtils.get(TextFragment.this.getActivity(), "fonts/" + StickerConst.fonts[position + 1]));
                btn2.setText(TextFragment.this.defaultText);
                btn2.setOnClickListener(TextFragment.this.clickListener);
            } else {
                btn2.setVisibility(View.GONE);
            }
            if (position + 2 < StickerConst.fonts.length) {
                btn3.setTag(Integer.valueOf(position + 2));
                btn3.setTypeface(TypefacesUtils.get(TextFragment.this.getActivity(), "fonts/" + StickerConst.fonts[position + 2]));
                btn3.setText(TextFragment.this.defaultText);
                btn3.setOnClickListener(TextFragment.this.clickListener);
            } else {
                btn3.setVisibility(View.GONE);
            }
            if (position + 3 < StickerConst.fonts.length) {
                btn4.setTag(Integer.valueOf(position + 3));
                btn4.setTypeface(TypefacesUtils.get(TextFragment.this.getActivity(), "fonts/" + StickerConst.fonts[position + 3]));
                btn4.setText(TextFragment.this.defaultText);
                btn4.setOnClickListener(TextFragment.this.clickListener);
            } else {
                btn4.setVisibility(View.GONE);
            }
            if (position + 4 < StickerConst.fonts.length) {
                btn5.setTag(Integer.valueOf(position + 4));
                btn5.setTypeface(TypefacesUtils.get(TextFragment.this.getActivity(), "fonts/" + StickerConst.fonts[position + 4]));
                btn5.setText(TextFragment.this.defaultText);
                btn5.setOnClickListener(TextFragment.this.clickListener);
            } else {
                btn5.setVisibility(View.GONE);
            }
            if (position + 5 < StickerConst.fonts.length) {
                btn6.setTag(Integer.valueOf(position + 5));
                btn6.setTypeface(TypefacesUtils.get(TextFragment.this.getActivity(), "fonts/" + StickerConst.fonts[position + 5]));
                btn6.setText(TextFragment.this.defaultText);
                btn6.setOnClickListener(TextFragment.this.clickListener);
            } else {
                btn6.setVisibility(View.GONE);
            }
            parent.addView(root);
            return root;
        }

        public boolean isViewFromObject(View var1, Object var2) {
            return var1 == var2;
        }
    }
}
