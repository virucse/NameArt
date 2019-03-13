package com.webapi.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.ParseException;

/**
 * Created by caliber fashion on 9/1/2017.
 */

public class UserImageUploader {
    public String uploadImage(final Activity activi, final String title, final String text, final File bitmapfile) {
        String url = WebConstant.getInstance().BASEURL + "post";
        RequestParams mRequestParam = new RequestParams();
        mRequestParam.put("title", title);
        mRequestParam.put("text", text);

        try {
            mRequestParam.put("picture", bitmapfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("WEBAPI", "FileNotFoundException: " + e.getMessage());
        }

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(activi, url, new Header[]{new Header() {
            @Override
            public String getName() {
                return "token";
            }

            @Override
            public String getValue() {
                return UserProfile.getInstance().getToken();
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        }}, mRequestParam, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("WEBAPI", "onSucess: " + new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("WEBAPI", "onFailure: res=>" + new String(responseBody) + " Error=>" + error.getMessage());
            }
        });
        return "";
        /*JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("WEBAPI", "onResponse: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("WEBAPI", "onErrorResponse: "+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String,String> param=new HashMap<>();
                param.put("title",title);
                param.put("text",text);
                param.put("picture","");
                return param;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("token",new UserProfile(activity).getToken());
                return headers;
            }
        };*/
        //App.getInstance().addToRequestQueue(jsonObjectRequest,"uploadimagetoserver");
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private StringBuffer request(String urlString) {
        // TODO Auto-generated method stub

        StringBuffer chaine = new StringBuffer("");
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("UserDetail-Agent", "");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }
        } catch (IOException e) {
            // Writing exception to log
            e.printStackTrace();
        }
        return chaine;
    }
}
