package com.webapi.common;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.WebConstant;
import com.webapi.fragments.CommentFragment;

import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/6/2017.
 */

public class CommentsApi {
    private String commentUrl = WebConstant.getInstance().COMMENT;
    private CommentFragment mCommentFragment;

    public CommentsApi() {

    }

    public void process(final Context context, long postId, String comment) {
        //Toast.makeText(context,""+comment,Toast.LENGTH_SHORT).show();
        RequestParams param = new RequestParams();
        param.put("post_id", "" + postId);
        param.put("comment", "" + comment);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, commentUrl, new Header[]{new MyHeader(context)}, param, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Log.i("WebApi.comment", "onSuccess: "+new String(responseBody));
                if (mCommentFragment != null) {
                    mCommentFragment.onCommentSuccess();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setThisFragment(CommentFragment cf) {
        mCommentFragment = cf;
    }
}
