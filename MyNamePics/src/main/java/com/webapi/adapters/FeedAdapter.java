package com.webapi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.BaseFragment;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.UnifiedNativeAdViewHolder;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.FeedDetails;
import com.webapi.model.FeedPost;
import com.webapi.model.MyHeader;
import com.webapi.fragments.SingleUserFeedFrag;
import com.webapi.fragments.UserFragment;
import com.webapi.model.UserProfile;
import com.webapi.model.WebConstant;
import com.webapi.common.BlockNotificationApi;
import com.webapi.common.BlockUserApi;
import com.webapi.common.CommanProcessingListener;
import com.webapi.common.FollowApi;
import com.webapi.common.LikeApi;
import com.webapi.common.MyImageView;
import com.webapi.fragments.CommentFragment;
import com.webapi.fragments.DrawableFragment;
import com.webapi.fragments.FeedFragment;
import com.webapi.fragments.LikeListFragment;
import com.webapi.fragments.OtherUserFragments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import circularprogress.customViews.CircularProgressButton;
import circularprogress.customViews.CircularProgressImageButton;
import circularprogress.interfaces.OnCircularProgressButtonClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/5/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CommanProcessingListener {

    public FeedDetails mFeedDetails;
    private Activity mActivity;
    private long currentUserId;
    private int currentClickedPos;
    private RecyclerView mRecyclerView;
    private long blockedUser;
    private boolean requestInProcess = false;
    private static final int UNIFIED_NATIVE_AD_TYPE=0;
    private static final int FEED_TYPE=1;

    public FeedAdapter(FeedDetails feed, Activity activi) {
        mFeedDetails = feed;
        mActivity = activi;
        if (BuildConfig.DEBUG)
            Toast.makeText(mActivity, "Next:" + mFeedDetails.is_next + " page:" + mFeedDetails.page, Toast.LENGTH_LONG).show();
    }
    private List<Object> mPostList;
    public FeedAdapter(FeedDetails feed, Activity activity, List<Object> postList){
        mFeedDetails=feed;
        mActivity=activity;
        mPostList=postList;
    }

    public FeedAdapter setRecyclerView(RecyclerView ff) {
        mRecyclerView = ff;
        return this;
    }
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    public void setNativeAds(List<UnifiedNativeAd> ads){
        mNativeAds.clear();
        mNativeAds.addAll(ads);

        /*if (mNativeAds.size() <= 0) {
        }else if(mPostList!=null&&mPostList.size()>0){
            int offset = (mPostList.size() / mNativeAds.size())+1;
            int index = 1;
            for (UnifiedNativeAd ad : mNativeAds) {
                mPostList.add(index, ad);
                notifyItemInserted(index);
                index = index + offset;
            }
            //notifyDataSetChanged();
        }*/
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        currentUserId = UserProfile.getInstance().getUserIdValue();
        switch (viewType) {
            case UNIFIED_NATIVE_AD_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.ad_unified,
                        parent, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);

            default:
                View myholderView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.feedadapter, parent, false);
                return new MyViewHolder(myholderView,parent.getContext());
        }
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedadapter, parent, false);
        //return new MyViewHolder(view, parent.getContext().getApplicationContext());
    }

    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {

        if(mPostList!=null&&mPostList.size()>0){
            Object recyclerViewItem = mPostList.get(position);
            if (recyclerViewItem instanceof UnifiedNativeAd) {
                return UNIFIED_NATIVE_AD_TYPE;
            }
            return FEED_TYPE;
        }else {
            return FEED_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        MyViewHolder holder;
        FeedPost feedPost=null;
        if(mPostList!=null&&mPostList.size()>0){
            int viewType=getItemViewType(position);
            switch (viewType){
                case UNIFIED_NATIVE_AD_TYPE:
                    UnifiedNativeAd nativeAd= (UnifiedNativeAd) mPostList.get(position);
                    populateNativeAdView(nativeAd,((UnifiedNativeAdViewHolder)holder1).getAdView());
                    break;
                default:
                    holder= (MyViewHolder) holder1;
                    feedPost = (FeedPost) mPostList.get(position);
                    loadIndivisualFeed(feedPost,holder,position);

            }//end switch
        }else {
            holder= (MyViewHolder) holder1;
            feedPost = mFeedDetails.posts.get(position);
            loadIndivisualFeed(feedPost,holder,position);
        }
    }
    private void loadIndivisualFeed(FeedPost feedPost,final MyViewHolder holder,int position){
        if (feedPost.user.id == blockedUser) {
            holder.rootView.setVisibility(View.GONE);
        } else {
            holder.rootView.setVisibility(View.VISIBLE);
        }
        holder.username.setText(feedPost.user.name );
        holder.time_userPost.setText(feedPost.time);

        holder.likeTv.setText(feedPost.like + " " + holder.context.getString(R.string.likes));
        holder.likeTv.setTag("" + position);
        holder.likeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentClickedPos = Integer.parseInt(v.getTag() + "");
                FeedPost fp;
                if(mPostList!=null&&mPostList.size()>0){
                    fp= (FeedPost) mPostList.get(currentClickedPos);
                }else {
                    fp = mFeedDetails.posts.get(currentClickedPos);
                }
                Bundle b = new Bundle();
                b.putLong("postid", fp.id);
                LikeListFragment lif = new LikeListFragment();
                lif.setArguments(b);
                ((NameArtMenu) mActivity).addNewFragment(lif);
            }
        });

        holder.commentsTv.setText(feedPost.comments.count + " " + holder.context.getString(R.string.comments));
        holder.commentsTv.setTag("" + position);
        holder.commentsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentClickedPos = Integer.parseInt(v.getTag() + "");
                FeedPost fp;
                if(mPostList!=null&&mPostList.size()>0){
                    fp= (FeedPost) mPostList.get(currentClickedPos);
                }else {
                    fp = mFeedDetails.posts.get(currentClickedPos);
                }
                Bundle b = new Bundle();
                b.putLong("postid", fp.id);
                CommentFragment cf = new CommentFragment();
                cf.setListener(FeedAdapter.this);
                cf.setArguments(b);
                ((NameArtMenu) mActivity).addNewFragment(cf);
            }
        });

        Glide.with(holder.context).load(WebConstant.getInstance().BASEURL + feedPost.picture).into(holder.userPostImage);
        holder.userPostImage.setPosition(position);
        holder.userPostImage.setMyClickListener(new MyImageView.ClickListener() {
            @Override
            public void onClick(View v, int position) {
                currentClickedPos = position;
                String url ;
                if(mPostList!=null&&mPostList.size()>0){
                    url = WebConstant.getInstance().BASEURL + ((FeedPost)mPostList.get(currentClickedPos)).picture;
                }else {
                    url = WebConstant.getInstance().BASEURL + mFeedDetails.posts.get(currentClickedPos).picture;
                }
                Bundle b = new Bundle();
                b.putString("imageurl", url);
                DrawableFragment df = new DrawableFragment();
                df.setArguments(b);
                ((NameArtMenu) mActivity).addNewFragment(df);
            }
        });
        Glide.with(holder.context).load(WebConstant.getInstance().BASEURL + feedPost.user.picture).apply(new RequestOptions() .placeholder(R.mipmap.self_share))
                .into(holder.userDp);

        holder.userNameDp.setTag(position + "");
        holder.userNameDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentClickedPos = Integer.parseInt(v.getTag() + "");
                //FeedPost feedPost = mFeedDetails.posts.get(currentClickedPos);
                FeedPost fp;
                if(mPostList!=null&&mPostList.size()>0){
                    fp= (FeedPost) mPostList.get(currentClickedPos);
                }else {
                    fp = mFeedDetails.posts.get(currentClickedPos);
                }
                if (fp.user.id == UserProfile.getInstance().getUserIdValue()) {
                    UserFragment uf = new UserFragment();
                    ((NameArtMenu) mActivity).addNewFragment(uf);
                } else {
                    Bundle b = new Bundle();
                    b.putString("name", "" + fp.user.name);
                    b.putLong("userid", fp.user.id);
                    BaseFragment f = new OtherUserFragments();
                    f.setArguments(b);
                    ((NameArtMenu) mActivity).addNewFragment(f);
                }
            }
        });
        if (feedPost.is_like) {
            holder.likeThumb.setImageResource(R.mipmap.liked);
        } else {
            holder.likeThumb.setImageResource(R.mipmap.like);
        }
        holder.likeThumb.setPosition(position);
        holder.likeThumb.addOnClickListener(new OnCircularProgressButtonClick() {
            @Override
            public void onClickImageButton(CircularProgressImageButton button, int index) {
                super.onClickImageButton(button, index);
                currentClickedPos = index;
                FeedPost fp;
                if(mPostList!=null&&mPostList.size()>0){
                    fp= (FeedPost) mPostList.get(currentClickedPos);
                }else {
                    fp = mFeedDetails.posts.get(currentClickedPos);
                }
                button.startAnimation();
                new LikeApi(holder.context, fp.id, button, index, FeedAdapter.this);
            }
        });
        if (feedPost.user.id == currentUserId) {
            holder.follow.setVisibility(View.INVISIBLE);
            holder.optionItem.setVisibility(View.INVISIBLE);
        } else {
            holder.follow.setVisibility(View.VISIBLE);
            holder.optionItem.setVisibility(View.VISIBLE);
            if (feedPost.user.is_follow) {
                holder.follow.setBackground(new ColorDrawable(Color.TRANSPARENT));
                holder.follow.setTextColor(ContextCompat.getColor(holder.context, R.color.black));
                holder.follow.setText(R.string.following);
            } else {
                holder.follow.setBackground(ContextCompat.getDrawable(holder.context, R.drawable.follow_bg));
                holder.follow.setTextColor(ContextCompat.getColor(holder.context, R.color.white));
                holder.follow.setText(R.string.follow);
            }
        }
        holder.follow.setPosition(position);
        holder.follow.addOnClickListener(new OnCircularProgressButtonClick() {
            @Override
            public void onClickButton(CircularProgressButton button, int index) {
                super.onClickButton(button, index);
                currentClickedPos = index;
                FeedPost fp;
                if(mPostList!=null&&mPostList.size()>0){
                    fp= (FeedPost) mPostList.get(currentClickedPos);
                }else {
                    fp = mFeedDetails.posts.get(currentClickedPos);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    button.startAnimation();
                } else {
                    FeedFragment.STARTANIM();
                }
                new FollowApi(holder.context, fp.user.id, button, FeedAdapter.this);
            }
        });

        holder.text.setText(feedPost.text + "");
        holder.optionItem.setTag("" + position);
        holder.optionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentClickedPos = Integer.parseInt(holder.optionItem.getTag() + "");
                final FeedPost fp;
                if(mPostList!=null&&mPostList.size()>0){
                    fp= (FeedPost) mPostList.get(currentClickedPos);
                }else {
                    fp = mFeedDetails.posts.get(currentClickedPos);
                }
                final long userId = fp.user.id;
                PopupMenu mPopUpMenu = new PopupMenu(mActivity, holder.optionItem);
                mPopUpMenu.getMenuInflater().inflate(R.menu.fedd_option_menu, mPopUpMenu.getMenu());
                mPopUpMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.action_block_user:
                                userBlockConfirmation(userId);
                                break;
                            case R.id.action_block_notification:
                                // blockNotification(mFeedDetails.posts[currentPos].id);
                                new BlockNotificationApi(mActivity , fp.id ,FeedAdapter.this);
                                break;
                        }
                        return false;
                    }
                });
                mPopUpMenu.show();
            }
        });
    }

    public void onScrollViewEnd() {
        if (BuildConfig.DEBUG) {
            Toast.makeText(mActivity, "LAst", Toast.LENGTH_LONG).show();
        }
        if (mFeedDetails.is_next && !requestInProcess) {
            callPostRequestAgain();
        }
    }

    public void onScrollViewStart() {
        if (BuildConfig.DEBUG) {
            Toast.makeText(mActivity, "start", Toast.LENGTH_LONG).show();
        }
    }

    private void userBlockConfirmation(final long userId) {
        final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
        alertDialog.setMessage(mActivity.getString(R.string.are_you_sure_to_block_user) + "");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mActivity.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                new BlockUserApi(mActivity, userId, FeedAdapter.this);
                FeedFragment.STARTANIM();
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
    public int getItemCount() {
        return mPostList!=null?mPostList.size():mFeedDetails.posts.size();
    }

    @Override//done
    public void onLikeSuccess(final boolean liked, int index) {
        if(mPostList!=null&&mPostList.size()>0){
            if (liked) {
                ((FeedPost)mPostList.get(index)).is_like = true;
                ((FeedPost)mPostList.get(index)).like = ((FeedPost)mPostList.get(index)).like + 1;
            } else {
                ((FeedPost)mPostList.get(index)).is_like = false;
                ((FeedPost)mPostList.get(index)).like = ((FeedPost)mPostList.get(index)).like - 1;
            }
            if (mRecyclerView != null) {
                final MyViewHolder mvh = (MyViewHolder) mRecyclerView.findViewHolderForAdapterPosition(index);
                if (mvh != null) {
                    if (liked) {
                        mvh.likeThumb.setImageResource(R.mipmap.liked);
                    } else {
                        mvh.likeThumb.setImageResource(R.mipmap.like);
                    }
                    mvh.likeThumb.invalidate();
                    mvh.likeTv.setText(((FeedPost)mPostList.get(index)).like + " " + mvh.context.getString(R.string.likes));
                    notifyDataSetChanged();
                }
            } else {
                notifyItemChanged(index);
            }
        }else {
            if (liked) {
                mFeedDetails.posts.get(index).is_like = true;
                mFeedDetails.posts.get(index).like = mFeedDetails.posts.get(index).like + 1;
            } else {
                mFeedDetails.posts.get(index).is_like = false;
                mFeedDetails.posts.get(index).like = mFeedDetails.posts.get(index).like - 1;
            }
            if (mRecyclerView != null) {
                final MyViewHolder mvh = (MyViewHolder) mRecyclerView.findViewHolderForAdapterPosition(index);
                if (mvh != null) {
                    if (liked) {
                        mvh.likeThumb.setImageResource(R.mipmap.liked);
                    } else {
                        mvh.likeThumb.setImageResource(R.mipmap.like);
                    }
                    mvh.likeThumb.invalidate();
                    mvh.likeTv.setText(mFeedDetails.posts.get(index).like + " " + mvh.context.getString(R.string.likes));
                    notifyDataSetChanged();
                }
            } else {
                notifyItemChanged(index);
            }
        }

    }

    @Override//done
    public void onFollowSuccess(boolean isFollowing, long userId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        } else {
            FeedFragment.STOPANIM();
        }
        if(mPostList!=null&&mPostList.size()>0){
            for (int i = 0; i < mPostList.size(); i++) {
                if(mPostList.get(i) instanceof UnifiedNativeAdView){
                   // continue;
                }else{
                    if (((FeedPost)mPostList.get(i)).user.id == userId) {
                        if (isFollowing) {
                            ((FeedPost)mPostList.get(i)).user.is_follow = true;
                        } else {
                            ((FeedPost)mPostList.get(i)).user.is_follow = false;
                        }
                        final MyViewHolder mvh = (MyViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
                        if (mvh != null) {
                            if (isFollowing) {
                                mvh.follow.setBackground(new ColorDrawable(Color.TRANSPARENT));
                                mvh.follow.setTextColor(ContextCompat.getColor(mvh.context, R.color.black));
                                mvh.follow.setText(R.string.following);
                            } else {
                                mvh.follow.setBackground(ContextCompat.getDrawable(mvh.context, R.drawable.follow_bg));
                                mvh.follow.setTextColor(ContextCompat.getColor(mvh.context, R.color.white));
                                mvh.follow.setText(R.string.follow);
                            }
                        }
                        notifyDataSetChanged();
                    }
                }
            }
        }else {
            for (int i = 0; i < mFeedDetails.posts.size(); i++) {
                if (mFeedDetails.posts.get(i).user.id == userId) {
                    if (isFollowing) {
                        mFeedDetails.posts.get(i).user.is_follow = true;
                    } else {
                        mFeedDetails.posts.get(i).user.is_follow = false;
                    }
                    final MyViewHolder mvh = (MyViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
                    if (mvh != null) {
                        if (isFollowing) {
                            mvh.follow.setBackground(new ColorDrawable(Color.TRANSPARENT));
                            mvh.follow.setTextColor(ContextCompat.getColor(mvh.context, R.color.black));
                            mvh.follow.setText(R.string.following);
                        } else {
                            mvh.follow.setBackground(ContextCompat.getDrawable(mvh.context, R.drawable.follow_bg));
                            mvh.follow.setTextColor(ContextCompat.getColor(mvh.context, R.color.white));
                            mvh.follow.setText(R.string.follow);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        }

    }

    @Override//done
    public void onCommentSuccess(long postId) {
        if(mPostList!=null&&mPostList.size()>0){
            for (int i = 0; i < mPostList.size(); i++) {
                if(mPostList.get(i) instanceof UnifiedNativeAdView){
                    //continue;
                }else {
                    if (((FeedPost)mPostList.get(i)).id == postId) {
                        ((FeedPost)mPostList.get(i)).comments.count++;
                        notifyDataSetChanged();
                        break;
                    }
                }

            }
        }else {
            for (int i = 0; i < mFeedDetails.posts.size(); i++) {
                if (mFeedDetails.posts.get(i).id == postId) {
                    mFeedDetails.posts.get(i).comments.count++;
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void onBlockUser(long userId, String response) {
        FeedFragment.STOPANIM();
        blockedUser = -1;
        if (response != null && !response.isEmpty()) {
            try {
                JSONObject jo = new JSONObject(response);
                if (jo.getString("status").equals("success")) {
                    boolean isBlocked = jo.getBoolean("is_blocked");
                    if (isBlocked) {
                        blockedUser = userId;
                    } else {
                        blockedUser = -1;
                    }
                    notifyItemChanged(currentClickedPos);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDeletePost(long postId, String response) { }

    @Override
    public void onBlockNotification(long postId, String response) {
        SingleUserFeedFrag.STOPANIM();
        final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
        if (response != null && !response.isEmpty()) {
            try {
                JSONObject jo = new JSONObject(response);
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
    public void onReadNotification(long notification_id, String response) { }

    private List<Object> tempList=new ArrayList<>();
    private void callPostRequestAgain() {
        FeedFragment.STARTANIM();
        requestInProcess = true;
        String url = WebConstant.getInstance().FEEDNEW;
        RequestParams rp = new RequestParams();
        rp.put("page", "" + mFeedDetails.page);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(mActivity, url, new Header[]{new MyHeader(mActivity)}, rp, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Log.i("WEBAPI","res:"+new String(responseBody).toString());
                FeedFragment.STOPANIM();
                FeedDetails feed = new Gson().fromJson(new String(responseBody), FeedDetails.class);
                mFeedDetails.is_next = feed.is_next;
                mFeedDetails.page = feed.page;

                tempList.clear();
                tempList.addAll(feed.posts);
                addNativeAdsIntoRawFeed(mNativeAds);
                feed.posts.clear();

                notifyDataSetChanged();
                requestInProcess = false;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("WEBAPI", "Failure: statuscode=>" + statusCode);
                FeedFragment.STOPANIM();
                requestInProcess = false;
            }
        });
    }

    private void addNativeAdsIntoRawFeed(List<UnifiedNativeAd> natAds){
        if(tempList!=null&&tempList.size()>0&&natAds!=null&&natAds.size()>0){
            int offset = (tempList.size() / natAds.size())+1;
            int index = 1;
            for (UnifiedNativeAd ad : natAds) {
                tempList.add(index, ad);
                index = index + offset;
            }
            mPostList.addAll(tempList);
            tempList.clear();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public Context context;
        public ImageView userDp;
        public MyImageView userPostImage;
        public TextView username, likeTv, commentsTv, text, hashtag, time_userPost;
        public CircularProgressImageButton likeThumb;
        public CircularProgressButton follow;
        public LinearLayout userNameDp;
        public ImageButton optionItem;

        public MyViewHolder(View itemView, Context ctx) {
            super(itemView);
            rootView = itemView;
            context = ctx;
            userDp = (ImageView) itemView.findViewById(R.id.user_dp);
            userPostImage = (MyImageView) itemView.findViewById(R.id.postimage);
            int width = AppUtils.screenWidth - (AppUtils.dpToPx(mActivity, 2 * 10));
            int height = AppUtils.screenWidth - (AppUtils.dpToPx(mActivity, 2 * 5));
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) userPostImage.getLayoutParams();
            lp.width = width;
            lp.height = height;
            userPostImage.setLayoutParams(lp);
            username = (TextView) itemView.findViewById(R.id.user_name);
            time_userPost = (TextView) itemView.findViewById(R.id.time_userpost);
            follow = (CircularProgressButton) itemView.findViewById(R.id.follow);
            commentsTv = (TextView) itemView.findViewById(R.id.comments_count);
            likeTv = (TextView) itemView.findViewById(R.id.like_count);
            likeThumb = (CircularProgressImageButton) itemView.findViewById(R.id.likethumb);
            userNameDp = (LinearLayout) itemView.findViewById(R.id.user_name_dp);
            text = (TextView) itemView.findViewById(R.id.text_feedada);
            hashtag = (TextView) itemView.findViewById(R.id.hashtag_feedada);
            optionItem = (ImageButton) itemView.findViewById(R.id.feed_option_btn);

            NameArtMenu.setFont((ViewGroup) itemView, AppUtils.webApiFont);
        }
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
}
