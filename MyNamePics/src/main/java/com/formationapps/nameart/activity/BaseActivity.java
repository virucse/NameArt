package com.formationapps.nameart.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.downloader.DownloadListener;
import com.downloader.FileDownloadService;
import com.downloader.FileUtils;
import com.editor.activity.EditorActivity;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.MyStickerText;
import com.formationapps.nameart.helper.SaveToStorage;
import com.formationapps.nameart.helper.TypefacesUtils;
import com.formationapps.nameart.permission.MarseMallowPermission;
import com.gallery.activity.GalleryFragment;
import com.gif.GIFActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import billingmodule.billing.PurchaseUtils;
import formationapps.helper.db.ServerFont;
import formationapps.helper.stickers.StickerFragment;
import nonworkingcode.grid.activity.CollageActivity;
import nonworkingcode.pip.activity.PIPActivity;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by caliber fashion on 3/13/2017.
 */

public class BaseActivity extends Boss {
    protected static final int LAUNCH_GALLERY_HEAD = 3;
    protected static final int LAUNCH_HEAD_PHOTO = 4;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    public static File image_file;
    public static int[] brushThumb, backgroundImages;
    public static int[] TEXTURE;
    public static String[] fonts;
    private static BaseActivity instanse;
    private final String TAG = BaseActivity.class.getSimpleName();
    protected Toolbar mToolbar;
    protected StickerView stickerView;
    protected ImageView img_bg;
    protected int cl = Color.parseColor("#000000");
    protected int actualContentWidth, actualContentHeight;
    int selected = 0;
    int[] xSizes, ySizes;
    private boolean isKeyboardShown = false;
    // private float[] matrixValues;
    private float oldDist = 1f;
    private float oldAngle = 0f;
    private float newAngle = 0f;
    private float[] lastEvent = null;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private Matrix savedMatrix = new Matrix();
    private Matrix matrix = new Matrix();
    private int mode = NONE;
    private View.OnTouchListener onHeadViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    lastEvent = new float[4];
                    lastEvent[0] = event.getX(0);
                    lastEvent[1] = event.getX(1);
                    lastEvent[2] = event.getY(0);
                    lastEvent[3] = event.getY(1);
                    oldAngle = rotation(event);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        // ...
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                        if (lastEvent != null && event.getPointerCount() == 2) {
                            newAngle = rotation(event);
                            float rotateDegree = newAngle - oldAngle;
                            midPoint(mid, event);
                            matrix.postRotate(rotateDegree, mid.x, mid.y);
                        }
                    }
                    break;
            }
            //headView.setImageMatrix(matrix);
            return true;
        }
    };
    private ProgressDialog pd;

    public static void showNetworkErrorMessage(Activity activity) {
        try {
            final Dialog dialog = new Dialog(activity, R.style.Theme_IAPTheme);
            dialog.setContentView(R.layout.internet_message_dialog);
            Button No = (Button) dialog.findViewById(R.id.dialogButton_no);
            ((Button) dialog.findViewById(R.id.dialogButtonyes)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {

        }

    }

    private Dialog backPressDialog;
    private void initBackPressedDialog(boolean showImmediate){
        backPressDialog = new Dialog(this, R.style.Theme_IAPTheme);
        backPressDialog.setContentView(R.layout.backpressed_dialog);
        AdsHelper.loadNativeAdAndShow(this, null,
                (UnifiedNativeAdView) backPressDialog.findViewById(R.id.ad_view));
        backPressDialog.findViewById(R.id.dialogButtonyes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressDialog.dismiss();
                finish();
            }
        });
        ;
        backPressDialog.findViewById(R.id.dialogButton_no).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backPressDialog.dismiss();
            }
        });
        if(showImmediate){
            backPressDialog.show();
        }

    }

    public void showBackpressedDialog() {
        if(backPressDialog!=null&&!backPressDialog.isShowing()){
            backPressDialog.show();
        }else {
            initBackPressedDialog(true);
        }
    }

    public static void destroy() {
        if (instanse != null) {
            instanse.finish();
        }
    }

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (PurchaseUtils.isYearlyPurchased(this) || PurchaseUtils.isPermanentPurchased(this)) {
            //dont show ads its premium purchase
        } else {
            if (AppUtils.baseActivityStartCount % 2 == 0) {
                AdsHelper.initAdmobInterstitialAndShow(BaseActivity.this);
            }
        }
        AppUtils.baseActivityStartCount++;

        AppUtils.textSticker = null;
        stickerView = null;
        instanse = this;

        try {
            fonts = getAssets().list("fonts");
            for (int i = 0; i < fonts.length; i++) {
                fonts[i] = "fonts/" + fonts[i];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //load background image
        backgroundImages = new int[15];
        for (int i = 1; i <= backgroundImages.length; i++) {
            try {
                int id = getResources().getIdentifier("bg" + i, "drawable", getPackageName());
                backgroundImages[i - 1] = id;
            } catch (Exception e) {
                backgroundImages[i - 1] = R.mipmap.icon_72;
            }
        }

        //load brush thumb
        brushThumb = new int[]{R.mipmap.brush1, R.mipmap.brush2, R.mipmap.brush3, R.mipmap.brush4, R.mipmap.brush5, R.mipmap.brush6,
                R.mipmap.brush7, R.mipmap.brush8, R.mipmap.brush9, R.mipmap.brush10, R.mipmap.brush11, R.mipmap.brush12, R.mipmap.brush13, R.mipmap.brush14, R.mipmap.brush15,
                R.mipmap.brush16, R.mipmap.brush17, R.mipmap.brush18, R.mipmap.brush19, R.mipmap.brush20, R.mipmap.brush21, R.mipmap.brush22, R.mipmap.brush23, R.mipmap.brush24,
                R.mipmap.brush25, R.mipmap.brush26, R.mipmap.brush27, R.mipmap.brush28, R.mipmap.brush29, R.mipmap.brush30, R.mipmap.brush31, R.mipmap.brush32, R.mipmap.brush33,
                R.mipmap.brush34, R.mipmap.brush35, R.mipmap.brush36, R.mipmap.brush37, R.mipmap.brush38, R.mipmap.brush39, R.mipmap.brush40, R.mipmap.brush41, R.mipmap.brush42,
                R.mipmap.brush43, R.mipmap.brush44, R.mipmap.brush45, R.mipmap.brush46, R.mipmap.brush47, R.mipmap.brush48, R.mipmap.brush49, R.mipmap.brush50, R.mipmap.brush51,
                R.mipmap.brush52};

        //load textture
        TEXTURE = new int[]{R.mipmap.t1, R.mipmap.t2, R.mipmap.t3, R.mipmap.t4, R.mipmap.t5, R.mipmap.t6, R.mipmap.t7,
                R.mipmap.t8, R.mipmap.t9, R.mipmap.t10, R.mipmap.t11, R.mipmap.t12, R.mipmap.t13, R.mipmap.t14, R.mipmap.t15,
                R.mipmap.t16, R.mipmap.t17, R.mipmap.t18, R.mipmap.t19, R.mipmap.t20};

        initBackPressedDialog(false);
    }

    public void setStickerOperation(StickerView stikerView) {
        stickerView = stikerView;
        setStickerOperation();
    }

    protected void setStickerOperation() {
        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                stickerView.setShowBorder(true);
                stickerView.setShowIcons(true);
                stickerView.setLocked(false);
                stickerView.invalidate();
                if (sticker instanceof MyStickerText) {
                    AppUtils.textSticker = (MyStickerText) sticker;
                    openDoubleTabTextEditBox(AppUtils.textSticker.getText().toString(), "");
                }
            }

            @Override
            public void onStickerClicked(Sticker sticker) {
                //stickerView.setLocked(false);
                stickerView.setBringToFrontCurrentSticker(true);
                AppUtils.textSticker = null;
                if (sticker instanceof MyStickerText) {
                    AppUtils.textSticker = (MyStickerText) sticker;
                }
                Log.d(TAG, "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
                if (sticker instanceof TextSticker) {
                    AppUtils.textSticker = null;
                }
                stickerView.invalidate();
            }

            @Override
            public void onStickerDragFinished(Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
                AppUtils.textSticker = null;
                if (sticker instanceof MyStickerText) {
                    AppUtils.textSticker = (MyStickerText) sticker;
                }
            }

            @Override
            public void onStickerZoomFinished(Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
                AppUtils.textSticker = null;
                if (sticker instanceof MyStickerText) {
                    AppUtils.textSticker = (MyStickerText) sticker;
                }
            }

            @Override
            public void onStickerFlipped(Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
                AppUtils.textSticker = null;
                if (sticker instanceof MyStickerText) {
                    AppUtils.textSticker = (MyStickerText) sticker;
                }
            }

            @Override
            public void onStickerDoubleTapped(Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
                AppUtils.textSticker = null;
                if (sticker instanceof MyStickerText) {
                    AppUtils.textSticker = (MyStickerText) sticker;
                    openDoubleTabTextEditBox("", AppUtils.textSticker.getText().toString());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppUtils.chk_sticker) {
            AppUtils.chk_sticker = false;
            AppUtils.set_somtthing = true;
            this.stickerView.addSticker(AppUtils.sticker);
        }

    }

    public void showGalleryFragment(GalleryFragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content, fragment, "text").commit();
    }

    protected String createDirInFilesDir(String folderName){
        String dirPath = getFilesDir().getAbsolutePath() + File.separator + folderName;
        File projDir = new File(dirPath);
        if (!projDir.exists())
            projDir.mkdirs();
        return dirPath;
    }

    public void addStickerToView(String stickerTypeFolder,final String stpath,String rootPrefixUrl) {
        if (stpath.contains("http")) {
            /*AppUtils.loadImage(stpath, new AppUtils.CloudImageLoadListener() {
                @Override
                public void onCloudImageLoaded(Bitmap bitmap) {
                    stickerBitmap = bitmap;
                    initRemainigItem();
                }
            });*/
        } else {
            String p = stpath.replace("/", "");
            String currPath=createDirInFilesDir(stickerTypeFolder);
            final File file=new File(currPath,p);
            if (file.isFile()&&file.exists()) {
                loadFileAndInit(file);
            } else if (isNetworkConnected()) {
                try {
                        file.createNewFile();
                        final String baseUrl =rootPrefixUrl;
                        try {

                            Glide.with(getApplicationContext()).asBitmap().load(baseUrl+stpath).into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                   // theBitmap=resource;
                                    try {
                                        FileOutputStream fos = new FileOutputStream(file);
                                        resource.compress(Bitmap.CompressFormat.PNG, 90, fos);
                                        fos.close();
                                    } catch (FileNotFoundException e) {
                                        Log.d(TAG, "File not found: " + e.getMessage());
                                       // return null;
                                    } catch (IOException e) {
                                        Log.d(TAG, "Error accessing file: " + e.getMessage());
                                        //return null;
                                    }
                                    loadFileAndInit(file);
                                }
                            });

                        }catch (Exception e){

                        }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadFileAndInit(File file) {
        try {
            Bitmap stickerBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //initRemainigItem();
            if (stickerBitmap != null) {
                Drawable d = new BitmapDrawable(getResources(), stickerBitmap);
                if(stickerView!=null)stickerView.addSticker(new DrawableSticker(d));
            }
        } catch (Exception e) {
            file.delete();
        }
    }

    protected void loadBanner(int visibility) {
        AdView adView = (AdView) findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setVisibility(visibility);
    }

    private void openDoubleTabTextEditBox(String hint, String text) {
        if (AppUtils.textSticker == null) {
            Toast.makeText(BaseActivity.this.getApplication(),
                    "Please select text first", Toast.LENGTH_LONG).show();
            return;
        }
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.doubletap__text_edit_box);
        final EditText edt = (EditText) dialog.findViewById(R.id.editbox_doubletap);
        edt.setHint(hint + "");
        edt.setText(text + "");
        Button cancle = (Button) dialog.findViewById(R.id.cancle_button);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button update = (Button) dialog.findViewById(R.id.update_button);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt=edt.getText().toString();
                if(AppUtils.textSticker!=null&&txt!=null&&!txt.isEmpty()){
                    AppUtils.textSticker.setText(txt);
                    AppUtils.textSticker.setDrawable(AppUtils.textSticker.getDrawable());
                    AppUtils.textSticker.resizeText();
                    stickerView.replace(AppUtils.textSticker);
                }
                dialog.dismiss();
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //im.toggleSoftInput(1, 0);
                im.toggleSoftInputFromWindow(edt.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            }
        });
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;// AppUtils.dpToPx(this,240);
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private String readFile(Reader reader) throws IOException {
        final StringBuilder mStringBuilder = new StringBuilder();
        BufferedReader mBUfferedReader = new BufferedReader(reader);
        String line;
        while ((line = mBUfferedReader.readLine()) != null) {
            mStringBuilder.append(line);
        }
        mBUfferedReader.close();
        return mStringBuilder.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void saveButtonClicked(final View view) {
        if (MarseMallowPermission.storagePermitted(this, 0)) {
            if(view!=null){
                view.invalidate();
                showResForSave(view);
            }
        }
    }

    private void showResForSave(final View view) {
        this.selected = 0;
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        xSizes = new int[]{view.getWidth(), 512, 960, 1024, 1280, 1920, 2560};
        ySizes = new int[xSizes.length];
        bubbleSortOrderAscending(xSizes);

        List<String> sizes = new ArrayList();

        sizes.clear();
        int maxSize = AppUtils.screenHeight * 2;
        int i = 0;
        for (int size : xSizes) {
            int y = (int) ((float) size * (((float) view.getHeight()) / (((float) view.getWidth()) * 1.0f)));
            if (y < 3000 && y < maxSize) {
                sizes.add(new StringBuilder(String.valueOf(size)).append(" X ").append(y).toString());
                ySizes[i] = y;
            } else {
                // Toast.makeText(this,"viewWidth:"+viewWidth+" ViewHeight:"+viewHeight+" scaleY:"+scaleY,Toast.LENGTH_LONG).show();
            }
            i++;
        }

        mBuilder.setSingleChoiceItems((CharSequence[]) sizes.toArray(new String[0]),
                this.selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected = which;
                    }
                });
        mBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                dialog.dismiss();
                saveAsDialog(view, xSizes[selected]);
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBuilder.create().show();
            }
        });
    }

    private int[] bubbleSortOrderAscending(int[] array) {
        for (int j = 0; j < array.length - 1; j++) {
            for (int k = j + 1; k < array.length; k++) {
                if (array[j] > array[k]) {
                    int dummy = array[j];
                    array[j] = array[k];
                    array[k] = dummy;
                }
            }
        }
        return array;
    }
    protected void setTextSticker(){
        if (stickerView != null) {
            stickerView.setLocked(true);
        }
    }
    protected void saveAsDialog(final View view, final int sizeX) {
        if(view!=null&&view.getWidth()>0&&view.getHeight()>0){
        }else {
            return;
        }
        if (stickerView != null) {
            stickerView.setLocked(true);
        }
        pd = new ProgressDialog(BaseActivity.this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        new AsyncTask<Void, Void, Boolean>() {
            Bitmap tempBitmap;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    pd.show();
                } catch (Exception e) {

                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                tempBitmap=Bitmap.createBitmap(view.getWidth()>0?view.getWidth():1,view.getHeight()>0?view.getHeight():1,Bitmap.Config.ARGB_8888);
                Canvas c=new Canvas(tempBitmap);
                view.draw(c);

                final Bitmap finalBitmap = Bitmap.createScaledBitmap(tempBitmap, sizeX,
                        (int) ((float) sizeX * (((float) tempBitmap.getHeight()) / (((float) tempBitmap.getWidth()) * 1.0f))), true);

                return saveImageToExternalStorage(finalBitmap);
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                try {
                    pd.dismiss();
                } catch (Exception e) {

                }
                recycleBitmap(tempBitmap);
                if (aVoid) {
                    switchToNextActivity("");
                } else {
                    Toast.makeText(getApplicationContext(), "Error,Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void switchToNextActivity(String result) {
        Intent intent = new Intent(this, ShareActivity.class);
        startActivity(intent);
    }
    public void recycleWithDelay(final Bitmap bitmap, long delayinms){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycleBitmap(bitmap);
            }
        },delayinms);
    }

    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            //bitmap.recycle();
        }
    }

    public boolean saveImageToExternalStorage(Bitmap capturebmp) {
        try {
            try {
                String file=SaveToStorage.save(capturebmp,this,null);
                image_file=new File(file);
                return true;
            } catch (Exception e) {
                Log.e("saveToExternalStorage()", e.getMessage());
                return false;
            }
        } catch (Exception e2) {
            Log.e("saveToExternalStorage()", e2.getMessage());
            return false;
        }
    }

    private void galleryAddPic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(image_file); //out is your output file
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        } else {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }
        /*Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        Uri contentUri = Uri.fromFile(image_file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", contentUri));
        ContentValues values = new ContentValues();
        values.put("_data", image_file.getAbsolutePath());
        values.put("mime_type", "image/jpeg");
        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/
    }

    public void addNewText() {
        if (stickerView == null) {
            Toast.makeText(BaseActivity.this.getApplication(),
                    "Not Supported,Please go back and start again", Toast.LENGTH_LONG).show();
            return;
        }
        AppUtils.chk_textSticker = true;
        MyStickerText sticker = new MyStickerText(BaseActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sticker.setDrawable(getResources().getDrawable(R.drawable.bg_text, getTheme()));
        } else {
            sticker.setDrawable(getResources().getDrawable(R.drawable.bg_text));
        }
        sticker.setText("Double Tap To Open keyboard");
        sticker.setTypeface(TypefacesUtils.get(BaseActivity.this, fonts[0]));
        sticker.setTextColor(Color.BLACK);
        sticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
        sticker.setMaxTextSize(50.0f);
        sticker.resizeText();
        AppUtils.textSticker = sticker;
        stickerView.addSticker(AppUtils.textSticker);

        AppUtils.set_somtthing = true;
    }

    public void setTextToCreative() {
        if (AppUtils.textSticker == null) {
            Toast.makeText(BaseActivity.this.getApplication(),
                    "Please select text first", Toast.LENGTH_LONG).show();
            return;
        }
        AppUtils.textSticker.setCreative();
        stickerView.replace(AppUtils.textSticker);
    }

    public void setTextBold() {
        if (checkText()) {
            if (AppUtils.textSticker.getTextStyle() == Typeface.BOLD) {
                AppUtils.textSticker.setTextStyle(Typeface.NORMAL);
                stickerView.replace(AppUtils.textSticker);
            } else {
                AppUtils.textSticker.setTextStyle(Typeface.BOLD);
                stickerView.replace(AppUtils.textSticker);
            }
        }

    }

    public void setTextItalic() {
        if (checkText()) {
            if (AppUtils.textSticker.getTextStyle() == Typeface.ITALIC) {
                AppUtils.textSticker.setTextStyle(Typeface.NORMAL);
                stickerView.replace(AppUtils.textSticker);
            } else {
                AppUtils.textSticker.setTextStyle(Typeface.ITALIC);
                stickerView.replace(AppUtils.textSticker);
            }
        }

    }

    public void setTextUnderLines() {
        if (checkText()) {
            if (AppUtils.textSticker.isTextUnderLine()) {
                AppUtils.textSticker.setTextUnderline(false);
                stickerView.replace(AppUtils.textSticker);
            } else {
                AppUtils.textSticker.setTextUnderline(true);
                stickerView.replace(AppUtils.textSticker);
            }
        }

    }

    public void setTextAlignMent(Layout.Alignment align) {
        if (checkText()) {
            AppUtils.textSticker.setTextAlign(align);
            stickerView.replace(AppUtils.textSticker);
        }

    }

    public void setShadow(float radius, float dx, float dy, int shadowColor) {
        if (checkText()) {
            AppUtils.textSticker.setShadow(radius, dx, dy, shadowColor);
            stickerView.replace(AppUtils.textSticker);
        }
    }

    public void setTextColor() {
        if (checkText())
            new AmbilWarnaDialog(this, cl, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    cl = color;
                    if (AppUtils.textSticker != null) {
                        AppUtils.textSticker.setTextColor(cl);
                        stickerView.replace(AppUtils.textSticker);
                    }
                }
            }).show();
    }

    public void setTextSize(int progress) {
        if (checkText()) {
            AppUtils.textSticker.setMaxTextSize(progress);
            stickerView.replace(AppUtils.textSticker);
        }
    }

    public void setFont(String fontPath, File fontFile) {
        if (checkText()) {
            try {
                if (fontFile == null) {
                    AppUtils.textSticker.setTypeface(TypefacesUtils.get(this, fontPath));
                    stickerView.replace(AppUtils.textSticker);
                } else {
                    AppUtils.textSticker.setTypeface(Typeface.createFromFile(fontFile));
                    stickerView.replace(AppUtils.textSticker);
                }
            } catch (Exception e) {

            }

        }
    }

    public void setTextureOnText(int res) {
        if (checkText()) {
            AppUtils.textSticker.setTextureBitmap(BitmapFactory.decodeResource(getResources(), res));
            stickerView.replace(AppUtils.textSticker);
        }
    }

    private boolean checkText() {
        if (AppUtils.textSticker == null) {
            Toast.makeText(BaseActivity.this.getApplication(),
                    "Please select text first", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void logAnalyticEvent(String id){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, this.getClass().getName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
