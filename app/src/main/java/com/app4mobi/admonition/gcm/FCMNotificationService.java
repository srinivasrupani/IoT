package com.app4mobi.admonition.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app4mobi.admonition.R;
import com.app4mobi.admonition.activity.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Srinivas Rupani on 3/1/2017.
 */

public class FCMNotificationService extends FirebaseMessagingService {
    public static final int NOTIFICATION_ID = 1000;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    private int notificationCount;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.e("Admo", "From: " + remoteMessage.getFrom());
        Log.e("Admo", "Notification Message Body: " + remoteMessage.getNotification().getBody());
        sendNotification(remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String notificationMessage) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), new Intent(this,
            LoginActivity.class), 0);
        //vibrate
        long[] v = {500, 1000};
        mBuilder = new NotificationCompat.Builder(
            this).setSmallIcon(R.drawable.ic_menu_camera)
            .setContentTitle("FCM Notification")
            .setContentText(notificationMessage)
            .setVibrate(v)
            .setSound(soundUri)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMessage));
        //            .addAction(R.drawable.ic_action_document_purple, "View", contentIntent)
        //            .addAction(0, "Remind", contentIntent);
        //mBuilder = ++notificationCount;

        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
