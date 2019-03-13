package com.webapi.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.formationapps.nameart.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ROSHAN on 1/5/2018.
 */

public class InviteFriendsAdapter extends RecyclerView.Adapter<InviteFriendsAdapter.MyViewHolder> {


    private RecyclerView mRecyclerView;
    private Activity mActivity;
    private List<String> nameOfUsers;
    private List<Bitmap> pictureOfUsers;

    public InviteFriendsAdapter(Activity activity,List<String> name,List<Bitmap> pictures){
        mActivity=activity;
        nameOfUsers = name;
        pictureOfUsers = pictures;

    }

    public InviteFriendsAdapter setRecyclerView(RecyclerView ff) {
        mRecyclerView = ff;
        return this;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invitefriendsadapter, parent, false);
        return new MyViewHolder(view, parent.getContext().getApplicationContext());

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (nameOfUsers !=null ){
            holder.userName.setText(nameOfUsers.get(position));
        }
       if (pictureOfUsers != null){
           holder.userDp.setImageBitmap(pictureOfUsers.get(position));
       }
       holder.btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inviteFriendsOnNameArt();
                Toast.makeText(mActivity, "Invited", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public ImageView userDp;
        public TextView userName;
        public ImageButton btn_invite;

        public MyViewHolder(View view, Context applicationContext) {
            super(view);
            userDp = (ImageView)view.findViewById(R.id.user_dp);
            userName = (TextView)view.findViewById(R.id.user_name);
            btn_invite = (ImageButton)view.findViewById(R.id.btn_invite);

        }
    }
}
