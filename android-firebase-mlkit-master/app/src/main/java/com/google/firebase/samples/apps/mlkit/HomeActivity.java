package com.google.firebase.samples.apps.mlkit;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public static Thread maskThread;

    //private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        maskThread = new Thread(new MaskThread(HomeActivity.this));
        maskThread.start();

        //Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_home);
        setupBottomNavigationView();


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mToolbar = findViewById(R.id.all_users_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    //BottomNavugationView Setup
    private void setupBottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
       // bottomNavigationViewEx.setItemBackground(0,R.color.colorAccent);
        //bottomNavigationViewEx.setIconSize(30);
        bottomNavigationViewEx.setIconSizeAt(2,30,30);
        bottomNavigationViewEx.setCurrentItem(0).enableShiftingMode(0,true);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(HomeActivity.this,bottomNavigationViewEx);

       // updateNavigationBarState(R.id.ic_house);

    }

   /* private void updateNavigationBarState(int actionId){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        Menu menu = bottomNavigationViewEx.getMenu();

        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == actionId);
        }
    }*/








    @Override
    protected void onStart(){
        super.onStart();

        currentUser = mAuth.getCurrentUser();

        if(currentUser==null)
        {

            logoutUser();
        }
        else if(currentUser!=null)
        {
            // mDatabaseReference.child("online").setValue("true");
          //  mDatabaseReference.child("online").setValue(true);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(currentUser!=null)
        {
            // mDatabaseReference.child("online").setValue(ServerValue.TIMESTAMP);
           // mDatabaseReference.child("online").setValue(false);

        }

    }

    private void logoutUser() {
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout_button){

            /* if(currentUser != null){
                 mDatabaseReference.child("online").setValue(ServerValue.TIMESTAMP);
             }*/

            mAuth.signOut();
            logoutUser();
        }
      /*  if(item.getItemId() == R.id.main_accountsetting_button){

            Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.main_allusers_button){

            Intent intent = new Intent(HomeActivity.this,AllUsersActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.main_addProducts_button){

            Intent intent = new Intent(HomeActivity.this,AddProductActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.main_pimple_button){
            Intent intent = new Intent(HomeActivity.this,PimpleProductActivity.class);
            startActivity(intent);

        }

        if(item.getItemId() == R.id.main_pigmentation_button){
            Intent intent = new Intent(HomeActivity.this,PigmentationProductActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.main_pours_button){
            Intent intent = new Intent(HomeActivity.this,PoursProductActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.main_wrinkles_button){
            Intent intent = new Intent(HomeActivity.this,WrinklesProductActivity.class);
            startActivity(intent);
        }*/
        if(item.getItemId()==R.id.main_camera){
            Intent intent = new Intent(HomeActivity.this,ChooserActivity.class);
            startActivity(intent);
        }

        return true;
    }
}
