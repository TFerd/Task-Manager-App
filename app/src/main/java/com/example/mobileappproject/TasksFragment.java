package com.example.mobileappproject;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

        taskArray = new ArrayList<>();


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

                        //If the task does NOT have a description
                        if (taskName.getText().toString().length() > 0 && taskDesc.getText().toString().length() < 1) {

                            Task task = new Task(taskName.getText().toString(), taskNotify.isChecked(),
                                    timePicker.getHour(), timePicker.getMinute(),
                                    datePicker.getMonth(), datePicker.getDayOfMonth(), datePicker.getYear());

                            adapter.addItem(task);


                            Log.i(TAG, "onClick: Task added WITHOUT description. Notifications = " + task.isNotification()
                            + "\nThe tasks hour is: " + task.getHour() + " | The minute is: " + task.getMinute());

                            //scheduleNotification(task.getTaskName(), 3500);
                            //scheduleNotification(task);

                            //Adds a notification ONLY if notifications are checked
                            if (task.isNotification()){
                                scheduleNotification(task);

                                Log.i(TAG, "onClick: Notification is " + task.isNotification() + ", notification scheduled...");
                            }

                            dialog.dismiss();
                        }

                        //If the task DOES have a description
                        else if (taskName.getText().toString().length() > 0 && taskDesc.getText().toString().length() > 0) {

                            Task task = new Task(taskName.getText().toString(), taskDesc.getText().toString(), taskNotify.isChecked(),
                                    timePicker.getHour(), timePicker.getMinute(),
                                    datePicker.getMonth(), datePicker.getDayOfMonth(), datePicker.getYear());


                            adapter.addItem(task);


                            Log.i(TAG, "onClick: Task added WITH description. Notifications = " + task.isNotification()
                            + "\nThe tasks hour is: " + task.getHour() + " | The minute is: " + task.getMinute());

                            //scheduleNotification(task.getTaskName(), 3500);
                            //scheduleNotification(task);

                            //Adds a notification ONLY if notifications are checked
                            if (task.isNotification() == true){
                                scheduleNotification(task);

                                Log.i(TAG, "onClick: Notification is " + task.isNotification() + ", notification scheduled...");
                            }

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


}
