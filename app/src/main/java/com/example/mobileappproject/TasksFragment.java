package com.example.mobileappproject;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


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
        final View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        taskArray = new ArrayList<>();


        //Now you can use findViewById()
        //Make sure you start it like v.findViewById()
        listView = (ListView)view.findViewById(R.id.taskList);
        final CustomAdapter adapter = new CustomAdapter(taskArray, getContext());
        listView.setAdapter(adapter);


        //@TODO
        //  Add more options to the FAB, maybe like delete all? Or maybe dont add anything to it at all lol
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.addFAB);


        //Creates a dialog popup when addTask is clicked.
        //This dialog is where the user will fill in the details of the task.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.custom_add_dialog);
                dialog.setTitle("Create new task");

                final EditText editText = (EditText)dialog.findViewById(R.id.dialogInput);
                Button dialogOkBtn = (Button)dialog.findViewById(R.id.dialogOk);
                Button dialogCancel = (Button)dialog.findViewById(R.id.dialogCancel);


                dialogOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().toString().length() > 0) {
                            adapter.addItem(editText.getText().toString());
                            dialog.dismiss();
                        }

                        else {
                            editText.setError("Cannot be blank!");
                        }
                    }
                });
                
                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        Log.i(TAG, "onClick: Dialog dismissed.");
                    }
                });
                
                dialog.show();

                Log.i(TAG, "onClick: Dialog shown.");
            }
        });

        return view;
    }


}
