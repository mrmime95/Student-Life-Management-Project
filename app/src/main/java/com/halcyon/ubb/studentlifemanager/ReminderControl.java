package com.halcyon.ubb.studentlifemanager;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
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
    public void updateReminders(RecyclerView recyclerView, MyDBHandler db)
    {
        try {
            reminderAdapter = new ReminderContactAdapter(db.getAllReminderCotacts(), ctx, recyclerView, new ReminderControl(ctx));
        } catch (Exception e){
            reminderAdapter = new ReminderContactAdapter(new ArrayList<ReminderContact>(), ctx, recyclerView, new ReminderControl(ctx));
        }
        recyclerView.setHasFixedSize(true);
        reminderLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(reminderLayoutManager);
        recyclerView.setAdapter(reminderAdapter);
    }
    public void setNewReminderIcons(boolean visability, boolean pipe, View view)
    {
        View viewClose = view.findViewById(R.id.closeBtn);
        View viewPipe = view.findViewById(R.id.pipeBtn);
        View viewDate = view.findViewById(R.id.date);
        View viewTime = view.findViewById(R.id.time);
        ImageView img = (ImageView)view.findViewById(R.id.pipeBtn);
        EditText reminderName = (EditText)view.findViewById(R.id.reminderName);
        if (visability){
            viewClose.setVisibility(View.VISIBLE);
            viewPipe.setVisibility(View.VISIBLE);
            viewDate.setVisibility(View.VISIBLE);
            viewTime.setVisibility(View.VISIBLE);
            reminderName.setEnabled(visability);
        }else{
            viewClose.setVisibility(View.GONE);
            viewPipe.setVisibility(View.GONE);
            viewDate.setVisibility(View.GONE);
            viewTime.setVisibility(View.GONE);
            reminderName.setEnabled(visability);
        }
        if (pipe) img.setImageResource(R.drawable.ic_check_ok);
        else img.setImageResource(R.drawable.ic_check);
    }



}
