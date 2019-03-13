package nonworkingcode.effects.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
import com.gallery.utils.ImageUtils;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRenderer;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.PixelBuffer;
import nonworkingcode.effects.custuminterface.IOnBackStateChangeListener;
import nonworkingcode.effects.util.EffectsUtils;
import nonworkingcode.effects.viewpanel.EffectPanel;
import nonworkingcode.grid.util.CollageUtil;

/**
 * Created by Caliber Fashion on 12/5/2016.
 */

public class EffectActivity extends BaseActivity {

    private static final String TAG = EffectActivity.class.getSimpleName();
    private ImageButton ibBack;
    private ImageButton ibReset;
    private ImageButton ibSave;
    private EffectPanel mEffectPanel;
    private GPUImageView mImageView;
    private FrameLayout relContent;
    private Bitmap sourceBitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.start_bottom_in,R.anim.start_bottom_out);
        setContentView(R.layout.effects_activity);

        LinearLayout adContainer = (LinearLayout) findViewById(R.id.ad_container);
        AdsHelper.loadAdmobBanner(this, adContainer);

        this.mEffectPanel = (EffectPanel) findViewById(R.id.panel_filter_menu);
        this.mImageView = new GPUImageView(this);
        this.relContent = (FrameLayout) findViewById(R.id.rel_content);
        this.ibSave = (ImageButton) findViewById(R.id.ib_efct_apply);
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int appType = getIntent().getIntExtra(EffectsUtils.EXTRA_TYPE, EffectsUtils.APP_TYPE_PIP);
                if (appType == EffectsUtils.APP_TYPE_COLLAGE) {
                    Intent intent = new Intent();
                    if (getIntent().hasExtra(EffectsUtils.EXTRA_INDEX)) {
                        intent.putExtra(CollageUtil.EXTRA_INDEX, getIntent().getIntExtra(EffectsUtils.EXTRA_INDEX, -1));
                    }
                    setResult(RESULT_OK, intent);
                } else {
                    //this is EffectsUtils.APP_TYPE_PIP
                    setResult(RESULT_OK);

                }
                //overridePendingTransition(17432576, R.anim.start_bottom_out);
                finish();
            }
        });
        ibBack = (ImageButton) findViewById(R.id.ib_efct_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibReset = (ImageButton) findViewById(R.id.ib_efct_reset);
        ibReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyEffect(sourceBitmap);
            }
        });
        if (getIntent().getData() != null) {
            sourceBitmap = ImageUtils.getinstance().loadBitmap(this, getIntent().getData(), 1);
            EffectsUtils.getInstance().setBitmap(sourceBitmap, true, true);
            EffectsUtils.setImage(this.ibSave, R.mipmap.ic_done);
            init();
        } else {
            EffectsUtils.setImage(this.ibSave, R.mipmap.ic_done);
            init();
        }
    }

    private void init() {
        sourceBitmap = EffectsUtils.getInstance().getBitmap(true, false);
        initView();
        this.mEffectPanel.setIOnBackStateChangeListener(new IOnBackStateChangeListener() {
            @Override
            public void canBack(boolean canPressed) {
                //AppUtils.showHideToolBar(EffectActivity.this.findViewById(R.id.top_bar_ac), canPressed);
            }
        });
    }

    private void initView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!EffectActivity.this.isFinishing() && EffectActivity.this.sourceBitmap != null) {
                    int viewWidth = AppUtils.screenWidth;
                    int viewHeight = AppUtils.screenHeight - (((int) AppUtils.dpToPx(EffectActivity.this, 120.0f)) + (AppUtils.screenHeight / 4));
                    int imageWidth = EffectActivity.this.sourceBitmap.getWidth();
                    int imageHeight = EffectActivity.this.sourceBitmap.getHeight();
                    int width = viewWidth;
                    int height = viewHeight;
                    if (imageWidth >= imageHeight) {
                        height = (int) (((float) viewWidth) * ((((float) imageHeight) * 1.0) / ((float) imageWidth)));
                        width = viewWidth;
                    } else {
                        width = (int) (((float) viewHeight) * ((((float) imageWidth) * 1.0) / ((float) imageHeight)));
                        height = viewHeight;
                    }
                    RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(width, height);
                    layoutParam.addRule(RelativeLayout.CENTER_IN_PARENT);
                    relContent.setLayoutParams(layoutParam);
                    relContent.removeAllViews();
                    relContent.addView(mImageView);
                    mImageView.setImage(sourceBitmap);
                    mEffectPanel.setGPUImageView(EffectActivity.this, mImageView);
                }
            }
        });

    }

    public void onApplyEffect(final GPUImageFilter filter) {
        final GPUImageFilter mFilter = filter;
        if (mFilter != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = EffectsUtils.getInstance().getBitmap(false, false)
                            .copy(Bitmap.Config.ARGB_8888, true);
                    if (bitmap != null && !bitmap.isRecycled()) {
                        GPUImageRenderer renderer = new GPUImageRenderer(mFilter);
                        renderer.setImageBitmap(bitmap, false);
                        PixelBuffer buffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
                        buffer.setRenderer(renderer);
                        final Bitmap filterBitmap = buffer.getBitmap();
                        renderer.deleteImage();
                        buffer.destroy();
                        EffectActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!(bitmap == null || bitmap.isRecycled())) {
                                    //bitmap.recycle();
                                }
                                EffectActivity.this.applyEffect(filterBitmap);
                            }
                        });
                    }
                }
            }).start();
        }

    }

    public void applyEffect(Bitmap bitmap) {
        this.mImageView = new GPUImageView(this);
        this.relContent.removeAllViews();
        this.relContent.addView(this.mImageView);
        this.mEffectPanel.setGPUImageView(this, this.mImageView);
        this.mImageView.setImage(bitmap);
        EffectsUtils.getInstance().setBitmap(bitmap, true, true);
        this.mImageView.invalidate();
    }

    @Override
    public void onBackPressed() {
        if (!this.mEffectPanel.onBackPressed()) {
            super.onBackPressed();
            // overridePendingTransition(17432576, R.anim.start_bottom_out);
        }
    }
}
