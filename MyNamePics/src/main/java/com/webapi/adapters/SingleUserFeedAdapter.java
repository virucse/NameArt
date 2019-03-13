package com.webapi.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;
import com.webapi.model.OtherUserDetails;
import com.webapi.model.OtherUserPosts;
import com.webapi.fragments.SingleUserFeedFrag;
import com.webapi.model.UserProfile;
import com.webapi.model.WebConstant;
import com.webapi.common.BlockNotificationApi;
import com.webapi.common.CommanProcessingListener;
import com.webapi.common.DeletePostApi;
import com.webapi.common.LikeApi;
import com.webapi.common.MyImageView;
import com.webapi.fragments.CommentFragment;
import com.webapi.fragments.DrawableFragment;
import com.webapi.fragments.LikeListFragment;

import org.json.JSONException;
import org.json.JSONObject;

import circularprogress.customViews.CircularProgressButton;
import circularprogress.customViews.CircularProgressImageButton;
import circularprogress.interfaces.OnCircularProgressButtonClick;

/**
 * Created by caliber fashion on 9/13/2017.
 */

public class SingleUserFeedAdapter extends RecyclerView.Adapter<SingleUserFeedAdapter.MyViewHolder> implements CommanProcessingListener {
    public OtherUserDetails mFeedDetails;
    private Activity mActivity;
    private long currentUserId;
    private MyViewHolder currentView;
    private int currentPos;
    private RecyclerView mRecyclerView;
    private long userId;
    private long deletedPostId;
    //private IjkWrapper ijkWrapper = new IjkWrapper();

    public SingleUserFeedAdapter(OtherUserDetails feed, Activity context) {
        mFeedDetails = feed;
        mActivity = context;
        currentUserId = UserProfile.getInstance().getUserIdValue();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedadapter, parent, false);
        mRecyclerView = (RecyclerView) parent;
        //ijkWrapper.init();
        return new MyViewHolder(view);
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OtherUserPosts feedPost = mFeedDetails.posts[position];
        if (feedPost.id == deletedPostId) {
            holder.rootView.setVisibility(View.GONE);
        } else {
            holder.rootView.setVisibility(View.VISIBLE);
        }

        holder.username.setText(mFeedDetails.user.name + "");
        holder.time_userpost.setText(feedPost.time + "");

