package com.formationapps.nameart.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.QuotesActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by caliber fashion on 7/30/2017.
 */

public class HindiQuotesFragment extends Fragment {
    private String HINDIQUOTESFILENAME = "files/hindiquotesdetails.txt";
    private Activity mActivity;
    private int selectedTab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        SharedPreferences pref = AppUtils.getSharedPref(mActivity);
        selectedTab = pref.getInt("hindiquotestab", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hindiquotesfragment, container, false);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager_quotes_activity);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tabs_quotes_activity);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SharedPreferences p = AppUtils.getSharedPref(mActivity);
                SharedPreferences.Editor ed = p.edit();
                ed.putInt("hindiquotestab", position);
                ed.commit();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //create quotes
        try {
            InputStream ins = mActivity.getAssets().open(HINDIQUOTESFILENAME);
            String file = QuotesActivity.readFile(new InputStreamReader(ins));
            //Toast.makeText(mActivity,"FILE==>"+file.trim(),Toast.LENGTH_LONG).show();
            HindiQuotesName hindiQuotesName = new Gson().fromJson(file, HindiQuotesName.class);
            if (hindiQuotesName != null) {
                mViewPager.setAdapter(new MyFStatePaperAdapter(getChildFragmentManager(), mActivity, hindiQuotesName.quotesFileDetails));
                mTabLayout.setupWithViewPager(mViewPager, true);
                mViewPager.setCurrentItem(selectedTab);
                ViewGroup slidingTabStrip = (ViewGroup) mTabLayout.getChildAt(0);
                int count = slidingTabStrip.getChildCount();
                for (int i = 0; i < count; i++) {
                    AppCompatTextView textView = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
                    textView.setTypeface(AppUtils.defaultTypeface, Typeface.NORMAL);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mActivity, "HindiQuotesException" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public static class MyFStatePaperAdapter extends FragmentStatePagerAdapter {
        private Context context;
        private QuotesFileDetails[] quotes_name;

        public MyFStatePaperAdapter(FragmentManager fm, Context context, QuotesFileDetails[] quotes_name) {
            super(fm);
            this.context = context;
            this.quotes_name = quotes_name;
            Toast.makeText(context, "Name:" + quotes_name[0].file, Toast.LENGTH_LONG).show();
        }

        @Override
        public Fragment getItem(int position) {
            return new QuotesFragment();
        }

        @Override
        public int getCount() {
            return quotes_name.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return quotes_name[position].name;
        }
    }

    public static class QuotesFragment extends Fragment {

    }
}
