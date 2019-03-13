package com.webapi.common;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.WebConstant;

import org.json.JSONException;
import org.json.JSONObject;

import circularprogress.customViews.CircularProgressImageButton;
import circularprogress.interfaces.OnAnimationEndListener;
import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/6/2017.
 */

public class LikeApi {
    private String likeUrl = WebConstant.getInstance().LIKE;

    public LikeApi(final Context context, long postId, final CircularProgressImageButton imageView, final int index, final CommanProcessingListener listener) {
        RequestParams param = new RequestParams();
        param.put("post_id", "" + postId);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, likeUrl, new Header[]{new MyHeader(context)}, param, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                imageView.revertAnimation(new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        try {
                            JSONObject jo = new JSONObject(new String(responseBody).toString());
                            boolean isLike = jo.getBoolean("is_like");
                            listener.onLikeSuccess(isLike, index);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                imageView.stopAnimation();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
