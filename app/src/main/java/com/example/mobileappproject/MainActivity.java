package com.example.mobileappproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.util.Log;


import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Toolbar myToolbar;
    TabLayout myTabLayout;
    TabItem itemHome, itemTasks, itemCalendar;
    ViewPager myPager;

    PagerController myPagerController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "started.");

        //Toolbar
        myToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //TabLayout
        myTabLayout = (TabLayout)findViewById(R.id.tabLayout);

        //Tab Items
        itemHome = (TabItem)findViewById(R.id.tabHome);
        itemTasks = (TabItem)findViewById(R.id.tabTasks);
        itemCalendar = (TabItem)findViewById(R.id.tabCalendar);

        //ViewPager
        myPager = (ViewPager)findViewById(R.id.viewPager);

        //Setting up the Pager Controller
        myPagerController = new PagerController(getSupportFragmentManager(), myTabLayout.getTabCount());
        myPager.setAdapter(myPagerController);

        //This Listener is how the view changes based on which tab is selected
        myTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                myPager.setCurrentItem(tab.getPosition());

                Log.i(TAG, "Tab changed to - " + myPager.getCurrentItem());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Adding an on page change listener
        myPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myTabLayout));
    }

}
