package com.webapi.model;

/**
 * Created by caliber fashion on 9/14/2017.
 */

public class FollowerListDetails {
    public String status;
    public Followers[] followers;

    public int getFollowerList() {
        return followers.length;
    }

    public long getFollowerId(int index) {
        return followers[index].id;
    }

    public boolean isFollow(int index) {
        return followers[index].is_follow;
    }

    public void setIsFollow(boolean isFollow, int index) {
        followers[index].is_follow = isFollow;
    }

    public String getFollowerName(int index) {
        return followers[index].name;
    }

    public String getPicture(int index) {
        return followers[index].picture;
    }
}

class Followers {
    public long id;
    public boolean is_follow;
    public String name;
    public String picture;
}
