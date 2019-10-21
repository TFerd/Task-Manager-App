package com.example.mobileappproject;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class TasksFragment extends Fragment {

    private static final String TAG = "TasksFragment";

    DBSQLiteOpenHelper db;

    ListView listView;

    //@TODO:
    //  Create an array of tasks somehow that the user will be able to add/edit/remove.
    //  Maybe make a Task class
    //ArrayList<String> taskArray;
    ArrayList<Task> taskArray;


    public TasksFragment() {
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
        listView = (ListView) view.findViewById(R.id.taskList);
        final CustomAdapter adapter = new CustomAdapter(taskArray, getContext());
        listView.setAdapter(adapter);


        //@TODO
        //  Add more options to the FAB, maybe like delete all? Or maybe dont add anything to it at all lol
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.addFAB);


        //@TODO
        //  Add a warning about adding tasks with the same name as another task
        //Creates a dialog popup when addTask is clicked.
        //This dialog is where the user will fill in the details of the task.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DBSQLiteOpenHelper db = new DBSQLiteOpenHelper(v.getContext());

                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.custom_add_dialog);
                dialog.setTitle("Create new task");

                final EditText taskName = (EditText) dialog.findViewById(R.id.dialogInput);
                final EditText taskDesc = (EditText) dialog.findViewById(R.id.dialogDesc);

                final CheckBox taskNotify = (CheckBox) dialog.findViewById(R.id.dialog_notify);

                Button dialogOkBtn = (Button) dialog.findViewById(R.id.dialogOk);
                Button dialogCancel = (Button) dialog.findViewById(R.id.dialogCancel);

                final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                datePicker.setMinDate(System.currentTimeMillis() - 1000);                           //Sets the minimum date to the current date


                final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);

                //The on-click listener for the pop-up dialog's confirm button
                dialogOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Name variables for repeated use
                        String name = taskName.getText().toString();
                        String desc = taskDesc.getText().toString();
                        boolean notify = taskNotify.isChecked();
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();
                        int day = datePicker.getDayOfMonth();

                        if (name.length() > 0) {
                            //If the task does NOT have a description add other variable

                            if (desc.toString().length() < 1) {

                                Task task = new Task(name, notify, hour, minute, month, day, year);

                                adapter.addItem(task);


                                Log.i(TAG, "onClick: Task added WITHOUT description. Notifications = " + notify
                                        + "\nThe tasks hour is: " + hour + " | The minute is: " + minute);

                                scheduleNotification(task.getTaskName(), 3500);

                                dialog.dismiss();
                            }

                            //If the task DOES have a description
                            else if (desc.toString().length() > 0) {

                                Task task = new Task(name, desc,notify,
                                        hour, minute, month, day, year);


                                adapter.addItem(task);


                                Log.i(TAG, "onClick: Task added WITH description. Notifications = " + notify
                                        + "\nThe tasks hour is: " + hour + " | The minute is: " + minute);

                                dialog.dismiss();
                            }

                            // Add task to database
                            db.insertData(name, desc, hour, minute, month, day, year, notify, false);

                            /*
                                OUTPUTS STORED DATA, NOT NECESSARY TO RUN PROGRAM, DELETE AFTER DEBUGGING
                             */
                            Cursor res = db.getAllData();
                            if(res.getCount() == 0)
                                return;

                            StringBuffer buffer = new StringBuffer();
                            while(res.moveToNext()){
                                buffer.append("id: "+ res.getString(0)+"\n");
                                buffer.append("taskname: "+ res.getString(1)+"\n");
                                buffer.append("task description: "+ res.getString(2)+"\n");
                                buffer.append("hour: "+ res.getString(3)+"\n");
                                buffer.append("minute: "+ res.getString(4)+"\n");
                                buffer.append("month: "+ res.getString(5)+"\n");
                                buffer.append("day: "+ res.getString(6)+"\n");
                                buffer.append("year: "+ res.getString(7)+"\n");
                                buffer.append("notify: "+ res.getString(8)+"\n");
                                buffer.append("complete: "+ res.getString(9)+"\n");
                                buffer.append("\n");
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setCancelable(true);
                            builder.setTitle("Data");
                            builder.setMessage(buffer);
                            builder.show();

                            /*
                                END OF DELETEABLE FILES
                             */
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

    //*******************************************************************************
    //***************************** NOTIFICATION METHOD *****************************               //Edit stuff here for the notification when the time comes
    //*******************************************************************************
    private void scheduleNotification(String taskName, int delay){

        Log.i(TAG, "scheduleNotification: Called");

        Notification.Builder builder;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel = new NotificationChannel("default", "name", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("description");
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);


            builder = new Notification.Builder(getContext(), "default");

            Log.i(TAG, "scheduleNotification: SDK is 26 or greater. Using notification channel: " + notificationChannel.getName());
        }
        else{
            builder = new Notification.Builder(getContext());

            Log.i(TAG, "scheduleNotification: SDK is lower than 26");
        }

        builder.setContentTitle("Title");
        builder.setContentText(taskName + " notiictaion");
        builder.setSmallIcon(R.drawable.add_circle);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(getContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long timeTilNotif = SystemClock.elapsedRealtime() + delay;
        Log.i(TAG, "scheduleNotification: Time before notif: " + timeTilNotif);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeTilNotif, pendingIntent);

        Log.i(TAG, "scheduleNotification: Completed");

    }


}
