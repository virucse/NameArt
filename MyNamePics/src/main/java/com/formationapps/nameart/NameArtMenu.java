package com.formationapps.nameart;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.activity.ShareActivity;
import com.formationapps.nameart.fragments.BaseFragment;
import com.formationapps.nameart.fragments.HomeFragment;
import com.formationapps.nameart.fragments.RateFragment;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.NativeAdsLoadListener;
import com.formationapps.nameart.helper.OpenPlayStore;
import com.formationapps.nameart.helper.TemplateDataHolder;
import com.formationapps.nameart.permission.MarseMallowPermission;
import com.gallery.activity.ImageSelectAcivity;
import com.gallery.utils.GalleryUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.webapi.fragments.LogRegis;
import com.webapi.model.MyHeader;
import com.webapi.model.NotificationDetail;
import com.webapi.fragments.RegisterFragment;
import com.webapi.fragments.SingleUserFeedFrag;
import com.webapi.fragments.UserFragment;
import com.webapi.model.UserProfile;
import com.webapi.model.WebConstant;
import com.webapi.fragments.FeedFragment;
import com.webapi.fragments.LoginFragment;
import com.webapi.fragments.NotificationFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import billingmodule.billing.BillingConstants;
import billingmodule.billing.BillingManager;
import billingmodule.billing.PurchaseItemClickListener;
import billingmodule.billing.PurchaseOptions;
import billingmodule.billing.PurchaseUtils;
import cz.msebera.android.httpclient.Header;

import static billingmodule.billing.BillingManager.BILLING_MANAGER_NOT_INITIALIZED;
import static com.android.billingclient.api.BillingClient.SkuType.INAPP;
import static com.android.billingclient.api.BillingClient.SkuType.SUBS;
import static com.formationapps.nameart.fragments.SymbolFragment.symbolList;
import static com.formationapps.nameart.fragments.TemplateFragment.templateList;


public class NameArtMenu extends BaseActivity implements NativeAdsLoadListener {

    private static final String TAG = "NameArtMenu";
    public static NameArtMenu instanse;
    private static StorageReference mBaseStorageRef;
    public FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    int adCount = 0;
    Stack<String> nameStack = new Stack<>();
    //private PlusOneButton mPlusOneButton;
    private WebView mWebView_pp;
    private ImageButton btn_notification;
    private  UserProfile userProfile;
    //billing manager
    private BillingManager mBillingManager;
    private boolean daily = true;
    private boolean isPaused;
    private boolean doubleBackToExitPressedOnce = false;

