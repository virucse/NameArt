package com.editor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.editor.panel.EditorPanel;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
import com.gallery.utils.ImageUtils;
import com.xiaopo.flying.sticker.StickerView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import formationapps.helper.stickers.StickerFragment;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRenderer;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.PixelBuffer;
import nonworkingcode.effects.custuminterface.IOnBackStateChangeListener;
import nonworkingcode.effects.util.EffectsUtils;

/**
 * Created by Caliber Fashion on 12/5/2016.
 */

public class EditorActivity extends BaseActivity {

    private static final String TAG = EditorActivity.class.getSimpleName();
    private ImageButton ibBack;
    private ImageButton ibReset;
    private ImageButton ibSave;
    private EditorPanel mEffectPanel;
    private GPUImageView mImageView;
    private FrameLayout relContent;
    private Bitmap sourceBitmap;
    private RelativeLayout mainRelView, mBorderLayout;
    private ImageView actualView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        logAnalyticEvent("EDITOR_ACTIVITY");

        // AdView adview=(AdView)findViewById(R.id.ad_view);
        //adview.loadAd(new AdRequest.Builder().build());
        AdsHelper.loadAdmobBanner(this, (LinearLayout) findViewById(R.id.ad_container));

        this.mEffectPanel = (EditorPanel) findViewById(R.id.panel_filter_menu);
        this.mImageView = new GPUImageView(this);
        mainRelView = (RelativeLayout) findViewById(R.id.main_rel_view);

