package com.example.brogrammers.voiceofmumbai;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class ExploreFragment extends Fragment {
    RecyclerView recyclerView;
    private PostAdapter adapter;
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("grievances");
    private List<Post> postList;  public ExploreFragment() {
        // Required empty public constructor
    }
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_explore, container, false);
        recyclerView=v.findViewById(R.id.recycler_view);

        postList=new ArrayList<>();
        adapter = new PostAdapter(getActivity().getApplicationContext(), postList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Log.v("TEST1234","2");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("TEST1234","1");
                postList.clear();
                adapter.notifyDataSetChanged();
                for(DataSnapshot iGrievances:dataSnapshot.getChildren()){
                    Post p=iGrievances.getValue(Post.class);
                    p.setPost_id(Long.parseLong(iGrievances.getKey()));
                    Log.v("TEST1234",p.toString());
                    postList.add(p);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab=v.findViewById(R.id.newPostButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),UploadActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        return v;
    }


}
