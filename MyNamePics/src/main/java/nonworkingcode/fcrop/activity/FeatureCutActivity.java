package nonworkingcode.fcrop.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AdsHelper;

import nonworkingcode.fcrop.util.FeatureCropUtil;
import nonworkingcode.fcrop.util.ScalingUtilities;
import nonworkingcode.fcrop.view.FeatureCropView;

/**
 * Created by caliber fashion on 2/21/2017.
 */

public class FeatureCutActivity extends AppCompatActivity implements View.OnClickListener {
    static FeatureCutActivity instance;
    RelativeLayout cropLayer;
    Bitmap mBitmap, mFinalBitmap;
    FeatureCropView mFeatureCropView;
    private int width;
    private int height;
    //private GoogleAd f169g;
    private Dialog mDialog;

    public static FeatureCutActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feature_cut_activity);
        instance = this;

        LinearLayout header = (LinearLayout) findViewById(R.id.header);
        AdsHelper.loadAdmobBanner(this, header);

        cropLayer = (RelativeLayout) findViewById(R.id.cropLyt);
        findViewById(R.id.retryBtn).setOnClickListener(this);
        findViewById(R.id.InnercropBtn).setOnClickListener(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        try {
            width = displayMetrics.widthPixels;
            height = displayMetrics.heightPixels;
        } catch (Exception e) {
        }
        //this.mBitmap = m89a(getIntent().getStringExtra("mImageUri"));
        mBitmap = m89a(FeatureCropUtil.getInstance().getBitmap(true, true));
        if (mBitmap == null) {
            Toast.makeText(this, R.string.onbitmapnull, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(mBitmap.getWidth(), mBitmap.getHeight());
        layoutParams.addRule(13, -1);
        cropLayer.setLayoutParams(layoutParams);
        mFeatureCropView = new FeatureCropView(this);
        mFeatureCropView.m26a(this.mBitmap);
        cropLayer.addView(mFeatureCropView);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.hint_dialog_title);
        adb.setCancelable(true);
        adb.setMessage(R.string.hint_dialog_message);
        adb.setPositiveButton(R.string.hint_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.create().show();
        /*final Dialog dialog = new Dialog(this, R.style.TemplateDialog);
        dialog.setContentView(R.layout.hint1dialog);
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();*/
    }

    private Bitmap m89a(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return ScalingUtilities.m113a(bitmap,
                this.width, this.height, ScalingUtilities.ScalingEnum.FIT);
    }

    private Bitmap m89a(String mImageUri) {
        int i = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageUri, options);
        int i2 = options.outWidth;
        int i3 = options.outHeight;
        while (true) {
            if (i2 > 1200 || i3 > 1200) {
                i2 /= 2;
                i3 /= 2;
                i *= 2;
            } else {
                BitmapFactory.Options options2 = new BitmapFactory.Options();
                options2.inSampleSize = i;
                return ScalingUtilities.m113a(BitmapFactory.decodeFile(mImageUri, options2),
                        this.width, this.height, ScalingUtilities.ScalingEnum.FIT);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retryBtn:
                this.mFeatureCropView.m25a();
                findViewById(R.id.optionLyt).setVisibility(View.GONE);
                break;
            case R.id.InnercropBtn:
                if (mFinalBitmap != null && !mFinalBitmap.isRecycled()) {
                    //mFinalBitmap.recycle();
                }
                mFinalBitmap = cropBitmap(true);
                mDialog = new Dialog(this, R.style.aleartDialog);
                mDialog.requestWindowFeature(1);
                mDialog.setContentView(R.layout.feather_dialog);
                ((ImageView) this.mDialog.findViewById(R.id.img))
                        .setImageBitmap(mFinalBitmap);
                mDialog.findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Constants.f193a = true;
                        mDialog.cancel();
                        //Intent intent = new Intent(FeatureCutActivity.this, FeatureMasterActivity.class);
                        //intent.putExtra("crop", true);
                        //startActivity(intent);
                        //f169g.m112b();
                        FeatureCropUtil.getInstance().setBitmap(mFinalBitmap);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                mDialog.findViewById(R.id.btnimgDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Constants.f193a = false;
                        mDialog.cancel();
                        //Intent intent = new Intent(FeatureCutActivity.this, FeatureMasterActivity.class);
                        //intent.putExtra("crop", true);
                        //startActivity(intent);
                        //f169g.m112b();
                        //FeatureCutActivity.getInstance().finish();
                        mFeatureCropView.m25a();
                        findViewById(R.id.optionLyt).setVisibility(View.GONE);
                    }
                });
                mDialog.show();
                break;
        }
    }

    private Bitmap cropBitmap(boolean smartcut) {
        Bitmap bitmap1 = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap1);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#CCCCCD"));
        if (smartcut) {
            paint.setMaskFilter(new BlurMaskFilter(40.0f, BlurMaskFilter.Blur.NORMAL));
        } else {

        }
        Path path = new Path();
        for (int i = 0; i < FeatureCropView.mCropPointList.size(); i++) {
            path.lineTo(FeatureCropView.mCropPointList.get(i).x, FeatureCropView.mCropPointList.get(i).y);
        }
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBitmap, 0.0f, 0.0f, paint);
        return bitmap1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyBitmap(mBitmap);
    }

    private void destroyBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            //bitmap.recycle();
        }
    }
}
