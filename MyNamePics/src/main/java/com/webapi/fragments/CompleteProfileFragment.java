package com.webapi.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.BaseFragment;
import com.formationapps.nameart.permission.MarseMallowPermission;
import com.gallery.activity.GalleryFragment;
import com.gallery.utils.GalleryUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.UserProfile;
import com.webapi.model.WebConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import circularprogress.customViews.CircularProgressButton;
import circularprogress.interfaces.OnCircularProgressButtonClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/4/2017.
 */

public class CompleteProfileFragment extends BaseFragment implements Spinner.OnItemSelectedListener {
    private File file;
    private String[] genderList = new String[]{"Select Gender", "Male", "Female"};
    private boolean validated;
    private int genderSelectPos = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.completeprofilefragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = (TextView) view.findViewById(R.id.titlebar);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((NameArtMenu) getActivity()).removeLastFragment()) {
                    ((NameArtMenu) getActivity()).popBackStackImmediate();
                }
            }
        });
        final UserProfile uProfile = UserProfile.getInstance();
        final EditText name = (EditText) view.findViewById(R.id.name);
        String n = uProfile.getUserName();
        if (n != null && !n.isEmpty()) {
            name.setText(n);
        }

        final Spinner gender = (Spinner) view.findViewById(R.id.gender);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, genderList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        gender.setOnItemSelectedListener(this);
        String g = uProfile.getGender();
        if (g != null && !g.isEmpty()) {
            if (g.toLowerCase().equals("male")) {
                gender.setSelection(1, true);
            } else if (g.toLowerCase().equals("female")) {
                gender.setSelection(2, true);
            }
        }

        final ImageView image = (ImageView) view.findViewById(R.id.imageview);
        Button selectFile = (Button) view.findViewById(R.id.selectfile);
        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MarseMallowPermission.storagePermitted(getActivity(), 0)) {
                    GalleryFragment gf = GalleryFragment.newInstance(new GalleryFragment.OnGalleryFinishListener() {
                        @Override
                        public void onImageChooseListener(Uri uri) {
                            image.setVisibility(View.VISIBLE);
                            image.setImageURI(uri);
                            file = new File(uri.getPath());
                            ((NameArtMenu) getActivity()).removeLastFragment();
                        }

                        @Override
                        public int getAppType() {
                            return GalleryUtil.APP_TYPE_DEFAULT;
                        }
                    });
                    ((NameArtMenu) getActivity()).addNewFragment(gf);
                }

            }
        });
        final TextView responseText = (TextView) view.findViewById(R.id.response);
        CircularProgressButton update = (CircularProgressButton) view.findViewById(R.id.updateprofile);
        update.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.follow_bg));
        update.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        update.addOnClickListener(new OnCircularProgressButtonClick() {
            @Override
            public void onClickButton(final CircularProgressButton button, int index) {
                super.onClickButton(button, index);
                if (!validated) {
                    return;
                }
                button.startAnimation();
                if (file == null) {
                    responseText.setText(R.string.file_not_selected_update_profile);
                    button.revertAnimation();
                    return;
                }
                String url = WebConstant.getInstance().COMPLETEPROFILE;
                RequestParams param = new RequestParams();
                try {
                    param.put("picture", file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (!isValidate(name.getText().toString())) {
                    responseText.setText("This Is Invalid Name,Please try another");
                    button.revertAnimation();
                    return;
                }
                if (name.getText().toString().equals("nameart320#")) {
                    param.put("name", "NameArt");
                } else {
                    param.put("name", "" + name.getText().toString());
                }

                param.put("gender", "" + genderList[genderSelectPos].toString());

                uProfile.setGender(genderList[genderSelectPos].toString());

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(getActivity(), url, new Header[]{new MyHeader(getActivity())}, param, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String respo = new String(responseBody).toString();
                        String msg = null;
                        try {
                            JSONObject jo = new JSONObject(respo);
                            msg = jo.getString("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        responseText.setText(msg);
                        button.revertAnimation();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private boolean isValidate(String text) {
        if (text.contains("nameart320#")) {
            return true;
        } else if (text.toLowerCase().contains("nameart".toLowerCase())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            validated = true;
            genderSelectPos = position;
        } else {
            validated = false;
            //Toast.makeText(getActivity(),"Please Select either "+genderList[1]+" or "+genderList[2],Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        validated = false;
    }
}
