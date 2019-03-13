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
import com.webapi.adapters.FollowerListAdapter;
import com.webapi.common.RecyclerTouchListener;
import com.webapi.model.FollowerListDetails;

import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/14/2017.
 */

public class FollowingListFragment extends BaseFragment {
    private static FollowingListFragment instanse;
    private long userid;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.commentfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instanse = this;
        TextView title = (TextView) view.findViewById(R.id.titlebar);
        title.setText(getString(R.string.following));
        LinearLayout commentboxcon = (LinearLayout) view.findViewById(R.id.commentbox_container);
        commentboxcon.setVisibility(View.GONE);
        loadData();
        LinearLayout back = (LinearLayout) view.findViewById(R.id.header_commentfragment);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((NameArtMenu) getActivity()).removeLastFragment()) {
                    ((NameArtMenu) getActivity()).popBackStackImmediate();
                }
            }
        });

    }

    private void loadData() {
        startAnim();
        String url = WebConstant.getInstance().FOLLOWINGLIST;
        userid = getArguments().getLong("userid");
        RequestParams rp = new RequestParams();
        rp.put("user_id", userid);

        final RecyclerView rv = (RecyclerView) getView().findViewById(R.id.commentuserlist);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getContext(), url, new Header[]{new MyHeader(getContext())}, rp, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                stopAnim();
                FollowerListDetails fld = new Gson().fromJson(new String(responseBody).toString(), FollowerListDetails.class);
                if (fld != null) {
                    rv.setHasFixedSize(true);
                    RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
                    rv.setLayoutManager(lm);
                    rv.setItemAnimator(new DefaultItemAnimator());
                    //rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                    rv.setAdapter(new FollowerListAdapter(fld));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                stopAnim();
            }
        });

        rv.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(view.getContext(),"pos"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
}
