package com.example.mobileappproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

//@Todo
//  Change the ArrayList<String> parameter to ArrayList<Task> when the Task class is set up.
//  Change the Add button to an Edit button and add an Add button somewhere else.
//  *** The add button can be kept at the bottom by using list.add(list.size() - 1, <task>) when you add a task***
//  ANOTHER WAY FOR ADD BUTTON: Set the buttons visibility to true (from GONE)


public class CustomAdapter extends BaseAdapter implements ListAdapter {

    private static final String TAG = "CustomAdapter";

    private ArrayList<String> list;
    private Context context;

    private int taskCount = 0;


    public CustomAdapter(ArrayList<String> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    //Leave this as return 0
    @Override
    public long getItemId(int position) {
        //return list.get(position).getId();
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Log.i(TAG, "getView: getItem(): " + getItem(position));
        Log.d(TAG, "getView: LIST COUNT: " + getCount());

        if(view == null){
            Log.d(TAG, "getView: view == null passed");
            
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_layout, null);

            
            Log.i(TAG, "getView: View inflated successfully");
        }

        //Displays the String from the List
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_text);
        listItemText.setText(list.get(position));

        //Button initializer
        Button deleteButton = (Button)view.findViewById(R.id.delete_btn);
        //final Button addButton = (Button)view.findViewById(R.id.add_btn);
        Button editButton = (Button)view.findViewById(R.id.edit_btn);

        //FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.addFAB);


        //Button Handlers
        //@TODO
        //  Add confirmation to delete items
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: Delete button clicked. Task: " + getItem(position));

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage(getItem(position) + " will be deleted. Are you sure you want to delete it?");
                builder.setCancelable(true);

                final int positionToDelete = position;

                //Negative button does nothing other than closing the pop-up
                builder.setNegativeButton("Cancel", null);

                //Positive button will delete the task
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(positionToDelete);
                        notifyDataSetChanged();


                        Log.i(TAG, "onClick: Task " + position + " deleted.");
                    }
                });

                AlertDialog confirmDeleteDialog = builder.create();
                confirmDeleteDialog.show();


                /*
                list.remove(position);
                notifyDataSetChanged();

                Log.i(TAG, "onClick: Task deleted");
                 */
            }
        });

        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                
                list.add("Task: " + ++taskCount);
                
                notifyDataSetChanged();

                Log.i(TAG, "onClick: Task added");
            }
        });

         */
        
/*
        //TODO@
        //  Add the AlertDialog here to change the details of the task.
        //This is where you add Tasks to the List i think
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //list.add();

                ++taskCount;

                //Adds the task to the ListView
                //The edit and delete button are GONE by default, so we set them visible here
                //We also set the add button to GONE so we only have one add button
                list.add(list.size(), "Task " + taskCount);



                notifyDataSetChanged();

                Toast toast = Toast.makeText(v.getContext(), "Added " + getItem(position).toString(), Toast.LENGTH_SHORT);
                toast.show();
                Log.i(TAG, "onClick: Task added");


            }
        });

 */



        //Edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: Edit button clicked.");

                notifyDataSetChanged();
            }
        });

        return view;
    }


    public void addItem(String task){

        Log.d(TAG, "addItem: called");

        list.add(task + ++taskCount);
        notifyDataSetChanged();

        Log.i(TAG, "addItem: completed. Task added.");

    }
}
