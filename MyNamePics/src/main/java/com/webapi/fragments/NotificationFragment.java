package com.webapi.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.NotificationDetail;
import com.webapi.model.UserProfile;
import com.webapi.model.WebConstant;
import com.webapi.adapters.NotificationAdapter;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment {

    private static NotificationFragment instanse;
    String url = WebConstant.getInstance().NOTIFICATION;
    NotificationDetail nd = null;
    UserProfile userProfile;

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


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instanse = this;
        userProfile = UserProfile.getInstance();
        startAnim();
        if (!userProfile.isLoggedIn()) {
            LoginFragment lf = new LoginFragment();
           /* if (((NameArtMenu) getActivity()).removeLastFragment()) {
                ((NameArtMenu) getActivity()).popBackStackImmediate();
            }*/
            ((NameArtMenu) getActivity()).addNewFragment(lf);
            return;
        }
        RequestParams rp = new RequestParams();
        rp.put("page", "1");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getContext(), url, new Header[]{new MyHeader(getContext())}, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("WEBAPI","res:"+new String(responseBody).toString());
                onResponseRec(view, new String(responseBody).toString());
                stopAnim();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("WEBAPI", "Failure: statuscode=>" + statusCode);
                stopAnim();
            }
        });
    }
    private void onResponseRec(View view , String response){
        nd = new Gson().fromJson(response, NotificationDetail.class);
        if (nd != null){
            RecyclerView rv = (RecyclerView) view.findViewById(R.id.notification_recylerView);
            rv.setHasFixedSize(true);
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
            rv.setLayoutManager(lm);
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(new NotificationAdapter(nd , userProfile,getActivity()).setRecyclerView(rv));

        }

    }
}
