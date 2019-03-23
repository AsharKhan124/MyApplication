package com.google.firebase.samples.apps.mlkit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ProductsTabsPagerAdapter extends FragmentPagerAdapter {
    public ProductsTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                PimpleProductFragment pimpleProductFragment = new PimpleProductFragment();
                return pimpleProductFragment;

            case 1:
                PigmentationProductFragment pigmentationProductFragment = new PigmentationProductFragment();
                return pigmentationProductFragment;

            case 2:
                PoursProductFragment poursProductFragment = new PoursProductFragment();
                return poursProductFragment;

            case 3:
                WrinklesProductFragment wrinklesProductFragment = new WrinklesProductFragment();
                return wrinklesProductFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position){

        switch (position){

            case 0:
                return "Pimples";

            case 1:
                return "Pigmentation";

            case 2:
                return "Pours";

            case 3:
                return "Wrinkles";

            default:
                return null;
        }
    }

    @Override
    public float getPageWidth(int position) {
         super.getPageWidth(position);

         return 1f;


        }

}
