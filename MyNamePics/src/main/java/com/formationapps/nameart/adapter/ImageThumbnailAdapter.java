package com.formationapps.nameart.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;
import com.xiaopo.flying.sticker.DrawableSticker;

public class ImageThumbnailAdapter extends BaseAdapter {
    private static double DAMPER;
    private static double TENSION;

    static {
        TENSION = 800.0d;
        DAMPER = 20.0d;
    }

    int flag;
    LayoutInflater inflater;
    private Activity context;
    private int[] data;

    public ImageThumbnailAdapter(Activity context, int[] abc) {
        this.flag = 0;
        this.context = context;
        this.data = abc;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.library_grid_item, null);
        }
        ((ImageView) convertView.findViewById(R.id.imgv)).setImageResource(this.data[position]);
        convertView.setOnClickListener(new C04481(position));
        return convertView;
    }

    public int getCount() {
        return this.data.length;
    }

    public Object getItem(int position) {
        return Integer.valueOf(this.data[position]);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    class C04481 implements OnClickListener {
        final int val$position;

        C04481(int i) {
            this.val$position = i;
        }

        public void onClick(View view) {
            AppUtils.chk_sticker = true;
            AppUtils.sticker = new DrawableSticker(ContextCompat.getDrawable(ImageThumbnailAdapter.this.context, ImageThumbnailAdapter.this.data[this.val$position]));
            ImageThumbnailAdapter.this.context.finish();
        }
    }
}
