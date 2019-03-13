package com.webapi.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.BaseFragment;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.OtherUserDetails;
import com.webapi.model.OtherUserPosts;
import com.webapi.model.WebConstant;
import com.webapi.common.CommanProcessingListener;
import com.webapi.common.FollowApi;

import circularprogress.customViews.CircularProgressButton;
import circularprogress.interfaces.OnCircularProgressButtonClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/6/2017.
 */

public class OtherUserFragments extends BaseFragment implements CommanProcessingListener {
    private String url = WebConstant.getInstance().BASEURL + "userposts";
    private String base = WebConstant.getInstance().BASEURL;
    private CircularProgressButton follow;
    private TextView followers;
    private OtherUserDetails oud;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.anotheruserfragments, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startAnim();
        Bundle b = getArguments();
        String name = b.getString("name", "");
        TextView namet = (TextView) view.findViewById(R.id.anotherusername);
        namet.setText(name);

        long uid = b.getLong("userid");
        RequestParams rp = new RequestParams();
        rp.put("user_id", uid);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getActivity(), url, new Header[]{new MyHeader(getActivity())}, rp, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("WebApi.OtherUserFrag", "onSuccess");
                onRecieveResponse(view, new String(responseBody).toString());
                stopAnim();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                stopAnim();
                Log.e("WebApi.OtherUserFrag", "Failure: statuscode=>" + statusCode+"\n error:"+error.getMessage());
            }
        });
    }

    private void onRecieveResponse(View view, String response) {
        oud = new Gson().fromJson(response, OtherUserDetails.class);
        if (oud != null) {
            TextView namet = (TextView) view.findViewById(R.id.anotherusername);
            namet.setText(oud.user.name);
            namet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    follow.dispose();
                    if (((NameArtMenu) getActivity()).removeLastFragment()) {
                        ((NameArtMenu) getActivity()).popBackStackImmediate();
                    }
                }
            });

            follow = (CircularProgressButton) view.findViewById(R.id.followanother);
            if (oud.user.is_follow) {
                follow.setBackground(new ColorDrawable(Color.TRANSPARENT));
                follow.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                follow.setText(R.string.following);
            } else {
                follow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.follow_bg));
                follow.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                follow.setText(R.string.follow);
            }
            follow.addOnClickListener(new OnCircularProgressButtonClick() {
                @Override
                public void onClickButton(CircularProgressButton button, int index) {
                    super.onClickButton(button, index);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        follow.startAnimation();
                    } else {
                        startAnim();
                    }
                    new FollowApi(getActivity(), oud.user.id, follow, OtherUserFragments.this);
                }
            });

            ImageView dp = (ImageView) view.findViewById(R.id.img_user_profile_another);
            Glide.with(getActivity().getApplicationContext()).load(base + oud.user.picture)
                    .apply(new RequestOptions() .placeholder(R.mipmap.self_share).fitCenter()).into(dp);

            TextView postCount = (TextView) view.findViewById(R.id.post_count_user_another);
            postCount.setText(oud.user.postcount + "");

            TextView following = (TextView) view.findViewById(R.id.following_count_user_another);
            following.setText(oud.user.following + "");
            LinearLayout followingL = (LinearLayout) view.findViewById(R.id.following_contai);
            followingL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putLong("userid", oud.user.id);
                    FollowingListFragment flf = new FollowingListFragment();
                    flf.setArguments(b);
                    ((NameArtMenu) getActivity()).addNewFragment(flf);
                }
            });

            followers = (TextView) view.findViewById(R.id.followers_count_user_another);
            followers.setText(oud.user.followers + "");
            LinearLayout followerL = (LinearLayout) view.findViewById(R.id.followers_contai);
            followerL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putLong("userid", oud.user.id);
                    FollowersListFragment flf = new FollowersListFragment();
                    flf.setArguments(b);
                    ((NameArtMenu) getActivity()).addNewFragment(flf);
                }
            });

            GridView gd = (GridView) view.findViewById(R.id.gridView_another);
            gd.setAdapter(new MyGridViewAdapter(oud.posts, getActivity()));
            gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OtherUserPosts oup = oud.posts[position];
                    Bundle b = new Bundle();
                    b.putString("name", "" + oud.user.name);
                    b.putLong("postid", oup.id);
                    b.putLong("userid", oud.user.id);
                    SingleUserFeedFrag suff = new SingleUserFeedFrag();
                    suff.setArguments(b);
                    ((NameArtMenu) getActivity()).addNewFragment(suff);
                }
            });
        }
    }

    @Override
    public void onLikeSuccess(boolean success, int index) {
    }

    @Override
    public void onBlockUser(long userId, String response) {
    }

    @Override
    public void onFollowSuccess(boolean isFollowing, long userId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        } else {
            stopAnim();
        }
        if (isFollowing) {
            follow.setBackground(new ColorDrawable(Color.TRANSPARENT));
            follow.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            follow.setText(R.string.following);
            oud.user.followers = oud.user.followers + 1;
            followers.setText(oud.user.followers + "");
        } else {
            follow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.follow_bg));
            follow.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            follow.setText(R.string.follow);
            oud.user.followers = oud.user.followers - 1;
            followers.setText(oud.user.followers + "");
        }
    }

    @Override
    public void onDeletePost(long postId, String response) {
    }

    @Override
    public void onBlockNotification(long postId, String response) {

    }

    @Override
    public void onReadNotification(long notification_id, String response) {

    }

    @Override
    public void onCommentSuccess(long postId) {
    }

    public static class MyGridViewAdapter extends BaseAdapter {
        private OtherUserPosts[] posts;
        private Context context;

        public MyGridViewAdapter(OtherUserPosts[] posts, Context context) {
            this.posts = posts;
            this.context = context;
        }

        @Override
        public int getCount() {
            return posts.length;
        }

        @Override
        public Object getItem(int position) {
            return posts[position].picture;
        }

        @Override
        public long getItemId(int position) {
            return posts[position].id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gridadapter_userpost, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview_gridadapter);
            String baseUrl = WebConstant.getInstance().BASEURL;
            Glide.with(context.getApplicationContext())
                    .load(baseUrl + posts[position].picture)
                    .into(imageView);
            return convertView;
        }
    }
}
