package com.webapi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.formationapps.nameart.R;
import com.webapi.model.WebConstant;
import com.webapi.model.CommentDetail;
import com.webapi.model.CommentListDetail;

/**
 * Created by caliber fashion on 9/8/2017.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyHolder> {
    private CommentListDetail mCommentList;
    private Context context;

    public CommentListAdapter(CommentListDetail cld, Context ctx) {
        mCommentList = cld;
        context = ctx;
    }

    public void addNewItem(CommentDetail cd) {
        mCommentList.comments.add(cd);
        notifyItemInserted(mCommentList.comments.size() - 1);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentfragadapter, parent, false);
        return new MyHolder(view, context);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        CommentDetail cd = mCommentList.comments.get(position);
        holder.name.setText(cd.user.name + "");
        holder.comment.setText(cd.comment);
        Glide.with(holder.context.getApplicationContext()).load(WebConstant.getInstance().BASEURL +
                cd.user.picture).into(holder.dp);
    }

    @Override
    public int getItemCount() {
        return mCommentList.comments.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public Context context;
        public ImageView dp;
        public TextView name, comment;

        public MyHolder(View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            dp = (ImageView) itemView.findViewById(R.id.dp_cfa);
            name = (TextView) itemView.findViewById(R.id.username_cfa);
            comment = (TextView) itemView.findViewById(R.id.comment_cfa);
        }
    }
}
