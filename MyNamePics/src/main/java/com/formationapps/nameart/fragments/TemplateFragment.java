package com.formationapps.nameart.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.activity.TemplateActivity;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.TemplateDataHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by caliber fashion on 3/13/2017.
 */

public class TemplateFragment extends Fragment {
    private static final String TAG = TemplateFragment.class.getSimpleName();
    public static List<TemplateDataHolder> templateList = new ArrayList<>();
    private static Activity activity;
    private static StorageReference mBaseStorageRef;
    private static OnTemplateDismissListener mTemplateListener = null;
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private boolean isRootViewCreated = false;
    private ViewPagerAdapter mViewPagerAdapter;
    private int selectedIndex = 0;
    private String selectedIndexKey = "templateIndexKey";
    private SharedPreferences sp;
    private static String STICKERSREMOTEPATH="jkk";
    public TemplateFragment() {
        mTemplateListener = null;
    }

    public static TemplateFragment newFrag(OnTemplateDismissListener tds) {
        TemplateFragment tf = new TemplateFragment();
        mTemplateListener = tds;
        return tf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = null;
        activity = getActivity();
        SharedPreferences pref = AppUtils.getSharedPref(activity);
        selectedIndex = pref.getInt(selectedIndexKey, 0);
        mBaseStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.template_fragment, container, false);
        viewPager = (ViewPager) root.findViewById(R.id.template_view_pager);
        mTabLayout = (TabLayout) root.findViewById(R.id.tabs_template);
        isRootViewCreated = true;
        //loadTemplate();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AdsHelper.loadAdmobBanner(getContext(), (LinearLayout) view.findViewById(R.id.ad_container));
        sp=AppUtils.getSharedPref(activity);
        STICKERSREMOTEPATH= AppUtils.getTemplatesRemotePath();
        if(!STICKERSREMOTEPATH.endsWith("/")){
            STICKERSREMOTEPATH=STICKERSREMOTEPATH+"/";
        }

        if (getActivity()!=null&&!((BaseActivity)getActivity()).isNetworkConnected()) {
            BaseActivity.showNetworkErrorMessage(getActivity());
        }

