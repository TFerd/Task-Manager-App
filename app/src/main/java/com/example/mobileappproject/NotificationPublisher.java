package com.example.mobileappproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;


public class NotificationPublisher extends BroadcastReceiver {

    private static final String TAG = "NotificationPublisher";

    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_ID = "notification_id";

    //This method is called when the notification actually happens
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive: Called");


        //Receives the Task from the intent as a bundle
        //Task task = (Task) intent.getSerializableExtra("TASK");
        Bundle receiveBundle = intent.getBundleExtra("DATA");
        Task task = (Task)receiveBundle.getSerializable("taskKey");

        Log.i(TAG, "onReceive: Tasks notifications are set to: " + task.isNotification());

        Log.i(TAG, "onReceive: Task hasBeenNotified pre-notification (Should return false): " + task.hasBeenNotified());

        //Checks if the notification is still true for the task
        if (task.isNotification()) {

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //Receives the notification from the intent
            Notification notification = intent.getParcelableExtra(NOTIFICATION);
            Log.i(TAG, "onReceive: notification = " + NOTIFICATION);

            //Receives the notification ID from the intent
            int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
            Log.i(TAG, "onReceive: notificationID = " + NOTIFICATION_ID + "/" + notificationId);

            //Creates the notification with the attributes from the intent
            notificationManager.notify(notificationId, notification);


            Log.i(TAG, "onReceive: Task received is taskName: " + task.getTaskName());

            //Sets the Task as notified
            task.setHasBeenNotified(true);

            Log.i(TAG, "onReceive: Task hasBeenNotified post-notification (Should return true): " + task.hasBeenNotified());

            Log.i(TAG, "onReceive: Finished");
        }
        else {
            Log.i(TAG, "onReceive: isNotification is false.");
        }
    }


}
