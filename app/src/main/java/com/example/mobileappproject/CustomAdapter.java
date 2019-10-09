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

        if(view == null){
            Log.d(TAG, "getView: view == null passed");
            
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_layout, null);

            
            Log.i(TAG, "getView: View inflated successfully");
        }

        //Displays the String from the List
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_text);
        listItemText.setText(list.get(position));

        //Button initializer
        final Button deleteButton = (Button)view.findViewById(R.id.delete_btn);
        final Button addButton = (Button)view.findViewById(R.id.add_btn);
        final Button editButton = (Button)view.findViewById(R.id.edit_btn);


        //Button Handlers
        //@TODO
        //  Add confirmation to delete items
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: Delete button clicked.");

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("This task will be deleted. Are you sure you want to delete it?");
                builder.setCancelable(true);

                //Negative button does nothing other than closing the pop-up
                builder.setNegativeButton("Cancel", null);

                //Positive button will delete the task
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        notifyDataSetChanged();

                        Log.i(TAG, "onClick: Task deleted.");
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


        //TODO@
        //  Add the AlertDialog here to change the details of the task.
        //This is where you add Tasks to the List i think
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //list.add();


                //Adds the task to the ListView
                //The edit and delete button are GONE by default, so we set them visible here
                //We also set the add button to GONE so we only have one add button
                list.add(list.size() - 1, "Added by button");
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                //listItemText.setText(View.VISIBLE);

                addButton.setVisibility(View.GONE);
                //v.findViewById(R.id.delete_btn).setVisibility(View.VISIBLE);
                //v.findViewById(R.id.add_btn).setVisibility(View.GONE);



                notifyDataSetChanged();

                Log.i(TAG, "onClick: Task added");


            }
        });



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
}
