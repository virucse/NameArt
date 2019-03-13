package com.webapi.model;

import android.content.Context;

import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.helper.AppUtils;

/**
 * Created by caliber fashion on 9/8/2017.
 */

public class WebConstant {
    public String USERDPFILENAME = "userdpfile.png";
    public String BASEURL = "";
    public String LOGIN = BASEURL + "auth/login";
    public String REGISTER = BASEURL + "auth/register";
    public String SELFTVIEW = BASEURL + "user/profile";
    public String COMPLETEPROFILE = BASEURL + "auth/profile";
    public String FEED = BASEURL + "feed";
    public String FEEDNEW = BASEURL + "feednew";
    public String LIKE = BASEURL + "like";
    public String COMMENT = BASEURL + "comment";
    public String FOLLOW = BASEURL + "follow";
    public String LIKELIST = BASEURL + "likelist";
    public String COMMENTLIST = BASEURL + "commentlist";
    public String FOLLOWERSLIST;
    public String FOLLOWINGLIST;
    public String BLOCKUSER;
    public String DELETEPOST;
    public String LOGINWITHFB;
    public String NOTIFICATION;
    public String BLOCKNOTIFICATION;
    public String READNOTIFICATION;

    private static WebConstant instance;
    public static WebConstant getInstance(){
        if(instance==null){
            instance=new WebConstant();
        }
        return instance;
    }
    private WebConstant(){
        initialise();
    }
    private static boolean isRealDomain=false;
    public void setRealDomain(boolean isDomain){
        isRealDomain=isDomain;
        instance=new WebConstant();
    }

    private void initialise() {
       /* if(BuildConfig.DEBUG){
            BASEURL = "http://35.154.251.30/";
        }else{*/
            BASEURL = AppUtils.getWebApiDomain();
      // }

        LOGIN = BASEURL + "auth/login";
        REGISTER = BASEURL + "auth/register";
        SELFTVIEW = BASEURL + "user/profile";
        COMPLETEPROFILE = BASEURL + "auth/profile";
        FEED = BASEURL + "feed";
        LIKE = BASEURL + "like";
        COMMENT = BASEURL + "comment";
        FOLLOW = BASEURL + "follow";
        LIKELIST = BASEURL + "likelist";
        COMMENTLIST = BASEURL + "commentlist";
        FOLLOWERSLIST = BASEURL + "followerlist";
        FOLLOWINGLIST = BASEURL + "followinglist";
        BLOCKUSER = BASEURL + "blockuser";
        DELETEPOST = BASEURL + "deletepost";
        FEEDNEW = BASEURL + "feednew";
        LOGINWITHFB = BASEURL + "auth/fblogin";
        NOTIFICATION = BASEURL +"notification";
        BLOCKNOTIFICATION = BASEURL +"blocknotification";
        READNOTIFICATION = BASEURL +"readnotification";
    }
}
