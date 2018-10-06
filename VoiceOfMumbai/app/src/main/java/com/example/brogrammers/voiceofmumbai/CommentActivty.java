package com.example.brogrammers.voiceofmumbai;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentActivty extends AppCompatActivity {
    String POST_ID;
    DatabaseReference reference;
    ImageButton sendButton;
    EditText editBox;
    RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<Comment> commentList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_activty);
        POST_ID=getIntent().getStringExtra("POST_ID");

        reference=FirebaseDatabase.getInstance().getReference("grievances/"+POST_ID+"/comments");
        editBox=findViewById(R.id.editBox);
        sendButton=findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=editBox.getText().toString();
                if(TextUtils.isEmpty(comment))
                    return;
                String commentId=System.currentTimeMillis()+"";
                String userName=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                editBox.setText("");
                reference.child(commentId).child("mComment").setValue(comment);
                reference.child(commentId).child("uName").setValue(userName);

            }
        });

        recyclerView=findViewById(R.id.recycler_view_comment);

        commentList=new ArrayList<>();
        adapter = new CommentAdapter(this, commentList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("TEST1234","1");
                commentList.clear();
                adapter.notifyDataSetChanged();
                for(DataSnapshot iGrievances:dataSnapshot.getChildren()){
                    Comment p=iGrievances.getValue(Comment.class);
                    Log.v("TEST1234",p.toString());
                    commentList.add(p);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
