package com.google.firebase.samples.apps.mlkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseAuth mAuth;

    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsPagerAdapter myTabsPagerAdapter;
    //private Color white = Color.WHITE;

   private FirebaseUser currentUser;

   private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();




        if(currentUser!=null){

            String current_emailId = mAuth.getCurrentUser().getEmail();
            String online_UserID = mAuth.getCurrentUser().getUid();
            if(current_emailId.equals("carecenter@gmail.com")){
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Admin").child(online_UserID);

            }
            else{
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(online_UserID);
            }
        }

        //Tabs for MainActivity
        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsPagerAdapter);
        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);




        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        //mToolbar.setTitleTextAppearance(this,R.style.TextAppearance_AppCompat_Headline);
       // mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Care Center");


    }



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
            mDatabaseReference.child("online").setValue(true);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(currentUser!=null)
        {
           // mDatabaseReference.child("online").setValue(ServerValue.TIMESTAMP);
            mDatabaseReference.child("online").setValue(false);

        }

    }

    private void logoutUser() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
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
        /* if(item.getItemId() == R.id.main_accountsetting_button){

             Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
             startActivity(intent);
         }
        if(item.getItemId() == R.id.main_allusers_button){

            Intent intent = new Intent(MainActivity.this,AllUsersActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.main_addProducts_button){

            Intent intent = new Intent(MainActivity.this,AddProductActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.main_pimple_button){
            Intent intent = new Intent(MainActivity.this,PimpleProductActivity.class);
            startActivity(intent);

        }

        if(item.getItemId() == R.id.main_pigmentation_button){
            Intent intent = new Intent(MainActivity.this,PigmentationProductActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.main_pours_button){
            Intent intent = new Intent(MainActivity.this,PoursProductActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.main_wrinkles_button){
            Intent intent = new Intent(MainActivity.this,WrinklesProductActivity.class);
            startActivity(intent);
        }*/
        if(item.getItemId()==R.id.main_camera){
            Intent intent = new Intent(MainActivity.this,ChooserActivity.class);
            startActivity(intent);
        }

         return true;
    }
}
