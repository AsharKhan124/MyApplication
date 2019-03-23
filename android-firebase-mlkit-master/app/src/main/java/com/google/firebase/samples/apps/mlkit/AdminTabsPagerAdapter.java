package com.google.firebase.samples.apps.mlkit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdminTabsPagerAdapter extends FragmentPagerAdapter {
    public AdminTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                UsersFragment usersFragment = new UsersFragment();
                return usersFragment;

            case 1:
                AddProductFragment addProductFragment = new AddProductFragment();
                return addProductFragment;

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
                return "Users";

            case 1:
                return "Add Products";

            // case 2:
            //   return "Friends";

            default:
                return null;
        }
    }
}
