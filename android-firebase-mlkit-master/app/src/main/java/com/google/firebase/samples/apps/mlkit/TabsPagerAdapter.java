package com.google.firebase.samples.apps.mlkit;

/**
 * Created by Hp on 1/27/2019.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
               // RequestsFragment requestsFragment = new RequestsFragment();
                //return requestsFragment;
            UsersFragment usersFragment = new UsersFragment();
            return usersFragment;

            case 1:
                RecommendedProductsFragment recommendedProductsFragment = new RecommendedProductsFragment();
                return recommendedProductsFragment;

             //case 2:
                //UsersFragment usersFragment = new UsersFragment();
                //return usersFragment;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        switch (position){

            case 0:
                return "Care Center";

            case 1:
                return "Recommended Products";

           // case 2:
             //   return "Friends";

            default:
                return null;
        }
    }
}
