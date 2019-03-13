package com.webapi.common;

/**
 * Created by caliber fashion on 9/6/2017.
 */

public interface CommanProcessingListener {
    public void onLikeSuccess(boolean success, int index);

    public void onFollowSuccess(boolean isFollowing, long userId);

    public void onCommentSuccess(long postId);

    public void onBlockUser(long userId, String response);

    public void onDeletePost(long postId, String response);

    public void onBlockNotification(long postId , String response);

    public void onReadNotification(long notification_id,String response);
}
