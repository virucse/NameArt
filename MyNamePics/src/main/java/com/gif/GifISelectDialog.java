package com.gif;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.formationapps.nameart.R;
import com.gallery.utils.PhotoItem;

import java.util.List;

/**
 * Created by ROSHAN on 11/22/2017.
 */

public class GifISelectDialog extends DialogFragment{

    private static List<Bitmap> mList;
    private OnImagePosSelectListener mOnImagePosSelectListener;

    public static GifISelectDialog newInstance(List<Bitmap> list,OnImagePosSelectListener listener){
        GifISelectDialog gifISelectDialog=new GifISelectDialog();
        gifISelectDialog.mList=list;
        gifISelectDialog.mOnImagePosSelectListener=listener;
        return gifISelectDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.select_image_for_editing,container,false);
        ViewPager viewPager =(ViewPager) view.findViewById(R.id.viewPager_gifsid);
        MyViewPagerAdaptor adaptor = new MyViewPagerAdaptor(getChildFragmentManager());
        viewPager.setAdapter(adaptor);
        return view;
    }
    class MyViewPagerAdaptor extends FragmentStatePagerAdapter{

        public MyViewPagerAdaptor(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment mf=new MyFragment();
            Bundle b=new Bundle();
          //  b.p("imgpath",mList.get(position));
            b.putInt("position",position);
            mf.onSetSelectPosListener(mOnImagePosSelectListener);
            mf.setArguments(b);
            return mf;
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
    public static class MyFragment extends Fragment{
        int pos;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return   inflater.inflate(R.layout.view_pager_layout,container,false);

        }
        private OnImagePosSelectListener mListener;
        public void onSetSelectPosListener(OnImagePosSelectListener listener){
            mListener=listener;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
           String path= getArguments().getString("imgpath");
            pos=getArguments().getInt("position",0);
            ImageView mImageView = view.findViewById(R.id.select_image_view);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        mListener.onImagePosSelected(pos);
                    }
                }
            });
           // Glide.with(view).load(path).into(mImageView);
            mImageView.setImageBitmap(mList.get(pos));
        }
    }
}


