package com.google.firebase.samples.apps.mlkit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog loadingbar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference Data;

    private EditText RegisterUserName;
    private EditText RegisterUserEmail;
    private EditText RegisterUserPassword;
    private Button CreateAccount;
    private TextView login;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.INVISIBLE);

        //Sprite doublebounce = new DoubleBounce();
        Sprite foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);

        RegisterUserName = (EditText) findViewById(R.id.register_name);
        RegisterUserEmail = (EditText) findViewById(R.id.register_email);
        RegisterUserPassword = (EditText) findViewById(R.id.register_password);

        CreateAccount = (Button) findViewById(R.id.create_account_button);
        login = (TextView) findViewById(R.id.login_txt);


        loadingbar = new ProgressDialog(this);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = RegisterUserName.getText().toString();
                final String email = RegisterUserEmail.getText().toString();
                String password = RegisterUserPassword.getText().toString();

                if(email.equals("carecenter@gmail.com")){
                    registerAdminAccount(name,email,password);
                }
                else{
                registerAccount(name,email,password);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setClickable(true);
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }



    private void registerAccount(final String name, final String email, String password) {
        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this,"Please write your name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this,"Please write your email", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"Please write your password", Toast.LENGTH_SHORT).show();
        }
        else{

            //loadingbar.setTitle("Creating New Account");
            //loadingbar.setMessage("Please Wait..");
            //loadingbar.show();
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
                        Data = FirebaseDatabase.getInstance().getReference().child("Data").child(currentUserId);

                        Data.child("user_name").setValue(name);
                        Data.child("user_email").setValue(email);
                        Data.child("user_thumb_image").setValue("Default Image");

                        mDatabaseReference.child("user_name").setValue(name);
                        mDatabaseReference.child("user_email").setValue(email);
                        mDatabaseReference.child("user_status").setValue("User Status");
                        mDatabaseReference.child("user_image").setValue("Default Profile");
                        mDatabaseReference.child("user_thumb_image").setValue("Default Image")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                });
                       /* Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();*/

                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Error Occured try Again.." , Toast.LENGTH_SHORT).show();
                    }

                    //loadingbar.dismiss();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }
    }





    private void registerAdminAccount(final String name, final String email, String password) {

        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this,"Please write your name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this,"Please write your email", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"Please write your password", Toast.LENGTH_SHORT).show();
        }

        else{

           // loadingbar.setTitle("Creating Admin Account");
           // loadingbar.setMessage("Please Wait..");
           // loadingbar.show();
            progressBar.setVisibility(View.VISIBLE);


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Admin").child(currentUserId);
                        Data = FirebaseDatabase.getInstance().getReference().child("Data").child(currentUserId);

                        Data.child("user_name").setValue(name);
                        Data.child("user_email").setValue(email);
                        Data.child("user_thumb_image").setValue("Default Image");



                        mDatabaseReference.child("user_name").setValue(name);
                        mDatabaseReference.child("user_email").setValue(email);
                        mDatabaseReference.child("user_status").setValue("User Status");
                        mDatabaseReference.child("user_image").setValue("Default Profile");
                        mDatabaseReference.child("user_thumb_image").setValue("Default Image")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                });
                       /* Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();*/

                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Error Occured try Again.." , Toast.LENGTH_SHORT).show();
                    }


                    //loadingbar.dismiss();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });




        }
    }
}
