package com.google.firebase.samples.apps.mlkit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private Button SendFriendRequest;
    private Button DeclineFriendRequest;
    private TextView ProfileName;
    private TextView ProfileStatus;
    private ImageView ProfileImage;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        String current_emailId = mAuth.getCurrentUser().getEmail();

        if(current_emailId.equals("carecenter@gmail.com")){
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

            // Offline
            mDatabaseReference.keepSynced(true);
        }
        else{
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");

            // Offline
            mDatabaseReference.keepSynced(true);
        }

        String visit_user_id = getIntent().getExtras().get("visit_user_id").toString();

        //Toast.makeText(ProfileActivity.this,visit_user_id,Toast.LENGTH_SHORT).show();


        ProfileName = (TextView) findViewById(R.id.profileVisitUserName);
        ProfileStatus = (TextView) findViewById(R.id.profileVisitUserStatus);
        ProfileImage = (ImageView) findViewById(R.id.profileVisitUserImage);

        mDatabaseReference.child(visit_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                String name = dataSnapshot.child("user_name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                final String image = dataSnapshot.child("user_image").getValue().toString();






                ProfileName.setText(name);
                ProfileStatus.setText(status);



     //           Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profileimage).into(ProfileImage);
                Picasso.with(ProfileActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.profileimage).into(ProfileImage, new Callback() {
                    @Override
                    public void onSuccess()
                    {

                    }

                    @Override
                    public void onError()
                    {

                        Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profileimage).into(ProfileImage);
                    }
                });
                //Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profileimage).into(ProfileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
