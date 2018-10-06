package com.example.brogrammers.voiceofmumbai;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ProfileFragment extends Fragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Button signOutButton;
    FirebaseUser currUser=firebaseAuth.getCurrentUser();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference userReference=firebaseDatabase.getReference("users/"+currUser.getUid());
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        final EditText userNameEditText=(EditText)v.findViewById(R.id.fullName);
        final EditText addressEditText=(EditText)v.findViewById(R.id.address);
        final EditText emailIdEditText=(EditText)v.findViewById(R.id.emailId);
        signOutButton=v.findViewById(R.id.signOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
            }
        });
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("name")) {
                    userNameEditText.setText(dataSnapshot.child("name").getValue().toString());
                }else{
                    userNameEditText.setEnabled(true);
                }
                if(dataSnapshot.hasChild("emailId")) {
                    emailIdEditText.setText(dataSnapshot.child("emailId").getValue().toString());
                }else{
                    emailIdEditText.setEnabled(true);
                }
                if(dataSnapshot.hasChild("address")) {
                    addressEditText.setText(dataSnapshot.child("address").getValue().toString());
                }else{
                    addressEditText.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button b=v.findViewById(R.id.submitDetailsButton);
        ((TextView)v.findViewById(R.id.phoneNumber)).setText("Phone Number: "+currUser.getPhoneNumber());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String userName=(userNameEditText).getText().toString();
                String address=(addressEditText).getText().toString();
                String emailid=(emailIdEditText).getText().toString();
                if(TextUtils.isEmpty(userName)){
                    (userNameEditText).setError("Field Must not Be Empty");
                }else if(TextUtils.isEmpty((emailid)) || !Patterns.EMAIL_ADDRESS.matcher(emailid).matches()){
                    (emailIdEditText).setError("Field Is Empty Or Invalid Format");

                }

                else if(TextUtils.isEmpty(address)) {
                    (addressEditText).setError("Field Must not Be Empty");

                }


                    userReference.child("name").setValue(userName);
                    userReference.child("emailId").setValue(emailid);
                    userReference.child("address").setValue(address);
                    Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();

            }
        });
        return v;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
