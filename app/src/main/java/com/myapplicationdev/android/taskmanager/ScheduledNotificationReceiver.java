package com.myapplicationdev.android.taskmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class ScheduledNotificationReceiver extends BroadcastReceiver {

    int reqCode = 12345;
    int notificationID = 888;


    @Override
    public void onReceive(Context context, Intent intent)  {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel("default", "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("This is for default notification");
            notificationManager.createNotificationChannel(channel);
        }

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, reqCode,
                i, PendingIntent.FLAG_CANCEL_CURRENT);

        // build notification
        Bitmap picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.sentosa);
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    long[] vibrate = {0, 100, 200,300};

                   NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"default");
                   builder.setContentTitle("Task Manager Reminder");
                   builder.setContentText("Post Letters");
                   builder.setSmallIcon(android.R.drawable.ic_dialog_info);
                   builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(picture).bigLargeIcon(null));
                   builder.setSound(alarmSound);
                   builder.setVibrate(vibrate);
                   builder.setContentIntent(pIntent);
                   builder.setAutoCancel(true);

        Notification n = builder.build();
        notificationManager.notify(888, n);
    }
}
