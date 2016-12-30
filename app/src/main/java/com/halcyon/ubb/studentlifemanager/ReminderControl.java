package com.halcyon.ubb.studentlifemanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.halcyon.ubb.studentlifemanager.database.local.reminder.ReminderDatabase;

import java.util.ArrayList;

/**
 *
 * Created by Szilard on 11.12.2016.
 */

public class ReminderControl {
    private Context ctx;
    private RecyclerView.Adapter reminderAdapter;
    private RecyclerView.LayoutManager reminderLayoutManager;

    public ReminderControl(Context ctx)
    {
        this.ctx = ctx;
    }
    public void updateReminders(RecyclerView recyclerView, ReminderDatabase db)
    {
        try {
            reminderAdapter = new ReminderRecyclerAdapter(db.getAllReminder(), ctx, recyclerView, new ReminderControl(ctx));
        } catch (Exception e){
            reminderAdapter = new ReminderRecyclerAdapter(new ArrayList<Reminder>(), ctx, recyclerView, new ReminderControl(ctx));
        }
        recyclerView.setHasFixedSize(true);
        reminderLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(reminderLayoutManager);
        recyclerView.setAdapter(reminderAdapter);
    }
}
