package com.formationapps.nameart.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.formationapps.nameart.LoginDialog;
import com.formationapps.nameart.MyTempListener;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.ShareImageDialog;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.OpenPlayStore;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.webapi.model.WebConstant;
import com.webapi.model.UserProfile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import billingmodule.billing.PurchaseUtils;

import static com.formationapps.nameart.helper.AppUtils.appsList;

public class ShareActivity extends AppCompatActivity implements MyTempListener, TextureView.SurfaceTextureListener {
    private static final String TAG = "ShareActivity";
    public static int rewardedVideoCount = 0;
    ImageView img_display;
    int type;
    int sharecount = 0;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    private Bitmap bitmap;
    private Uri uri;
    private boolean shouldShowInterstitial;
    //private IjkWrapper ijkWrapper = new IjkWrapper();
    private  int mediaType;

    public ShareActivity() {
        this.type = 0;
    }

    private FirebaseAnalytics mFirebaseAnalytics;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareactivity);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "SHARE_ACTIVITY");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, this.getClass().getName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        //ijkWrapper.init();

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                NameArtMenu.setFont((ViewGroup) findViewById(android.R.id.content), AppUtils.webApiFont);
            }
        }, 0);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView title = new TextView(this);
        title.setText("Preview");
        title.setTextSize(18.0f);
        title.setTextColor(Color.parseColor("#FF4081"));
        title.setTypeface(AppUtils.webApiFont);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(title);

        try {
            AdsHelper.initShareInterstitialAndShow(this);
            AdsHelper.loadAdmobBanner(this, (LinearLayout) findViewById(R.id.nativeAdContainer));
        } catch (Exception e) {

        }

        img_display = (ImageView) findViewById(R.id.img_display);
       /* if (GIFActivity.gifFile==null){
            Toast.makeText(getApplicationContext(), "gif Please try again.\nSomething Wrong.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (GIFActivity.gifFile.exists()){
            Glide.with(getApplicationContext()).asGif().load(GIFActivity.gifFile).into(img_display);
        }*/

        if (BaseActivity.image_file == null) {
            Toast.makeText(getApplicationContext(), " Please try again.\nSomething Wrong.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if(BaseActivity.image_file.exists()){
            if(BaseActivity.image_file.isFile()){
                if(BaseActivity.image_file.getAbsolutePath().endsWith(".gif")){
                    Glide.with(getApplicationContext()).asGif().load(BaseActivity.image_file).into(img_display);
                }else{
                    Glide.with(getApplicationContext()).load(BaseActivity.image_file).into(img_display);
                }
            }
            uri = Uri.fromFile(BaseActivity.image_file);
        }else {
            Toast.makeText(getApplicationContext(), "Please try again.\nSomething Wrong.", Toast.LENGTH_LONG).show();
            finish();
        }

        final String mediaPath = getIntent().getStringExtra("save_file_path");
        mediaType = getIntent().getIntExtra("save_file_type",-1);
        if(mediaPath!=null&&mediaType!=-1){
            File mediaFile = new File(mediaPath);
            if(mediaType<0) finish();

            TextureView textureView = (TextureView) findViewById(R.id.video_view);
            textureView.setSurfaceTextureListener(this);

            /*if(mediaType== MimeType.PHOTO){
                //videoPreview.setVisibility(View.GONE);
                textureView.setVisibility(View.VISIBLE);
                img_display.setImageBitmap(BitmapUtils.loadBitmapFromFile(mediaPath));
            }else if(mediaType== MimeType.VIDEO) {
                img_display.setVisibility(View.GONE);
                textureView.setVisibility(View.VISIBLE);
                //videoPreview.setVisibility(View.VISIBLE);
                ijkWrapper.openRemoteFile(mediaPath);
                ijkWrapper.prepare();
            }*/
        }

        /*if (BaseActivity.image_file.exists()) {
           // bitmap = BitmapFactory.decodeFile(BaseActivity.image_file.getAbsolutePath());
            //img_display.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getApplicationContext(), "Please try again.\nSomething Wrong.", Toast.LENGTH_LONG).show();
            finish();
        }*/

        ImageButton upload = (ImageButton) findViewById(R.id.self_share);
        upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (bitmap != null) {
                if (BaseActivity.image_file.getAbsolutePath().endsWith(".mp4")){
                    Toast.makeText(ShareActivity.this, "upload video feature coming soon...", Toast.LENGTH_SHORT).show();

                }else {
                    checkAndShowShareDialog();
                }
            }
        });

        ImageButton whatsapp = (ImageButton) findViewById(R.id.whatsapp_share);
        whatsapp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsappShare();
            }
        });

        ImageButton instagra = (ImageButton) findViewById(R.id.instagram_share);
        instagra.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                instagramShare();
            }
        });

        ImageButton facebook_share = (ImageButton) findViewById(R.id.facebook_share);
        facebook_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookShare();
            }
        });
        ImageButton allShare = (ImageButton) findViewById(R.id.more_share);
        allShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImages();
            }
        });

        gotoNextActivity(getIntent());

        readFireBaseStorage();

        setNewTagAnimation();

        findViewById(R.id.orderToPrint).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    orderToPrintPhoto();
                }catch (Exception e){

                }
            }
        });

    }

    private ShareImageDialog mShareDialog;
    private LoginDialog mLoginDialog;
    private void checkAndShowShareDialog() {
        if(!isFinishing()){
            if (UserProfile.getInstance().isLoggedIn()) {
                if(mShareDialog!=null&&mShareDialog.isVisible()){
                    mShareDialog.dismiss();
                }
                mShareDialog = new ShareImageDialog();
                mShareDialog.setBitmapFile(BaseActivity.image_file);
                mShareDialog.show(getSupportFragmentManager(), "shareDialog");
            }else {
                if(mLoginDialog!=null&&mLoginDialog.isVisible()){
                    mLoginDialog.dismiss();
                }
                mLoginDialog = new LoginDialog();
                mLoginDialog.show(getSupportFragmentManager(), "logindialog");
            }
        }
    }

    private void orderToPrintPhoto() {
        //PicsDream.getInstance().withImageUri(uri).launch(this);
        //OrderPrint.getInstance().ImageUri(uri).launch(this);
    }

    @Override
    public void onBackPressed() {
        if (PurchaseUtils.isYearlyPurchased(this) || PurchaseUtils.isPermanentPurchased(this)) {
            //dont show ads its premium purchase
        } else {
            AdsHelper.initShareInterstitialAndShow(this);
        }
        super.onBackPressed();
        //onBackPressedAds();
    }

    @Override
    protected void onPause() {
        //ijkWrapper.pause();
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        //ijkWrapper.destroy();
    }

    protected void onResume() {
        super.onResume();
        //ijkWrapper.resume();
    }


    private boolean isNetworkConnected() {
        return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_card, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 16908332) {
            finish();
            return true;
        } else if (id == R.id.action_save) {
            this.type = 0;
            return true;
        } else if (id == R.id.action_share) {
            this.type = 1;
            shareImages();
            return true;
        } else if (id != R.id.action_dp) {
            return super.onOptionsItemSelected(item);
        } else {
            this.type = 2;
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                if (ShareActivity.this.appInstalledOrNot("com.whatsapp")) {
                    ShareActivity.this.setprofilePhoto();
                } else {
                    Toast.makeText(ShareActivity.this.getApplicationContext(), "WhatsApp is not install",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            Toast.makeText(getApplicationContext(), "Data-Connection unavailable..!", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    void setprofilePhoto() {
        try{
            Uri sendUri = Uri.fromFile(BaseActivity.image_file);
            Intent intent = new Intent("android.intent.action.ATTACH_DATA");
            intent.setDataAndType(sendUri, "image/jpg");
            intent.putExtra("mimeType", "image/jpg");
            intent.setPackage("com.whatsapp");
            startActivityForResult(intent, 200);
        }catch (Exception e){
        }
    }

    private boolean appInstalledOrNot(String uri) {
        try {
            getPackageManager().getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    private void instagramShare() {
        if (verificaInstagram()) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                if (uri != null) {
                    Uri uri1 = Uri.fromFile(BaseActivity.image_file);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri1);
                } else {
                    Toast.makeText(this, "Unable to load Image", Toast.LENGTH_LONG).show();
                    return;
                }
                shareIntent.setPackage("com.instagram.android");
                startActivity(shareIntent);
            } catch (Exception e) {

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
            if (BaseActivity.image_file != null) {
                Uri uri1 = Uri.fromFile(BaseActivity.image_file);
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

    public void shareImages() {
        Intent share = new Intent("android.intent.action.SEND");
        share.setType("image/*");
        share.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + BaseActivity.image_file));
        share.putExtra("android.intent.extra.TEXT",
                "Try this awesome app to Write your name using " + getString(R.string.app_name) +
                        " on different social media." +
                        "\nhttps://goo.gl/iFebss");
        try {
            startActivity(Intent.createChooser(share, "Photo Share Chooser"));
        } catch (Exception e) {
        }
        if (sharecount % 3 == 0) {
            //mInterstitialAd.loadAd(new AdRequest.Builder().build());
            sharecount++;
        } else {
            sharecount++;
        }

    }

    private void readFireBaseStorage() {
        loadAppFromFirebaseServer();
    }

    void loadAppFromFirebaseServer() {
        try {
            GridView gd = (GridView) findViewById(R.id.grid_view_menu);
            gd.setAdapter(new NameArtMenu.GridViewAdapter(ShareActivity.this, appsList));
            gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = "https://play.google.com/store/apps/details?id=" + appsList.get(position).appPackageName
                            + "" + appsList.get(position).referrer;
                    new OpenPlayStore(ShareActivity.this).execute(url);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this,"IOException:"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void gotoNextActivity(Intent intent) {
        Button button = (Button) findViewById(R.id.tryanother);
        button.setText("Edit More");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onSelfCloudLoginSuccess() {
        if(!isFinishing()&&!isDestroyed()){
            if(mLoginDialog!=null&&mLoginDialog.isVisible()){
                mLoginDialog.dismiss();
            }
            if(mShareDialog!=null&&mShareDialog.isVisible()){
                mShareDialog.dismiss();
            }
            mShareDialog=new ShareImageDialog();
            mShareDialog.setBitmapFile(BaseActivity.image_file);
            mShareDialog.show(getSupportFragmentManager(), "shareDialog");
        }
    }

    @Override
    public void onPostSuccessFulAndCheckPost() {
        if(!isFinishing()&&!isDestroyed()){
            if(mShareDialog!=null&&mShareDialog.isVisible()){
                mShareDialog.dismiss();
            }
            LinearLayout checkPost = (LinearLayout) findViewById(R.id.check_post);
            checkPost.setVisibility(View.VISIBLE);
            checkPost.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdsHelper.initShareInterstitialAndShow(ShareActivity.this);
                    finish();
                    BaseActivity.destroy();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    NameArtMenu.showFeed();
                                }
                            });
                        }
                    }, 1000);
                }
            });
        }
    }

    private void setNewTagAnimation() {
        final ImageView newTag = (ImageView) findViewById(R.id.self_share);
        final Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        final Animation zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
        newTag.startAnimation(zoomin);
        zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                newTag.startAnimation(zoomout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                newTag.startAnimation(zoomin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //ijkWrapper.setSurface(new Surface(surface));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        //ijkWrapper.setSurface(null);
        return true;

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
