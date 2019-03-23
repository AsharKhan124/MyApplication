package com.google.firebase.samples.apps.mlkit;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class RecommendedProductsActivity extends AppCompatActivity {

    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private ProductsTabsPagerAdapter myTabsPagerAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_products);

        setupBottomNavigationView();

        //Tabs for MainActivity
        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsPagerAdapter = new ProductsTabsPagerAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsPagerAdapter);
        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);

        myTabLayout.setTabGravity(TabLayout.INDICATOR_GRAVITY_STRETCH);


        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        //mToolbar.setTitleTextAppearance(this,R.style.TextAppearance_AppCompat_Headline);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Recommended Products");
        //getSupportActionBar().setIcon(R.drawable.ic_recommended_product);
        mToolbar.setNavigationIcon(R.drawable.ic_recommended_product);
    }

    //BottomNavugationView Setup
    private void setupBottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        //bottomNavigationViewEx.setItemBackground(3,R.color.colorAccent);
        //bottomNavigationViewEx.setIconSize(30);
        bottomNavigationViewEx.setIconSizeAt(2,30,30);
        bottomNavigationViewEx.setCurrentItem(3).enableShiftingMode(3,true);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(RecommendedProductsActivity.this,bottomNavigationViewEx);

       // updateNavigationBarState(R.id.ic_product);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bucket_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.bucket){

            Intent intent = new Intent(RecommendedProductsActivity.this,BucketActivity.class);
            startActivity(intent);
        }

        return true;
    }


}
