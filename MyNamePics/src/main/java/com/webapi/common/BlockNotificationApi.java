package com.webapi.common;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.WebConstant;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ROSHAN on 1/3/2018.
 */

public class BlockNotificationApi {

    private String blockNotification = WebConstant.getInstance().BLOCKNOTIFICATION;
    public BlockNotificationApi(final Context context, final long post_id, final CommanProcessingListener listener) {
        RequestParams param = new RequestParams();
        param.put("post_id", "" + post_id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, blockNotification, new Header[]{new MyHeader(context)}, param, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("web",new String(responseBody).toString());
                if (listener != null) {
                    listener.onBlockNotification(post_id, new String(responseBody).toString());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (listener != null){
                    listener.onBlockNotification(post_id,"");
                }

            }
        });

    }
}
