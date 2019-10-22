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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TasksFragment extends Fragment {

    private static final String TAG = "TasksFragment";

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

        final DBSQLiteOpenHelper db = new DBSQLiteOpenHelper(view.getContext());

        taskArray = new ArrayList<>();

        taskArray = fillTasks(db);


        //Now you can use findViewById()
        //Make sure you start it like v.findViewById()
        listView = (ListView) view.findViewById(R.id.taskList);
        final CustomAdapter adapter = new CustomAdapter(taskArray, getContext());
        listView.setAdapter(adapter);


        //@TODO
        //  Add more options to the FAB, maybe like delete all? Or maybe don't add anything to it at all lol
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.addFAB);


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

                final EditText taskName = (EditText) dialog.findViewById(R.id.dialogInput);
                final EditText taskDesc = (EditText) dialog.findViewById(R.id.dialogDesc);

                final CheckBox taskNotify = (CheckBox) dialog.findViewById(R.id.dialog_notify);

                Button dialogOkBtn = (Button) dialog.findViewById(R.id.dialogOk);
                Button dialogCancel = (Button) dialog.findViewById(R.id.dialogCancel);

                final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                datePicker.setMinDate(System.currentTimeMillis() - 1000);                           //Sets the minimum date to the current date


                final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);

                //@TODO
                //  Dissolve the if statement for the description so that only one constructor is needed.
                //  Honestly this is really easy and i should have done it before but idk.
                //The on-click listener for the pop-up dialog's confirm button
                dialogOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = taskName.getText().toString();
                        String desc = taskDesc.getText().toString();
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();
                        int day = datePicker.getDayOfMonth();
                        boolean notify = taskNotify.isChecked();

                        // Add task to database
                        db.insertData(name, desc, hour, minute, month, day, year, notify, false);

                            /*
                                OUTPUTS STORED DATA, NOT NECESSARY TO RUN PROGRAM, DELETE AFTER DEBUGGING
                             */
                        Cursor res = db.getAllData();
                        if (res.getCount() == 0)
                            return;

                        int id = 0;

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("id: " + res.getString(0) + "\n");
                            buffer.append("taskname: " + res.getString(1) + "\n");
                            buffer.append("task description: " + res.getString(2) + "\n");
                            buffer.append("hour: " + res.getString(3) + "\n");
                            buffer.append("minute: " + res.getString(4) + "\n");
                            buffer.append("month: " + res.getString(5) + "\n");
                            buffer.append("day: " + res.getString(6) + "\n");
                            buffer.append("year: " + res.getString(7) + "\n");
                            buffer.append("notify: " + res.getString(8) + "\n");
                            buffer.append("complete: " + res.getString(9) + "\n");
                            buffer.append("\n");
                            id = res.getInt(0);
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setCancelable(true);
                        builder.setTitle("Data");
                        builder.setMessage(buffer);
                        builder.show();

                        if(name.length() > 0) {

                            //If the task does NOT have a description
                            if (taskDesc.getText().toString().length() < 1) {

                                Task task = new Task(id, taskName.getText().toString(), taskNotify.isChecked(),
                                        timePicker.getHour(), timePicker.getMinute(),
                                        datePicker.getMonth(), datePicker.getDayOfMonth(), datePicker.getYear());

                                adapter.addItem(task);


                                Log.i(TAG, "onClick: Task added WITHOUT description. Notifications = " + task.isNotification()
                                        + "\nThe tasks hour is: " + task.getHour() + " | The minute is: " + task.getMinute());

                                //scheduleNotification(task.getTaskName(), 3500);
                                //scheduleNotification(task);

                                //Adds a notification ONLY if notifications are checked
                                if (task.isNotification()) {
                                    scheduleNotification(task);

                                    Log.i(TAG, "onClick: Notification is " + task.isNotification() + ", notification scheduled...");
                                }

                                dialog.dismiss();
                            }

                            //If the task DOES have a description
                            else if (taskDesc.getText().toString().length() > 0) {

                                Task task = new Task(id, taskName.getText().toString(), taskDesc.getText().toString(), taskNotify.isChecked(),
                                        timePicker.getHour(), timePicker.getMinute(),
                                        datePicker.getMonth(), datePicker.getDayOfMonth(), datePicker.getYear());


                                adapter.addItem(task);


                                Log.i(TAG, "onClick: Task added WITH description. Notifications = " + task.isNotification()
                                        + "\nThe tasks hour is: " + task.getHour() + " | The minute is: " + task.getMinute());

                                //scheduleNotification(task.getTaskName(), 3500);
                                //scheduleNotification(task);

                                //Adds a notification ONLY if notifications are checked
                                if (task.isNotification() == true) {
                                    scheduleNotification(task);

                                    Log.i(TAG, "onClick: Notification is " + task.isNotification() + ", notification scheduled...");
                                }

                                dialog.dismiss();
                            }

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
    //@TODO
    //  Make it so if the user chooses NO notifications, and then edits to say YES notifications, it will
    //  call the notification.
    //  Also, add more notifications for a task. (Remind 1 hour before or 30 minutes before etc.)
    //  Also, add the ability to take the user back to the app when the notification is clicked.
    private void scheduleNotification(Task task){

        Log.i(TAG, "scheduleNotification: Called");

        Notification.Builder builder;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel = new NotificationChannel("default", "name", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Default notification channel.");
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);


            builder = new Notification.Builder(getContext(), "default");

            Log.i(TAG, "scheduleNotification: SDK is 26 or greater. Using notification channel: " + notificationChannel.getName());
        }
        else{
            builder = new Notification.Builder(getContext());

            Log.i(TAG, "scheduleNotification: SDK is lower than 26");
        }

        //builder.setContentTitle("Title");
        //builder.setContentText(task.getTaskName() + " notification");
        builder.setContentTitle("Reminder!");
        builder.setContentText(task.getTaskName() + " is scheduled for now!");

        builder.setSmallIcon(R.drawable.ic_alarm_black_24dp);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(getContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        //notificationIntent.putExtra("TASK", task);
        //The Bundle is for passing the task to the NotificationPublisher so that it can edit the task once the notification happens.
        Bundle taskBundle = new Bundle();
        taskBundle.putSerializable("taskKey", (Serializable) task);
        notificationIntent.putExtra("DATA", taskBundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        long timeTilNotif = SystemClock.elapsedRealtime() + calcTime(task);

        Log.i(TAG, "scheduleNotification: Time before notif: " + timeTilNotif);
        Date d = new Date(timeTilNotif);
        Log.i(TAG, "scheduleNotification: Date is: " + d);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeTilNotif, pendingIntent);

        Log.i(TAG, "scheduleNotification: Task hasBeenNotified pre-notification (Should return false): " + task.hasBeenNotified());

        Log.i(TAG, "scheduleNotification: Completed");


    }


    //This method calculates the time before the notification arrives.
    //Currently it just subtracts the date of the task to the current time to figure out the time buffer before the notification.
    //I think this function can be improved, as of right now, it doesn't notify the user right when the clock hits a certain minute.
    //If the task is scheduled for the next minute, then it will wait all 60 seconds before notifying, rather than notifying right when the clock hits the next minute.
    private long calcTime(Task task){

        long time = 0;

        time = task.getDate() - Calendar.getInstance().getTimeInMillis();

        Log.i(TAG, "calcTime: The elapsed time is: " + time);

        return time;
    }

    private ArrayList<Task> fillTasks(DBSQLiteOpenHelper db) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            return tasks;
        }
        while (res.moveToNext()) {
            int id = res.getInt(0);
            String name = res.getString(1);
            String desc = res.getString(2);
            int hour = res.getInt(3);
            int minute = res.getInt(4);
            int month = res.getInt(5);
            int day = res.getInt(6);
            int year = res.getInt(7);
            boolean notify = res.getInt(8) > 0;
            boolean complete = res.getInt(9) > 0;
            Task oldtask = new Task(id, name, desc, notify, hour, minute, month, day, year);
            tasks.add(oldtask);
        }
        return tasks;
    }


}
