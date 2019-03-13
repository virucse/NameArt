package com.webapi.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.fragments.BaseFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.webapi.adapters.InviteFriendsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFriendsFragment extends BaseFragment {
    private static InviteFriendsFragment instanse;
    List<String> nameOfUsersInFriendList = null ;
    List<Bitmap> pictureOfUsersInFriendList = null;


    public InviteFriendsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invite_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instanse = this;
       // getFacebookFriendList();
        getFriendList();
        TextView back = (TextView) view.findViewById(R.id.backIcon);
        back.setVisibility(View.VISIBLE);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.inviteFriends_layout);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NameArtMenu) getActivity()).removeLastFragment();
            }
        });
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.inviteFriends_recylerView);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(new InviteFriendsAdapter(getActivity(),nameOfUsersInFriendList,pictureOfUsersInFriendList));
    }

    private void getFacebookFriendList(){
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest graphRequest = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object == null || object.toString().isEmpty()) {
                    Log.e("inviteFriends","null");
                    return;
                }
                try {
                    JSONArray jsonArrayFriends = object.getJSONObject("user_friends").getJSONArray("data");
                    JSONObject friendlistObject = jsonArrayFriends.getJSONObject(0);
                    String friendListID = friendlistObject.getString("id");
                    myNewGraphReq(friendListID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle param = new Bundle();
        param.putString("fields", "user_friends");
        graphRequest.setParameters(param);
        graphRequest.executeAsync();
    }
    private void myNewGraphReq(final String friendlistId) {
        final String graphPath = "/"+friendlistId+"/members/";
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject object = graphResponse.getJSONObject();
                try {
                 JSONArray arrayOfUsersInFriendList = object.getJSONArray("data");
                /* Do something with the user list */
                friendList(arrayOfUsersInFriendList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle param = new Bundle();
        param.putString("fields", "name,email,picture.width(300).height(300)");
        request.setParameters(param);
        request.executeAsync();
    }

    private void friendList(JSONArray friendList){
        for (int i=0;i<friendList.length();i++){
            try {
                JSONObject user = friendList.getJSONObject(i);
                String userName = user.getString("name");
                nameOfUsersInFriendList.add(userName);
                JSONObject profile_pic_data = new JSONObject(user.get("picture").toString());
                JSONObject profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                String url = profile_pic_url.get("url").toString();
                downloadPicture(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    private void downloadPicture(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(responseBody,0,responseBody.length);
                pictureOfUsersInFriendList.add(bitmap);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private List<String> getFriendList(){
        final List<String> friendslist = new ArrayList<String>();
      GraphRequest request=  new GraphRequest(AccessToken.getCurrentAccessToken(),"/me/friendlists",null,
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
        Bundle bundle = new Bundle();
        bundle.putString("field","user_friends");
        request.setParameters(bundle);
        request.executeAsync();
        Log.e("Friends List: ", String.valueOf(friendslist.size()));
        return friendslist;
    }
}
