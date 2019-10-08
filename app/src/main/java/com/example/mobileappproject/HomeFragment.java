package com.example.mobileappproject;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
*   I honestly don't know what to put in the Home Fragment.
*   I mean TaskFragment has the tasks right, and the calendar has the calendar.
*   So yeah, idk...
*
 */



public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    public HomeFragment(){
        Log.d(TAG, "constructed.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "created.");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}
