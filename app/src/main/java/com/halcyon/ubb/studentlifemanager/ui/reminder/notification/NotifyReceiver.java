package com.halcyon.ubb.studentlifemanager.ui.reminder.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by szilard on 2016.12.19..
 */

public class NotifyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //create and intent to the ringtone service
        String title = intent.getExtras().getString("Title");
        String date = intent.getExtras().getString("DateTime");
        Intent serviceIntent = new Intent(context, NotifyService.class);
        serviceIntent.putExtra("Title", title);
        serviceIntent.putExtra("DateTime", date);
        //start the rington service
        context.startService(serviceIntent);

    }
}
