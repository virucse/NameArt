package com.webapi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.BaseFragment;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.FeedUser;
import com.webapi.model.MyHeader;
import com.webapi.model.UserProfile;
import com.webapi.model.WebConstant;
import com.webapi.adapters.CommentListAdapter;
import com.webapi.common.CommanProcessingListener;
import com.webapi.common.CommentsApi;
import com.webapi.common.DividerItemDecoration;
import com.webapi.common.RecyclerTouchListener;
import com.webapi.model.CommentDetail;
import com.webapi.model.CommentListDetail;

import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/8/2017.
 */

public class CommentFragment extends BaseFragment {
    private CommanProcessingListener mListener;
    private EditText commentbox;
    private long postId;
    private Button send;
    private ScrollView mScrollView;
    private CommentDetail comments;
    private CommentListAdapter mCommentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.commentfragment, container, false);
    }

    public void setListener(CommanProcessingListener listener) {
        mListener = listener;
    }

    public void onCommentSuccess() {
        mCommentAdapter.addNewItem(comments);
        // mScrollView.computeScroll();
        commentbox.setText("");
        send.setVisibility(View.VISIBLE);
        stopAnim();
        if (mListener != null) {
            mListener.onCommentSuccess(postId);
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startAnim();
        Bundle b = getArguments();
        postId = b.getLong("postid");
        RequestParams rp = new RequestParams();
        rp.put("post_id", postId);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getActivity(), WebConstant.getInstance().COMMENTLIST, new Header[]{new MyHeader(getActivity())}, rp, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                CommentListDetail cld = new Gson().fromJson(new String(responseBody).toString(), CommentListDetail.class);
                if (cld != null) {
                    onSuccesRes(cld, view);
                }
                stopAnim();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                stopAnim();
            }
        });
        //mScrollView=(ScrollView)view.findViewById(R.id.scroll_comment);
        //mScrollView.setEnabled(false);
        commentbox = (EditText) view.findViewById(R.id.commentbox_cf);
        send = (Button) view.findViewById(R.id.send_comment_cf);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!commentbox.getText().toString().isEmpty()) {
                    send.setVisibility(View.INVISIBLE);
                    startAnim();
                    comments = new CommentDetail();
                    comments.comment = commentbox.getText().toString();
                    FeedUser fu = new FeedUser();
                    fu.picture = UserProfile.getInstance().getUserPicture();
                    fu.name = UserProfile.getInstance().getUserName();
                    comments.user = fu;
                    String cmnt = new String(commentbox.getText().toString());
                    CommentsApi capi = new CommentsApi();
                    capi.setThisFragment(CommentFragment.this);
                    capi.process(getActivity(), postId, cmnt);
                }
            }
        });
        LinearLayout back = (LinearLayout) view.findViewById(R.id.header_commentfragment);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((NameArtMenu) getActivity()).removeLastFragment()) {
                    ((NameArtMenu) getActivity()).popBackStackImmediate();
                }
                ;
            }
        });
    }

    private void onSuccesRes(final CommentListDetail cld, View view) {
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.commentuserlist);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        if (cld.comments != null) {
            mCommentAdapter = new CommentListAdapter(cld, getActivity());
            rv.setAdapter(mCommentAdapter);
        }
        rv.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CommentDetail cd = cld.comments.get(position);
                if (cd.user.id == UserProfile.getInstance().getUserIdValue()) {
                    UserFragment uf = new UserFragment();
                    ((NameArtMenu) getActivity()).addNewFragment(uf);
                } else {
                    Bundle b = new Bundle();
                    b.putString("name", "" + cd.user.name);
                    b.putLong("userid", cd.user.id);
                    BaseFragment f = new OtherUserFragments();
                    f.setArguments(b);
                    ((NameArtMenu) getActivity()).addNewFragment(f);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
