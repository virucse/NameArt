package com.webapi.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.formationapps.nameart.App;

/**
 * Created by caliber fashion on 8/31/2017.
 */

public class UserProfile {
    public static final String LOGIN_PROVIDER_GOOGLE="google";
    public static final String LOGIN_PROVIDER_FACEBOOK="facebook";
    private static UserProfile instance;
    public static UserProfile getInstance(){
        if(instance==null){
            instance=new UserProfile();
        }
        return instance;
    }

    private SharedPreferences userPref;

    private UserProfile() {
        userPref = App.getInstance().getSharedPreferences("userprofilenameart", Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return userPref.getBoolean("isLoggedIn", false);
    }

    public void setLoggedIn(boolean loggedIn) {
        userPref.edit().putBoolean("isLoggedIn", loggedIn).apply();
    }
   public void setLoginProvider(String provider){
        userPref.edit().putString("loginprovider",provider).apply();
   }
   public String getLoginProvider(){
       return userPref.getString("loginprovider",LOGIN_PROVIDER_FACEBOOK);
   }
    public String getGender() {
        return userPref.getString("gender", "");
    }

    public void setGender(String gender) {
        userPref.edit().putString("gender", gender).apply();
    }

    public String getUserName() {
        return userPref.getString("userName", "");
    }

    public void setUserName(String name) {
        userPref.edit().putString("userName", name).apply();
    }

    public String getUserId() {
        return userPref.getString("userId", "");
    }

    public void setUserId(String userId) {
        userPref.edit().putString("userId", userId + "").apply();
    }

    public long getUserIdValue() {
        String s = userPref.getString("userIdstr", "-1");
        return Long.parseLong(s);
    }

    public void setUserIdValue(long value) {
        userPref.edit().putString("userIdstr", value + "").apply();
    }

    public String getPassword() {
        return userPref.getString("password", "");
    }

    public void setPassword(String password) {
        userPref.edit().putString("password", password + "").apply();
    }

    public String getToken() {
        return userPref.getString("userToken", "");
    }

    public void setToken(String token) {
        userPref.edit().putString("userToken", token + "").apply();
    }

    public String getUserPicture() {
        return userPref.getString("picture", "");
    }

    public void setUserPicture(String url) {
        userPref.edit().putString("picture", url).apply();
    }
}