        holder.likeTv.setText(feedPost.like + " " + mActivity.getString(R.string.likes));
        holder.likeTv.setTag("" + position);
        holder.likeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag() + "");
                OtherUserPosts fp = mFeedDetails.posts[pos];
                Bundle b = new Bundle();
                b.putLong("postid", fp.id);
                LikeListFragment lif = new LikeListFragment();
                lif.setArguments(b);
                ((NameArtMenu) mActivity).addNewFragment(lif);
            }
        });

        holder.commentsTv.setText(feedPost.getCommentCount() + " " + mActivity.getString(R.string.comments));
        holder.commentsTv.setTag("" + position);
        holder.commentsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag() + "");
                OtherUserPosts fp = mFeedDetails.posts[pos];
                Bundle b = new Bundle();
                b.putLong("postid", fp.id);
                CommentFragment cf = new CommentFragment();
                cf.setListener(SingleUserFeedAdapter.this);
                cf.setArguments(b);
                ((NameArtMenu) mActivity).addNewFragment(cf);
            }
        });
        if (feedPost.picture.endsWith(".mp4")){
            //ijkWrapper.openRemoteFile(WebConstant.getInstance().BASEURL + feedPost.picture);
            //ijkWrapper.prepare();
        }

        Glide.with(mActivity.getApplicationContext()).load(WebConstant.getInstance().BASEURL + feedPost.picture).into(holder.userPostImage);
        holder.userPostImage.setPosition(position);
        holder.userPostImage.setMyClickListener(new MyImageView.ClickListener() {
            @Override
            public void onClick(View v, int position) {
                String url = WebConstant.getInstance().BASEURL + mFeedDetails.posts[position].picture;
                Bundle b = new Bundle();
                b.putString("imageurl", url);
                DrawableFragment df = new DrawableFragment();
                df.setArguments(b);
                ((NameArtMenu) mActivity).addNewFragment(df);
            }
        });

        Glide.with(mActivity.getApplicationContext()).load(WebConstant.getInstance().BASEURL + mFeedDetails.user.picture)
                .apply(new RequestOptions() .placeholder(R.mipmap.self_share)).into(holder.userDp);

        if (feedPost.is_like) {
            holder.likeThumb.setImageResource(R.mipmap.liked);
        } else {
            holder.likeThumb.setImageResource(R.mipmap.like);
        }
        holder.likeThumb.setPosition(position);
        holder.likeThumb.addOnClickListener(new OnCircularProgressButtonClick() {
            @Override
            public void onClickImageButton(CircularProgressImageButton button, int index) {
                OtherUserPosts fp = mFeedDetails.posts[index];
                button.startAnimation();
                new LikeApi(mActivity, fp.id, button, index, SingleUserFeedAdapter.this);
                super.onClickImageButton(button, index);
            }
        });

        holder.text.setText(feedPost.text + "");
        holder.follow.setVisibility(View.INVISIBLE);
        if (userId == currentUserId) {
            holder.optionBtn.setVisibility(View.VISIBLE);
        } else {
            holder.optionBtn.setVisibility(View.INVISIBLE);
        }
        holder.optionBtn.setTag("" + position);
        holder.optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos = Integer.parseInt(holder.optionBtn.getTag() + "");
                final PopupMenu mPopUpMenu = new PopupMenu(mActivity, holder.optionBtn);
                mPopUpMenu.getMenuInflater().inflate(R.menu.single_user_feed_menu, mPopUpMenu.getMenu());
                mPopUpMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.action_delete_post:
                                postDeleteConfirmation(mFeedDetails.posts[currentPos].id);
                                break;
                            case R.id.action_block_notification:
                               // blockNotification(mFeedDetails.posts[currentPos].id);
                                new BlockNotificationApi(mActivity ,mFeedDetails.posts[currentPos].id ,SingleUserFeedAdapter.this);
                                break;

                        }
                        return false;
                    }
                });
                mPopUpMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (mFeedDetails.posts != null) ? mFeedDetails.posts.length : 0;
    }

    private void postDeleteConfirmation(final long postId) {
        final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
        alertDialog.setMessage(mActivity.getString(R.string.are_you_sure_to_delete_post) + "");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mActivity.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                SingleUserFeedFrag.STARTANIM();
                new DeletePostApi(mActivity, postId, SingleUserFeedAdapter.this);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mActivity.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onLikeSuccess(boolean success, int index) {
        OtherUserPosts fp = mFeedDetails.posts[index];
        if (success) {
            fp.is_like = true;
            fp.like = fp.like + 1;
        } else {
            fp.is_like = false;
            fp.like = fp.like - 1;
        }
        mFeedDetails.posts[index] = fp;
        if (mRecyclerView != null) {
            final MyViewHolder mvh = (MyViewHolder) mRecyclerView.findViewHolderForAdapterPosition(index);
            if (mvh != null) {
                if (success) {
                    mvh.likeThumb.setImageResource(R.mipmap.liked);
                } else {
                    mvh.likeThumb.setImageResource(R.mipmap.like);
                }
                mvh.likeThumb.invalidate();
                mvh.likeTv.setText(mFeedDetails.posts[index].like + " " + mActivity.getString(R.string.likes));
                notifyDataSetChanged();
            }
        } else {
            notifyItemChanged(index);
        }
    }

    @Override
    public void onFollowSuccess(boolean isFollowing, long userId) {

    }

    @Override
    public void onBlockUser(long userId, String response) {
    }

    @Override
    public void onDeletePost(long post_Id, String response) {
        SingleUserFeedFrag.STOPANIM();
        deletedPostId = -1;
        if (response != null && !response.isEmpty()) {
            try {
                JSONObject jo = new JSONObject(response);
                if (jo.getString("status").equals("success")) {
                    deletedPostId = post_Id;
                    notifyItemChanged(currentPos);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBlockNotification(long postId, String response) {
        SingleUserFeedFrag.STOPANIM();
        final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
        if (response != null && !response.isEmpty()) {
            try {
                JSONObject jo = new JSONObject(response);
                if (jo.getString("status").equals("success")){
                    notifyItemChanged(currentPos);
                }
                if (jo.getBoolean("is_block")) {
                    alertDialog.setMessage("Notification is turn off for this post");
                } else {
                    alertDialog.setMessage("Notification is turn on for this post");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();

            }
        });
        alertDialog.show();
    }

    @Override
    public void onReadNotification(long notification_id, String response) {

    }

    @Override
    public void onCommentSuccess(long postId) {
        for (int i = 0; i < mFeedDetails.posts.length; i++) {
            if (mFeedDetails.posts[i].id == postId) {
                int count = mFeedDetails.posts[i].getCommentCount();
                count++;
                mFeedDetails.posts[i].setCommentCount(count);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView userDp;
        public MyImageView userPostImage;
        public TextureView videoView;
        public TextView username, likeTv, commentsTv, text, hashtag, time_userpost;
        public CircularProgressImageButton likeThumb;
        public CircularProgressButton follow;
        public LinearLayout userNameDp;
        public ImageButton optionBtn;
        private View rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            userDp = (ImageView) itemView.findViewById(R.id.user_dp);
            videoView = (TextureView)itemView.findViewById(R.id.video_view);
            userPostImage = (MyImageView) itemView.findViewById(R.id.postimage);
            int width = AppUtils.screenWidth - (AppUtils.dpToPx(mActivity, 2 * 10));
            int height = AppUtils.screenWidth - (AppUtils.dpToPx(mActivity, 2 * 5));
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) userPostImage.getLayoutParams();
            lp.width = width;
            lp.height = height;
            userPostImage.setLayoutParams(lp);
            username = (TextView) itemView.findViewById(R.id.user_name);
            time_userpost = (TextView) itemView.findViewById(R.id.time_userpost);
            follow = (CircularProgressButton) itemView.findViewById(R.id.follow);
            commentsTv = (TextView) itemView.findViewById(R.id.comments_count);
            likeTv = (TextView) itemView.findViewById(R.id.like_count);
            likeThumb = (CircularProgressImageButton) itemView.findViewById(R.id.likethumb);
            userNameDp = (LinearLayout) itemView.findViewById(R.id.user_name_dp);
            text = (TextView) itemView.findViewById(R.id.text_feedada);
            hashtag = (TextView) itemView.findViewById(R.id.hashtag_feedada);
            optionBtn = (ImageButton) itemView.findViewById(R.id.feed_option_btn);
            videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    //ijkWrapper.setSurface(new Surface(surface));
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    //ijkWrapper.setSurface(null);
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            });
        }
    }
}
