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
import com.webapi.adapters.LikeListAdapter;
import com.webapi.common.DividerItemDecoration;
import com.webapi.model.LikeListDetail;

import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/11/2017.
 */

public class LikeListFragment extends BaseFragment {
    private long postId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.commentfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startAnim();
        TextView title = (TextView) view.findViewById(R.id.titlebar);
        title.setText(getString(R.string.likes));
        LinearLayout commentboxcon = (LinearLayout) view.findViewById(R.id.commentbox_container);
        commentboxcon.setVisibility(View.GONE);

        String url = WebConstant.getInstance().LIKELIST;
        postId = getArguments().getLong("postid");
        RequestParams rp = new RequestParams();
        rp.put("post_id", postId);

        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.commentuserlist);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(view.getContext(), url, new Header[]{new MyHeader(getContext())}, rp, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                stopAnim();
                LikeListDetail lid = new Gson().fromJson(new String(responseBody).toString(), LikeListDetail.class);
                if (lid != null) {
                    rv.setHasFixedSize(true);
                    RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
                    rv.setLayoutManager(lm);
                    rv.setItemAnimator(new DefaultItemAnimator());
                    rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                    rv.setAdapter(new LikeListAdapter(lid));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                stopAnim();
            }
        });
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
}