    private WebViewClient wClientPP = new WebViewClient() {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            view.loadUrl(url);
            return false;
        }
    };

    private PurchaseOptions billingDialog;
    private boolean mIsPermanent, mIsYearly;

    public static void showFeed() {
        if (instanse != null) {
            instanse.onUserProfileClick();
        }
    }

    public static String readFile(Reader reader) throws IOException {
        final StringBuilder mStringBuilder = new StringBuilder();
        BufferedReader mBUfferedReader = new BufferedReader(reader);
        String line;
        while ((line = mBUfferedReader.readLine()) != null) {
            mStringBuilder.append(line);
        }
        mBUfferedReader.close();
        return mStringBuilder.toString();
    }

    public static void setFont(ViewGroup group, Typeface font) {
        if (group == null || font == null) {
            return;
        }
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button /*etc.*/)
                ((TextView) v).setTypeface(font);
            else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logAnalyticEvent("NAME_ART_MENU");
        //mBaseStorageRef = mFirebaseStorage.getReferenceFromUrl(getString(R.string.firebase_base_url));
        remoteConfig();
        instanse = this;

        setContentView(R.layout.name_art_menu);

        //mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);
        btn_notification = (ImageButton)findViewById(R.id.notifications);

        AppUtils.onCreate(this);

        addHomeFragment();

        userProfile = UserProfile.getInstance();
        if (userProfile.isLoggedIn()){
            // start notification service ....
            int length = getIntent().getIntExtra("length",0);
            if (length !=0){
                for (int i=0;i < length;i++) {
                    if (getIntent().getStringExtra("type").equals("comment")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("name", userProfile.getUserName());
                        bundle.putLong("userid", userProfile.getUserIdValue());
                        bundle.putLong("postid", getIntent().getLongExtra("postid",0));
                        SingleUserFeedFrag suff = new SingleUserFeedFrag();
                        suff.setArguments(bundle);
                        addNewFragment(suff);
                        Log.e("second", "comment notification");

                        } else if (getIntent().getStringExtra("type").equals("post")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("name", getIntent().getStringExtra("name"));
                            bundle.putLong("userid", getIntent().getLongExtra("userid",0));
                            bundle.putLong("postid", getIntent().getLongExtra("postid",0));
                            SingleUserFeedFrag suff = new SingleUserFeedFrag();
                            suff.setArguments(bundle);
                            addNewFragment(suff);
                            Log.e("second", "post notification");
                    }
                }
            }else {
              //  setUpNotification(this);
            }
        }
        //generateHash();
        final ImageButton home = (ImageButton) findViewById(R.id.home);
        setSelection(R.id.home, R.mipmap.home_select);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(R.id.home, R.mipmap.home_select);
                ((FrameLayout) findViewById(R.id.fragment_container)).removeAllViews();
                nameStack.clear();
                addHomeFragment();
            }
        });

        ImageButton rate = (ImageButton) findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(R.id.rate, R.mipmap.rate_select);
                ((FrameLayout) findViewById(R.id.fragment_container)).removeAllViews();
                nameStack.clear();
                addRateFragment();
            }
        });
        ImageButton feeds = (ImageButton) findViewById(R.id.feeds);
        feeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedClick();
            }
        });

        ImageButton userprofile = (ImageButton) findViewById(R.id.userprofile);
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserProfileClick();
            }
        });

        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotificationClick();
            }
        });
        //permission

        if (Build.VERSION.SDK_INT >= 23) {
            MarseMallowPermission.checkPermissions(this,1);
        }


        mWebView_pp = (WebView) findViewById(R.id.webView_pp);
        mWebView_pp.getSettings().setJavaScriptEnabled(true);


        try {
            //get notification id from AlarmReceiver if exist.
            if (getIntent().hasExtra("notificationid")) {
                int notificationId = getIntent().getIntExtra("notificationid", 0);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notificationId);
            }
        } catch (Exception e) {

        }

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                NameArtMenu.setFont((ViewGroup) findViewById(android.R.id.content), AppUtils.defaultTypeface);
            }
        }, 1 * 0);

        //generateHash();

        createPostNotification();

        initBillingManager();

        initBackPressedDialog(false);

        loadNativeAds();

        //for photo printing
        try {
            /*OrderPrint.getInstance()
                    .with(getApplication())
                    .returnBackActivity(ShareActivity.class)
                    .runInSandboxMode(false)
                    .initialize("1506681087239704943");*/
        }catch (Exception e){

        }
    }

    public void setPrivacyPolicy() {
        String url2 = "http://formationapps-privacy-policy.blogspot.in/";
        mWebView_pp.setVisibility(View.VISIBLE);
        mWebView_pp.loadUrl(url2);
        mWebView_pp.setWebViewClient(wClientPP);
    }

    public void setSelection(int id, int resId) {
        ((ImageButton) findViewById(R.id.home)).setImageResource(R.mipmap.home_unselect);
        ((ImageButton) findViewById(R.id.rate)).setImageResource(R.mipmap.rate_unselect);
        ((ImageButton) findViewById(R.id.feeds)).setImageResource(R.mipmap.feeds_unselect);
        ((ImageButton) findViewById(R.id.userprofile)).setImageResource(R.mipmap.profile_unselect);
        btn_notification.setImageResource(R.mipmap.bell_unselect);
        ((ImageButton) findViewById(id)).setImageResource(resId);
    }

    private void onFeedClick() {
        setSelection(R.id.feeds, R.mipmap.feeds_select);
        ((FrameLayout) findViewById(R.id.fragment_container)).removeAllViews();
        nameStack.clear();
        BaseFragment ff = new FeedFragment();
        addNewFragment(ff);
    }

    private void onUserProfileClick() {
        setSelection(R.id.userprofile, R.mipmap.profile_select);
        ((FrameLayout) findViewById(R.id.fragment_container)).removeAllViews();
        nameStack.clear();
        UserFragment uf = new UserFragment();
        addNewFragment(uf);
    }

    private void onNotificationClick(){
        setSelection(R.id.notifications,R.mipmap.bell_select);
        ((FrameLayout) findViewById(R.id.fragment_container)).removeAllViews();
        nameStack.clear();
        BaseFragment nf = new NotificationFragment();
        addNewFragment(nf);
    }

    private void createDailyNotification() {
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION.NAMEPICSNAMEART");
        notificationIntent.addCategory("android.intent.category.DEFAULT.NAMEPICSNAMEART");

        //Intent myIntent = new Intent(Current.this , NotifyService.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 00);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, pendingIntent);
    }

    public void createPostNotification() {
        if (daily) {
            //createDailyNotification();
            //return;
        }
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION.NAMEPICSNAMEART");
        notificationIntent.addCategory("android.intent.category.DEFAULT.NAMEPICSNAMEART");

        //Intent myIntent = new Intent(NameArtMenu.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 8); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, 1); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        alarmManager.cancel(pendingIntent);

        /*firingCal.add(Calendar.DAY_OF_MONTH, 1);
        intendedTime = firingCal.getTimeInMillis();
        alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);*/

        if (intendedTime >= currentTime) {
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }

    }

    public void startEdit(int type) {
        if (MarseMallowPermission.storagePermitted(this, 3)) {
            Intent intent = new Intent(this, ImageSelectAcivity.class);
            intent.putExtra(GalleryUtil.EXTRA_TYPE, type);
            startActivityForResult(intent, type);
        } else {
            if (BuildConfig.DEBUG) {
                Toast.makeText(this, "permission:" + MarseMallowPermission.storagePermitted(this, 3), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //AppUtils.symbolAdsShownCount = 0;
        ShareActivity.rewardedVideoCount = 0;
        String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
        try {
            //mPlusOneButton.initialize(url, 0);
        } catch (Exception e) {

        }
        AppUtils.first_time = true;
        isPaused = false;

        manageAppRestrictDialog();
        // Note: We query purchases in onResume() to handle purchases completed while the activity
        // is inactive. For example, this can happen if the activity is destroyed during the
        // purchase flow. This ensures that when the activity is resumed it reflects the user's
        // current purchases.
        if (mBillingManager != null
                && mBillingManager.getBillingClientResponseCode() == BillingClient.BillingResponse.OK) {
            mBillingManager.queryPurchases();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    private void loadBannerAd1(boolean isAdmobShown) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_rate_app:
                String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                new OpenPlayStore(this).execute(url);
                break;
            case R.id.action_share_app:
                onShare();
                break;
            case R.id.action_email_app:
                emailAppToFriend();
                break;
            case R.id.action_suggestion_app:
                sendFeedback();
                break;
        }
        return true;
    }

    /* <com.formationapps.nameart.helper.CircleImageView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_centerInParent="true"
    android:id="@+id/image_view"
    android:layout_width="90dp"
    android:layout_height="90dp"
    android:src="@mipmap/ic_launcher"
    app:civ_border_width="1dp"
    app:civ_border_color="#FFFFFFFF"/>*/

    public void onShare() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(android.content.Intent.EXTRA_SUBJECT, "" + getResources().getString(R.string.app_name));
        share.putExtra(android.content.Intent.EXTRA_TEXT,
                "Hey!\nTry My Name Pics an awesome app '" + getResources().getString(R.string.app_name)
                        + "'having 50+ famous unique fonts style ,Greeting Cards,imogies to make your name on and more" +
                        "at\nhttps://goo.gl/jSzZwL"
        );
        //share.putExtra(Intent.EXTRA_STREAM, fromMyHtml(html));
        startActivity(Intent.createChooser(share, "AppUtils Via"));
    }

    public void sendFeedback() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String aEmailList[] = {"care.formationapps@gmail.com"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getString(R.string.app_name) + " " + getString(R.string.email_subject));
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "" + getString(R.string.email_msg));
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.chooser_text)));
    }

    public void emailAppToFriend() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String aEmailList[] = {"<--Enter Your Friend Email Here-->"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getString(R.string.app_name));
        emailIntent.setType("plain/text");
        String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey!\nTry My Name Pics an awesome app '" + getResources().getString(R.string.app_name)
                + " 'having 50+ famous unique fonts style ,Greeting Cards,imogies to make your name on and more " +
                "at\nhttps://goo.gl/50msVC");
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.email_send_choose_text)));
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "backpress: double:" + doubleBackToExitPressedOnce);
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            Log.i(TAG, "backpress: double:" + doubleBackToExitPressedOnce);
            return;
        }

        if (mWebView_pp.getVisibility() == View.VISIBLE) {
            mWebView_pp.setVisibility(View.GONE);
            return;
        }
        if (RateFragment.instanse != null) {
            if (RateFragment.instanse.onBack()) {
                return;
            }
        }
        Fragment frag = getSupportFragmentManager().findFragmentByTag("loginfragment");
        if (frag != null){
            getSupportFragmentManager().beginTransaction().remove(frag).commitAllowingStateLoss();
        }


        if (removeLastFragment()) {
            Log.i(TAG, "backpress.fragmentremove: double:" + doubleBackToExitPressedOnce);
            popBackStackImmediate();
            return;
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("text");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            return;
        }

       if(backPressDialog!=null&&!backPressDialog.isShowing()){
           backPressDialog.show();
       }
        doubleBackToExitPressedOnce=false;
        /*doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "" + getString(R.string.again_click_to_exit), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                Log.i(TAG, "backpress.run: double:" + doubleBackToExitPressedOnce);
            }
        }, (long) (1000 * 1.5f));*/

    }

    private Dialog backPressDialog;
    private void initBackPressedDialog(boolean showImmediate){
        backPressDialog = new Dialog(this, R.style.Theme_IAPTheme);
        backPressDialog.setContentView(R.layout.backpressed_dialog);
        ((TextView)backPressDialog.findViewById(R.id.title_dialog)).setText(R.string.are_you_sure_exit);
        AdsHelper.loadNativeAdAndShow(this, null,
                (UnifiedNativeAdView) backPressDialog.findViewById(R.id.ad_view));
        backPressDialog.findViewById(R.id.dialogButtonyes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressDialog.dismiss();
                doubleBackToExitPressedOnce=true;
                onBackPressed();
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

    private void remoteConfig() {
        //remote config setting
        long cacheExpiration = 10;
        final FirebaseRemoteConfig mFireConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFireConfig.setConfigSettings(configSettings);
        if (mFireConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 10;
        }

        mFireConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFireConfig.activateFetched();

                            AppUtils.setStickerRemotePath(mFireConfig.getString("sticker_root_url"));
                            AppUtils.setStickerFilePath(mFireConfig.getString("sticker_file_path"));
                            AppUtils.setSymbleRemotePath(mFireConfig.getString("symbol_root_url"));
                            AppUtils.setSymbleFilePath(mFireConfig.getString("symbol_file_path"));
                            AppUtils.setTemplatesRemotePath(mFireConfig.getString("template_root_url"));
                            AppUtils.setTemplatesFilePath(mFireConfig.getString("template_file_path"));
                            AppUtils.setTrendingTemplateRootUrl(mFireConfig.getString("trendingtemplaterooturl"));

                            //"http://192.168.1.14:5000/"
                            String webapidomain = mFireConfig.getString("webapidomain");
                            AppUtils.setWebApiDomain(NameArtMenu.this, webapidomain);
                            WebConstant.getInstance().setRealDomain(true);

                            AppUtils.downloadableRootUrl = mFireConfig.getString("static_image_root");

                            AppUtils.setFontDownloadRootUrl(mFireConfig.getString("fontDownloadRootUrl"));
                            AppUtils.setBorderDownloadRootUrl(mFireConfig.getString("borderdownloadrooturl"));
                            AppUtils.setBgDownloadRootUrl(mFireConfig.getString("bgdownloadrooturl"));
                            AppUtils.setPIPDownloadRootUrl(mFireConfig.getString("pipdownloadrooturl"));

                            AppUtils.setDownloadRootUrl(NameArtMenu.this, AppUtils.downloadableRootUrl);

                            boolean isAppUpdateAvailable = mFireConfig.getBoolean("app_update_key");
                            String version = mFireConfig.getString("app_version_name");
                            if (isAppUpdateAvailable&&!isFinishing()) {
                                try {
                                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                    String versionName = pInfo.versionName;
                                    if (!version.equalsIgnoreCase(versionName)) {
                                        updateNotify();
                                    }
                                } catch (Exception e) {

                                }
                            }
                            try {
                               String num=mFireConfig.getString("restrictappversioncode");
                               AppUtils.setAppRestrictVersionCode(NameArtMenu.this,Integer.parseInt(num+""));
                                manageAppRestrictDialog();
                            }catch (NumberFormatException e){

                            }
                        }
                    }
                });
    }
    private void manageAppRestrictDialog(){
        if(BuildConfig.VERSION_CODE>=AppUtils.getAppRestrictVersionCode(NameArtMenu.this)){
            dismissAppRestrictDialog();
        }else {
            showAppRestrictDialog();
        }
    }

    private void updateNotify() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NameArtMenu.this);
        // set title
        alertDialogBuilder.setTitle(R.string.app_update_title);
        alertDialogBuilder.setMessage(R.string.app_update_message);
        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, close
                // current activity
                dialog.dismiss();
                String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                new OpenPlayStore(NameArtMenu.this).execute(url);

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });
        // create alert dialog
        AlertDialog updateDialog = alertDialogBuilder.create();
        // show it
        updateDialog.show();

        try {
            //apply custom fonts
            Button btnPositive = updateDialog.getButton(Dialog.BUTTON_POSITIVE);
            btnPositive.setTypeface(AppUtils.defaultTypeface);

            Button btnNegative = updateDialog.getButton(Dialog.BUTTON_NEGATIVE);
            btnNegative.setTypeface(AppUtils.defaultTypeface);

            TextView tvM = (TextView) updateDialog.findViewById(android.R.id.message);
            tvM.setTypeface(AppUtils.defaultTypeface);
        } catch (Exception e) {
            showException("update exce", e);
        }
    }

    private void generateHash() {
        try {
            if(Build.VERSION.SDK_INT >= 28){
                PackageInfo info = getPackageManager().getPackageInfo("" + getPackageName(),
                        PackageManager.GET_SIGNING_CERTIFICATES);
                final Signature[] signatures=info.signingInfo.getApkContentsSigners();
                final MessageDigest md = MessageDigest.getInstance("SHA");
                for (Signature signature : signatures) {
                    md.update(signature.toByteArray());
                    String something = new String(Base64.encode(md.digest(), Base64.DEFAULT));
                    //String something = new String(Base64.encodeBytes(md.digest()));
                    System.out.println("Hash Key:" + something);
                    Log.d("hash key", something);
                }
            }else{
                PackageInfo info = getPackageManager().getPackageInfo("" + getPackageName(),
                        PackageManager.GET_SIGNATURES);
                for (Signature signature :
                        info.signatures) {
                    MessageDigest md;
                    md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String something = new String(Base64.encode(md.digest(), 0));
                    //String something = new String(Base64.encodeBytes(md.digest()));
                    System.out.println("Hash Key:" + something);
                    Log.d("hash key", something);
                }
            }

        } catch (PackageManager.NameNotFoundException e1) {
            Log.d("name not found", e1.toString());
        } catch
                (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm",
                    e.toString());
        } catch (Exception e) {
            Log.e("exception",
                    e.toString());
        }
    }

    private void readJsonForTemplate(String json) {
        if (json == null || json.isEmpty()) {
            try {
                InputStreamReader insr = new InputStreamReader(getAssets().open("files/templates2.txt"));
                json = readFile(insr);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

        }
        templateList.clear();
        String rootFolder;
        try {
            JSONObject rootObject = new JSONObject(json);
            rootFolder = rootObject.getString("rootfolder");
            JSONArray filesArray = rootObject.getJSONArray("files");
            JSONObject fileDetailObject = rootObject.getJSONObject("details");
            for (int i = 0; i < filesArray.length(); i++) {
                String parent = filesArray.getString(i);
                JSONArray childArray = fileDetailObject.getJSONArray(parent);
                String[] child = new String[childArray.length()];
                for (int j = 0; j < childArray.length(); j++) {
                    child[j] = rootFolder + "/" + parent + "/" + childArray.getString(j);
                }
                TemplateDataHolder tdh = new TemplateDataHolder();
                tdh.setParent(parent);
                tdh.setChilds(child);
                templateList.add(tdh);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying helper.");
        if (mBillingManager != null) {
            mBillingManager.destroy();
        }
        super.onDestroy();
        AdsHelper.clearAllAds();
    }

    public void addRateFragment() {
        ((ViewGroup) findViewById(R.id.fragment_container)).removeAllViews();
        nameStack.clear();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RateFragment(), "homefragment").commitAllowingStateLoss();
    }

    public void addHomeFragment() {
        ((ViewGroup) findViewById(R.id.fragment_container)).removeAllViews();
        nameStack.clear();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment(), "homefragment").commitAllowingStateLoss();
        //mPlusOneButton.setVisibility(View.VISIBLE);
    }

    public void onRegisterClick(View view) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new RegisterFragment(), "registerlogin").commitAllowingStateLoss();
    }

    public void onWebApiClick(View view) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LogRegis(), "registerlogin").commitAllowingStateLoss();
    }

    public void addNewFragment(BaseFragment fragment) {
       /* if (mPlusOneButton != null)
            mPlusOneButton.setVisibility(View.INVISIBLE);*/

       if (getSupportFragmentManager().findFragmentByTag("homefragment") != null) {
            getSupportFragmentManager().findFragmentByTag("homefragment").getView().setVisibility(View.INVISIBLE);
        }
        if (nameStack.size() > 0) {
            BaseFragment back = (BaseFragment) getSupportFragmentManager().findFragmentByTag(nameStack.peek());
            if (back != null&&back.getView()!=null) {
               back.getView().setVisibility(View.INVISIBLE);
            }
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add (R.id.fragment_container, fragment, fragment.getClass().getName())
                .addToBackStack(null).commitAllowingStateLoss();
        nameStack.push(new String(fragment.getClass().getName()));

        if (fragment.getClass() == FeedFragment.class) {
            setSelection(R.id.feeds, R.mipmap.feeds_select);
        } else if (fragment.getClass() == UserFragment.class) {
            setSelection(R.id.userprofile, R.mipmap.profile_select);
        } else if (fragment.getClass() == LoginFragment.class) {
            setSelection(R.id.userprofile, R.mipmap.profile_select);
        } else if (fragment.getClass() == RateFragment.class) {
            setSelection(R.id.rate, R.mipmap.rate_select);
        }else if (fragment.getClass() == NotificationFragment.class){
            setSelection(R.id.notifications,R.mipmap.bell_select);
        }
    }

    public void popBackStackImmediate() {
        try {
            getSupportFragmentManager().popBackStackImmediate();

        } catch (Exception e) {

        }
    }

    public boolean removeLastFragment() {
        if (nameStack.size() <= 0) {
            return false;
        }
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(nameStack.peek());
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            nameStack.pop();
      /* int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
        if (index >=0) {*/
         // String tag  = getSupportFragmentManager().getBackStackEntryAt(index-1).getName();
         // Log.e("last_frag",tag);
         // BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
           //if (fragment !=null){
               //  nameStack.pop();
             //  String tag2  = getSupportFragmentManager().getBackStackEntryAt(index-1).getName();
               //Log.e("last_frag", String.valueOf(fragment.getClass()));
               if (nameStack.size()>0){
               BaseFragment back = (BaseFragment) getSupportFragmentManager().findFragmentByTag(nameStack.peek());
                if (back != null) {
                   back.getView().setVisibility(View.VISIBLE);
                    if (back.getClass() == FeedFragment.class) {
                        setSelection(R.id.feeds, R.mipmap.feeds_select);
                    } else if (back.getClass() == UserFragment.class) {
                        setSelection(R.id.userprofile, R.mipmap.profile_select);
                    } else if (back.getClass() == LoginFragment.class) {
                        setSelection(R.id.userprofile, R.mipmap.profile_select);
                    } else if (back.getClass() == RateFragment.class) {
                        setSelection(R.id.rate, R.mipmap.rate_select);
                    }else if (back.getClass()==NotificationFragment.class){
                    setSelection(R.id.notifications,R.mipmap.bell_select);
                    }
                }
            } else {
                addHomeFragment();
                setSelection(R.id.home, R.mipmap.home_select);
           }
            return true;
        } else {
           setSelection(R.id.home, R.mipmap.home_select);
            return false;
       }
    }

    public void showBillingDialog() {
        if (billingDialog == null) {
            billingDialog = new PurchaseOptions(this, R.style.FullScreen);
            billingDialog.setmPurchaseItemClickListener(new PurchaseItemClickListener() {
                @Override
                public void onPermanentPurchaseClick(View view) {
                    NameArtMenu.this.onPermanentPurchaseClick(view);
                }

                @Override
                public void onYearlyPurchaseClick(View view) {
                    NameArtMenu.this.onYearlyPurchaseClick(view);
                }
            });
        }
        billingDialog.show();
    }

    private boolean isYearlyPremium() {
        return mIsYearly;
    }

    private boolean isPermanentPremium() {
        return mIsPermanent;
    }

    private void initBillingManager() {
        mIsPermanent = false;
        mIsYearly = false;
        // Create and initialize BillingManager which talks to BillingLibrary
        mBillingManager = new BillingManager(this, new BillingManager.BillingUpdatesListener() {
            @Override
            public void onBillingClientSetupFinished() {
            }

            @Override
            public void onConsumeFinished(String token, @BillingClient.BillingResponse int result) {
            }

            @Override
            public void onPurchasesUpdated(List<Purchase> purchases) {
                for (Purchase purchase : purchases) {
                    switch (purchase.getSku()) {
                        case BillingConstants.SKU_PERMANENT:
                            Log.d(TAG, "You are Premium! Congratulations!!!");
                            mIsPermanent = true;
                            PurchaseUtils.setPermanentPurchase(NameArtMenu.this, mIsPermanent);
                            if (billingDialog != null && billingDialog.isShowing()) {
                                billingDialog.dismiss();
                            }
                            break;
                        case BillingConstants.SKU_YEARLY:
                            Log.d(TAG, "You are Yearly Premium! Congratulations!!!");
                            mIsYearly = true;
                            PurchaseUtils.setYearlyPurchase(NameArtMenu.this, mIsYearly);
                            if (billingDialog != null && billingDialog.isShowing()) {
                                billingDialog.dismiss();
                            }
                            break;
                    }
                }
            }
        });
    }

    private void onPermanentPurchaseClick(final View arg0) {
        Log.d(TAG, "Purchase button clicked.");

        if (mBillingManager != null
                && mBillingManager.getBillingClientResponseCode()
                > BILLING_MANAGER_NOT_INITIALIZED) {
            Log.d(TAG, "mAcquireFragment shown");
            mBillingManager.initiatePurchaseFlow(BillingConstants.SKU_PERMANENT, INAPP);
        }
    }

    private void onYearlyPurchaseClick(final View arg0) {
        Log.d(TAG, "Purchase button clicked.");

        if (mBillingManager != null
                && mBillingManager.getBillingClientResponseCode()
                > BILLING_MANAGER_NOT_INITIALIZED) {
            Log.d(TAG, "mAcquireFragment shown");
            mBillingManager.initiatePurchaseFlow(BillingConstants.SKU_YEARLY, SUBS);
        }
    }

    public static class GridViewAdapter extends BaseAdapter {
        private Context mContext;
        private List<Apps> appItemList;

        public GridViewAdapter(Context c, List<Apps> appItemList) {
            mContext = c;
            this.appItemList = appItemList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return appItemList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                grid = new View(mContext);
                grid = inflater.inflate(R.layout.gridview_item_fromotion, null);
                TextView tv = (TextView) grid.findViewById(R.id.text_view_prom);
                tv.setText("" + appItemList.get(position).appName);
                tv.setVisibility(View.GONE);
                ImageView imageView = (ImageView) grid.findViewById(R.id.image_view);
                try {
                    StorageReference rf = mBaseStorageRef.child(appItemList.get(position).appIconUrl);
                   /* Glide.with(mContext)
                            .using(new FirebaseImageLoader())
                            .load(rf)
                            .into(imageView);*/
                } catch (Exception e) {

                }
            } else {
                grid = (View) convertView;
            }

            return grid;
        }
    }

    public static class Apps {
        public String appName, appPackageName, appIconUrl, referrer;
    }
    NotificationDetail nd = null;
    private static final int NOTIFICATION_ID = 1;
    private String ofn = "open_from_notification";

    private void setUpNotification(Context context){
       // NotificationEventReceiver.setupAlarm(getApplicationContext());
        String url =WebConstant.getInstance().getInstance().NOTIFICATION;
        AsyncHttpClient client = new AsyncHttpClient();
        Log.e("notification_url", url);
        client.get(getApplicationContext(), url, new Header[]{new MyHeader(getApplicationContext())}, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("notification_success",new String(responseBody).toString());
                setUpNotificationDetail(new String(responseBody).toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("notification_failure", String.valueOf(statusCode));
                Log.e("notification_failure","failed");
                Log.e("notification_failure", new String(responseBody).toString());
                Log.e("notification_failure", String.valueOf(error));
            }
        });
    }

    private void setUpNotificationDetail(String response){
        nd = new Gson().fromJson(response,NotificationDetail.class);
        if (nd != null){
            processStartNotification();
        }
    }

    private void processStartNotification() {
        NotificationCompat.Builder builder;
        if (nd != null) {
            if (nd.notification.length > 0) {
                for (int i = 0; i < nd.notification.length; i++) {
                    String username = nd.notification[i].username;
                    String post = nd.notification[i].type;
                    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                        builder = new NotificationCompat.Builder(this);
                    } else {
                        builder = new NotificationCompat.Builder(this, "notification_builder");
                    }
                    builder.setSmallIcon(R.mipmap.icon_72);
                    builder.setTicker("Notification from Name Art ");
                    builder.setContentTitle(username);
                    if (post.equals("comment")) {
                        builder.setContentText("" + username + " commented on your post");
                    }
                    if (post.equals("post")) {
                        builder.setContentText("" + username + " posted on Name Art");
                    }
                    builder.setAutoCancel(true);
                    Intent intent = new Intent(this, NameArtMenu.class);
                    intent.putExtra("length",nd.notification.length);
                    intent.putExtra("postid",nd.notification[i].post_id);
                    intent.putExtra("userid",nd.notification[i].user_id);
                    intent.putExtra("name",nd.notification[i].username);
                    intent.putExtra("type",nd.notification[i].type);

                    PendingIntent pendingIntent = PendingIntent.getActivity(this,
                            NOTIFICATION_ID, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(NOTIFICATION_ID, builder.build());
                }
            }
        }
    }

    // The number of native ads to load.
    public static final int NUMBER_OF_ADS = 5;
    // The AdLoader used to load ads.
    private AdLoader adLoader;
    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private void loadNativeAds() {
        mNativeAds.clear();
        if(PurchaseUtils.isPermanentPurchased(this)||PurchaseUtils.isYearlyPurchased(this)){
            return;
        }else {
            AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_nativeId_feed));
            adLoader = builder.forUnifiedNativeAd(
                    new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            // A native ad loaded successfully, check if the ad loader has finished loading
                            // and if so, insert the ads into the list.
                            mNativeAds.add(unifiedNativeAd);
                            if (!adLoader.isLoading()) {
                                //insertAdsInMenuItems();
                            }
                        }
                    }).withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // A native ad failed to load, check if the ad loader has finished loading
                            // and if so, insert the ads into the list.
                            Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                    + " load another.");
                            if (!adLoader.isLoading()) {
                                //insertAdsInMenuItems();
                            }
                        }
                    }).build();

            // Load the Native ads.
            adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
        }
    }

    @Override
    public List<UnifiedNativeAd> onNativeAdLoaded() {
        return mNativeAds;
    }
}
