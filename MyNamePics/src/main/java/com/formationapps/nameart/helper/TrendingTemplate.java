package com.formationapps.nameart.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.formationapps.nameart.App;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.TemplateActivity;
import com.formationapps.nameart.fragments.TemplateFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by caliber fashion on 7/7/2017.
 */

public class TrendingTemplate extends Fragment {
    private static TemplateFragment.OnTemplateDismissListener mTemplateListener = null;
    private static StorageReference mBaseStorageRef;
    private GridView mGridView;
    private JSONArray jsonArrayResponse;

    public static TrendingTemplate newFrag(TemplateFragment.OnTemplateDismissListener tds) {
        TrendingTemplate tf = new TrendingTemplate();
        mTemplateListener = tds;
        return tf;
    }

    private static String getPathFromJsonArray(JSONArray jsonArray, int index) {
        String path = null;
        try {
            JSONObject jo = jsonArray.getJSONObject(index);
            path = jo.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTrendingTemplate();
    }

    private void loadTrendingTemplate() {
        String url = AppUtils.getTrendingTemplateRootUrl();//WebConstant.getInstance().BASEURL + "trending";
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("trendingarray.response", response.toString());
                        //pDialog.hide();
                        jsonArrayResponse = response;
                        setTrendingData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("trendingarray.error", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
        // Adding request to request queue
        App.getInstance().addToRequestQueue(req, "trendingGet");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.template_adapter, container, false);
        AdsHelper.loadAdmobBanner(getContext(), (LinearLayout) view.findViewById(R.id.ad_container));
        mGridView = (GridView) view.findViewById(R.id.gridview_template_adapter);
        return view;
    }

    private void setTrendingData(JSONArray jsonArray) {
        if (mGridView == null) {
            return;
        }
        try {
            mGridView.setAdapter(new ImageAdapter(getActivity(), jsonArray));
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String path = ((ImageAdapter) parent.getAdapter()).getPathAt(position);
                    if (mTemplateListener != null) {
                        if (TemplateActivity.isActive) {
                            mTemplateListener.onTemplateDismiss(path,AppUtils.getTemplatesRemotePath());
                            getActivity().onBackPressed();
                        } else {
                            Intent intent = new Intent(getActivity(), TemplateActivity.class);
                            intent.putExtra("url", "" + path);
                            getActivity().startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), TemplateActivity.class);
                        intent.putExtra("url", "" + path);
                        getActivity().startActivity(intent);
                    }
                }
            });
        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (Exception e) {
            System.gc();
        }
    }

    public static class ImageAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater mInflater;
        private JSONArray mJsonArray;

        public ImageAdapter(Context c, JSONArray jsonArray) {
            context = c;
            mInflater = LayoutInflater.from(context);
            mJsonArray = jsonArray;
        }

        public String getPathAt(int index) {
            return getPathFromJsonArray(mJsonArray, index);
        }

        // ---returns the number of images---
        @Override
        public int getCount() {
            return mJsonArray.length();
        }

        // ---returns the ID of an item---
        @Override
        public Object getItem(int position) {
            return getPathFromJsonArray(mJsonArray, position);
        }

        // ---returns the ID of an item---
        @Override
        public long getItemId(int position) {
            return position;
        }

        // ---returns an ImageView view---
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.template_image_adapter, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.template_image_view);

            String url = getPathFromJsonArray(mJsonArray, position);

            String s = url.replace("/", "");
            File file = new File(context.getFilesDir(), s);

            try {
                if (file.isFile()) {
                    Glide.with(context.getApplicationContext())
                            .load(file)
                            .into(imageView);

                } else {
                    //load from another server
                    String baseUrl = AppUtils.getTemplatesRemotePath();//"http://d28thm5ortpe0d.cloudfront.net/";
                    Glide.with(context.getApplicationContext())
                            .load(baseUrl + url)
                            .into(imageView);
                }
            } catch (Exception e) {

            }

            return convertView;
        }
    }
}
