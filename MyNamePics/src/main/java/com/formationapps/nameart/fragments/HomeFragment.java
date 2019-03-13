package com.formationapps.nameart.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.DownloadListener;
import com.downloader.Downloader;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.ArtActivity;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.activity.FolderActivity;
import com.formationapps.nameart.activity.TemplateActivity;
import com.formationapps.nameart.helper.AdsHelper;
import com.formationapps.nameart.helper.AppUtils;
import com.formationapps.nameart.helper.OpenPlayStore;
import com.formationapps.nameart.helper.Styleable;
import com.gallery.utils.GalleryUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.formationapps.nameart.helper.AppUtils.appsList;

/**
 * Created by caliber fashion on 9/7/2017.
 */

public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    private static StorageReference mBaseStorageRef;
    private static JSONObject jsonObject;
    private static final String BEAUTIFY_MODEL_NAME = "track_face_action1.0.0.model";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((NameArtMenu) getActivity()).setSelection(R.id.home, R.mipmap.home_select);
        TextView template = (TextView) view.findViewById(R.id.menu_template_button);
        template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (downloadModelFileIfNeeded()) {
                    return;
                }*/
                AppUtils.first_time = false;
                Styleable.onClick(v, new Styleable.Clicked() {
                    @Override
                    public void onClicked(View view) {
                        Intent intent = new Intent(getActivity(), TemplateActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });

        TextView start = (TextView) view.findViewById(R.id.menu_start_button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (downloadModelFileIfNeeded()) {
                    return;
                }*/
                Styleable.onClick(v, new Styleable.Clicked() {
                    @Override
                    public void onClicked(View view) {
                        startActivity(new Intent(getActivity(), ArtActivity.class));
                    }
                });
            }
        });
        TextView pip = (TextView) view.findViewById(R.id.pip_button);
        pip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPipClick(v);
            }
        });

        TextView collage = (TextView) view.findViewById(R.id.collage_button);
        collage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (downloadModelFileIfNeeded()) {
                    return;
                }*/
                Styleable.onClick(v, new Styleable.Clicked() {
                    @Override
                    public void onClicked(View view) {
                        ((NameArtMenu) getActivity()).startEdit(GalleryUtil.APP_TYPE_COLLAGE);
                    }
                });
            }
        });

        TextView edit = (TextView) view.findViewById(R.id.edit_button);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (downloadModelFileIfNeeded()) {
                    return;
                }*/
                Styleable.onClick(v, new Styleable.Clicked() {
                    @Override
                    public void onClicked(View view) {
                        ((NameArtMenu) getActivity()).startEdit(GalleryUtil.APP_TYPE_EDITOR);
                    }
                });
            }
        });

        TextView gif = (TextView) view.findViewById(R.id.gif_button);
        gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (downloadModelFileIfNeeded()) {
                    return;
                }*/
                Styleable.onClick(v, new Styleable.Clicked() {
                    @Override
                    public void onClicked(View view) {
                        ((NameArtMenu) getActivity()).startEdit(GalleryUtil.APP_TYPE_GIF);
                    }
                });

            }
        });


        TextView camera = (TextView) view.findViewById(R.id.camera_button);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Styleable.onClick(v, new Styleable.Clicked() {
                    @Override
                    public void onClicked(View view) {
                        Intent intent = new Intent(getActivity(), FolderActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });


        try {
            String sharableUrl = "https://play.google.com/store/apps/details?id=com.formationapps.nameart&referrer=utm_source%3Din_app_fb_share%26utm_content%3Dusing_fb_share_button_on_menu";
            ShareButton shareButton = (ShareButton) view.findViewById(R.id.fb_share_button);
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(sharableUrl))
                    .build();
            shareButton.setShareContent(linkContent);
        } catch (Exception e) {

        }

        AdsHelper.loadAdmobBanner(getContext(), (LinearLayout) view.findViewById(R.id.ad_container));
    }

    @Override
    public void onResume() {
        super.onResume();
        String url = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
        try {
            //mPlusOneButton.initialize(url, 0);
        } catch (Exception e) {

        }
    }

    private void readFireBaseStorage() {
        mBaseStorageRef = ((NameArtMenu) getActivity()).mFirebaseStorage.getReferenceFromUrl(getString(R.string.firebase_base_url));
        StorageReference mTextFileRef = mBaseStorageRef.child("app_cross_promotion/mynamepics_promotion.txt");
        try {
            final StringBuilder mStringBuilder = new StringBuilder();
            final File mFile = File.createTempFile("promotions", "txt");
            mTextFileRef.getFile(mFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        BufferedReader mBUfferedReader = new BufferedReader(new FileReader(mFile));
                        String line;
                        while ((line = mBUfferedReader.readLine()) != null) {
                            mStringBuilder.append(line);
                        }
                        mBUfferedReader.close();
                        readJsonString(mStringBuilder.toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(NameArtMenu.this,"Exception:"+e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.d(TAG, "NameArtMenu.readFirebaseStorage Failure Exception" + e.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this,"IOException:"+e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d(TAG, "NameArtMenu.readFirebaseStorage IOException" + e.getMessage());
        }
    }

    private void readJsonString(String jsonString) {
        appsList.clear();
        try {
            jsonObject = new JSONObject(jsonString);
            if (jsonObject.getBoolean("status")) {
                int total = jsonObject.getInt("total_app");
                String referrer = jsonObject.getString("referrer");
                JSONArray jsonArray = jsonObject.getJSONArray("app_details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    NameArtMenu.Apps apps = new NameArtMenu.Apps();
                    apps.appName = obj.getString("name");
                    apps.appPackageName = obj.getString("package_name");
                    apps.appIconUrl = obj.getString("app_icon_url");
                    apps.referrer = referrer;
                    if (apps.appPackageName.equals(getActivity().getPackageName())) {
                    } else {
                        appsList.add(apps);
                    }
                }
                GridView gd = (GridView) getView().findViewById(R.id.grid_view_menu);
                gd.setAdapter(new NameArtMenu.GridViewAdapter(getActivity(), appsList));
                gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String url = "https://play.google.com/store/apps/details?id=" + appsList.get(position).appPackageName +
                                appsList.get(position).referrer;
                        new OpenPlayStore(getActivity()).execute(url);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(this,"IOException:"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void onPipClick( View v) {
       /* if (downloadModelFileIfNeeded()) {
            return;
        }*/
        AppUtils.first_time = false;
        Styleable.onClick(v, new Styleable.Clicked() {
            @Override
            public void onClicked(View view) {
                ((NameArtMenu) getActivity()).startEdit(GalleryUtil.APP_TYPE_PIP);
            }
        });
    }

    private void downloadModelFile(File file, String fileName, final Intent intent) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait...");
        String downUrl = AppUtils.downloadableRootUrl + "FaceEffects/facetracker.zip";

        Downloader.download(getActivity(), file.getAbsolutePath(), downUrl, new DownloadListener() {
            @Override
            public void onDownloadStarted() {
                BaseActivity baseActivity= (BaseActivity) getContext();
                if(baseActivity!=null&&!baseActivity.isNetworkConnected()){
                    BaseActivity.showNetworkErrorMessage(baseActivity);
                }else{
                    pd.show();
                }
            }

            @Override
            public void onDownloadCompleted() {
                pd.dismiss();

                if (intent != null) {
                    //startActivity(new Intent(getActivity(), CameraPreviewActivity.class));
                }
            }

            @Override
            public void onDownloadFailed() {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onDownloadProgress(int progress) {
                pd.setMessage("Please wait..." + progress);

            }
        });
    }

    private String getModulePath(String modelName) {
        String path = null;
        File dataDir = getActivity().getApplicationContext().getExternalFilesDir(null);
        if (dataDir != null) {
            path = dataDir.getAbsolutePath();//+ File.separator + modelName;
        }
        return path;

    }

    private boolean downloadModelFileIfNeeded() {
        String path = getModulePath(BEAUTIFY_MODEL_NAME);
        String newpath = path + File.separator + BEAUTIFY_MODEL_NAME;
        File rootFile = new File(newpath);
        if (newpath != null) {
            if (!rootFile.exists()) {
                downloadModelFile(new File(path), BEAUTIFY_MODEL_NAME,null);
                return true;
            }
        }
        return false;
    }

    private void openCameraActivity(Intent intent) {
        // Toast.makeText(getActivity(), "open camera", Toast.LENGTH_SHORT).show();
        String path = getModulePath(BEAUTIFY_MODEL_NAME);
        String newpath = path + File.separator + BEAUTIFY_MODEL_NAME;
        File rootFile = new File(newpath);
        if (newpath != null) {
            if (!rootFile.exists()) {
                downloadModelFile(new File(path), BEAUTIFY_MODEL_NAME,intent);
            } else {
                //  Toast.makeText(getActivity(), "camera open", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getActivity(), CameraPreviewActivity.class);
                startActivity(intent);
            }
        }
    }
}
