package com.webapi.model;

import android.content.Context;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.ParseException;

/**
 * Created by caliber fashion on 9/6/2017.
 */

public class MyHeader implements Header {
    UserProfile up;

    public MyHeader(Context context) {
        up = UserProfile.getInstance();
    }

    @Override
    public String getName() {
        return "token";
    }

    @Override
    public String getValue() {
        String token = up.getToken();
        up = null;
        return token;
    }

    @Override
    public HeaderElement[] getElements() throws ParseException {
        return new HeaderElement[0];
    }
}
