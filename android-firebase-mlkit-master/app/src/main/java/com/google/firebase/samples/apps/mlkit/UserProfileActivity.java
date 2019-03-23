package com.google.firebase.samples.apps.mlkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class UserProfileActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    Button edit_Profile_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setupBottomNavigationView();

        mToolbar = findViewById(R.id.all_users_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        mToolbar.setNavigationIcon(R.drawable.ic_prof);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_Profile_button = (Button) findViewById(R.id.EditProfileButton);

        edit_Profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }



    //BottomNavugationView Setup
    private void setupBottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        // bottomNavigationViewEx.setItemBackground(4,R.color.colorAccent);
        bottomNavigationViewEx.setIconSizeAt(2,30,30);
        bottomNavigationViewEx.setCurrentItem(4).enableShiftingMode(4,true);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(UserProfileActivity.this,bottomNavigationViewEx);

        //updateNavigationBarState(R.id.ic_buckett);

    }





}
