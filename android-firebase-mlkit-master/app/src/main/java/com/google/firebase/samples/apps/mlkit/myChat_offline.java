package com.google.firebase.samples.apps.mlkit;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class myChat_offline extends Application {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    public void onCreate() {

        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //load Picture Offline - Picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //  online User
        if(currentUser!=null){

            String online_UserID = mAuth.getCurrentUser().getUid();
            String current_emailId = mAuth.getCurrentUser().getEmail();

            if(current_emailId.equals("carecenter@gmail.com")){
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Admin").child(online_UserID);

                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        mDatabaseReference.child("online").onDisconnect().setValue(false);
                      //  mDatabaseReference.child("online").setValue(true);



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });

            }
            else {
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(online_UserID);

                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        mDatabaseReference.child("online").onDisconnect().setValue(false);
                       // mDatabaseReference.child("online").setValue(true);



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });


            }



            //hhhhhhhhhhhh
        }
    }
}
