package com.example.mobileappproject;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerController extends FragmentPagerAdapter {

    private static final String TAG = "PagerController";

    int tabCount;


    public PagerController(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
        Log.d(TAG, "tabCount is - " + tabCount);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.i(TAG, "Case 0 - HomeFragment()");
                return new HomeFragment();
            case 1:
                Log.i(TAG, "Case 1 - TasksFragment()");
                return new TasksFragment();
            case 2:
                Log.i(TAG, "Case 2 - CalendarFragment()");
                return new CalendarFragment();
            default:
                Log.wtf(TAG, "Case Default somehow reached, returning null...");
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
