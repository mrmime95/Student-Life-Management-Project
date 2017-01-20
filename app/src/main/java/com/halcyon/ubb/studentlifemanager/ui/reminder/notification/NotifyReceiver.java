package com.halcyon.ubb.studentlifemanager.ui.reminder.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 *
 * Created by szilard on 2016.12.19..
 */

public class NotifyReceiver extends BroadcastReceiver {
    //private final static Logger LOGGER = Logger.getLogger(NotifyReceiver.class.getName());
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getExtras().getString("Title");
        String date = intent.getExtras().getString("DateTime");

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notify_manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification_popup = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(date)
                .setSmallIcon(android.R.drawable.ic_menu_today)
                .setAutoCancel(true)
                .setSound(uri)
                .build();

        notify_manager.notify((int)System.currentTimeMillis(), notification_popup);
    }
}
