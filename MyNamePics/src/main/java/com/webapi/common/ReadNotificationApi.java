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
 * Created by caliber fashion on 1/16/2018.
 */

public class ReadNotificationApi {

    private String readNotification = WebConstant.getInstance().READNOTIFICATION;

    public ReadNotificationApi(final Context context, final long notification_id, final CommanProcessingListener listener){
        RequestParams param = new RequestParams();
        param.put("notification_id", ""+notification_id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context,readNotification,new Header[]{new MyHeader(context)}, param, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("web",new String(responseBody).toString());
                if (listener != null) {
                    listener.onReadNotification(notification_id, new String(responseBody).toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (listener != null){
                    listener.onReadNotification(notification_id,"");
                }
            }
        });
    }
}
