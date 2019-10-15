package com.example.mobileappproject;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
    //ArrayList<String> taskArray;
    ArrayList<Task> taskArray;


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


        //@TODO
        //  Add a warning about adding tasks with the same name as another task
        //Creates a dialog popup when addTask is clicked.
        //This dialog is where the user will fill in the details of the task.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.custom_add_dialog);
                dialog.setTitle("Create new task");

                final EditText taskName = (EditText)dialog.findViewById(R.id.dialogInput);
                final EditText taskDesc = (EditText)dialog.findViewById(R.id.dialogDesc);

                final CheckBox taskNotify = (CheckBox)dialog.findViewById(R.id.dialog_notify);

                Button dialogOkBtn = (Button)dialog.findViewById(R.id.dialogOk);
                Button dialogCancel = (Button)dialog.findViewById(R.id.dialogCancel);

                final CheckBox cb = (CheckBox)dialog.findViewById(R.id.list_checkbox);

                //The on-click listener for the pop-up dialog's confirm button
                dialogOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //If the task does NOT have a description
                        if (taskName.getText().toString().length() > 0 && taskDesc.getText().toString().length() < 1) {

                            Task task = new Task(taskName.getText().toString(), taskNotify.isChecked());

                            //adapter.addItem(taskName.getText().toString());
                            //adapter.addItem(task.getTaskName(), task.isNotification());
                            adapter.addItem(task);

                            Log.i(TAG, "onClick: Task added WITHOUT description. Notifications = " + task.isNotification());

                            dialog.dismiss();
                        }

                        //If the task DOES have a description
                        else if (taskName.getText().toString().length() > 0 && taskDesc.getText().toString().length() > 0){

                            Task task = new Task(taskName.getText().toString(), taskDesc.getText().toString(), taskNotify.isChecked());

                            //adapter.addItem(task.getTaskName(), task.getTaskDescription(), task.isNotification());
                            adapter.addItem(task);


                            Log.i(TAG, "onClick: Task added WITH description. Notifications = " + task.isNotification());

                            dialog.dismiss();
                        }

                        //Gives an error if there is no task name
                        else {
                            taskName.setError("Cannot be blank!");
                        }
                    }
                });

                //Dismisses the dialog
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
