package com.halcyon.ubb.studentlifemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Szilard on 09.12.2016.
 */

public class ReminderContactAdapter extends RecyclerView.Adapter<ReminderContactAdapter.ContactViewHolder> {
    private ArrayList<ReminderContact> contacts = new ArrayList<ReminderContact>();
    private Context ctx;
    private RecyclerView recyclerView;
    public ReminderContactAdapter(ArrayList<ReminderContact> contacts, Context ctx, RecyclerView recyclerView){
        this.contacts = contacts;
        this.ctx = ctx;
        this.recyclerView = recyclerView;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_card_view, parent, false);
        ContactViewHolder recyclerViewHolder = new ContactViewHolder(view, contacts, ctx, recyclerView);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ReminderContact reminderContact = contacts.get(position);
        holder.rem_time.setText(reminderContact.getTime());
        holder.rem_date.setText(reminderContact.getDate());
        holder.rem_name.setText(reminderContact.getName());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder
    {
        ArrayList<ReminderContact> contacts = new ArrayList<ReminderContact>();
        Context ctx;
        TextView rem_name, rem_date, rem_time;
        MyDBHandler db;
        RecyclerView recyclerView;
        private  RecyclerView.Adapter reminderAdapter;
        private RecyclerView.LayoutManager reminderLayoutManager;

        public ContactViewHolder(View view, ArrayList<ReminderContact> arrayList, final Context ctx, final RecyclerView recyclerView) {
            super(view);
            this.contacts = arrayList;
            this.ctx = ctx;
            this.recyclerView = recyclerView;
            db = new MyDBHandler(this.ctx);
            view.findViewById(R.id.dropBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.dropBtn) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        int position = getAdapterPosition();
                                        ReminderContact contact = contacts.get(position);
                                        ReminderContact deleteReminder = new ReminderContact(1, contact .getName(), contact.getDate(), contact.getTime());
                                        try {
                                            boolean ok = false;
                                            ok = db.deleteContact(deleteReminder);

                                            try {
                                                reminderAdapter = new ReminderContactAdapter(db.getAllReminderCotacts(), ctx, recyclerView);
                                            } catch (Exception e){
                                                reminderAdapter = new ReminderContactAdapter(new ArrayList<ReminderContact>(), ctx, recyclerView);
                                            }
                                            recyclerView.setHasFixedSize(true);
                                            reminderLayoutManager = new LinearLayoutManager(ctx);
                                            recyclerView.setLayoutManager(reminderLayoutManager);
                                            recyclerView.setAdapter(reminderAdapter);
                                        }catch (Exception e){
                                            Log.d("INFO","Deleting was not succesfull!" + contact.getName() + contact.getTime() + contact.getDate());
                                            Log.d("INFO", e.toString());
                                        }

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setMessage("Deleting your reminder will permanently remove it.").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).setTitle("Delete reminder?").show();

                    }
                }
            });
            rem_name = (TextView) view.findViewById(R.id.remName);
            rem_date = (TextView) view.findViewById(R.id.choosedDate);
            rem_time = (TextView) view.findViewById(R.id.choosedTime);
        }
    }
}
