package com.formationapps.nameart.activity;

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

import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.HindiQuotesFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by caliber fashion on 7/27/2017.
 */

public class QuotesActivity extends Fragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Activity mActivity;
    private int selectedTab;

    public static String readFile(Reader reader) throws IOException {
        final StringBuilder mStringBuilder = new StringBuilder();
        BufferedReader mBUfferedReader = new BufferedReader(reader);
        String line;
        while ((line = mBUfferedReader.readLine()) != null) {
            mStringBuilder.append(line);
        }
        mBUfferedReader.close();
        return mStringBuilder.toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        SharedPreferences pref = AppUtils.getSharedPref(mActivity);
        selectedTab = pref.getInt("quotestab", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quotes_activity, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_quotes_activity);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs_quotes_activity);
        mViewPager.setAdapter(new MyFStatePaperAdapter(getChildFragmentManager(), mActivity));
        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setCurrentItem(selectedTab);
        ViewGroup slidingTabStrip = (ViewGroup) mTabLayout.getChildAt(0);
        int count = slidingTabStrip.getChildCount();
        for (int i = 0; i < count; i++) {
            AppCompatTextView textView = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
            textView.setTypeface(AppUtils.defaultTypeface, Typeface.NORMAL);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SharedPreferences p = AppUtils.getSharedPref(mActivity);
                SharedPreferences.Editor ed = p.edit();
                ed.putInt("quotestab", position);
                ed.commit();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return view;
    }

    public static class MyFStatePaperAdapter extends FragmentStatePagerAdapter {
        private Context context;

        public MyFStatePaperAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HindiQuotesFragment();
            }
            return new HindiQuotesFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return context.getString(R.string.hindi);
            } else if (position == 1) {
                return context.getString(R.string.english);
            } else {
                return "Others";
            }
        }
    }
}
