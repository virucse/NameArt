package com.webapi.common;

import android.content.Context;
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

public class BlockUserApi {
    private String blockUser = WebConstant.getInstance().BLOCKUSER;

    public BlockUserApi(final Context context, final long userId, final CommanProcessingListener listener) {
        RequestParams param = new RequestParams();
        param.put("user_id", "" + userId);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, blockUser, new Header[]{new MyHeader(context)}, param, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                if (listener != null) {
                    listener.onBlockUser(userId, new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
                if (listener != null) {
                    listener.onBlockUser(userId, "");
                }
            }
        });
    }
}
