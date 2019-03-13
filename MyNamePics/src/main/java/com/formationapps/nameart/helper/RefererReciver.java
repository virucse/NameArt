package com.formationapps.nameart.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.formationapps.nameart.App;
import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caliber fashion on 5/7/2017.
 */

public class RefererReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        try {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString("REFERER", referrer);
            mFirebaseAnalytics.logEvent("REFERER_EVENT", bundle);
        } catch (Exception e) {

        }

        new CampaignTrackingReceiver().onReceive(context, intent);
    }
}
