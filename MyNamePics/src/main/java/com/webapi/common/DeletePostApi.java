package com.webapi.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.WebConstant;

import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/30/2017.
 */

public class DeletePostApi {
    private String deletepost = WebConstant.getInstance().DELETEPOST;

    public DeletePostApi(final Context context, final long postId, final CommanProcessingListener listener) {
        RequestParams param = new RequestParams();
        param.put("post_id", "" + postId);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, deletepost, new Header[]{new MyHeader(context)}, param, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                if (listener != null) {
                    listener.onDeletePost(postId, new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("deletePostApi:",""+new String(responseBody));
                if (listener != null) {
                    listener.onDeletePost(postId, "");
                }
            }
        });
    }
}
