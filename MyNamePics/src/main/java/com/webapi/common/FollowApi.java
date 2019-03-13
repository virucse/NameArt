package com.webapi.common;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.WebConstant;

import org.json.JSONException;
import org.json.JSONObject;

import circularprogress.customViews.CircularProgressButton;
import circularprogress.interfaces.OnAnimationEndListener;
import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/6/2017.
 */

public class FollowApi {
    private String followUrl = WebConstant.getInstance().FOLLOW;

    public FollowApi(final Context context, final long userId, final CircularProgressButton follow, final CommanProcessingListener listener) {
        RequestParams param = new RequestParams();
        param.put("user_id", "" + userId);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, followUrl, new Header[]{new MyHeader(context)}, param, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    follow.revertAnimation(new OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd() {
                            try {
                                JSONObject jo = new JSONObject(new String(responseBody).toString());
                                boolean isFollow = jo.getBoolean("is_follow");
                                listener.onFollowSuccess(isFollow, userId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    follow.stopAnimation();
                } else {
                    try {
                        JSONObject jo = new JSONObject(new String(responseBody).toString());
                        boolean isFollow = jo.getBoolean("is_follow");
                        listener.onFollowSuccess(isFollow, userId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
