package com.formationapps.nameart.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.formationapps.artpanel.TemplatePanel;
import com.formationapps.nameart.App;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.SymbolFragment;
import com.formationapps.nameart.fragments.TemplateFragment;
import com.formationapps.nameart.fragments.TemplateParentFragment;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
import com.google.android.gms.analytics.Tracker;
import com.removebg.BitmapContainer;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.StickerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import formationapps.helper.stickers.StickerFragment;
import nonworkingcode.brusheffects.BrushSetting;
import nonworkingcode.brusheffects.LoadBrush;
import nonworkingcode.brusheffects.OverlayBrushView;
import nonworkingcode.fcrop.activity.FeatureCutActivity;
import nonworkingcode.fcrop.util.FeatureCropUtil;

/**
 * Created by caliber fashion on 9/20/2017.
 */

public class TemplateActivity extends BaseActivity {
    private static final String TAG = TemplateActivity.class.getSimpleName();
    public static boolean isActive;
    private TemplatePanel artPanel;
    private OverlayBrushView mOverlayBrushView;
    private LoadBrush mLoadBrush;
    private int editorWidth, editorHeight;
    private BrushSetting bs;
    private float oldRatio;

    public static String getLastTemplateUrl(Context context) {
        SharedPreferences pref = AppUtils.getSharedPref(context);
        String str = pref.getString("templatedefaulturl", "templates/Alpha/alpha1.jpg");
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logAnalyticEvent("TEMPLATE_ACTIVITY");
        setContentView(R.layout.template_activity);
        isActive = true;

        //load admob adview
        AdsHelper.loadAdmobBanner(this, (LinearLayout) findViewById(R.id.ad_container));

        artPanel = (TemplatePanel) findViewById(R.id.panel_artmenu);

        RelativeLayout erase = (RelativeLayout) findViewById(R.id.erase_savephoto);
        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErase();
            }
        });
        RelativeLayout save = (RelativeLayout) findViewById(R.id.save_savephoto);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stickerView.setLocked(true);
                RelativeLayout lnr_draw = (RelativeLayout) findViewById(R.id.lnr_draw);
                saveButtonClicked(lnr_draw);
            }
        });

        //APP_PATH_SD_CARD = "/" + getString(R.string.app_name) + "/";
        img_bg = (ImageView) findViewById(R.id.img_bg);

        mOverlayBrushView = (OverlayBrushView) findViewById(R.id.overlaybrushview);
        mLoadBrush = LoadBrush.load(this);

        stickerView = (StickerView) findViewById(R.id.sticker_view);
        setStickerOperation();

        launchTemplateFragment();
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_editor_area);
        rl.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                rl.getViewTreeObserver().removeOnPreDrawListener(this);
                editorWidth = rl.getMeasuredWidth();
                editorHeight = rl.getMeasuredHeight();
                Log.i("LoadWithGlide", "Width:" + editorWidth + " Height:" + editorHeight);
                loadFromFireBase(getLastTemplateUrl(TemplateActivity.this),AppUtils.getTemplatesRemotePath());
                return true;
            }
        });
    }

    private void launchTemplateFragment() {
        findViewById(R.id.top_bar_ac).setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content,
                TemplateParentFragment.newFrag(new TemplateFragment.OnTemplateDismissListener() {
                    @Override
                    public void onTemplateDismiss(String path,String rootPrefixUrl) {
                        loadFromFireBase(path,rootPrefixUrl);
                        AppUtils.sendTemplateCount(path);
                    }
                }), "text").commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stickerView != null) {
            stickerView.setLocked(false);
        }
    }

    public void onBackPressed() {
        OverlayBrushView.shouldNotDraw = true;
        findViewById(R.id.top_bar_ac).setVisibility(View.VISIBLE);
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("text");
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                artPanel.resetLinSelector();
                return;
            }
            if (!artPanel.onBackPressed()) {
                showBackpressedDialog();
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
        isActive = false;
    }

    public void onPhotoBtnClick() {
        if (checkAndRequestPermissions(LAUNCH_GALLERY_HEAD)) {
            launchGallery();
        }
    }

    private void launchGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LAUNCH_GALLERY_HEAD);
    }

    public void launchTemplate() {
        launchTemplateFragment();
    }

    public void showSymbol() {
        //startActivity(new Intent(getApplicationContext(), SymbolActivity.class));
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content, new SymbolFragment(), "text").commit();
    }

    public void showSticker() {
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content, new StickerFragment(), "text").commit();
    }

    public void onBrushItemClick(int position) {
        if (position == 555) {
            if (bs == null) {
                bs = new BrushSetting(this, R.style.FullScreen);
            }
            bs.show();
        } else {
            mOverlayBrushView.setBrushResFoundMap(mLoadBrush.getBrushEffect(position));
            OverlayBrushView.shouldNotDraw = false;
        }
    }

    private boolean checkAndRequestPermissions(int permissionrequest) {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        List<String> listPermissionsNeeded = new ArrayList();
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add("android.permission.READ_EXTERNAL_STORAGE");
            listPermissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this,
                (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), permissionrequest);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LAUNCH_GALLERY_HEAD:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    try {
                        pd.show();
                        Glide.with(getApplicationContext()).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                pd.dismiss();
                                if (BitmapContainer.getInstance().setBitmap(resource)) {
                                    startActivityForResult(new Intent(getApplicationContext(), BgRemoverActivity.class), LAUNCH_HEAD_PHOTO);
                                    //recycleWithDelay(bitmap,1000);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Supplied image is not valid.please select another",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }
                break;
            case LAUNCH_HEAD_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = BitmapContainer.getInstance().getBitmap();
                    if (bitmap != null&&!bitmap.isRecycled()) {
                        Drawable d = new BitmapDrawable(getResources(), bitmap);
                        stickerView.addSticker(new DrawableSticker(d));
                    } else {
                        if (BuildConfig.DEBUG) {
                            Toast.makeText(getApplicationContext(), "Bitmap null", Toast.LENGTH_LONG).show();
                        }
                    }
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(getApplicationContext(), "LAUNCH_HEAD_PHOTO", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LAUNCH_GALLERY_HEAD:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    launchGallery();
                }
                break;
        }
    }

    public void onErase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) "Clear");
        builder.setMessage((CharSequence) "Are you sure you?").setCancelable(true)
                .setPositiveButton(R.string.yes_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        img_bg.setVisibility(View.VISIBLE);
                        img_bg.setBackgroundColor(-1);
                        img_bg.setImageBitmap(null);
                        AppUtils.textSticker = null;
                        AppUtils.set_somtthing = false;
                        stickerView.removeAllStickers();
                        mOverlayBrushView.clearScreen();
                    }
                }).setNeutralButton(R.string.no_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog d = builder.create();
        d.show();

        try {
            //apply custom fonts
            Button btnPositive = d.getButton(Dialog.BUTTON_POSITIVE);
            btnPositive.setTypeface(AppUtils.defaultTypeface);

            Button btnNegative = d.getButton(Dialog.BUTTON_NEUTRAL);
            btnNegative.setTypeface(AppUtils.defaultTypeface);

            TextView tvM = (TextView) d.findViewById(android.R.id.message);
            tvM.setTypeface(AppUtils.defaultTypeface);

            TextView tvT = (TextView) d.findViewById(android.R.id.title);
            tvT.setTypeface(AppUtils.defaultTypeface);
        } catch (Exception e) {

        }
    }

    private void loadFromFireBase(final String url,String rootPrefixUrl) {
        setLastTemplateUrl(url);
        try {
            if (Build.VERSION.SDK_INT >= 18)
                if (isDestroyed()) {
                    return;
                }
        } catch (Exception e) {

        }

        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "url:" + url, Toast.LENGTH_LONG).show();
        }
        if (url != null) {
            String s = url.replace("/", "");
            final File file = new File(getFilesDir(), s);
            if (file.isFile()) {
                loadWithGlide(file);
            } else if (isNetworkConnected()) {
                try {
                    file.createNewFile();
                    final String baseUrl = rootPrefixUrl;
                    try {
                               /* Bitmap theBitmap = Glide.with(getApplicationContext()).load(baseUrl + url).asBitmap()
                                        .into(-1, -1).get();*/
                        Glide.with(getApplicationContext()).asBitmap().load(baseUrl + url).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    resource.compress(Bitmap.CompressFormat.PNG, 90, fos);
                                    fos.close();
                                } catch (FileNotFoundException e) {
                                    Log.d(TAG, "File not found: " + e.getMessage());
                                } catch (IOException e) {
                                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                                }
                                loadWithGlide(file);
                            }
                        });

                    } catch (Exception e) {

                    }
                } catch (Exception e) {

                }
            }
        }
    }

    private void loadWithGlide(File file) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            int imgw = bitmap.getWidth();
            int imgh = bitmap.getHeight();
            Log.i("LoadWithGlide", "imgw:" + imgw + " imgh:" + imgh);
            float f = (float) imgh / imgw;
            Log.i("LoadWithGlide", "f:" + f);
            float y = f * (float) editorWidth;
            Log.i("LoadWithGlide", "y:" + y);
            setTemplateViewRatio(editorWidth, (int) y, file);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Toast.makeText(this, "exception:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            setTemplateViewRatio(editorWidth, editorWidth, file);
        }
    }

    private void setTemplateViewRatio(int width, int height, final File file) {
        float newR = (float) width / (float) height;
        if (newR == oldRatio) {
            setFileToImageView(file);
            return;
        } else {
            oldRatio = newR;
        }
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.lnr_draw);
        RelativeLayout.LayoutParams layp = (RelativeLayout.LayoutParams) rl.getLayoutParams();
        layp.width = width;
        layp.height = height;
        layp.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl.setLayoutParams(layp);
        rl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setFileToImageView(file);
            }
        });
    }

    private void setFileToImageView(File file) {
        try {
            Glide.with(getApplicationContext())
                    .load(file).apply(new RequestOptions().placeholder(R.mipmap.icon_72)).into(img_bg);

        } catch (Exception e) {
            file.delete();
        }
    }

    private void setLastTemplateUrl(String url) {
        SharedPreferences pref = AppUtils.getSharedPref(this);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("templatedefaulturl", url);
        edt.commit();
    }
}
