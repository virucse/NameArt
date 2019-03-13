package com.webapi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.BaseFragment;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.WebConstant;
import com.webapi.adapters.SingleUserFeedAdapter;
import com.webapi.model.OtherUserDetails;

import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/13/2017.
 */

public class SingleUserFeedFrag extends BaseFragment {
    private static SingleUserFeedFrag instanse;
    private String url = WebConstant.getInstance().BASEURL + "userposts";
    private String base = WebConstant.getInstance().BASEURL;
    private long postId;
    private RecyclerView rv;

    //this is helper method for SingleUserFeedAdapter
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instanse = this;
        TextView back = (TextView) view.findViewById(R.id.backIcon);
        back.setVisibility(View.VISIBLE);
        Bundle b = getArguments();
        String name = b.getString("name");
        TextView namet = (TextView) view.findViewById(R.id.titlebar);
        rv = (RecyclerView) view.findViewById(R.id.feed_recylerView);
        namet.setText(name);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.feedt_layout);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NameArtMenu) getActivity()).removeLastFragment();
            }
        });
        loadData();

    }

    private void loadData() {
        startAnim();
        long uid = getArguments().getLong("userid");
        postId = getArguments().getLong("postid");
        RequestParams rp = new RequestParams();
        rp.put("user_id", uid);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getActivity(), url, new Header[]{new MyHeader(getActivity())}, rp, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Log.i("WEBAPI","<==>"+new String(responseBody).toString()+"<==>");
                onResponse(new String(responseBody).toString());
                stopAnim();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                stopAnim();
            }
        });
    }

    private void onResponse(String response) {
        OtherUserDetails oud = new Gson().fromJson(response, OtherUserDetails.class);
        if (oud != null) {
            rv.setHasFixedSize(true);
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
            rv.setLayoutManager(lm);
            rv.setItemAnimator(new DefaultItemAnimator());
            //rv.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
            SingleUserFeedAdapter sufa = new SingleUserFeedAdapter(oud, getActivity());
            sufa.setUserId(getArguments().getLong("userid"));
            if (oud.posts != null && oud.user != null) {
                rv.setAdapter(sufa);
                if (postId != 0) {
                    for (int i = 0; i < oud.posts.length; i++) {
                        if (oud.posts[i].id == postId) {
                            rv.getLayoutManager().scrollToPosition(i);
                            break;
                        }
                    }
                }

            }
        }
    }
}
