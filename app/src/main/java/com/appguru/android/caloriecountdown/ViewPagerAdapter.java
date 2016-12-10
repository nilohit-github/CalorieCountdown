package com.appguru.android.caloriecountdown;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by jhani on 11/12/2016.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new AddFoodFragment();
            case 1:
                return new MacronutrientFragment();
            case 2:
                return new ProgressFragment();
            case 3:
                return new ProfileFragment();

        }
           return null;
    }

    @Override
    public int getCount()
    {
        return 4;           // As there are only 3 Tabs
    }



}
