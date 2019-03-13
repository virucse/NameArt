package formationapps.helper.stickers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
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
 * Created by photomediaapps on 12/13/2016.
 */

public class StickerFragment extends Fragment {
    public static final String LOCAL_TAG = "local";
    public static final String CLOUD_TAG = "cloud";
    private static final String TAG = StickerFragment.class.getSimpleName();
    public static StorageReference mBaseStorageRef;
    private static Activity activity;
    private static List<StickerDataHolder> stickersList = new ArrayList<>();
    private static ViewPagerAdapter mViewPagerAdapter;
    private static boolean shareFlag = false, rateFlag = false;
    private static SharedPreferences mSharedPreferences;
    private static AdapterView.OnItemClickListener mGridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ImageAdapter ia = (ImageAdapter) parent.getAdapter();
            String tag = ia.getArrayTag();
            if (tag.contains(LOCAL_TAG)) {
                String path = ia.getPathAt(position);
                ((BaseActivity) activity).addStickerToView("stickers",path,STICKERSREMOTEPATH);
            } else {
                String path = ia.getPathAt(position);
                ((BaseActivity) activity).addStickerToView("stickers",path,STICKERSREMOTEPATH);
            }
        }
    };
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    private boolean isViewCreated = false;
    private SharedPreferences sp;
    private static String STICKERSREMOTEPATH="jkk";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        sp=AppUtils.getSharedPref(activity);

        STICKERSREMOTEPATH= AppUtils.getStickerRemotePath();
        if(!STICKERSREMOTEPATH.endsWith("/")){
            STICKERSREMOTEPATH=STICKERSREMOTEPATH+"/";
        }

        mSharedPreferences = activity.getPreferences(MODE_PRIVATE);
        shareFlag = mSharedPreferences.getBoolean("shared", false);
        rateFlag = mSharedPreferences.getBoolean("rated", false);
        mBaseStorageRef = mFirebaseStorage.getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sticker_fragment, container, false);
        viewPager = (ViewPager) root.findViewById(R.id.sticker_view_pager);
        mTabLayout = (TabLayout) root.findViewById(R.id.tabs);
        isViewCreated = true;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AdsHelper.loadAdmobBanner(getContext(), (LinearLayout) view.findViewById(R.id.ad_container));

        if (getActivity()!=null&&!((BaseActivity)getActivity()).isNetworkConnected()) {
            BaseActivity.showNetworkErrorMessage(getActivity());
        }

        if (StickerConst.stickerJsonString != null&&isAdded()) {
            readJsonString(StickerConst.stickerJsonString);
        }else {
            //getFirebase SuitReading date
            int date=getCloudReadingDate();
            if(date!= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                readFireBaseStorageForSticker();
            }else{
                readFromLoacal();
            }
        }
    }

    private void loadStickers() {
        activity = getActivity();
        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                if (isViewCreated && isAdded() && activity != null) {
                    h.removeCallbacks(this);
                    mViewPagerAdapter = new ViewPagerAdapter(StickerFragment.this.getChildFragmentManager());
                    viewPager.setAdapter(mViewPagerAdapter);
                    mTabLayout.setupWithViewPager(viewPager);
                    //load custom fonts
                    ViewGroup slidingTabStrip = (ViewGroup) mTabLayout.getChildAt(0);
                    int tabsCount = slidingTabStrip.getChildCount();
                    for (int i = 0; i < tabsCount; i++) {
                        AppCompatTextView view = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
                        view.setTypeface(AppUtils.defaultTypeface, Typeface.NORMAL);
                    }
                } else {
                    h.post(this);
                }
            }
        });

    }

    private void readJsonString(String jsonString) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(getActivity(), "json:" + jsonString, Toast.LENGTH_LONG).show();
        }

        StickerConst.stickerJsonString = jsonString;
        stickersList.clear();
        try {
            JSONObject rootObject = new JSONObject(jsonString);
            if (rootObject.getBoolean("status")) {
                int total = rootObject.getInt("total_page");

                //read folders
                List<String> folderList=new ArrayList<>();
                JSONObject fileObj = rootObject.getJSONObject("files");
                for (int i = 0; i < total; i++) {
                    String n = fileObj.getString("" + (i + 1));
                    folderList.add(n);
                }

                String rootFolder = rootObject.getString("rootfolder");

                //read folders map
                JSONObject listObj = rootObject.getJSONObject("details");
                for (String key : folderList) {
                    JSONArray jsonArray = listObj.getJSONArray(key);
                    String[] child = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        child[i]=(rootFolder+"/"+key + "/" + jsonArray.getString(i));
                    }
                    //suitMapList.put(key, list);
                    StickerDataHolder tdh = new StickerDataHolder();
                    tdh.setParent(key);
                    tdh.setChilds(child);
                    stickersList.add(tdh);
                }
            }
            loadStickers();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(activity, "readJsonString.JSONException->" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    final String stickername = "stickers.txt";
    private void readFireBaseStorageForSticker() {
        mBaseStorageRef = mFirebaseStorage.getReference();
        String ssp=AppUtils.getStickerFilePath().isEmpty()?"ghh.txt":AppUtils.getStickerFilePath();
        StorageReference mTextFileRef = mBaseStorageRef.child(ssp);
        try {
            final File mFile = File.createTempFile("stickers", "txt");
            mTextFileRef.getFile(mFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        if(getActivity()!=null&&getContext()!=null){
                            final String str = readFile(new FileReader(mFile));
                            FileOutputStream fos = getActivity().openFileOutput(stickername, MODE_PRIVATE);
                            fos.write(str.getBytes());
                            fos.close();
                            readJsonString(str);
                            setCloudReadingDate(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "StickersFragment.readFirebaseStorage Failure Exception" + e.getMessage());
                    Toast.makeText(activity, "OnFailure.Exception:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    setCloudReadingDate(0);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "IOException:" + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "StickersFragment.readFirebaseStorage IOException" + e.getMessage());
            setCloudReadingDate(0);
        }

        readFromLoacal();
    }
    private void readFromLoacal(){
        File file=new File(activity.getFilesDir(),stickername);
        if (file.isFile() && file.exists()) {
            try {
                readJsonString(readFile(new FileReader(file)));
                //fileExit=true;
            } catch (IOException e) {
                e.printStackTrace();
                //fileExit=false;
                setCloudReadingDate(0);
            }
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

    public interface UnlockListener {
        public void onYesClicked();
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new StickerContainer();
            Bundle args = new Bundle();
            args.putInt(StickerContainer.ARG_OBJECT, i); // Our object is just an integer :-P
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 20-object collection.
            return stickersList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = stickersList.get(position).getParent();
            title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
            return title;
        }
    }

    public static class StickerContainer extends Fragment {
        public static final String ARG_OBJECT = "object";
        private int position = 0;
        private Activity activity;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            activity = getActivity();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.template_adapter, container, false);
            final GridView gd = (GridView) view.findViewById(R.id.gridview_template_adapter);
            gd.setNumColumns(5);
            //final ImageView imageView=(ImageView)view.findViewById(R.id.imageview_sticker_pager_adapt);
            //final RelativeLayout lockContainer = (RelativeLayout) view.findViewById(R.id.imageView_layout_sticker);
            lockAndUnlockStickers(gd);
            return view;
        }

        private void lockAndUnlockStickers(GridView gd) {
            Bundle b = getArguments();
            position = b.getInt(ARG_OBJECT);
            try {
                gd.setAdapter(new ImageAdapter(activity, stickersList.get(position)));
                gd.setOnItemClickListener(mGridItemClickListener);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(activity, "lockAndUnlockStickersException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public static class ImageAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private String[] childs;
        private Context context;
        private StickerDataHolder mStickerData;

        public ImageAdapter(Context c, StickerDataHolder mst) {
            context = c;
            mInflater = LayoutInflater.from(context);
            mStickerData = mst;
            childs = mStickerData.getChilds();
        }

        public String getPathAt(int index) {
            return childs[index];
        }

        public String getArrayTag() {
            return mStickerData.getTag();
        }

        // ---returns the number of images---
        @Override
        public int getCount() {
            return childs.length;
        }

        // ---returns the ID of an item---
        @Override
        public Object getItem(int position) {
            return position;
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
                CardView mCardView = (CardView) convertView.findViewById(R.id.card_view);
                int width = AppUtils.dpToPx(context, 80);
                RelativeLayout.LayoutParams layp = (RelativeLayout.LayoutParams) mCardView.getLayoutParams();
                layp.width = width;
                layp.height = width;
                layp.addRule(RelativeLayout.CENTER_IN_PARENT);
                mCardView.setLayoutParams(layp);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.template_image_view);
            String p = childs[position].replace("/", "");
            File file = new File(activity.getFilesDir(), p);
            if (file.isFile() && file.exists()) {
                Glide.with(activity).load(file).into(imageView);
            } else {
                //load from another server
                String baseUrl =STICKERSREMOTEPATH;//"http://d28thm5ortpe0d.cloudfront.net/";
                Glide.with(context.getApplicationContext())
                        .load(baseUrl + childs[position])
                        .into(imageView);
            }

            return convertView;
        }
    }

    static class OnFireBaseSucces implements OnSuccessListener<byte[]> {
        ImageView imgView;

        public OnFireBaseSucces(ImageView imgView) {
            this.imgView = imgView;
        }

        @Override
        public void onSuccess(byte[] bytes) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imgView.setImageBitmap(bitmap);
        }
    }

    static class OnFireBaseError implements OnFailureListener {
        String path;
        Context context;

        public OnFireBaseError(String p, Context c) {
            path = p;
            context = c;
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(context, "Exception At:" + path + " " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    static class TagArray {
        public String[] folderArray;
        public String arrayTag;

        public TagArray(String[] arr, String tag) {
            folderArray = arr;
            arrayTag = tag;
        }
    }

    private int getCloudReadingDate(){
        return sp.getInt("cloudstickerreadingdate",0);
    }
    private void setCloudReadingDate(int date){
        sp.edit().putInt("cloudstickerreadingdate",date).apply();
    }
}
