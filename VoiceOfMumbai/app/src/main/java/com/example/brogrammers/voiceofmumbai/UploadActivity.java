package com.example.brogrammers.voiceofmumbai;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

public class UploadActivity extends AppCompatActivity implements LocationListener {

    private static final int CAMERA_PIC_REQUEST = 1337;
    private LocationManager locationManager;
    private TextView locationContent;
    FirebaseUser currUser;
    DatabaseReference grievancesReferences=FirebaseDatabase.getInstance().getReference("grievances/");
    Button submitButton;
    ImageButton addImageButton;
    EditText descriptionEditBox;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ImageView imageview ; //sets imageview as the bitmap
    String filePath=null,loc,time;
 //   ProgressBar progressBar;
    double lattitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        currUser=FirebaseAuth.getInstance().getCurrentUser();
        descriptionEditBox=findViewById(R.id.descriptionEditText);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference =firebaseStorage.getReference("grievances/");
        addImageButton= findViewById(R.id.addImageButton);
        submitButton=findViewById(R.id.uploadPostButton);
        descriptionEditBox=findViewById(R.id.descriptionEditText);
        locationContent =  findViewById(R.id.content);
        imageview=findViewById(R.id.imageView);

      /*  DatabaseReference ref=FirebaseDatabase.getInstance().getReference("grievances/1538781189208");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url=dataSnapshot.child("pic_url").getValue().toString();
                 byte[] decodedByteArray = android.util.Base64.decode(url, Base64.DEFAULT);

                Bitmap bitmap=BitmapFactory.decodeByteArray(decodedByteArray,0,decodedByteArray.length);
                imageview.setImageBitmap(bitmap);
                Toast.makeText(UploadActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
        addImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   //progressBar.setVisibility(View.VISIBLE);
                time=""+System.currentTimeMillis();
                final String description=descriptionEditBox.getText().toString();
                if(TextUtils.isEmpty(description)){
                    descriptionEditBox.setError("Field Is mandatory");
                    //progressBar.setVisibility(View.INVISIBLE);
                    return;
                }else {

                    grievancesReferences.child(time+"/description").setValue(description);
                }
                if(filePath==null){
                    Toast.makeText(UploadActivity.this, "Please Upload a image", Toast.LENGTH_SHORT).show();
                    //progressBar.setVisibility(View.INVISIBLE);
                    return;
                }else{
                    Toast.makeText(UploadActivity.this, "Uploading And Posting....", Toast.LENGTH_SHORT).show();
                    /*storageReference.child(currUser.getUid()+"_"+time).putFile(filePath).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this, "Failed To Upload Image "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UploadActivity.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.INVISIBLE);
                            grievancesReferences.child(time+"/userUid").setValue(currUser.getUid());
                        }
                    }).addOn//progressListener(new On//progressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void on//progress(UploadTask.TaskSnapshot taskSnapshot) {
                            double //progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            //progressBar.set//progress((int)//progress);
                        }
                    });*/
                    FirebaseDatabase.getInstance().getReference().child("grievances/"+time+"/pic_url").setValue(filePath);
                    FirebaseDatabase.getInstance().getReference().child("grievances/"+time+"/user_id").setValue(currUser.getUid());
                    FirebaseDatabase.getInstance().getReference().child("grievances/"+time+"/user_name").setValue(currUser.getDisplayName());
                    FirebaseDatabase.getInstance().getReference().child("grievances/"+time+"/upvote").setValue(0);
                    FirebaseDatabase.getInstance().getReference().child("grievances/"+time+"/spam").setValue(0);

                    //progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UploadActivity.this, "Updated!", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        lattitude=location.getLatitude();
        longitude=location.getLongitude();
        //locationContent.setText("Latitude: " + lattitude + "\n Longitude: " + longitude);

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            loc=addresses.get(0).getAddressLine(0);
            String[] values = loc.split(",");
            time=""+System.currentTimeMillis();
            loc=values[values.length-4];
            locationContent.setText(values[values.length-4]);
            //locationContent.setText(locationContent.getText() + "\n"+addresses.get(0).getAddressLine(0));


            FirebaseDatabase.getInstance().getReference().child("grievances/"+time+"/location").setValue(loc);
            //locationContent.setText(loc);
        }catch(Exception e)
        {
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(UploadActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if(resultCode!=RESULT_OK)
                return;
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(image);
            Bundle bundle= data.getExtras();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            filePath=imageEncoded;

        }
    }
}
