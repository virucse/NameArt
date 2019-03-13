package com.webapi.model;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.formationapps.nameart.App;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.fragments.CompleteProfileFragment;
import com.webapi.fragments.FollowersListFragment;
import com.webapi.fragments.FollowingListFragment;
import com.webapi.fragments.SingleUserFeedFrag;

import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/1/2017.
 */

public class UserViewHelper {
    UserDetail ud = null;
    private Activity mActivity;
    private UserActionResolver mUserActionResolver;

    public void displayUserView(final Activity activity, final View view, final UserActionResolver resolver) {
        mActivity = activity;
        mUserActionResolver = resolver;
        String url = WebConstant.getInstance().SELFTVIEW;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(activity, url, new Header[]{new MyHeader(activity)}, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                UserViewHelper.this.onResponse(activity, view, resolver, new String(responseBody).toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        ImageView invite = (ImageView) view.findViewById(R.id.inviteFriends);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = WebConstant.getInstance().COMPLETEPROFILE;
                RequestParams param = new RequestParams();
                param.put("picture", "");
                param.put("dob", "01/03/1990");
                param.put("phone", "8281972");
                param.put("country", "India");
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(activity, url, new Header[]{new MyHeader(activity)}, param, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
    }

    private void onResponse(final Activity activity, View view, UserActionResolver resolver, String response) {
        try {
            ud = new Gson().fromJson(response, UserDetail.class);
        } catch (Exception e) {

        }
        if (ud != null) {
            UserProfile up = UserProfile.getInstance();
            up.setUserName(ud.name);
            up.setUserPicture(ud.thumb);

            RelativeLayout userDPContainer = (RelativeLayout) view.findViewById(R.id.userdpLayout);
            userDPContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CompleteProfileFragment cpf = new CompleteProfileFragment();
                    ((NameArtMenu) activity).addNewFragment(cpf);
                }
            });

            TextView name = (TextView) view.findViewById(R.id.userName);
            name.setText(ud.name);

            ImageView userDP = (ImageView) view.findViewById(R.id.img_user_profile);
            Glide.with(activity.getApplicationContext()).load(WebConstant.getInstance().BASEURL + ud.thumb)
                   .apply(new RequestOptions() .placeholder(R.mipmap.icon_72)).into(userDP);

            TextView postCount = (TextView) view.findViewById(R.id.post_count_user);
            postCount.setText(ud.postcount + "");
            TextView followers = (TextView) view.findViewById(R.id.followers_count_user);
            followers.setText(ud.followers + "");

            LinearLayout followerL = (LinearLayout) view.findViewById(R.id.follower_layout);
            followerL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putLong("userid", ud.user_id);
                    FollowersListFragment flf = new FollowersListFragment();
                    flf.setArguments(b);
                    ((NameArtMenu) activity).addNewFragment(flf);
                }
            });

            TextView following = (TextView) view.findViewById(R.id.following_count_user);
            following.setText(ud.following + "");

            LinearLayout followingL = (LinearLayout) view.findViewById(R.id.following_layout);
            followingL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putLong("userid", ud.user_id);
                    FollowingListFragment flf = new FollowingListFragment();
                    flf.setArguments(b);
                    ((NameArtMenu) activity).addNewFragment(flf);
                }
            });
            GridView gd = (GridView) view.findViewById(R.id.gridView_user_post);
            gd.setAdapter(new MyGridViewAdapter(ud.posts, activity));
            gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle b = new Bundle();
                    b.putString("name", "" + ud.name);
                    b.putLong("postid", ud.posts[position].id);
                    b.putLong("userid", ud.user_id);
                    SingleUserFeedFrag suff = new SingleUserFeedFrag();
                    suff.setArguments(b);
                    ((NameArtMenu) activity).addNewFragment(suff);
                }
            });
        } else {
            Toast.makeText(App.getInstance(), "Error Occured", Toast.LENGTH_SHORT).show();
        }

    }

    public static class MyGridViewAdapter extends BaseAdapter {
        private UserPost[] posts;
        private Context context;

        public MyGridViewAdapter(UserPost[] posts, Context context) {
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
                    .apply(new RequestOptions().placeholder(R.mipmap.icon_72))
                    .into(imageView);
            return convertView;
        }
    }
}
