package com.webapi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.BaseFragment;
import com.formationapps.nameart.helper.NativeAdsLoadListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.FeedDetails;
import com.webapi.model.MyHeader;
import com.webapi.model.UserProfile;
import com.webapi.model.WebConstant;
import com.webapi.adapters.FeedAdapter;

import java.util.ArrayList;
import java.util.List;

import billingmodule.billing.PurchaseUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/1/2017.
 */

public class FeedFragment extends BaseFragment {
    private static FeedFragment instanse;
    String url = WebConstant.getInstance().FEEDNEW;

    //this is helper method for feedAdapter
    public static void STARTANIM() {
        if (instanse != null) {
            instanse.startAnim();
        }
    }

    public static void STOPANIM() {
        if (instanse != null) {
            instanse.stopAnim();
        }
    }

    private NativeAdsLoadListener mLoadListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mLoadListener= (NativeAdsLoadListener) context;
        }catch (Exception e){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }
    private RecyclerView rv;
    private FeedDetails feed;
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instanse = this;
        startAnim();
        if (!UserProfile.getInstance().isLoggedIn()) {
            LoginFragment lf = new LoginFragment();
           /* if (((NameArtMenu) getActivity()).removeLastFragment()) {
                ((NameArtMenu) getActivity()).popBackStackImmediate();
            }*/
            ((NameArtMenu) getActivity()).addNewFragment(lf);
            return;
        }else {
            rv = (RecyclerView) view.findViewById(R.id.feed_recylerView);
            loadActuallFeedData();
        }
    }

    private void loadActuallFeedData() {
        RequestParams rp = new RequestParams();
        rp.put("page", "1");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getContext(), url, new Header[]{new MyHeader(getContext())}, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("WebApi.feedFragment","res:"+new String(responseBody));
                if(getContext()!=null&&getActivity()!=null){
                    onResponseRec(new String(responseBody));
                }
                stopAnim();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("WEBAPI.feedFragment", "Failure: statuscode=>" + statusCode+"\n error:"+error.getMessage());
                stopAnim();
            }
        });
    }

    private List<Object> mFeedPost = new ArrayList<>();
    private List<UnifiedNativeAd> mNativeAds;
    private void onResponseRec( String response) {
        feed = new Gson().fromJson(response, FeedDetails.class);
        if(feed!=null&&feed.posts!=null){
            mFeedPost.addAll(feed.posts);
            if(mLoadListener!=null){
                mNativeAds=mLoadListener.onNativeAdLoaded();
                if(mNativeAds!=null&&mNativeAds.size()>0){
                    int offset = (mFeedPost.size() / mNativeAds.size())+1;
                    int index = 1;
                    for (UnifiedNativeAd ad : mNativeAds) {
                        mFeedPost.add(index, ad);
                        index = index + offset;
                    }
                }
            }
            loadFeedAdapter();

            feed.posts.clear();
        }
    }

   /* // The number of native ads to load.
    public static final int NUMBER_OF_ADS = 5;
    // The AdLoader used to load ads.
    private AdLoader adLoader;
    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private void loadNativeAds() {
        mNativeAds.clear();
        AdLoader.Builder builder = new AdLoader.Builder(getContext(), getString(R.string.admob_nativeId_feed));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
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
                            insertAdsInMenuItems();
                        }
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }*/

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            return;
        }
        if(rv!=null){
            FeedAdapter feedAdapter = (FeedAdapter) rv.getAdapter();
            if(feedAdapter!=null){
                feedAdapter.setNativeAds(mNativeAds);
            }
        }
    }

    private void loadFeedAdapter() {
        if (feed != null&&rv!=null) {
            rv.setHasFixedSize(true);
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
            rv.setLayoutManager(lm);
            rv.setItemAnimator(new DefaultItemAnimator());
            //rv.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
            FeedAdapter feedAdapter=new FeedAdapter(feed, getActivity(),mFeedPost);
            feedAdapter.setRecyclerView(rv);
            feedAdapter.setNativeAds(mNativeAds);
            rv.setAdapter(feedAdapter);
            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    FeedAdapter feed = (FeedAdapter) recyclerView.getAdapter();
                    if (feed != null) {
                        if (!recyclerView.canScrollVertically(1)) {
                            //scroll end
                            feed.onScrollViewEnd();
                        }/*else if(recyclerView.canScrollVertically(-1)){
                            //scroll start
                            feed.onScrollViewStart();
                        }*/
                    }
                }
            });
        }
    }
}