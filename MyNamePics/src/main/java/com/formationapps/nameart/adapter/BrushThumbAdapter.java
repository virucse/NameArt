package com.formationapps.nameart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.formationapps.nameart.R;

/**
 * Created by caliber fashion on 2/22/2017.
 */

public class BrushThumbAdapter extends BaseAdapter {
    private int[] thumbs;
    private Context mContext;
    private LayoutInflater inflater;

    public BrushThumbAdapter(Context context, int[] data) {
        mContext = context;
        thumbs = data;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return thumbs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_item_brush, null);
        }
        ((ImageView) convertView.findViewById(R.id.iv_main_gallery)).setImageResource(thumbs[position]);
        return convertView;
    }
}
