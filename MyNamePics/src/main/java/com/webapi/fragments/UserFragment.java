package com.webapi.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.formationapps.nameart.App;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.BaseFragment;
import com.formationapps.nameart.helper.OpenPlayStore;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.webapi.model.MyHeader;
import com.webapi.model.UserDetail;
import com.webapi.model.UserPost;
import com.webapi.model.UserProfile;
import com.webapi.model.WebConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by caliber fashion on 8/31/2017.
 */

public class UserFragment extends BaseFragment {
    private final String regex = "([\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee])";
    String test = "josh stevens is the best 😂";
    UserDetail ud = null;

    private void testEmoji() {
        Matcher matchEmo = Pattern.compile(regex).matcher(test);
        while (matchEmo.find()) {
            System.out.println(matchEmo.group());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserProfile userProfile = UserProfile.getInstance();
        if (userProfile.isLoggedIn()) {
            startAnim();
            loadData();
        } else {
            LoginFragment lf = new LoginFragment();
           // ((NameArtMenu) getActivity()).removeLastFragment();
            ((NameArtMenu) getActivity()).addNewFragment(lf);
        }

        RelativeLayout userDPContainer = (RelativeLayout) view.findViewById(R.id.userdpLayout);
        userDPContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompleteProfileFragment cpf = new CompleteProfileFragment();
                ((NameArtMenu) getActivity()).addNewFragment(cpf);
            }
        });
        setMenu();

    }

    private void loadData() {
        String url = WebConstant.getInstance().SELFTVIEW;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getActivity(), url, new Header[]{new MyHeader(getActivity())}, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                onResponse(new String(responseBody));
                stopAnim();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                stopAnim();
                Log.e("WebApi.UserFragm", "Failure: statuscode=>" + statusCode+"\n error:"+error.getMessage());
            }
        });
    }

    private void onResponse(String response) {
        Log.d("WebApi.UserFragm", "UserFragment.onResponse: "+response);
        View view = getView();
        try {
            ud = new Gson().fromJson(response, UserDetail.class);
        } catch (Exception e) {

        }
        if (ud != null&&view!=null&&getContext()!=null&&getActivity()!=null) {
            UserProfile up = UserProfile.getInstance();
            up.setUserName(ud.name);
            up.setUserPicture(ud.thumb);

            TextView name = (TextView) view.findViewById(R.id.userName);
            name.setText(ud.name);

            ImageView userDP = (ImageView) getView().findViewById(R.id.img_user_profile);
            Glide.with(getActivity().getApplicationContext()).load(WebConstant.getInstance().BASEURL + ud.thumb)
                    .apply(new RequestOptions().placeholder(R.mipmap.icon_72)).into(userDP);

            TextView postCount = (TextView) view.findViewById(R.id.post_count_user);
            postCount.setText(ud.postcount + "");
            TextView followers = (TextView) view.findViewById(R.id.followers_count_user);
            followers.setText(ud.followers + "");

            LinearLayout followerL = (LinearLayout) view.findViewById(R.id.follower_layout);
            followerL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putLong("userid", ud.user_id);
                    FollowersListFragment flf = new FollowersListFragment();
                    flf.setArguments(b);
                    ((NameArtMenu) getActivity()).addNewFragment(flf);
                }
            });

            TextView following = (TextView) view.findViewById(R.id.following_count_user);
            following.setText(ud.following + "");

            LinearLayout followingL = (LinearLayout) view.findViewById(R.id.following_layout);
            followingL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putLong("userid", ud.user_id);
                    FollowingListFragment flf = new FollowingListFragment();
                    flf.setArguments(b);
                    ((NameArtMenu) getActivity()).addNewFragment(flf);
                }
            });
            GridView gd = (GridView) view.findViewById(R.id.gridView_user_post);
            gd.setAdapter(new MyGridViewAdapter(ud.posts, getActivity()));
            gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle b = new Bundle();
                    b.putString("name", "" + ud.name);
                    b.putLong("postid", ud.posts[position].id);
                    b.putLong("userid", ud.user_id);
                    SingleUserFeedFrag suff = new SingleUserFeedFrag();
                    suff.setArguments(b);
                    Log.e("url_of_post",ud.posts[position].picture);
                    ((NameArtMenu) getActivity()).addNewFragment(suff);
                }
            });
        } else {
            Toast.makeText(App.getInstance(), "Error Occured", Toast.LENGTH_SHORT).show();
        }

    }

    private void setMenu() {
        LinearLayout ll = (LinearLayout) getView().findViewById(R.id.setting_layout);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView menu = (ImageView) getView().findViewById(R.id.setting_user);
                PopupMenu mPopUpMenu = new PopupMenu(getActivity(), menu);
                mPopUpMenu.getMenuInflater().inflate(R.menu.menu_menu, mPopUpMenu.getMenu());
               if (UserProfile.getInstance().getLoginProvider().equals(UserProfile.LOGIN_PROVIDER_FACEBOOK)){
                   mPopUpMenu.getMenu().findItem(R.id.action_invite_frnds).setVisible(false);
               }
                mPopUpMenu.getMenu().findItem(R.id.SignOut).setVisible(true);
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
                            case R.id.privacy_policy_app:
                                ((NameArtMenu) getActivity()).setPrivacyPolicy();
                                break;
                            case R.id.remove_ads:
                                ((NameArtMenu) getActivity()).showBillingDialog();
                                break;
                            case R.id.SignOut:
                                UserProfile.getInstance().setLoggedIn(false);
                               /* if (((NameArtMenu) getActivity()).removeLastFragment()) {
                                    //((NameArtMenu) getActivity()).popBackStackImmediate();
                                }*/
                                LoginFragment lf = new LoginFragment();
                                ((NameArtMenu) getActivity()).addNewFragment(lf);
                                break;
                            case R.id.action_invite_frnds:
                                String appLinkUrl = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                                if (AppInviteDialog.canShow()) {
                                    AppInviteContent content = new AppInviteContent.Builder()
                                            .setApplinkUrl(appLinkUrl)
                                            .build();
                                    AppInviteDialog.show(getActivity(), content);
                                }
                               // InviteFriendsFragment iff = new InviteFriendsFragment();
                               // ((NameArtMenu) getActivity()).addNewFragment(iff);
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



    public static class MyGridViewAdapter extends BaseAdapter {
        private UserPost[] posts;
        private Context context;
        //private IjkWrapper ijkWrapper = new IjkWrapper();

        public MyGridViewAdapter(UserPost[] posts, Context context) {
            this.posts = posts;
            this.context = context;
        }

        @Override
        public int getCount() {
            return posts.length;
        }

        @Override
        public Object getItem(int position) {
            return posts[position].picture;
        }

        @Override
        public long getItemId(int position) {
            return posts[position].id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gridadapter_userpost, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview_gridadapter);
           /* RelativeLayout.LayoutParams rlp= (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            rlp.width=RelativeLayout.LayoutParams.MATCH_PARENT;
            rlp.height=RelativeLayout.LayoutParams.MATCH_PARENT;
            imageView.setLayoutParams(rlp);*/
            String baseUrl = WebConstant.getInstance().BASEURL;
            TextureView videoView = convertView.findViewById(R.id.video_view);
           if (posts[position].picture.endsWith(".mp4")){
               //ijkWrapper.init();
               videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                   @Override
                   public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                       //ijkWrapper.setSurface(new Surface(surface));
                   }

                   @Override
                   public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                   }

                   @Override
                   public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                       //ijkWrapper.setSurface(null);
                       return true;
                   }

                   @Override
                   public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                   }
               });
               //ijkWrapper.openRemoteFile(baseUrl+posts[position].picture);
               //ijkWrapper.prepare();
           }else {
               Glide.with(context.getApplicationContext())
                       .load(baseUrl + posts[position].picture)
                       .into(imageView);
           }
            //Log.i("WebApi.picture"," "+baseUrl+posts[position].picture);
            return convertView;
        }
    }
}
