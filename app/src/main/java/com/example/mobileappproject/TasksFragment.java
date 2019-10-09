package com.example.mobileappproject;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/*
* @TODO: Add a ListView and try to implement the plus arrow icon to add tasks. Arrow found in the res/drawable folder.
*   ACTUALLY: TRY A RecyclerView.
*   ACTUALLY x2: Maybe a ListView is fine after all lol.
*
*
* The plus sign might be at the top of the ListView. If there are tasks, then find the very bottom task and put
* the plus sign under it.
*
*
 */

public class TasksFragment extends Fragment {

    private static final String TAG = "TasksFragment";

    ListView listView;
    //ArrayAdapter adapter;

    //@TODO:
    //  Create an array of tasks somehow that the user will be able to add/edit/remove.
    //  Maybe make a Task class
    ArrayList<String> taskArray;


    public TasksFragment(){
        Log.d(TAG, "constructed.");
    }

    //@TODO
    //  Maybe move the declaration of the ListView to the onCreate method, which is only declared once i think?
    //  Also i think i cant use findViewById here, stackoverflow says that u can only use it after onCreateView()???
    //@TODO
    //  OKAY NEVERMIND JUST USE THE onCreateView() for findViewById(), it should work...

    /*
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    */

    //***************************************************************************
    //IF YOU REPLACE 'return' WITH A VIEW VARIABLE THEN U CAN USE findViewById()*
    //***************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "created.");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        taskArray = new ArrayList<>();
        taskArray.add("");
        /*
        taskArray.add("Task 1");
        taskArray.add("Task 2");
        taskArray.add("Task 3");

         */


        //Now you can use findViewById()
        //Make sure you start it like v.findViewById()
        listView = (ListView)view.findViewById(R.id.taskList);
        CustomAdapter adapter = new CustomAdapter(taskArray, getContext());
        listView.setAdapter(adapter);

        //adapter.add("Task 4 added by adapter");

        return view;
    }


}
