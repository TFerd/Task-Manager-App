package com.example.mobileappproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



//*********************************************************************
//* CustomAdapter class is for the ListView in the TaskFragment class *
//*********************************************************************

public class CustomAdapter extends BaseAdapter implements ListAdapter {

    private static final String TAG = "CustomAdapter";

    private ArrayList<MyTask> list;
    private Context context;

    private TextView listItemText;

    public CustomAdapter(ArrayList<MyTask> list, Context context) {
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
        return 0;
    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
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
        listItemText = (TextView) view.findViewById(R.id.list_item_text);
        listItemText.setText(list.get(position).getTaskName());

        //Sets task description
        final TextView taskDescription = (TextView) view.findViewById(R.id.list_item_description);
        taskDescription.setText(list.get(position).getTaskDescription());

        final TextView scheduledForTextView = (TextView) view.findViewById(R.id.scheduled_for_text);

        //Button initializer
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_btn);
        ImageButton editButton = (ImageButton) view.findViewById(R.id.edit_btn);
        ImageButton directionButton = (ImageButton) view.findViewById(R.id.directions_btn);

        //Calendar builds the date and time which is set to the textView
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, list.get(position).getYear());
        calendar.set(Calendar.MONTH, list.get(position).getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, list.get(position).getDay());
        calendar.set(Calendar.HOUR, list.get(position).getHour());
        calendar.set(Calendar.MINUTE, list.get(position).getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        SimpleDateFormat dtFormat = new SimpleDateFormat("EEE, d MMM yyyy \nhh:mm aaa");
        String date = dtFormat.format(calendar.getTime());

        final TextView taskDateTime = (TextView) view.findViewById(R.id.list_item_datetime);
        taskDateTime.setText(date);

        Log.i(TAG, "getView: Date is: " + date);

        final DBSQLiteOpenHelper db = new DBSQLiteOpenHelper(view.getContext());


        //When the user clicks on the task name, the description will show, along with any other attributes that will be added later
        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (taskDescription.getVisibility() == View.GONE) {
                    taskDescription.setVisibility(View.VISIBLE);
                    taskDateTime.setVisibility(View.VISIBLE);
                    scheduledForTextView.setVisibility(View.VISIBLE);

                } else {
                    taskDescription.setVisibility(View.GONE);
                    taskDateTime.setVisibility(View.GONE);
                    scheduledForTextView.setVisibility(View.GONE);
                }

                Log.i(TAG, "onClick: Item clicked");
            }
        });

        //Button Handlers
        //Delete button has a confirmation screen to delete the task.
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: Delete button clicked. MyTask: " + getItem(position));


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
                        db.deleteData(list.get(positionToDelete).getId() + "");
                        list.remove(positionToDelete);
                        notifyDataSetChanged();

                        Log.i(TAG, "onClick: MyTask " + position + " deleted.");
                    }
                });

                AlertDialog confirmDeleteDialog = builder.create();
                confirmDeleteDialog.show();

            }
        });


        //Edit button onClick
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.custom_add_dialog);
                dialog.setTitle("Edit " + list.get(position).getTaskName());

                final EditText taskName = (EditText) dialog.findViewById(R.id.dialogInput);
                taskName.setText(list.get(position).getTaskName());

                final EditText taskDesc = (EditText) dialog.findViewById(R.id.dialogDesc);
                taskDesc.setText(list.get(position).getTaskDescription());

                final CheckBox taskNotify = (CheckBox) dialog.findViewById(R.id.dialog_notify);
                taskNotify.setChecked(list.get(position).isNotification());

                Button dialogOkBtn = (Button) dialog.findViewById(R.id.dialogOk);
                dialogOkBtn.setText("Confirm");

                Button dialogCancel = (Button) dialog.findViewById(R.id.dialogCancel);

                final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                datePicker.setMinDate(System.currentTimeMillis() - 1000);                           //Sets the minimum date to the current date


                final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);


                //The on-click listener for the pop-up dialog's confirm button
                dialogOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (taskName.getText().toString().length() > 0) {

                            list.get(position).setTaskName(taskName.getText().toString());
                            list.get(position).setTaskDescription(taskDesc.getText().toString());
                            list.get(position).setNotification(taskNotify.isChecked());
                            list.get(position).setHour(timePicker.getHour());
                            list.get(position).setMinute(timePicker.getMinute());
                            list.get(position).setMonth(datePicker.getMonth());
                            list.get(position).setDay(datePicker.getDayOfMonth());
                            list.get(position).setYear(datePicker.getYear());

                            MyTask myTask = list.get(position);

                            System.out.println(myTask.getId());
                            if (db.updateData(myTask.getId(), taskName.getText().toString(),
                                    taskDesc.getText().toString(), timePicker.getHour(), timePicker.getMinute(),
                                    datePicker.getMonth(), datePicker.getDayOfMonth(), datePicker.getYear(), taskNotify.isChecked(),
                                    myTask.isComplete(), myTask.getLocationId(), myTask.getLat(), myTask.getLng())){
                                Log.i(TAG, "onClick: MyTask edited.");
                            }
                            else
                                Log.i(TAG, "onClick: Failed MyTask edited.");

                            notifyDataSetChanged();

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

        //Directions button handler (Opens up Google Directions app)
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int taskId = list.get(position).getId();
                double lat = -1;
                double lng = -1;
                String placeIdTester = "";
                DBSQLiteOpenHelper db = new DBSQLiteOpenHelper(context);

                Cursor cursor = db.selectFrom(taskId);

                if(cursor.moveToFirst()){
                    placeIdTester = cursor.getString(10);
                    lat = cursor.getDouble(11);
                    lng = cursor.getDouble(12);

                    Log.i(TAG, "onClick: LATLNG: " + lat + ", " + lng);
                }

                if (placeIdTester.length() > 1 && !placeIdTester.isEmpty()) {
                    getDirections(lat, lng);
                    Log.i(TAG, "onClick: clicked.");
                }
                else{
                    Toast.makeText(context, "No location assigned.", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onClick: Place id is empty.");
                }
            }
        });
        return view;
    }

    public void addItem(MyTask myTask) {
        list.add(myTask);
        notifyDataSetChanged();

        Log.i(TAG, "addItem: MyTask added");
    }

    private void getDirections(final double lat, final double lng) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        
        builder.setMessage("Open Google Maps?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                        String latitude = String.valueOf(lat);
                        String longitude = String.valueOf(lng);
                        Uri intentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        try {
                                context.startActivity(mapIntent);
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.getMessage());
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }
}
