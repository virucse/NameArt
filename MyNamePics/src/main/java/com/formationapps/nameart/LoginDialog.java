package com.formationapps.nameart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 9/14/2017.
 */

public class LoginDialog extends DialogFragment {
    private static final int RC_SIGN_IN = 1007;
    //private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private ProgressDialog mProgressDialog;
    private MyTempListener mMyTempListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Wait...");
        return inflater.inflate(R.layout.login_dialog, container, false);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if(context instanceof MyTempListener){
                mMyTempListener= (MyTempListener) context;
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //###########################################################login with social
        LoginButton lb = (LoginButton) view.findViewById(R.id.login_button);
        lb.setFragment(this);
        lb.setReadPermissions(Arrays.asList( "email", "public_profile"));
        mCallbackManager = CallbackManager.Factory.create();
        lb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest grapReq = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("facebookLoginSuc", object.toString() + "");
                        try {
                            //String userId = object.get("id").toString();
                            String name="unknown";
                            if(object.has("name")){
                                name = object.get("name").toString();
                            }
                            String picUrl="no";
                            if(object.has("picture")){
                                JSONObject profile_pic_data = new JSONObject(object.get("picture").toString());
                                if(profile_pic_data.has("data")){
                                    JSONObject profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                                    if(profile_pic_url.has("url")){
                                        picUrl=profile_pic_url.get("url").toString();
                                    }
                                }
                            }
                            String email="unknown@mail.com";
                            if(object.has("email")){
                                email = object.get("email").toString();
                            }
                            String gender="unknown";
                            if(object.has("gender")){
                                gender = object.get("gender").toString();
                            }
                            downloadProfileImage(picUrl, email, name, gender,UserProfile.LOGIN_PROVIDER_FACEBOOK);//from fb login
                        } catch (Exception e) {


                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,picture.width(300).height(300)");
                grapReq.setParameters(parameters);
                grapReq.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "onCancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "onError:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //##################################Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        SignInButton btnSignIn = (SignInButton) view.findViewById(R.id.gPlusSignIn);
        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_WIDE);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);//Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(getContext()!=null&&getActivity()!=null){
                handleSignInResult(task);
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d("loginInWithGmail", "handleSignInResult:" + completedTask.toString());
        String personName = null;
        String personEmail="unknown@mail.com";
        String personPhotoUrl = "no";
        String gender="unknown";
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                personName = account.getDisplayName();
                personEmail = account.getEmail();
                Uri ur = account.getPhotoUrl();
                if (ur != null) {
                    personPhotoUrl = ur.toString();
                }
            }
            Log.i("WebApi", "Name: " + personName + ", email: " + personEmail + ", Image: " + personPhotoUrl);
            downloadProfileImage(personPhotoUrl, personEmail, personName, gender,UserProfile.LOGIN_PROVIDER_GOOGLE);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            int statusCode=e.getStatusCode();
            Log.w("LoginDialog", "signInResult:failed code=" + statusCode
                    +" Name:"+GoogleSignInStatusCodes.getStatusCodeString(statusCode));
            //updateUI(false);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                personName = acct.getDisplayName();//String personGivenName = acct.getGivenName();//String personFamilyName = acct.getFamilyName();//String personId = acct.getId();
                personEmail = acct.getEmail();
                Uri personPhoto = acct.getPhotoUrl();
                if(personPhoto!=null){
                    personPhotoUrl=personPhoto.toString(); }
                downloadProfileImage(personPhotoUrl, personEmail, personName, gender,UserProfile.LOGIN_PROVIDER_GOOGLE);
            }else{
                if(BuildConfig.DEBUG){
                    Toast.makeText(getActivity(),"GoogleSignInAccount null",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();//Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void downloadProfileImage(final String url, final String email, final String name, final String gender, final String logInProvider) {
        try {
            showProgressDialog();
            if (url != null &&url.contains("http")&&getContext()!=null) {
                Glide.with(getContext().getApplicationContext()).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                       saveBitmap(resource,email,name,gender,logInProvider);
                       mProgressDialog.dismiss();
                    }
                });
            } else if (gender != null && !gender.isEmpty()&&getContext()!=null) {
                dismissProgressDialog();
                Bitmap theBitmap;
                if (gender.toLowerCase().equals("male")) {
                    theBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.male_default_dp);
                } else if (gender.toLowerCase().equals("female")) {
                    theBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.female_default_dp);
                } else {
                    theBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.default_dp);
                }
                saveBitmap(theBitmap,email,name,gender,logInProvider);

            } else if(getContext()!=null){
                dismissProgressDialog();
                Bitmap theBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.default_dp);
                saveBitmap(theBitmap,email,name,gender,logInProvider);
            }else {
                dismissProgressDialog();
            }
        } catch (Exception e) {
            dismissProgressDialog();
            Log.e("LoginDialog","downloadProfileImage:Exce "+e.getMessage());
        }
    }
    private void saveBitmap(Bitmap bitmap,String email,String name,String gender,String logInProvider){
        try {
            if(getContext()!=null&&getActivity()!=null){
                final File file = new File(getContext().getFilesDir(), WebConstant.getInstance().USERDPFILENAME);
                file.deleteOnExit();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
                loginWithMyServer(email,name,gender,file,logInProvider);
            }
        } catch (FileNotFoundException e) {
            // Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            //Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private void loginWithMyServer(String email, final String name, final String gender, File dp, final String logInProvider) {
        if(getContext()!=null&&getActivity()!=null){
            showProgressDialog();
            RequestParams param = new RequestParams();
            param.put("email", "" + email);
            param.put("name", "" + name);
            param.put("gender", "" + gender);
            try {
                param.put("picture", dp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                dismissProgressDialog();
                Toast.makeText(App.getInstance(), "Error Occured", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = WebConstant.getInstance().LOGINWITHFB;
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(getActivity(), url, new Header[]{new MyHeader(getActivity())}, param, null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dismissProgressDialog();
                    String respo = new String(responseBody);
                    try {
                        JSONObject jo = new JSONObject(respo);
                        String status = jo.getString("status");
                        if (status.toLowerCase().equals("success")) {
                            String token = jo.getString("token");
                            long userid = jo.getLong("userid");
                            UserProfile up = UserProfile.getInstance();
                            up.setLoginProvider(logInProvider);
                            up.setLoggedIn(true);
                            up.setToken(token);
                            up.setUserIdValue(userid);
                            up.setUserName(name);
                            up.setGender(gender);

                            if (mMyTempListener != null) {
                                mMyTempListener.onSelfCloudLoginSuccess();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(BuildConfig.DEBUG)Toast.makeText(App.getInstance(), "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    dismissProgressDialog();
                    Log.i("WebApi", "login With Fb onFailure: statusCode=>" + statusCode);
                    Toast.makeText(App.getInstance(), "OnFailure =>Error Occured", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isValidate(String text) {
        if (text.contains("nameart320#")) {
            return true;
        } else if (text.toLowerCase().contains("nameart".toLowerCase())) {
            return false;
        } else if (!text.contains("@")) {
            return false;
        } else {
            return true;
        }
    }
    private void dismissProgressDialog(){
        try {
            if(mProgressDialog!=null&&mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
        }catch (Exception e){

        }
    }
    private void showProgressDialog(){
        try {
            if(mProgressDialog!=null&&!mProgressDialog.isShowing()){
                mProgressDialog.show();
            }
        }catch (Exception e){

        }

    }
}
