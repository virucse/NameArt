package com.webapi.model;

/**
 * Created by caliber fashion on 9/6/2017.
 */

public class OtherUserPosts {
    public String picture;
    public int like;
    public String title;
    public String text;
    public Comment comments;
    public long id;
    public boolean is_like;
    public String time;

    public int getCommentCount() {
        return comments.count;
    }

    public void setCommentCount(int count) {
        comments.count = count;
    }
}

class Comment {
    public int count;
}
