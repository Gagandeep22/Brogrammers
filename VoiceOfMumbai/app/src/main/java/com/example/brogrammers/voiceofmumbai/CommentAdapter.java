package com.example.brogrammers.voiceofmumbai;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Sagar on 10/5/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<Comment> commentList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameView,commentView;


        public MyViewHolder(View view) {
            super(view);
            usernameView=view.findViewById(R.id.card_post_user_name);
            commentView=view.findViewById(R.id.card_post_comment);
        }
    }

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Comment comment = commentList.get(position);
        holder.usernameView.setText(comment.getuName()+"");
        holder.commentView.setText(comment.getmComment()+"");

    }

    public int getItemCount() {
        return commentList.size();
    }
}
