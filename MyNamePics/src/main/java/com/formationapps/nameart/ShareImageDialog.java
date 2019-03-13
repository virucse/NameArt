package com.formationapps.nameart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.formationapps.nameart.helper.AppUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.WebConstant;

import java.io.File;
import java.io.FileNotFoundException;

import circularprogress.customViews.CircularProgressButton;
import circularprogress.interfaces.OnAnimationEndListener;
import circularprogress.interfaces.OnCircularProgressButtonClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/11/2017.
 */

public class ShareImageDialog extends DialogFragment {
    private File mBitmapFile;
    private MyTempListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Share with " + getString(R.string.app_name));
        return inflater.inflate(R.layout.share_image_dialog, container, false);
    }

    public void setBitmapFile(File file) {
        mBitmapFile = file;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if(context instanceof MyTempListener){
                listener= (MyTempListener) context;
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String url = WebConstant.getInstance().BASEURL + "post";
        ImageView img = (ImageView) view.findViewById(R.id.img_share_image_dialog);
        img.setImageURI(Uri.fromFile(mBitmapFile));
        final CircularProgressButton shareButton = (CircularProgressButton) view.findViewById(R.id.share_btn_share_i_dialog);
        shareButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.follow_bg));
        shareButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        shareButton.addOnClickListener(new OnCircularProgressButtonClick() {
            @Override
            public void onClickButton(CircularProgressButton button, int index) {
                super.onClickButton(button, index);
                if (("" + shareButton.getTag()).equals("checkpost")) {
                    if (listener != null) {
                        listener.onPostSuccessFulAndCheckPost();
                    }
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    shareButton.startAnimation();
                } else {
                    shareButton.setText("wait...");
                }

                String text = ((EditText) view.findViewById(R.id.text_img_share_dialog)).getText().toString();
                RequestParams mRequestParam = new RequestParams();
                mRequestParam.put("title", "");
                mRequestParam.put("text", text);
                try {
                    mRequestParam.put("picture", mBitmapFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.i("WEBAPI", "FileNotFoundException: " + e.getMessage());
                }
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(view.getContext(), url, new Header[]{new MyHeader(getActivity())}, mRequestParam, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if(getContext()!=null&&getActivity()!=null){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                shareButton.revertAnimation(new OnAnimationEndListener() {
                                    @Override
                                    public void onAnimationEnd() {
                                        int color = Color.parseColor("#82CFFD");
                                        shareButton.setText(R.string.check_post);
                                        shareButton.setBackground(new ColorDrawable(color));
                                        shareButton.setTag("checkpost");

                                        if (listener != null) {
                                            listener.onPostSuccessFulAndCheckPost();
                                        }
                                    }
                                });
                            } else {
                                int color = Color.parseColor("#82CFFD");
                                shareButton.setText(R.string.check_post);
                                shareButton.setBackground(new ColorDrawable(color));
                                shareButton.setTag("checkpost");

                                if (listener != null) {
                                    listener.onPostSuccessFulAndCheckPost();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("web_response_123","failure");
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = AppUtils.screenWidth - (AppUtils.screenWidth / 10);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }
}
