package com.example.mobileappproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;


import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

/*
*   Using TabLayout to store the different Tab Fragments (Home, Task, CalendarFragment classes) and control them using PagerController class.
*   The individual fragments can be worked on in their respective Java Classes.
*
*   Using Youtube tutorial: https://www.youtube.com/watch?v=7HZmkaxB_PA
*   along with https://www.tutlane.com/tutorial/android/android-tabs-with-fragments-and-viewpager
*
*
*   USE:     Log.i(TAG, "insert message here");      FOR DEBUGGING PLEASE.
*   USE IT A LOT!!!
*
*   ALSO: Write a lot of comments
*
*
*   Currently, I don't know what the ToolBar does in the XML file, along with the setSupportActionBar(myToolbar) thing,
*   i was pretty much copying the youtube tutorial.
*   It seems like commenting-out the toolbar stuff has no effect on the program.
*
*
*   ####    IMPORTANT    ####
*   When working in the fragment class files, it is important to remember that the onCreate() method runs as soon as the program is launched
*   and before the fragment is even visible.
*
*   The onCreateView() method is called when the fragment is actually shown on the screen.
 */


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

        Log.i(TAG, "started.");

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
