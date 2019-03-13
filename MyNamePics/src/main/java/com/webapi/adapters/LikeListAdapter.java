package com.webapi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.BaseFragment;
import com.webapi.fragments.UserFragment;
import com.webapi.model.UserProfile;
import com.webapi.model.WebConstant;
import com.webapi.common.CommanProcessingListener;
import com.webapi.common.FollowApi;
import com.webapi.fragments.OtherUserFragments;
import com.webapi.model.LikeListDetail;
import com.webapi.model.LikeTemp;

import circularprogress.customViews.CircularProgressButton;
import circularprogress.interfaces.OnCircularProgressButtonClick;

/**
 * Created by caliber fashion on 9/11/2017.
 */

public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.MyHolder> implements CommanProcessingListener {
    private LikeListDetail likeListDetail;
    private long currentUserId;

    public LikeListAdapter(LikeListDetail lid) {
        likeListDetail = lid;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likelistfragment_item, parent, false);
        currentUserId = UserProfile.getInstance().getUserIdValue();
        return new MyHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        final LikeTemp lt = likeListDetail.users[position];
        holder.followers.setText(lt.user.followers + " " + holder.context.getString(R.string.followers));
        holder.name.setText(lt.user.name + "");
        Glide.with(holder.context).load(WebConstant.getInstance().BASEURL + lt.user.picture).into(holder.dp);
        if (lt.user.id == currentUserId) {
            holder.follow.setVisibility(View.INVISIBLE);
        } else {
            if (lt.user.is_follow) {
                holder.follow.setBackground(new ColorDrawable(Color.TRANSPARENT));
                holder.follow.setTextColor(ContextCompat.getColor(holder.context, R.color.black));
                holder.follow.setText(holder.context.getString(R.string.following));
            } else {
                holder.follow.setBackground(ContextCompat.getDrawable(holder.context, R.drawable.follow_bg));
                holder.follow.setTextColor(ContextCompat.getColor(holder.context, R.color.white));
                holder.follow.setText(holder.context.getString(R.string.follow));
            }
        }
        holder.follow.setPosition(position);
        holder.follow.addOnClickListener(new OnCircularProgressButtonClick() {
            @Override
            public void onClickButton(CircularProgressButton button, int index) {
                super.onClickButton(button, index);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    button.startAnimation();
                } else {
                }
                new FollowApi(holder.context, lt.user.id, button, LikeListAdapter.this);
            }
        });
        holder.userContainer.setTag("" + position);
        holder.userContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag() + "");
                long id = likeListDetail.users[pos].user.id;
                if (id == currentUserId) {
                    UserFragment uf = new UserFragment();
                    ((NameArtMenu) holder.context).addNewFragment(uf);
                } else {
                    Bundle b = new Bundle();
                    b.putString("name", "" + likeListDetail.users[pos].user.name);
                    b.putLong("userid", id);
                    BaseFragment f = new OtherUserFragments();
                    f.setArguments(b);
                    ((NameArtMenu) holder.context).addNewFragment(f);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return likeListDetail.users != null ? likeListDetail.users.length : 0;
    }

    @Override
    public void onLikeSuccess(boolean success, int index) {
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

    }

    @Override
    public void onFollowSuccess(boolean isFollowing, long userId) {
        for (int i = 0; i < likeListDetail.users.length; i++) {
            LikeTemp lt = likeListDetail.users[i];
            if (lt.user.id == userId) {
                lt.user.is_follow = isFollowing;
                likeListDetail.users[i] = lt;
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public void onCommentSuccess(long postId) {
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public Context context;
        public ImageView dp;
        public TextView name, followers;
        public CircularProgressButton follow;
        public LinearLayout userContainer;

        public MyHolder(View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            dp = (ImageView) itemView.findViewById(R.id.userdp);
            name = (TextView) itemView.findViewById(R.id.username);
            followers = (TextView) itemView.findViewById(R.id.followers);
            follow = (CircularProgressButton) itemView.findViewById(R.id.follow);
            userContainer = (LinearLayout) itemView.findViewById(R.id.user_layout);
        }
    }
}
