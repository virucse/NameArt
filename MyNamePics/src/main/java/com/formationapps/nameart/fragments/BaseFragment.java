package com.formationapps.nameart.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

/**
 * Created by caliber fashion on 9/7/2017.
 */

public class BaseFragment extends Fragment {
    public static final String SKU_PREMIUM_YEAR = "sku_premium_purchase_year";
    public static final String SKU_PREMIUM_PERMANENT = "sku_premium_purchase_permanent";
    private static final String TAG = BaseFragment.class.getName();
    // (arbitrary) request code for the purchase flow
    private static final int RC_REQUEST = 10001;
    public static boolean mIsPremiumYear, mIsPremiumParmanent;
    CircularProgressView mCircularProgressView;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                NameArtMenu.setFont((ViewGroup) getView(), AppUtils.webApiFont);
            }
        }, 0);
    }

    public void startAnim() {
        /* ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.circularprogressBarAnim);
        // see this max value coming back here, we animale towards that value
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 500);
        animation.setDuration (500); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();*/
        try {
            mCircularProgressView = (CircularProgressView) getView().findViewById(R.id.circularprogressBarAnim);
            mCircularProgressView.setVisibility(View.VISIBLE);
            mCircularProgressView.startAnimation();
        } catch (Exception e) {

        }

    }

    public void stopAnim() {
        //progressBar.clearAnimation();
        try {
            mCircularProgressView.stopAnimation();
            mCircularProgressView.setVisibility(View.INVISIBLE);
        } catch (Exception e) {

        }
    }

    public boolean isNetworkConnected(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    // User clicked the "Upgrade to Premium" button.
    public void onPremimumYearClicked(View arg0) {

    }

    // "Subscribe to infinite gas" button clicked. Explain to user, then start purchase
    // flow for subscription.
    public void onPremiumPermanentClicked(View arg0) {

    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
