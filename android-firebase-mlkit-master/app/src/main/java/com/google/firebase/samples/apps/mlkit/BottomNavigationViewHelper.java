package com.google.firebase.samples.apps.mlkit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;


import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewMe1";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG,"setupBottomNavigationView: Setting Up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(true);
       // bottomNavigationViewEx.setItemBackground(bottomNavigationViewEx.getCurrentItem(), Color.WHITE);
       // bottomNavigationViewEx.enableItemShiftingMode(false);
        //bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
      //  bottomNavigationViewEx.setCurrentItem(3).ic
        //bottomNavigationViewEx.enableShiftingMode(3,false);



    }

    public static void enableNavigation(final Context context, final BottomNavigationViewEx bottomNavigationViewEx){
     bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

             switch (menuItem.getItemId()){
                 case R.id.ic_house:

                     Intent intent = new Intent(context, HomeActivity.class);
                     context.startActivity(intent);
                     menuItem.setChecked(true);
                     break;
                 case R.id.ic_chat:
                     Intent intent1 = new Intent(context, CareCenterActivity.class);
                     //bottomNavigationViewEx.setVisibility(View.INVISIBLE);
                     context.startActivity(intent1);
                     menuItem.setChecked(true);
                     break;

                 case R.id.ic_camera:
                     Intent intent2 = new Intent(context, LivePreviewActivity.class);
                     context.startActivity(intent2);
                     menuItem.setChecked(true);
                     break;

                 case R.id.ic_product:
                     Intent intent3 = new Intent(context,RecommendedProductsActivity.class);
                     context.startActivity(intent3);
                     menuItem.setChecked(true);
                     break;

                 case R.id.ic_personal_profile:
                     Intent intent4 = new Intent(context,UserProfileActivity.class);
                     context.startActivity(intent4);
                     menuItem.setChecked(true);
                     break;


             }

             return false;
         }
     });

    }
}
