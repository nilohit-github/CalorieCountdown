package com.appguru.android.caloriecountdown;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity implements AddFoodFragment.Callback {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        //setSupportActionBar(toolbar);

        /*
        TabLayout.newTab() method creates a tab view, Now a Tab view is not the view
        which is below the tabs, its the tab itself.
         */

        final TabLayout.Tab AddFood = tabLayout.newTab();
        final TabLayout.Tab Macronutrient = tabLayout.newTab();
        final TabLayout.Tab Progress = tabLayout.newTab();
        final TabLayout.Tab Profile = tabLayout.newTab();

        /*
        Setting Title text for our tabs respectively
         */

        AddFood.setText("AddFood");
        Macronutrient.setText("Nutrients");
        Progress.setText("Progress");
        Profile.setText("Profile");

        /*
        Adding the tab view to our tablayout at appropriate positions
        As I want home at first position I am passing home and 0 as argument to
        the tablayout and like wise for other tabs as well
         */
        tabLayout.addTab(AddFood, 0);
        tabLayout.addTab(Macronutrient, 1);
        tabLayout.addTab(Progress, 2);
        tabLayout.addTab(Profile, 3);

        /*
        TabTextColor sets the color for the title of the tabs, passing a ColorStateList here makes
        tab change colors in different situations such as selected, active, inactive etc

        TabIndicatorColor sets the color for the indiactor below the tabs
         */

       // tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        //tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.indicator));

        /*
        Adding a onPageChangeListener to the viewPager
        1st we add the PageChangeListener and pass a TabLayoutPageChangeListener so that Tabs Selection
        changes when a viewpager page changes.
         */

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int curr = viewPager.getCurrentItem();
            if(curr == 0){
                Intent intent = new Intent(this, LoginActivity.class);
                                startActivity(intent);
            }
            viewPager.setCurrentItem(0, true);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    /**
     * DetailFragmentCallback for when an item has been selected.
     *
     * @param username
     * @param foodItem
     */
    @Override
    public void onSearchClick(String username, String foodItem) {

        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("fooditem", foodItem);
        Log.v(" main fragment user", "umain:::::: " +username );
        Log.v(" main fragment food", "ufood:::::: " +foodItem );

      //  startActivity(intent);

    }
}



