package com.formationapps.nameart.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.widget.BgEraseBottomView;
import com.removebg.BgEraserImageUtils;
import com.removebg.BgEraserView;
import com.removebg.BitmapContainer;
import com.removebg.MultiTouchListener;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.removebg.BgEraserView.ERASE;
import static com.removebg.BgEraserView.FREEMODE;
import static com.removebg.BgEraserView.RESTORE;
import static com.removebg.BgEraserView.TARGET;

public class BgRemoverActivity extends BaseActivity {
    private static final int REQUEST_FEATHER_ACTIVITY = 106;
    public static Bitmap bgCircleBit = null;
    public Bitmap bitmap = null;
    public static int curBgType = 1;
    public static int orgBitHeight;
    public static int orgBitWidth;
    private Bitmap orgBitmap;
    public static BitmapShader patternBMPshader;
    private final int margin=40;
    private Bitmap f22b = null;

    private BgEraserView mEV;
    private ImageView mRESHelper;

    private int height;

    private RelativeLayout main_rel;

    private ImageButton redo_btn;
    private ImageButton undo_btn;

    private ImageButton save_btn;

    public Uri selectedImageUri;
    //private MagnifyView magnifyView;

    private boolean showDialog = false;
    private ImageView tbg_img;

    private TextView txt_redo;
    private TextView txt_undo;

    private int width;
    private BgEraseBottomView mBgEraseBottomView;

