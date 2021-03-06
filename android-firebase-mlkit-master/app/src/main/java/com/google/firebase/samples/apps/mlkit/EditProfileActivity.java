package com.google.firebase.samples.apps.mlkit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EditProfileActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private CircleImageView DisplayImage;
    private TextView ChangePhoto;
    private EditText UserName;
    private EditText Status;
    private EditText Age;
  //  private EditText AlternativeEmail;
    private EditText PhoneNumber;
    private ImageView SaveChanges;
    ProgressBar progressBar;


    private final static int Gallery_PIC = 1;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;

    private Bitmap thumbBitmap= null;
    private StorageReference thumbImageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mToolbar = findViewById(R.id.profileToolBar);
        setSupportActionBar(mToolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        //Sprite doublebounce = new DoubleBounce();
        Sprite cubegrid = new CubeGrid();
        progressBar.setIndeterminateDrawable(cubegrid);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        String online_UserId = mAuth.getCurrentUser().getUid();
        String current_emailId = mAuth.getCurrentUser().getEmail();

        if(current_emailId.equals("carecenter@gmail.com")){
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Admin").child(online_UserId);

            //Offline
            mDatabaseReference.keepSynced(true);

            //StorageReference
            mStorageReference = FirebaseStorage.getInstance().getReference().child("Profile_Images");

            //ThumbImageReference
            thumbImageReference = FirebaseStorage.getInstance().getReference().child("Thumb_Images");

            DisplayImage = (CircleImageView) findViewById(R.id.profile_photo);

            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //String name = dataSnapshot.child("user_name").getValue().toString();
                    //String status = dataSnapshot.child("user_status").getValue().toString();
                    final String image = dataSnapshot.child("user_image").getValue().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    //String thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();
                    if(!image.equals("Default Profile"))
                    {
                        // Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.profileimage).into(DisplayImage);

                        Picasso.with(EditProfileActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.profileimage).into(DisplayImage, new Callback() {
                            @Override
                            public void onSuccess()
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError()
                            {
                                Picasso.with(EditProfileActivity.this).load(image).placeholder(R.drawable.profileimage).into(DisplayImage);

                            }
                        });

                        // Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profileimage).into(DisplayImage);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {

            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(online_UserId);

            //Offline
            mDatabaseReference.keepSynced(true);

            //StorageReference
            mStorageReference = FirebaseStorage.getInstance().getReference().child("Profile_Images");


            //ThumbImageReference
            thumbImageReference = FirebaseStorage.getInstance().getReference().child("Thumb_Images");

            DisplayImage = (CircleImageView) findViewById(R.id.profile_photo);


            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                   // String name = dataSnapshot.child("user_name").getValue().toString();
                    //String status = dataSnapshot.child("user_status").getValue().toString();
                    final String image = dataSnapshot.child("user_image").getValue().toString();
                   // String thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();

                   // DisplayName.setText(name);
                   // DisplayStatus.setText(status);

                    if(!image.equals("Default Profile"))
                    {
                        //               Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.profileimage).into(DisplayImage);

                        Picasso.with(EditProfileActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.profileimage).into(DisplayImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                Picasso.with(EditProfileActivity.this).load(image).placeholder(R.drawable.profileimage).into(DisplayImage);

                            }
                        });

                        //Picasso.get().load(image).placeholder(R.drawable.profileimage).into(DisplayImage);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

       // DisplayImage = (CircleImageView) findViewById(R.id.profile_photo);
        ChangePhoto = (TextView) findViewById(R.id.changeProfilePhoto);
        UserName = (EditText) findViewById(R.id.username);
        Status = (EditText) findViewById(R.id.status);
        Age = (EditText) findViewById(R.id.age);
       // AlternativeEmail = (EditText) findViewById(R.id.alter_email);
        PhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        SaveChanges = (ImageView) findViewById(R.id.saveChanges);

        SaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = UserName.getText().toString();
                String status = Status.getText().toString();
                String age = Age.getText().toString();
                //String alter_email = AlternativeEmail.getText().toString();
                String phoneno=PhoneNumber.getText().toString();
                
                SavingChanges(username,status,age,phoneno);

            }
        });


        ChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_PIC);
            }
        });

    }

    private void SavingChanges(String username, String status, String age, String phoneno) {



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_PIC && resultCode == RESULT_OK && data!=null){
            Uri ImageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
               // loadingbar.setTitle("Updating Profile Image");
               // loadingbar.setMessage("Please wait..");
                //loadingbar.show();
                progressBar.setVisibility(View.VISIBLE);

                Uri resultUri = result.getUri();

                File thumb_filePathUri = new File(resultUri.getPath());


                String user_id = mAuth.getCurrentUser().getUid();

                try {
                    thumbBitmap = new Compressor(this).setMaxWidth(200).setMaxHeight(200).setQuality(50).compressToBitmap(thumb_filePathUri);
                }catch (IOException e){
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
                final byte[] thumbByte = byteArrayOutputStream.toByteArray();


                final StorageReference filePath = mStorageReference.child(user_id + ".jpg");
                final StorageReference thumbFilePath = thumbImageReference.child(user_id + ".jpg");




               /* filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this,"Saving your Profile Image...", Toast.LENGTH_LONG).show();

                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();

                            UploadTask uploadTask = thumbFilePath.putBytes(thumbByte);

                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task)
                                {

                                    String thumb_downloadUrl = thumb_task.getResult().getStorage().getDownloadUrl().toString();

                                    if(task.isSuccessful()){
                                        Map update_user_data = new HashMap();
                                        update_user_data.put("user_image",downloadUrl);
                                        update_user_data.put("user_thumb_image",thumb_downloadUrl);



                                        mDatabaseReference.updateChildren(update_user_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                Toast.makeText(SettingsActivity.this,"Image Updated Sucessfully", Toast.LENGTH_SHORT).show();
                                                loadingbar.dismiss();

                                            }
                                        });

                                    }
                                }
                            });



                        }
                        else{
                            Toast.makeText(SettingsActivity.this,"Error occured, while uploading your Image!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

                //add file on Firebase and got Download Link
                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull final Task<Uri> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(EditProfileActivity.this,"Saving your Profile Image...", Toast.LENGTH_LONG).show();
                            Uri downUri = task.getResult();
                            final String downloadUrl = downUri.toString();

                            UploadTask uploadTask = thumbFilePath.putBytes(thumbByte);

                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task)
                                {

                                    //String thumb_downloadUrl = thumb_task.getResult().getStorage().getDownloadUrl().toString();
                                    Uri thumbUri=task.getResult();
                                    String thumb_downloadUrl = thumbUri.toString();

                                    if(task.isSuccessful()){
                                        Map update_user_data = new HashMap();
                                        update_user_data.put("user_image",downloadUrl);
                                        update_user_data.put("user_thumb_image",thumb_downloadUrl);



                                        mDatabaseReference.updateChildren(update_user_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                Toast.makeText(EditProfileActivity.this,"Image Updated Sucessfully", Toast.LENGTH_SHORT).show();
                                              //  loadingbar.dismiss();
                                                progressBar.setVisibility(View.INVISIBLE);

                                            }
                                        });

                                    }
                                }
                            });

                            //Log.d(TAG, "onComplete: Url: "+ downUri.toString());
                        }
                        else{
                            Toast.makeText(EditProfileActivity.this,"Error occured, while uploading your Image!", Toast.LENGTH_SHORT).show();
                        }

                    }

                });//////////////////
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }
}
