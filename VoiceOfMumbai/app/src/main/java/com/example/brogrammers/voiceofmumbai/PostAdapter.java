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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private Context context;
    private List<Post> postList;
    private String sta;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, location, status, description;
        public Button upvote, comment, spam;
        public ImageView images;
        public MyViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.card_post_user_name);
            location = (TextView) view.findViewById(R.id.card_post_location);
            status = (TextView) view.findViewById(R.id.card_post_status);
            description = (TextView) view.findViewById(R.id.card_post_description);
            upvote = (Button) view.findViewById(R.id.card_upvote);
            comment = (Button) view.findViewById(R.id.card_comment);
            spam = (Button) view.findViewById(R.id.card_spam);
            images = (ImageView) view.findViewById(R.id.card_post_image);
        }
    }

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Post post = postList.get(position);
        holder.username.setText(post.getUser_name()+"");
        if(post.getLocation()!=null)
            holder.location.setText(post.getLocation()+"");
        else
            holder.location.setText("Location Not Captured!");
        holder.upvote.setText(post.getUpvote()+"");
        holder.spam.setText(post.getSpam()+"");
        byte[] decodedByteArray = android.util.Base64.decode(post.getPic_url(), Base64.DEFAULT);
        Bitmap bitmap=BitmapFactory.decodeByteArray(decodedByteArray,0,decodedByteArray.length);
        holder.images.setImageBitmap(bitmap);
        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("grievances/"+post.getPost_id()+"/upvote").setValue(post.getUpvote()+1);
                holder.upvote.setEnabled(false);
                if(!holder.spam.isEnabled()){
                    holder.spam.setEnabled(true);
                    FirebaseDatabase.getInstance().getReference("grievances/"+post.getPost_id()+"/spam").setValue(post.getSpam()-1);

                }



            }
        });
        holder.spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("grievances/"+post.getPost_id()+"/spam").setValue(post.getSpam()+1);
                holder.spam.setEnabled(false);
                if(!holder.upvote.isEnabled()){
                    holder.upvote.setEnabled(true);
                    FirebaseDatabase.getInstance().getReference("grievances/"+post.getPost_id()+"/upvote").setValue(post.getUpvote()-1);

                }



            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,CommentActivty.class);
                intent.putExtra("POST_ID",post.getPost_id()+"");
                context.startActivity(intent);
            }
        });
        sta=post.getStatus();
        if(sta.equals("worst"))
        {
            holder.status.setTextColor(Color.rgb(255,0,0));
        }
        else if(sta.equals("reviewed"))
        {
            holder.status.setTextColor(Color.rgb(0,255,0));
        }
        else
        {
            holder.status.setTextColor(Color.rgb(255,255,0));
        }
        holder.status.setText(post.getStatus()+"");
        holder.description.setText(post.getDescription()+"");

    }

    public int getItemCount() {
        return postList.size();
    }
}