    private ProgressBar mProgressBar;
    private LAUNCHTYPE launchType=LAUNCHTYPE.MAIN;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.bg_eraser_activity);

        BitmapContainer.getInstance().destroyBitmaps();

        View adsView=getLayoutInflater().inflate(R.layout.ads_layout,null,false);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(adsView);

        mProgressBar=findViewById(R.id.circularProgressbar);

        mBgEraseBottomView =findViewById(R.id.bottom_button_view);

        AdsHelper.loadAdmobBanner(this, (LinearLayout)adsView.findViewById(R.id.adConatiner));

        this.main_rel =  findViewById(R.id.main_rel);

        this.undo_btn = findViewById(R.id.btn_undo_img);
        this.redo_btn = findViewById(R.id.btn_redo_img);

        this.save_btn = findViewById(R.id.btn_done);


        undo_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //onUndoBtn();
            }
        });

        redo_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //onRedoBtn();
            }
        });
        this.undo_btn.setEnabled(false);
        this.redo_btn.setEnabled(false);
        this.save_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveBtn();
            }
        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int sh = displaymetrics.heightPixels;
        this.width = displaymetrics.widthPixels;
        this.height = sh - BgEraserImageUtils.dpToPx(this, 120);
        curBgType = 2;
        this.main_rel.postDelayed(new Runnable() {
            @Override
            public void run() {
                //tbg_img.setImageBitmap(BgEraserImageUtils.getTiledBitmap(BgRemoverActivity.this, R.drawable.trans_bg_tile, width, height));
                //bgCircleBit = BgEraserImageUtils.getBgCircleBit(BgRemoverActivity.this, R.drawable.trans_bg_tile);
                setSelectedImage(getIntent().getData());
            }
        }, 5);
        //tbg_img.setImageBitmap(BgEraserImageUtils.getTiledBitmap(BgRemoverActivity.this, R.drawable.trans_bg_tile, width, height));
        findViewById(R.id.main_rel_parent).setBackground(new BitmapDrawable(getResources(),
                BgEraserImageUtils.getTiledBitmap(BgRemoverActivity.this, R.drawable.tp, width, height)));
    }
    private MODEOPTIONS selectedMode;
    public void onSelectedMode(MODEOPTIONS selected){
        if(mEV==null){
            return;
        }
        selectedMode=selected;
        setSelected(selectedMode);
        switch (selectedMode){
            case AUTO:
                this.mEV.enableTouchClear(true);
                this.main_rel.setOnTouchListener(null);
                this.mEV.setMODE(TARGET);
                this.mEV.invalidate();
                break;
            case ERASE:
                this.mEV.enableTouchClear(true);
                this.main_rel.setOnTouchListener(null);
                this.mEV.setMODE(ERASE);
                this.mEV.invalidate();
                break;
            case FREEHAND:
                this.mEV.enableTouchClear(true);
                this.main_rel.setOnTouchListener(null);
                this.mEV.setMODE(FREEMODE);
                this.mEV.invalidate();
                break;
            case FREEHAND_INSIDE:
                this.mEV.enableInsideRemover(true);
                break;
            case FREEHAND_OUTSIDE:
                this.mEV.enableInsideRemover(false);
                break;
            case RESTORE:
                this.mEV.enableTouchClear(true);
                this.main_rel.setOnTouchListener(null);
                this.mEV.setMODE(RESTORE);
                this.mEV.invalidate();
                break;
            case ZOOM:
                this.mEV.enableTouchClear(false);
                MultiTouchListener touchListener = new MultiTouchListener();
                this.main_rel.setOnTouchListener(touchListener);
                this.mEV.setMODE(BgEraserView.ZOOM);
                this.mEV.invalidate();
                break;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == REQUEST_FEATHER_ACTIVITY) {
            setResult(-1);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mBgEraseBottomView.onBackPressed()) {
            setResult(RESULT_CANCELED);
            showBackpressedDialog();
        }

    }

    private void setSelectedImage(Uri imageUri) {
        if(isDestroyed()||isFinishing()){
            return;
        }
        this.selectedImageUri = imageUri;
        this.showDialog = false;

        final ProgressDialog dialog = ProgressDialog.show(this, null, null);
        ProgressBar spinner = new ProgressBar(this, null,android.R.attr.progressBarStyle);
        spinner.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.progressdialog_circle_color),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(spinner);
        dialog.setCancelable(false);
        dialog.show();
        if(selectedImageUri!=null){
            Glide.with(getApplicationContext()).asBitmap().load(selectedImageUri).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    f22b=resource;
                    if (f22b.getWidth() >width ||f22b.getHeight() >height || (f22b.getWidth() <width &&f22b.getHeight() <height)) {
                        f22b = BgEraserImageUtils.resizeBitmap(f22b, width, height);
                    }
                    launchType=LAUNCHTYPE.MAIN;
                    checkBitmap(dialog);
                }
            });
        }else {
            f22b= BitmapContainer.getInstance().getBitmap();
            launchType=LAUNCHTYPE.SUPPORT;
            checkBitmap(dialog);
        }
    }
    private void checkBitmap(ProgressDialog dialog){
        dialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (BgRemoverActivity.this.showDialog) {
                    BgRemoverActivity.this.finish();
                } else {
                    BgRemoverActivity.this.setImageBitmap();
                }
            }
        });
        try {
            if (BgRemoverActivity.this.f22b == null) {
                BgRemoverActivity.this.showDialog = true;
            } else {
                orgBitmap = BgRemoverActivity.this.f22b.copy(Bitmap.Config.ARGB_8888, true);
                int space = BgEraserImageUtils.dpToPx(BgRemoverActivity.this, margin);
                orgBitWidth = BgRemoverActivity.this.f22b.getWidth();
                orgBitHeight = BgRemoverActivity.this.f22b.getHeight();
                Bitmap dest = Bitmap.createBitmap((f22b.getWidth() + space) + space, (f22b.getHeight() + space) + space, BgRemoverActivity.this.f22b.getConfig());
                Canvas canvas = new Canvas(dest);
                canvas.drawColor(0);
                canvas.drawBitmap(BgRemoverActivity.this.f22b, (float) space, (float) space, null);
                f22b = dest;
                if (f22b.getWidth() > width || f22b.getHeight() > height || (f22b.getWidth() < width && f22b.getHeight() < height)) {
                    f22b = BgEraserImageUtils.resizeBitmap(f22b, width, height);
                }
            }
            dialog.dismiss();
        }catch (OutOfMemoryError e){
            BgRemoverActivity.this.showDialog = true;
            dialog.dismiss();
        }catch (Exception e1){
            BgRemoverActivity.this.showDialog = true;
            dialog.dismiss();
        }

    }

    private void setImageBitmap() {
        this.mEV = new BgEraserView(this);
        mBgEraseBottomView.setEraseView(mEV);
        this.mRESHelper = new ImageView(this);
        this.mEV.setImageBitmap(this.f22b);
        Bitmap bb=getGreenLayerBitmap(this.f22b);
        if(bb!=null&&!bb.isRecycled())
            this.mRESHelper.setImageBitmap(bb);
        this.mEV.enableTouchClear(false);
        this.mEV.setMODE(BgEraserView.ZOOM);
        this.mEV.invalidate();

        this.mEV.radius(20);
        this.mEV.offset(75);
        this.mEV.invalidate();
        this.main_rel.setScaleX(1.0f);
        this.main_rel.setScaleY(1.0f);
        this.main_rel.setTranslationX(0.0f);
        this.main_rel.setTranslationY(0.0f);
        this.main_rel.removeAllViews();
        this.main_rel.addView(this.mRESHelper);
        this.main_rel.addView(this.mEV);
        this.mEV.invalidate();
        this.mRESHelper.setVisibility(GONE);
        this.mEV.undoRedoListener(new BgEraserView.UndoRedoListener() {
            public void enableUndo(boolean undo, int num) {
                if (undo) {
                    setBGDrawable(BgRemoverActivity.this.txt_undo, num, undo_btn, R.mipmap.undo, undo);
                    return;
                }
                setBGDrawable(BgRemoverActivity.this.txt_undo, num, undo_btn, R.mipmap.undo, undo);
            }

            public void enableRedo(boolean redo, int num) {
                if (redo) {
                    setBGDrawable(BgRemoverActivity.this.txt_redo, num, redo_btn, R.mipmap.redo, redo);
                    return;
                }
                setBGDrawable(BgRemoverActivity.this.txt_redo, num, redo_btn, R.mipmap.redo, redo);
            }
        });
        //destroyBitmap(f22b);

        this.mEV.actionListener(new BgEraserView.ActionListener() {
            public void onActionCompleted(final int mode) {
                BgRemoverActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (mode == FREEMODE) {
                            //BgRemoverActivity.this.offset_seekbar_lay.setVisibility(GONE);
                           if(mBgEraseBottomView !=null){
                               mBgEraseBottomView.addFreehandEraseOptions();
                           }
                        }
                    }
                });
            }

            public void onAction(final int action) {
                BgRemoverActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (action == 0) {
                            mBgEraseBottomView.setVisibility(GONE);
                        }
                        if (action == 1) {
                            mBgEraseBottomView.setVisibility(VISIBLE);
                        }
                    }
                });
            }
        });
        //onClick(findViewById(R.id.btn_magic_rel_lay));
        onSelectedMode(MODEOPTIONS.ZOOM);
    }

    private void onRedoBtn(){
        final ProgressDialog dialog = ProgressDialog.show(this, null, null);
        ProgressBar spinner = new ProgressBar(this, null,android.R.attr.progressBarStyle);
        spinner.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.progressdialog_circle_color),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(spinner);
        dialog.setCancelable(false);
        dialog.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    BgRemoverActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEV.redoChange();
                        }
                    });
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }).start();
    }
    private void onUndoBtn(){
        //Toast.makeText(this,"undo",Toast.LENGTH_SHORT).show();
        final ProgressDialog dialog = ProgressDialog.show(this, null, null);
        ProgressBar spinner = new ProgressBar(this, null,android.R.attr.progressBarStyle);
        spinner.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.progressdialog_circle_color),
                android.graphics.PorterDuff.Mode.SRC_IN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(spinner);
        dialog.setCancelable(false);
        dialog.show();
        new Thread(new Runnable() {

            public void run() {
                try {
                    BgRemoverActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BgRemoverActivity.this.mEV.undoChange();
                        }
                    });
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }).start();
    }
    public void onSaveBtn(){
        if(mEV!=null){
            final Bitmap bitmap1 = this.mEV.finalBitmap();
            if (bitmap1 != null) {
                try {
                    int space = BgEraserImageUtils.dpToPx(this, margin);
                    final Bitmap bitmap2 = BgEraserImageUtils.resizeBitmap(bitmap1, (orgBitWidth + space) + space, (orgBitHeight + space) + space);
                    final Bitmap bitmap3 = Bitmap.createBitmap(bitmap2, space, space, bitmap2.getWidth() - (space + space), bitmap2.getHeight() - (space + space));
                    final Bitmap bitmap4 = Bitmap.createScaledBitmap(bitmap3, orgBitWidth, orgBitHeight, true);
                    bitmap = BgEraserImageUtils.bitmapmasking1(orgBitmap, bitmap4);

                    if(launchType==LAUNCHTYPE.MAIN){
                        saveButtonClicked(bitmap) ;
                    }else if(launchType==LAUNCHTYPE.SUPPORT){
                        BitmapContainer.getInstance().setBitmap(bitmap);
                        setResult(RESULT_OK);
                        finish();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                destroyBitmap(bitmap1);
                                destroyBitmap(bitmap2);
                                destroyBitmap(bitmap3);
                                destroyBitmap(bitmap4);
                                destroyBitmap(orgBitmap);
                                finish();
                            }
                        },1000);
                    }
                    return;
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    return;
                }
            }else {
                finish();
            }

        }
    }

    public void setBGDrawable(TextView tv, int num, ImageView iv, int id, boolean b) {
        final ImageView imageView = iv;
        final int i = id;
        final boolean z = b;
        final TextView textView = tv;
        final int i2 = num;
        runOnUiThread(new Runnable() {
            public void run() {
                if(imageView!=null){
                    imageView.setBackgroundResource(i);
                    imageView.setEnabled(z);
                }
                if(textView!=null)
                    textView.setText(String.valueOf(i2));
            }
        });
    }

    public Bitmap getGreenLayerBitmap(Bitmap b) {
        if(b!=null&&!b.isRecycled()){
            Paint p = new Paint();
            //p.setColor(-16711936);
            p.setColor(Color.parseColor("#53CBF1"));
            p.setAlpha(100);
            int space = BgEraserImageUtils.dpToPx(this, margin);
            Bitmap.Config config=b.getConfig()==null? Bitmap.Config.ARGB_8888:b.getConfig();
            Bitmap dest = Bitmap.createBitmap((orgBitWidth + space) + space, (orgBitHeight + space) + space, config);
            Canvas canvas = new Canvas(dest);
            canvas.drawColor(0);
            canvas.drawBitmap(orgBitmap, (float) space, (float) space, null);
            canvas.drawRect((float) space, (float) space, (float) (orgBitWidth + space), (float) (orgBitHeight + space), p);
            Bitmap dest1 = Bitmap.createBitmap((orgBitWidth + space) + space, (orgBitHeight + space) + space, b.getConfig());
            Canvas canvas1 = new Canvas(dest1);
            canvas1.drawColor(0);
            canvas1.drawBitmap(orgBitmap, (float) space, (float) space, null);
            patternBMPshader = new BitmapShader(BgEraserImageUtils.resizeBitmap(dest1, this.width, this.height), TileMode.REPEAT, TileMode.REPEAT);
            return BgEraserImageUtils.resizeBitmap(dest, this.width, this.height);
        }
        return null;
    }

    public void setSelected(MODEOPTIONS selected) {
        if (selected == MODEOPTIONS.RESTORE) {
            this.mRESHelper.setVisibility(VISIBLE);
        } else {
            this.mRESHelper.setVisibility(GONE);
        }
        if (selected != MODEOPTIONS.ZOOM) {
            this.mEV.updateOnScale(this.main_rel.getScaleX());
        }
    }

    protected void onDestroy() {
        if(launchType==LAUNCHTYPE.SUPPORT){
           if (this.f22b != null&&!f22b.isRecycled()) {
            this.f22b.recycle();
            this.f22b = null;
            }
        }

        if (!(isFinishing()|| mEV ==null || this.mEV.pd == null || !this.mEV.pd.isShowing())) {
            this.mEV.pd.dismiss();
        }
        if(bitmap!=null&&!bitmap.isRecycled()){
            bitmap.recycle();bitmap=null;
        }
        super.onDestroy();
    }
    public enum MODEOPTIONS{
        AUTO,ERASE,FREEHAND,FREEHAND_INSIDE,FREEHAND_OUTSIDE,RESTORE,ZOOM
    }
    public enum LAUNCHTYPE{
        MAIN,SUPPORT
    }

}