        this.ibSave = (ImageButton) findViewById(R.id.ib_efct_apply);
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = EffectsUtils.getInstance().getBitmap(false, false);
                if(relContent!=null)relContent.setVisibility(View.INVISIBLE);
                if(actualView!=null)actualView.setImageBitmap(bitmap);
                saveButtonClicked(mainRelView);
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
                if (!isFinishing() && sourceBitmap != null) {
                    int viewWidth = AppUtils.screenWidth;
                    int viewHeight = AppUtils.screenHeight - (((int) AppUtils.dpToPx(EditorActivity.this, 120.0f)) + (AppUtils.screenHeight / 4));
                    int imageWidth = sourceBitmap.getWidth();
                    int imageHeight = sourceBitmap.getHeight();
                    int width = viewWidth;
                    int height = viewHeight;
                    if (imageWidth >= imageHeight) {
                        height = (int) (((float) viewWidth) * ((((float) imageHeight) * 1.0) / ((float) imageWidth)));
                        width = viewWidth;
                    } else {
                        width = (int) (((float) viewHeight) * ((((float) imageWidth) * 1.0) / ((float) imageHeight)));
                        height = viewHeight;
                    }
                    setWidthHeightToLayout(width, height, null);

                }
            }
        });

    }

    private void setWidthHeightToLayout(int width, int height, final Bitmap bitmap) {
        if (width <= 0 || height <= 0) {
            return;
        }
        final RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(width, height);
        RelativeLayout.LayoutParams layp = (RelativeLayout.LayoutParams) mainRelView.getLayoutParams();
        layp.width = width;
        layp.height = height;
        layp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mainRelView.setLayoutParams(layp);
        mainRelView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainRelView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setActualParamToView(layoutParam);
                if (bitmap != null) {
                    applyEffect(bitmap);
                }
            }
        });
    }

    public void setActualParamToView(RelativeLayout.LayoutParams layoutParam) {
        layoutParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        if (relContent == null) {
            relContent = (FrameLayout) findViewById(R.id.rel_content);
        }
        relContent.setLayoutParams(layoutParam);
        relContent.removeAllViews();
        relContent.addView(mImageView);
        mImageView.setImage(sourceBitmap);
        mEffectPanel.setGPUImageView(this, mImageView);


        //border view
        if (mBorderLayout == null)
            mBorderLayout = (RelativeLayout) findViewById(R.id.borderView);
        //mBorderLayout.setLayoutParams(layoutParam);

        //init actual view for the time when you are about to save
        if (actualView == null)
            actualView = (ImageView) findViewById(R.id.actualimg);
        //actualView.setLayoutParams(layoutParam);
        if (stickerView == null) {
            stickerView = (StickerView) findViewById(R.id.sticker_view);
            setStickerOperation();
        }
        //stickerView.setLayoutParams(layoutParam);
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!(bitmap == null || bitmap.isRecycled())) {
                                    //bitmap.recycle();
                                }
                                applyEffect(filterBitmap);
                            }
                        });
                    }
                }
            }).start();
        }
    }

    public void applyEffect(Bitmap bitmap) {
        relContent.setVisibility(View.VISIBLE);
        this.mImageView = new GPUImageView(this);
        this.relContent.removeAllViews();
        this.relContent.addView(this.mImageView);
        this.mEffectPanel.setGPUImageView(this, this.mImageView);
        this.mImageView.setImage(bitmap);
        EffectsUtils.getInstance().setBitmap(bitmap, true, true);
        this.mImageView.invalidate();
    }

    public void onCropImage() {
        openCropInBackground();
    }

    public void showSticker() {
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content_temp,
                new StickerFragment(), "text").commit();
    }

    public void addBorder(Drawable drawable) {
        Drawable bitmap = mBorderLayout.getBackground();
        recycleDrawable(bitmap);
        mBorderLayout.setBackground(drawable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri imageUri = UCrop.getOutput(data);
            if (imageUri != null && imageUri.getScheme().equals("file")) {
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    //EffectsUtils.getInstance().setBitmap(mBitmap,true,true);
                    setWidthHeightToLayout(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap);
                } catch (Exception e) {
                    Log.e("ERROS - uCrop", imageUri.toString(), e);
                }
            }
        }
    }

    public void recycleDrawable(Drawable d) {
        if (d != null) {
            Bitmap b = ((BitmapDrawable) d).getBitmap();
            if (b != null && !b.isRecycled()) {
                //b.recycle();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stickerView != null)
            stickerView.setLocked(false);
        if (relContent != null && mainRelView != null) {
            relContent.setVisibility(View.VISIBLE);
            mainRelView.invalidate();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("text");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            //CollageUtil.hideAndShow(findViewById(R.id.top_bar_ac),true);
            mEffectPanel.resetLinSelector();
            return;
        }
        if (!mEffectPanel.onBackPressed()) {
            showBackpressedDialog();
            // overridePendingTransition(17432576, R.anim.start_bottom_out);
        }
    }

    private void openCropInBackground() {
        final Bitmap bitmapImage = EffectsUtils.getInstance().getBitmap(false, false);
        new AsyncTask<Void, Void, File>() {
            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(EditorActivity.this);
                pd.setMessage("Please Wait...");
                pd.show();
            }

            @Override
            protected File doInBackground(Void... params) {
                return saveToInternalStorage(bitmapImage);
            }

            @Override
            protected void onPostExecute(File s) {
                super.onPostExecute(s);
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                UCrop uCrop = UCrop.of(Uri.fromFile(s), Uri.fromFile(new File(getCacheDir(), "croppedImage.png")));
                uCrop.withOptions(getUcropOptions(bitmapImage));
                uCrop.start(EditorActivity.this);
            }
        }.execute();
    }

    private File saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("saveTempDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "temp.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath;
    }

    private void loadImageFromStorage(String path) {

        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = (ImageView) findViewById(R.id.actualimg);
            img.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public UCrop.Options getUcropOptions(Bitmap bitmap) {
        int[] sd = new int[2];
        sd[0] = AppUtils.screenWidth;
        sd[1] = AppUtils.screenHeight;
        if (bitmap != null) {
            try {
                sd[0] = bitmap.getWidth();
                sd[1] = bitmap.getHeight();
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        }
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setCompressionQuality(90);
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.black_color));
        options.setToolbarColor(ContextCompat.getColor(this, R.color.black_color));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.black_color));
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        options.setFreeStyleCropEnabled(true);
        //options.withAspectRatio(sd[0], sd[1]);
        return options;
    }
}
