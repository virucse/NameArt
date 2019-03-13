package com.formationapps.nameart.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.formationapps.nameart.App;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import billingmodule.billing.PurchaseUtils;

/**
 * Created by caliber fashion on 11/9/2017.
 */

public class AdsHelper {

    //private static Banner bannerView;
    public static void loadAdmobBanner(Context context, LinearLayout containerLayout) {
        if (PurchaseUtils.isYearlyPurchased(context) || PurchaseUtils.isPermanentPurchased(context)) {
            //dont show ads its premium purchase
            if (containerLayout != null) {
                containerLayout.setVisibility(View.GONE);
            }
            return;
        }

        if(containerLayout!=null){
            containerLayout.setVisibility(View.VISIBLE);
            if(containerLayout.getChildCount()>1){
                containerLayout.getChildAt(1).setVisibility(View.GONE);
                loadAdmobBanner((AdView) containerLayout.getChildAt(0));
            }else{
                loadAdmobBanner((AdView) containerLayout.getChildAt(0));
            }
        }

    }
    private static void loadAdmobBanner(AdView adView){
        adView.setVisibility(View.VISIBLE);
        adView.loadAd(new AdRequest.Builder().build());
    }

    private static InterstitialAd mInterstitialAd;
    public static void initAdmobInterstitialAndShow(Context context) {
        if (PurchaseUtils.isYearlyPurchased(context) || PurchaseUtils.isPermanentPurchased(context)) {
            //dont show ads its premium purchase
            return;
        }
        if(mInterstitialAd==null){
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.admob_interstId));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if(mInterstitialAd!=null)mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            });
        }else {
            if(mInterstitialAd.isLoaded()){
                mInterstitialAd.show();
            }else{
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        }


    }
    private static InterstitialAd interstitialTemplates;
    public static boolean isTemplateInterstitialInit(){
        return interstitialTemplates!=null;
    }
    public static void initTemplatesInterstitialAndShow(Context context) {
        if (PurchaseUtils.isYearlyPurchased(context) || PurchaseUtils.isPermanentPurchased(context)) {
            //dont show ads its premium purchase
            return;
        }
        if(interstitialTemplates==null){
            interstitialTemplates = new InterstitialAd(context);
            interstitialTemplates.setAdUnitId(context.getResources().getString(R.string.admob_interstitial_templates));
            interstitialTemplates.loadAd(new AdRequest.Builder().build());
            interstitialTemplates.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if(interstitialTemplates!=null)interstitialTemplates.loadAd(new AdRequest.Builder().build());
                }
            });
        }else {
            if(interstitialTemplates.isLoaded()){
                interstitialTemplates.show();
            }else{
                interstitialTemplates.loadAd(new AdRequest.Builder().build());
            }
        }
    }
    private static InterstitialAd shareInterstitial;
    public static void initShareInterstitialAndShow(Context context) {
        if (PurchaseUtils.isYearlyPurchased(context) || PurchaseUtils.isPermanentPurchased(context)) {
            //dont show ads its premium purchase
            return;
        }
        if(shareInterstitial==null){
            shareInterstitial = new InterstitialAd(context);
            shareInterstitial.setAdUnitId(context.getResources().getString(R.string.admob_interstitial_share));
            shareInterstitial.loadAd(new AdRequest.Builder().build());
            shareInterstitial.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    //art_OnBackPressInter.loadAd(new AdRequest.Builder().build());
                }
            });
        }else {
            if(shareInterstitial.isLoaded()){
                shareInterstitial.show();
            }else{
                shareInterstitial.loadAd(new AdRequest.Builder().build());
            }
        }

    }

    private static UnifiedNativeAd nativeAd;
    public static void loadNativeAdAndShow(final Context context, final FrameLayout frameLayout,final UnifiedNativeAdView nativeAdView){
        if (PurchaseUtils.isYearlyPurchased(context) || PurchaseUtils.isPermanentPurchased(context)) {
            //dont show ads its premium purchase
            return;
        }
        AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(R.string.admob_nativeId));
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if(context!=null){
                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;
                    if(nativeAdView!=null){
                        nativeAdView.setVisibility(View.VISIBLE);
                        populateUnifiedNativeAdView(unifiedNativeAd, nativeAdView);
                    }else {
                        View adView = ((Activity)context).getLayoutInflater()
                                .inflate(R.layout.ad_unified, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, (UnifiedNativeAdView) adView.findViewById(R.id.ad_view));
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                }
            }
        }).withAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }
        });

        /*VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(startVideoAdsMuted.isChecked())
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);*/

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                //refresh.setEnabled(true);
                if(BuildConfig.DEBUG){
                    Toast.makeText(App.getInstance(), "Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show();
                }
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

        //videoStatus.setText("");
    }
    private static void hideNativeView(FrameLayout frameLayout,UnifiedNativeAdView nativeAdView){
        if(nativeAdView!=null){
            nativeAdView.setVisibility(View.GONE);
        }else if(frameLayout!=null){
            frameLayout.removeAllViews();
        }
        if(frameLayout!=null){
            frameLayout.removeAllViews();
        }
    }
    public static void loadNativeAdAndShow(final Context context, final FrameLayout frameLayout){
        loadNativeAdAndShow(context,frameLayout,null);
    }

    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc!=null&&vc.hasVideoContent()) {
            //videoStatus.setText(String.format(Locale.getDefault(),
            //      "Video status: Ad contains a %.2f:1 video asset.", vc.getAspectRatio()));

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    //refresh.setEnabled(true);
                    //videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
            //videoStatus.setText("Video status: Ad does not contain a video asset.");
            //refresh.setEnabled(true);
        }
    }

    public static void clearAllAds(){
        mInterstitialAd=null;
        interstitialTemplates=null;
        shareInterstitial=null;
        if (nativeAd != null) {
            nativeAd.destroy();
        }
    }

}