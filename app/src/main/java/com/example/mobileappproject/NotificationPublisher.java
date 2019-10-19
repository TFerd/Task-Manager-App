package com.example.mobileappproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;


public class NotificationPublisher extends BroadcastReceiver {

    private static final String TAG = "NotificationPublisher";

    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_ID = "notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive: Called");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        Log.i(TAG, "onReceive: notification = " + NOTIFICATION);

        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
        Log.i(TAG, "onReceive: notificationID = " + NOTIFICATION_ID + "/" + notificationId);

        notificationManager.notify(notificationId, notification);

        Log.i(TAG, "onReceive: Finished");
    }


}
