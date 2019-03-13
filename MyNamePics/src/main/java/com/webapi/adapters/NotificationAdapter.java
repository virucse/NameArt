package com.webapi.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.webapi.model.NotificationData;
import com.webapi.model.NotificationDetail;
import com.webapi.fragments.SingleUserFeedFrag;
import com.webapi.model.UserProfile;
import com.webapi.common.CommanProcessingListener;
import com.webapi.common.ReadNotificationApi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ROSHAN on 1/2/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> implements CommanProcessingListener {
    public NotificationDetail nd;
    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private UserProfile userProfile;
    private boolean isRead=false;
    public NotificationAdapter(NotificationDetail feed, UserProfile up, Activity activi) {
        nd = feed;
        mActivity = activi;
        userProfile = up;

    }

    public NotificationAdapter setRecyclerView(RecyclerView ff) {
        mRecyclerView = ff;
        return this;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationadapter, parent, false);
        return new MyViewHolder(view, parent.getContext().getApplicationContext());
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final int notread = Color.parseColor("#dbdada");
        final int read = Color.parseColor("#ffffff");
        final NotificationData nData = nd.notification[position];
        new ReadNotificationApi(mActivity, nData.id ,NotificationAdapter.this);
        holder.user_name.setText(nData.username);
        if (nData.type.equals("post")){
            holder.postDetails.setText("posted in Name Art");
        }else if (nData.type.equals("comment")){
            holder.postDetails.setText("commented on your post");
        }
      /*  if (isRead){
            holder.userNameDp.setBackgroundColor(read);
        }else {
            holder.userNameDp.setBackgroundColor(notread);
        }*/
        holder.userDp.setImageResource(R.mipmap.icon_72);

        holder.userNameDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (isRead){
                    holder.userNameDp.setBackgroundColor(read);
                }else {
                    holder.userNameDp.setBackgroundColor(notread);
                }
                if (nData.type.equals("post")){
                    openUserPost(nData);
                }else if (nData.type.equals("comment")){
                    openUserComment(nData);
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return nd.notification.length;
    }

    public void openUserComment(NotificationData notificationData){
        Bundle bundle = new Bundle();
        bundle.putString("name", userProfile.getUserName());
        bundle.putLong("userid", userProfile.getUserIdValue());
        bundle.putLong("postid", notificationData.post_id);
        SingleUserFeedFrag suff = new SingleUserFeedFrag();
        suff.setArguments(bundle);
        ((NameArtMenu) mActivity).addNewFragment(suff);
    }

    public void openUserPost(NotificationData notificationData){
        Bundle bundle = new Bundle();
        bundle.putString("name", notificationData.username);
        bundle.putLong("userid", notificationData.user_id);
        bundle.putLong("postid", notificationData.post_id);
        SingleUserFeedFrag suff = new SingleUserFeedFrag();
        suff.setArguments(bundle);
        ((NameArtMenu) mActivity).addNewFragment(suff);
    }

    @Override
    public void onLikeSuccess(boolean success, int index) {

    }

    @Override
    public void onFollowSuccess(boolean isFollowing, long userId) {

    }

    @Override
    public void onCommentSuccess(long postId) {

    }

    @Override
    public void onBlockUser(long userId, String response) {

    }

    @Override
    public void onDeletePost(long postId, String response) {

    }

    @Override
    public void onBlockNotification(long postId, String response) {

    }

    @Override
    public void onReadNotification(long notification_id, String response) {
        if (response!=null && !response.isEmpty()){

            try {
                JSONObject jo = new JSONObject(response);
                if (jo.getString("status").equals("success")){
                    isRead=true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public Context context;
        public ImageView userDp;
        public TextView user_name,postDetails , time_userPost;
        public LinearLayout userNameDp;

        public MyViewHolder(View itemView , Context ctx) {
            super(itemView);
            rootView = itemView;
            context = ctx;
            userDp = itemView.findViewById(R.id.user_dp);
            postDetails = itemView.findViewById(R.id.post_details);
            time_userPost = itemView.findViewById(R.id.time_userpost);
            userNameDp = itemView.findViewById(R.id.user_name_dp);
            user_name = itemView.findViewById(R.id.user_name);
        }
    }
}
