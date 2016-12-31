package com.halcyon.ubb.studentlifemanager.ui.reminder.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.halcyon.ubb.studentlifemanager.R;

/**
 * Created by szilard on 2016.12.19..
 */

public class NotifyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String title = intent.getExtras().getString("Title");
        String date = intent.getExtras().getString("DateTime");
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification_popup = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(date)
                .setSmallIcon(android.R.drawable.ic_menu_today)
                .setAutoCancel(true)
                .setSound(uri)
                .build();
        int Unique_Integer_Number = (int) System.nanoTime();
        notify_manager.notify(Unique_Integer_Number, notification_popup);
        return START_NOT_STICKY;
    }
}