package com.webapi.model;

/**
 * Created by caliber fashion on 9/5/2017.
 */

public class FeedPost {
    public String picture;
    public int like;
    public String title;
    public String text;
    public FeedComments comments;
    public FeedUser user;
    public long id;
    public boolean is_like;
    public String time;
}
