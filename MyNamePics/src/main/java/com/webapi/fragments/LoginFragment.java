package com.webapi.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.formationapps.nameart.App;
import com.formationapps.nameart.BuildConfig;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.BaseActivity;
import com.formationapps.nameart.fragments.BaseFragment;
import com.formationapps.nameart.helper.OpenPlayStore;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.webapi.model.MyHeader;
import com.webapi.model.UserProfile;
import com.webapi.model.WebConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 8/27/2017.
 */

public class LoginFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 1007;
    private static final String TAG = LoginFragment.class.getSimpleName();
    public CallbackManager mCallbackManager;
    //private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private GoogleSignInClient mGoogleSignInClient;


    private static Bitmap getFacebookProfilePicture(String userID) {
        URL imageURL = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            setMenu();
            if (!isNetworkConnected(getContext())) {
                BaseActivity.showNetworkErrorMessage(getActivity());
                return;
            }
        } catch (Exception e) {

        }

        LoginButton lb = (LoginButton) view.findViewById(R.id.login_button);
        lb.setFragment(this);
        //lb.setReadPermissions(Arrays.asList("user_status", "email", "public_profile","user_friends","read_custom_friendlists"));
        lb.setReadPermissions(Arrays.asList("email", "public_profile"));
        mCallbackManager = CallbackManager.Factory.create();
        lb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                if (loginResult == null) {
                    return;
                }
                GraphRequest grapReq = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response1) {
                        if (object == null || object.toString().isEmpty()) {
                            return;
                        }
                        Log.i("facebookLoginSuc", object.toString() + "");
                        Log.e("facebookLoginSuc",response1.toString());
                        try {
                            //getFriendList();
                            String userId = object.get("id").toString();
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
                            downloadProfileImage(picUrl, email, name, gender,UserProfile.LOGIN_PROVIDER_FACEBOOK);//from facebook login
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("facebookLoginSucExce", e.getMessage() + "");

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
                .requestEmail().requestProfile().build();
        /*mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();*/

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(UserProfile.getInstance().isLoggedIn()){
            NameArtMenu nameArtMenu=(NameArtMenu)getActivity();
            if(nameArtMenu!=null){
                nameArtMenu.setSelection(R.id.userprofile, R.mipmap.profile_select);
                nameArtMenu.getSupportFragmentManager().popBackStackImmediate();
                nameArtMenu.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN ) {
            if(BuildConfig.DEBUG){
                Toast.makeText(getActivity(),"onActivityResult",Toast.LENGTH_SHORT).show();
            }
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);//Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(getContext()!=null&&getActivity()!=null){
                handleSignInResult(task);
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private List<String> getFriendList(){
        final List<String> friendslist = new ArrayList<String>();
        GraphRequest request=  new GraphRequest(AccessToken.getCurrentAccessToken(),"/me/friends",null,
                HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                Log.e("Friends List: ", response.toString());
                try {
                    JSONObject responseObject = response.getJSONObject();
                    JSONArray dataArray = responseObject.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        String fbId = dataObject.getString("id");
                        String fbName = dataObject.getString("name");
                        Log.e("Friends List: ", fbName);
                        friendslist.add(fbId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        request.executeAsync();
        Log.e("Friends List: ", String.valueOf(friendslist.size()));
        return friendslist;
    }

    private void loginWithMyServer(String email, final String name, final String gender, File dp, final String login) {
        if(getContext()!=null&&getActivity()!=null){
            startAnim();
            RequestParams param = new RequestParams();
            param.put("email", "" + email);
            param.put("name", "" + name);
            param.put("gender", "" + gender);
            try {
                param.put("picture", dp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                stopAnim();
                Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = WebConstant.getInstance().LOGINWITHFB;
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(getActivity(), url, new Header[]{new MyHeader(getActivity())}, param, null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if(getContext()!=null&&getActivity()!=null){
                        stopAnim();
                        String respo = new String(responseBody);
                        try {
                                JSONObject jo = new JSONObject(respo);
                                String status = jo.getString("status");
                                if (status.toLowerCase().equals("success")) {
                                    String token = jo.getString("token");
                                    long userid = jo.getLong("userid");
                                    UserProfile up = UserProfile.getInstance();
                                    up.setLoginProvider(login);
                                    up.setLoggedIn(true);
                                    up.setToken(token);
                                    up.setUserIdValue(userid);
                                    up.setUserName(name);
                                    up.setGender(gender);

                                    if (((NameArtMenu) getActivity()).removeLastFragment()) {
                                        //((NameArtMenu) getActivity()).popBackStackImmediate();
                                    }
                                    if(getActivity()!=null){
                                        UserFragment uf = new UserFragment();
                                        ((NameArtMenu) getActivity()).addNewFragment(uf);
                                    }
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(App.getInstance(), "Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    stopAnim();
                    Log.i("WebApi", "login With Fb onFailure: statusCode=>" + statusCode);
                    if(BuildConfig.DEBUG)Toast.makeText(App.getInstance(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    private void facebookFriendsList() {
       /* LoginClient.Request.newMyFriendsRequest(session, new GraphUserListCallback() {
            @Override
            public void onCompleted(List users, Response response) {
                makeFacebookFriendList(users);
            }
        }).executeAsync();*/
    }

    private void downloadProfileImage(final String url, final String email, final String name, final String gender, final String login) {
        startAnim();
        Log.i("WebApi","ProfileImage");
        if(url!=null&&url.contains("http")&&getContext()!=null){
            Glide.with(getContext().getApplicationContext()).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    stopAnim();
                    saveBitmap(resource,email,name,gender,login);
                }
            });
        }else if (gender != null && !gender.isEmpty()&&getContext()!=null) {
            Bitmap theBitmap;
            if (gender.toLowerCase().equals("male")) {
                theBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.male_default_dp);
            } else if (gender.toLowerCase().equals("female")) {
                theBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.female_default_dp);
            } else {
                theBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.default_dp);
            }
            stopAnim();
            saveBitmap(theBitmap,email,name,gender,login);
        } else if(getContext()!=null){
            stopAnim();
            Bitmap theBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.default_dp);
            saveBitmap(theBitmap,email,name,gender,login);
        }else {
            stopAnim();
        }
    }
    private void saveBitmap(Bitmap theBitmap, String email, String name , String gender, String login){
        try {
            Log.i("WebApi","saveAndLogin");
            if(theBitmap!=null&&getContext()!=null&&getActivity()!=null){
                final File file = new File(getContext().getFilesDir(), WebConstant.getInstance().USERDPFILENAME);
                file.deleteOnExit();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                theBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
                loginWithMyServer(email, name, gender, file,login);
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    /*this is only used when
    we login using email and pass  @loginWithEmail()*/
    private void completeProfile(File file, String name, String gender) {
        startAnim();
        String url = WebConstant.getInstance().COMPLETEPROFILE;
        RequestParams param = new RequestParams();
        try {
            param.put("picture", file);
            param.put("gender", "" + gender);
            param.put("name", "" + name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getActivity(), url, new Header[]{new MyHeader(getActivity())}, param, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                stopAnim();
                String respo = new String(responseBody).toString();
                String msg = null;
                try {
                    JSONObject jo = new JSONObject(respo);
                    msg = jo.getString("msg");
                    ((NameArtMenu) getActivity()).removeLastFragment();

                    UserFragment uf = new UserFragment();
                    ((NameArtMenu) getActivity()).addNewFragment(uf);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                stopAnim();
            }
        });
    }

    public void onStart() {
        super.onStart();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "handleSignInResult:" + completedTask.toString());
        String personName = null;
        String personEmail="unknown@mail.com";
        String personPhotoUrl = "no";
        String gender="unknown";
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account!=null){
                personName = account.getDisplayName();
                personEmail = account.getEmail();
                Uri ur = account.getPhotoUrl();
                if (ur != null) {
                    personPhotoUrl = ur.toString();
                }
            }
            Log.i("WebApi", "Name: " + personName + ", email: " + personEmail + ", Image: " + personPhotoUrl);
            downloadProfileImage(personPhotoUrl, personEmail, personName, gender,UserProfile.LOGIN_PROVIDER_GOOGLE);//from gmail login
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            int statusCode=e.getStatusCode();
            Log.w(TAG, "signInResult:failed code=" + statusCode
                    +" Name:"+GoogleSignInStatusCodes.getStatusCodeString(statusCode));
            updateUI(false);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                personName = acct.getDisplayName();
                personEmail = acct.getEmail();
                Uri personPhoto = acct.getPhotoUrl();
                if (personPhoto != null) {
                    personPhotoUrl = personPhoto.toString();
                }
                downloadProfileImage(personPhotoUrl, personEmail, personName, gender,UserProfile.LOGIN_PROVIDER_GOOGLE);//from gamil login when exception
            }else{
                if(BuildConfig.DEBUG){
                    Toast.makeText(getActivity(),"onActivityResult",Toast.LENGTH_SHORT).show();
                }
            }

        }

        /*if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }*/
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();//Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            // btnSignIn.setVisibility(View.GONE);
            // btnSignOut.setVisibility(View.VISIBLE);
            // btnRevokeAccess.setVisibility(View.VISIBLE);
            // llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            //btnSignIn.setVisibility(View.VISIBLE);
            //btnSignOut.setVisibility(View.GONE);
            //btnRevokeAccess.setVisibility(View.GONE);
            //llProfileLayout.setVisibility(View.GONE);
        }
    }

    private void setMenu() {
        final ImageView ll = (ImageView) getView().findViewById(R.id.setting_user);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu mPopUpMenu = new PopupMenu(getActivity(), ll);
                mPopUpMenu.getMenuInflater().inflate(R.menu.menu_menu, mPopUpMenu.getMenu());
                mPopUpMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.action_rate_app:
                                String url = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                                new OpenPlayStore(getActivity()).execute(url);
                                break;
                            case R.id.action_share_app:
                                onShare();
                                break;
                            case R.id.action_email_app:
                                emailAppToFriend();
                                break;
                            case R.id.action_suggestion_app:
                                sendFeedback();
                                break;
                            case R.id.remove_ads:
                                ((NameArtMenu) getActivity()).showBillingDialog();
                                break;
                            case R.id.privacy_policy_app:
                                ((NameArtMenu) getActivity()).setPrivacyPolicy();
                                break;
                        }
                        return false;
                    }
                });
                mPopUpMenu.show();
            }
        });
    }

    private void onShare() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(android.content.Intent.EXTRA_SUBJECT, "" + getResources().getString(R.string.app_name));
        share.putExtra(android.content.Intent.EXTRA_TEXT,
                "Hey!\nTry My Name Pics an awesome app '" + getResources().getString(R.string.app_name)
                        + "'having 50+ famous unique fonts style ,Greeting Cards,imogies to make your name on and more" +
                        "at\nhttps://goo.gl/jSzZwL"
        );
        //share.putExtra(Intent.EXTRA_STREAM, fromMyHtml(html));
        startActivity(Intent.createChooser(share, "AppUtils Via"));
    }

    private void sendFeedback() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String aEmailList[] = {"care.formationapps@gmail.com"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getString(R.string.app_name) + " " + getString(R.string.email_subject));
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "" + getString(R.string.email_msg));
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.chooser_text)));
    }

    private void emailAppToFriend() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String aEmailList[] = {"<--Enter Your Friend Email Here-->"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getString(R.string.app_name));
        emailIntent.setType("plain/text");
        String url = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey!\nTry My Name Pics an awesome app '" + getResources().getString(R.string.app_name)
                + " 'having 50+ famous unique fonts style ,Greeting Cards,imogies to make your name on and more " +
                "at\nhttps://goo.gl/50msVC");
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.email_send_choose_text)));
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}
