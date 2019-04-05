package com.formationapps.nameart.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.formationapps.artpanel.ArtbgListener;
import com.formationapps.artpanel.NameArtPanel;
import com.formationapps.nameart.App;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.SymbolFragment;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
import com.google.android.gms.analytics.Tracker;
import com.removebg.BitmapContainer;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.StickerView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import formationapps.helper.stickers.StickerFragment;
import nonworkingcode.brusheffects.BrushSetting;
import nonworkingcode.brusheffects.LoadBrush;
import nonworkingcode.brusheffects.OverlayBrushView;
import nonworkingcode.fcrop.activity.FeatureCutActivity;
import nonworkingcode.fcrop.util.FeatureCropUtil;
import nonworkingcode.grid.util.ColorFilterGenerator;

/**
 * Created by caliber fashion on 9/20/2017.
 */

public class ArtActivity extends BaseActivity implements ArtbgListener {
    private NameArtPanel artPanel;
    private OverlayBrushView mOverlayBrushView;
    private LoadBrush mLoadBrush;
    private BrushSetting bs;
    private float mHueValue = 0.0f;

    public static Bitmap uriToBitmap(Context context, Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.art_activity);
        logAnalyticEvent("ART_ACTIVITY");

        artPanel = (NameArtPanel) findViewById(R.id.panel_artmenu);
        artPanel.setArtBgListener(this);

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
                //stickerView.setLocked(true);
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

        AdsHelper.loadAdmobBanner(this, (LinearLayout) findViewById(R.id.ad_container));
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
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("text");
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                //CollageUtil.hideAndShow(findViewById(R.id.top_bar_ac),true);
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

    public void showSymbol() {
        //startActivity(new Intent(getApplicationContext(), SymbolActivity.class));
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content, new SymbolFragment(), "text").commitAllowingStateLoss();
    }

    public void showSticker() {
        getSupportFragmentManager().beginTransaction().add(R.id.rel_content, new StickerFragment(), "text").commitAllowingStateLoss();
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

    @Override
    public void setSquareBackground(Drawable drawable) {
        img_bg.setVisibility(View.VISIBLE);
        setTopBackground(drawable);
        img_bg.invalidate();
    }

    @Override
    public void setHueValue(float value) {
        //Log.i("ARTBG","Hue:"+value);
        mHueValue = value;
    }

    @Override
    public void handleImage() {
        img_bg.setVisibility(View.VISIBLE);
        if (img_bg == null || img_bg.getBackground() == null) {
            img_bg.setColorFilter(ColorFilterGenerator.adjustHue(this.mHueValue));
            img_bg.invalidate();
            return;
        }
        img_bg.getBackground().setColorFilter(ColorFilterGenerator.adjustHue(this.mHueValue));
        img_bg.invalidate();
        //Log.i("ARTBG","handleImage");
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri) {
        final Bitmap bitmap = uriToBitmap(this, uri);

        if (bitmap != null) {
            final RelativeLayout lnr_draw = (RelativeLayout) findViewById(R.id.lnr_draw);
            int imgw = bitmap.getWidth();
            int imgh = bitmap.getHeight();
            float f = (float) imgh / imgw;

            float y = f * (float) (lnr_draw.getWidth());

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lnr_draw.getLayoutParams();
            lp.width = lnr_draw.getWidth();
            lp.height = (int) y;
            lnr_draw.setLayoutParams(lp);
            lnr_draw.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    lnr_draw.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    img_bg.setVisibility(View.VISIBLE);
                    img_bg.setImageBitmap(bitmap);
                }
            });
            //Toast.makeText(this,"Width:"+lnr_draw.getWidth()+" Height:"+lnr_draw.getHeight(),Toast.LENGTH_LONG).show();
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

    private void setTopBackground(Drawable drawable) {
        Drawable d = img_bg.getDrawable();
        img_bg.setImageDrawable(null);
        recycleDrawable(d);

        if (Build.VERSION.SDK_INT < 16) {
            img_bg.setBackgroundDrawable(drawable);
        } else {
            setBackground16(img_bg, drawable);
        }
    }

    private void setBackground16(ImageView imageView, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(drawable);
        }
    }

    private void recycleDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitamp = ((BitmapDrawable) drawable).getBitmap();
            if (bitamp != null && !bitamp.isRecycled()) {
                //bitamp.recycle();
            }
        }
    }

}
