package com.example.brogrammers.voiceofmumbai;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ActionBar toolbar;
    String UUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        if(mUser==null) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            return;
        }
        UUID=mUser.getUid();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference userreference=firebaseDatabase.getReference("users/"+UUID);

        toolbar=getSupportActionBar();
        final BottomNavigationView navigation = (BottomNavigationView ) findViewById(R.id.navigationView);

        userreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                if(dataSnapshot.hasChild("name")) {
                    toolbar.setTitle("Explore");
                    loadFragment(new ExploreFragment());
                }
                else {
                    toolbar.setTitle("Profile");
                    loadFragment(new ProfileFragment());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_recents:
                    toolbar.setTitle("Explore");
                    loadFragment(new ExploreFragment());
                    return true;

                case R.id.navigation_stats:
                    toolbar.setTitle("Maps");
                  //  loadFragment(new MapFragment());
                    return true;

                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    loadFragment(new ProfileFragment());
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
