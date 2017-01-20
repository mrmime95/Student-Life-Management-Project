package com.halcyon.ubb.studentlifemanager.ui.reminder.update;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.halcyon.ubb.studentlifemanager.model.reminder.Reminder;
import com.halcyon.ubb.studentlifemanager.database.local.reminder.ReminderDatabase;
import com.halcyon.ubb.studentlifemanager.ui.reminder.adapter.ReminderRecyclerAdapter;

import java.util.ArrayList;

/**
 *
 * Created by Szilard on 11.12.2016.
 */

public class ReminderUpdateData {
    private Context ctx;

    public ReminderUpdateData(Context ctx)
    {
        this.ctx = ctx;
    }
    public void updateReminders(RecyclerView recyclerView, ReminderDatabase db)
    {
        RecyclerView.Adapter reminderAdapter;
        try {
            reminderAdapter = new ReminderRecyclerAdapter(db.getAllReminder(), ctx, recyclerView, new ReminderUpdateData(ctx));
        } catch (Exception e){
            reminderAdapter = new ReminderRecyclerAdapter(new ArrayList<Reminder>(), ctx, recyclerView, new ReminderUpdateData(ctx));
        }
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager reminderLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(reminderLayoutManager);
        recyclerView.setAdapter(reminderAdapter);
    }
}
