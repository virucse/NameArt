package com.formationapps.nameart.helper;

import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.List;

public interface NativeAdsLoadListener {
    public List<UnifiedNativeAd> onNativeAdLoaded();
}
