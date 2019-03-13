package com.formationapps.nameart.activity;

import java.io.File;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AdsHelper;

public class PreviewActivity extends BaseActivity implements OnClickListener {
    private Bitmap finalBitmap;
    private Uri uri;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        logAnalyticEvent("PREVIEW_ACTIVITY");

        filePath = getIntent().getStringExtra("IMG_PATH");
        finalBitmap = BitmapFactory.decodeFile(filePath);


        final ImageView imgImage = (ImageView) findViewById(R.id.imgImage);
        if(filePath.endsWith(".gif")){
           Glide.with(this).asGif().load(filePath).into(imgImage);
            uri = Uri.fromFile(new File(filePath));
        }
        else if (finalBitmap != null) {
            uri = Uri.fromFile(new File(filePath));
            imgImage.setImageBitmap(finalBitmap);
        } else {
            Glide.with(this).load(filePath).into(imgImage);
            uri = Uri.fromFile(new File(filePath));
            //Toast.makeText(this, "Unable to load Image", Toast.LENGTH_LONG).show();
        }

        LinearLayout facebook = (LinearLayout) findViewById(R.id.facebookshare);
        facebook.setOnClickListener(this);
        LinearLayout whatsapp = (LinearLayout) findViewById(R.id.whatsappshare);
        whatsapp.setOnClickListener(this);
        LinearLayout googleplus = (LinearLayout) findViewById(R.id.instagram);
        googleplus.setOnClickListener(this);
        LinearLayout share = (LinearLayout) findViewById(R.id.allshare);
        share.setOnClickListener(this);

        AdsHelper.loadAdmobBanner(this, (LinearLayout) findViewById(R.id.adConatiner));

    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
                                             int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    public static Bitmap loadBitmapFromView(View v) {

        // int width = display.getWidth(); // deprecated
        // int height = display.getHeight(); // deprecated

        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        // Paint paint = new Paint();
        v.draw(c);
        return b;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        if(finalBitmap!=null&&!finalBitmap.isRecycled()){
            //finalBitmap.recycle();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebookshare:
                facebookShare();
                break;
            case R.id.whatsappshare:
                whatsappShare();
                break;
            case R.id.instagram:
                //googlePlusShare();
                instagramShare();
                break;
            case R.id.allshare:
                allsShare();
                break;
        }
    }

    protected void allsShare() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpg");
        share.putExtra(Intent.EXTRA_SUBJECT, ""
                + getResources().getString(R.string.app_name));
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
        if (uri != null) {
            Uri uri1 = Uri.fromFile(new File(filePath));
            share.putExtra(Intent.EXTRA_STREAM, uri1);
        } else {
            Toast.makeText(this, "Unable to load Image", Toast.LENGTH_LONG).show();
            return;
        }

        startActivity(Intent.createChooser(share, "Share Via"));
    }

    private void instagramShare() {
        if (verificaInstagram()) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            if (uri != null) {
                Uri uri1 = Uri.fromFile(new File(filePath));
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri1);
            } else {
                Toast.makeText(this, "Unable to load Image", Toast.LENGTH_LONG).show();
                return;
            }
            shareIntent.setPackage("com.instagram.android");
            try {
                startActivity(shareIntent);
            }catch (ActivityNotFoundException e){

            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Instagram app not installed", Toast.LENGTH_LONG).show();
        }
    }

    private boolean verificaInstagram() {
        boolean installed = false;
        try {
            ApplicationInfo info =
                    getPackageManager().getApplicationInfo("com.instagram.android", 0);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    private void facebookShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpg");
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = getPackageManager()
                .queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase()
                    .startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }
        if (facebookAppFound) {
            if (uri != null) {
                Uri uri1 = Uri.fromFile(new File(filePath));
                intent.putExtra(Intent.EXTRA_STREAM, uri1);
            } else {
                Toast.makeText(this, "Unable to load Image", Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Facebook not installed", Toast.LENGTH_LONG).show();
        }
    }

    private void whatsappShare() {
        if (uri == null) {
            Toast.makeText(this, "Unable to load Image", Toast.LENGTH_LONG).show();
            return;
        }
        Uri imageUri = uri;
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //Target whatsapp:
        shareIntent.setPackage("com.whatsapp");
        //Add text and then Image URI
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed...Please install.", Toast.LENGTH_LONG).show();
        }
    }
}
