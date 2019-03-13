package com.formationapps.nameart;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.formationapps.nameart.helper.AppUtils;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
//import com.inappertising.ads.ExternalAnalyticsManager;

import nonworkingcode.errorreportmail.ErrorHandler;

//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

/**
 * Created by caliber fashion on 2/10/2017.
 */

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    private static App mInstance;
    private Tracker mTracker;
    private RequestQueue mRequestQueue;

    public static synchronized App getInstance() {
        return mInstance;
    }
    public Context getContext(){
        return getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //ErrorHandler.register(this, "Name Art Bug");

        AppUtils.setAppOpenCount(this);
        StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(vmPolicyBuilder.build());
        try {
            MobileAds.initialize(this, getString(R.string.admob_appId));
            //ExternalAnalyticsManager.initAllMetrics(this);
        } catch (Exception e) {

        }

        try {
            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);
        } catch (Exception e) {

        }

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
