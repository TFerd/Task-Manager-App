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
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//@Todo
//  Change the ArrayList<String> parameter to ArrayList<Task> when the Task class is set up.

//*********************************************************************
//* CustomAdapter class is for the ListView in the TaskFragment class *
//*********************************************************************

public class CustomAdapter extends BaseAdapter implements ListAdapter {

    private static final String TAG = "CustomAdapter";

    private ArrayList<Task> list;
    private Context context;


    public CustomAdapter(ArrayList<Task> list, Context context) {
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


        if (view == null) {
            Log.d(TAG, "getView: view == null passed");

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_layout, null);


            Log.i(TAG, "getView: View inflated successfully");
        }

        //**************************************************************************
        //**********THIS IS WHERE YOU CAN EDIT THE ListView XML ITEMS***************
        //**************************************************************************
        //Displays the String from the List
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_text);
        listItemText.setText(list.get(position).getTaskName());

        //Sets if the checkbox is checked based on notification status
        CheckBox notifyCheckBox = (CheckBox) view.findViewById(R.id.list_checkbox);
        notifyCheckBox.setChecked(list.get(position).isNotification());

        //Sets task description
        final TextView taskDescription = (TextView) view.findViewById(R.id.list_item_description);
        taskDescription.setText(list.get(position).getTaskDescription());

        //Button initializer
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_btn);
        ImageButton editButton = (ImageButton) view.findViewById(R.id.edit_btn);

        //Calendar builds the date and time which is set to the textView
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, list.get(position).getYear());
        calendar.set(Calendar.MONTH, list.get(position).getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, list.get(position).getDay());
        calendar.set(Calendar.HOUR, list.get(position).getHour());
        calendar.set(Calendar.MINUTE, list.get(position).getMinute());
        calendar.set(Calendar.AM_PM, Calendar.AM);

        SimpleDateFormat dtFormat = new SimpleDateFormat("EEE, d MMM yyyy \nhh:mm aaa");
        String date = dtFormat.format(calendar.getTime());

        final TextView taskDateTime = (TextView) view.findViewById(R.id.list_item_datetime);
        taskDateTime.setText(date);

        Log.i(TAG, "getView: Date is: " + date);



        //When the user clicks on the task name, the description will show, along with any other attributes that will be added later
        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (taskDescription.getVisibility() == View.GONE) {
                    taskDescription.setVisibility(View.VISIBLE);
                    taskDateTime.setVisibility(View.VISIBLE);

                } else {
                    taskDescription.setVisibility(View.GONE);
                    taskDateTime.setVisibility(View.GONE);

                }

                //taskDescription.setVisibility(View.GONE);

                Log.i(TAG, "onClick: Item clicked");
            }
        });

        //Button Handlers
        //Delete button has a confirmation screen to delete the task.
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: Delete button clicked. Task: " + getItem(position));


                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage(list.get(position).getTaskName() + " will be deleted. Are you sure you want to delete it?");
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


    //@TODO
    //  This is probably where we will add more task attributes.
    /*
    public void addItem(String task, boolean notify){

        Log.d(TAG, "addItem: called. WITHOUT DESCRIPTION");


        list.add(task);

        notifyDataSetChanged();

        Log.i(TAG, "addItem: completed. Task added.");

    }

    public void addItem(String task, String desc, boolean notify){

        Log.d(TAG, "addItem: called. WITH DESCRIPTION");




        list.add(task);
        notifyDataSetChanged();

        Log.i(TAG, "addItem: completed. Task added.");

    }*/

    public void addItem(Task task) {

        list.add(task);
        notifyDataSetChanged();

        Log.i(TAG, "addItem: Task added");

    }
}
