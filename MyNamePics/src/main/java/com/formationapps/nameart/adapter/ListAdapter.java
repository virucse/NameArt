package com.formationapps.nameart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.TypefacesUtils;

public class ListAdapter extends BaseAdapter {
    Activity activity;
    String[] font_arr;
    LayoutInflater inflater;

    public ListAdapter(Activity editActivity, String[] as) {
        if (font_arr != null) {
            font_arr = null;
            System.gc();
        }
        font_arr = as;
        activity = editActivity;
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.font_arr.length;
    }

    @Override
    public Object getItem(int i) {
        return font_arr[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewgroup) {
        ViewHolder viewholder;
        View view1 = view;
        if (view == null) {
            view1 = this.inflater.inflate(R.layout.text_raw, null);
            viewholder = new ViewHolder();
            viewholder.text = (TextView) view1.findViewById(R.id.txtFont);
            view1.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) view1.getTag();
        }
        // viewholder.text.setTypeface(Typeface.createFromAsset(this.activity.getAssets(), this.font_arr[i]));
        viewholder.text.setTypeface(TypefacesUtils.get(activity, font_arr[i]));
        viewholder.text.setText("Name");
        viewholder.text.setTextSize(15.0f);
        return view1;
    }

    public static class ViewHolder {
        public TextView text;
    }
}
