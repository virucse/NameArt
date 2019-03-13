package com.formationapps.nameart.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.formationapps.nameart.helper.TemplateDataHolder;
import com.formationapps.nameart.helper.TrendingTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;

import static com.formationapps.nameart.fragments.TemplateFragment.templateList;

/**
 * Created by caliber fashion on 8/14/2017.
 */

public class TemplateParentFragment extends Fragment {
    private static TemplateFragment.OnTemplateDismissListener mTemplateListener = null;
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private int selectedIndex;
    private Activity mActivity;
    private String selectedIndexKey = "parenttemplateIndexKey";

    private SharedPreferences sp;
    private static String STICKERSREMOTEPATH="jkk";

    public static TemplateParentFragment newFrag(TemplateFragment.OnTemplateDismissListener tds) {
        TemplateParentFragment tf = new TemplateParentFragment();
        mTemplateListener = tds;
        return tf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        SharedPreferences pref = AppUtils.getSharedPref(mActivity);
        selectedIndex = pref.getInt(selectedIndexKey, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.template_parent_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_template_parent);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs_template_parent);

        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(viewPager, true);
        viewPager.setCurrentItem(selectedIndex);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SharedPreferences p = AppUtils.getSharedPref(mActivity);
                SharedPreferences.Editor ed = p.edit();
                ed.putInt(selectedIndexKey, position);
                ed.commit();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        ViewGroup slidingTabStrip = (ViewGroup) mTabLayout.getChildAt(0);
        int tabsCount = slidingTabStrip.getChildCount();
        for (int i = 0; i < tabsCount; i++) {
            AppCompatTextView viewT = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
            viewT.setTypeface(AppUtils.defaultTypeface, Typeface.NORMAL);
        }
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                Fragment fragment = TemplateFragment.newFrag(mTemplateListener);
                return fragment;

            } else {
                return TrendingTemplate.newFrag(mTemplateListener);
            }
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 20-object collection.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                String title = "Templates Category";
                //title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
                return title;

            } else {
                String title = "Trending Templates";
                //title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
                return title;
            }
        }
    }


}