        int date=getCloudReadingDate();
        if(date!= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
            readFireBaseStorageForSticker();
        }else{
            readFromLoacal();
        }
    }

    private void loadTemplate() {
        activity = getActivity();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isAdded() && activity != null) {
                    handler.removeCallbacks(this);
                    mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
                    viewPager.setAdapter(mViewPagerAdapter);
                    mTabLayout.setupWithViewPager(viewPager, true);
                    viewPager.setCurrentItem(selectedIndex);

                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }

                        @Override
                        public void onPageSelected(int position) {
                            SharedPreferences p = AppUtils.getSharedPref(activity);
                            SharedPreferences.Editor ed = p.edit();
                            ed.putInt(selectedIndexKey, position);
                            ed.apply();
                            if(AdsHelper.isTemplateInterstitialInit()){
                                if(position%6==0){
                                    AdsHelper.initTemplatesInterstitialAndShow(getContext());
                                }
                            }else {
                                AdsHelper.initTemplatesInterstitialAndShow(getContext());
                            }

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });

                    ViewGroup slidingTabStrip = (ViewGroup) mTabLayout.getChildAt(0);
                    int tabsCount = slidingTabStrip.getChildCount();
                    for (int i = 0; i < tabsCount; i++) {
                        AppCompatTextView view = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
                        view.setTypeface(AppUtils.defaultTypeface, Typeface.NORMAL);
                    }
                } else {
                    handler.post(this);
                }
            }
        });
    }

    public boolean isNetworkConnected(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public interface OnTemplateDismissListener {
        public void onTemplateDismiss(String path,String rootPrefixUrl);
    }

    public static class TemplateAdapterFragment extends Fragment {
        public static final String KEY = "key";

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.template_adapter, container, false);
            final GridView gd = (GridView) view.findViewById(R.id.gridview_template_adapter);
            final int pos = getArguments().getInt(KEY, 0);
            try {
                gd.setAdapter(new ImageAdapter(activity, templateList.get(pos).getChilds()));
                gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String path = ((ImageAdapter) parent.getAdapter()).getPathAt(position);
                        if (mTemplateListener != null) {
                            if (TemplateActivity.isActive) {
                                mTemplateListener.onTemplateDismiss(path,STICKERSREMOTEPATH);
                                activity.onBackPressed();
                            } else {
                                Intent intent = new Intent(activity, TemplateActivity.class);
                                intent.putExtra("url", "" + path);
                                activity.startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(activity, TemplateActivity.class);
                            intent.putExtra("url", "" + path);
                            activity.startActivity(intent);
                        }
                    }
                });
            } catch (ArrayIndexOutOfBoundsException e) {

            } catch (Exception e) {
                System.gc();
            }
            return view;
        }
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void invalidate() {
            super.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new TemplateAdapterFragment();
            Bundle args = new Bundle();
            args.putInt(TemplateAdapterFragment.KEY, i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 20-object collection.
            return templateList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = templateList.get(position).getParent();
            title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
            return title;
        }
    }

    public static class ImageAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private Context context;
        private String[] templates;

        public ImageAdapter(Context c, String[] img) {
            context = c;
            mInflater = LayoutInflater.from(context);
            templates = img;
            //Toast.makeText(context,""+templateList.get(0),Toast.LENGTH_LONG).show();
        }

        public String getPathAt(int index) {
            return templates[index];
        }

        // ---returns the number of images---
        @Override
        public int getCount() {
            return templates.length;
        }

        // ---returns the ID of an item---
        @Override
        public Object getItem(int position) {
            return templates[position];
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

            String s = templates[position].replace("/", "");
            File file = new File(context.getFilesDir(), s);

            try {
                if (file.isFile()) {
                    Glide.with(context.getApplicationContext())
                            .load(file)
                            .into(imageView);

                } else {
                    //load from another server
                    String baseUrl = STICKERSREMOTEPATH;
                    Glide.with(context.getApplicationContext())
                            .load(baseUrl + templates[position])
                            .into(imageView);
                }
            } catch (Exception e) {

            }

            return convertView;
        }
    }

    final String stickername = "templates.txt";
    private void readFireBaseStorageForSticker() {
        mBaseStorageRef = FirebaseStorage.getInstance().getReference();
        String ssp=AppUtils.getTemplatesFilePath().isEmpty()?"ghh.txt":AppUtils.getTemplatesFilePath();
        StorageReference mTextFileRef = mBaseStorageRef.child(ssp);
        try {
            final File mFile = File.createTempFile("templates", "txt");
            mTextFileRef.getFile(mFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        if(getActivity()!=null&&getContext()!=null){
                            final String str = readFile(new FileReader(mFile));
                            FileOutputStream fos = getActivity().openFileOutput(stickername, MODE_PRIVATE);
                            fos.write(str.getBytes());
                            fos.close();
                            readJsonForTemplate(str);
                            setCloudReadingDate(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        readFromLoacal();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Symbol.readFirebaseStorage Failure Exception" + e.getMessage());
                    //Toast.makeText(activity, "OnFailure.Exception:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    readFromLoacal();
                    setCloudReadingDate(0);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(activity, "IOException:" + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Symbol.readFirebaseStorage IOException" + e.getMessage());
            readFromLoacal();
            setCloudReadingDate(0);
        }
    }
    private void readFromLoacal(){
        File file=new File(activity.getFilesDir(),stickername);
        if (file.isFile() && file.exists()) {
            try {
                readJsonForTemplate(readFile(new FileReader(file)));
                //fileExit=true;
            } catch (IOException e) {
                e.printStackTrace();
                //fileExit=false;
                setCloudReadingDate(0);
            }
        }
    }

    private void readJsonForTemplate(String json) {
        if (json == null || json.isEmpty()) {
            try {
                InputStreamReader insr = new InputStreamReader(activity.getAssets().open("files/templates2.txt"));
                json = readFile(insr);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

        }
        templateList.clear();
        String rootFolder;
        try {
            JSONObject rootObject = new JSONObject(json);

            rootFolder = rootObject.getString("rootfolder");

            int total = rootObject.getInt("total_page");
            //read folders
            List<String> folderList=new ArrayList<>();
            JSONObject fileObj = rootObject.getJSONObject("files");
            for (int i = 0; i < total; i++) {
                String n = fileObj.getString("" + (i + 1));
                folderList.add(n);
            }

            //read folders map
            JSONObject listObj = rootObject.getJSONObject("details");
            for (String key : folderList) {
                JSONArray jsonArray = listObj.getJSONArray(key);
                String[] child = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    child[i]=(rootFolder+"/"+key + "/" + jsonArray.getString(i));
                }
                //suitMapList.put(key, list);
                TemplateDataHolder tdh = new TemplateDataHolder();
                tdh.setParent(key);
                tdh.setChilds(child);
                templateList.add(tdh);
            }

            /*JSONArray filesArray = rootObject.getJSONArray("files");
            JSONObject fileDetailObject = rootObject.getJSONObject("details");
            for (int i = 0; i < filesArray.length(); i++) {
                String parent = filesArray.getString(i);
                JSONArray childArray = fileDetailObject.getJSONArray(parent);
                String[] child = new String[childArray.length()];
                for (int j = 0; j < childArray.length(); j++) {
                    child[j] = rootFolder + "/" + parent + "/" + childArray.getString(j);
                }
                TemplateDataHolder tdh = new TemplateDataHolder();
                tdh.setParent(parent);
                tdh.setChilds(child);
                templateList.add(tdh);
            }*/
            loadTemplate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String readFile(Reader reader) throws IOException {
        final StringBuilder mStringBuilder = new StringBuilder();
        BufferedReader mBUfferedReader = new BufferedReader(reader);
        String line;
        while ((line = mBUfferedReader.readLine()) != null) {
            mStringBuilder.append(line);
        }
        mBUfferedReader.close();
        return mStringBuilder.toString();
    }
    private int getCloudReadingDate(){
        return sp.getInt("cloudtemplatereadingdate",0);
    }
    private void setCloudReadingDate(int date){
        sp.edit().putInt("cloudtemplatereadingdate",date).apply();
    }

}
