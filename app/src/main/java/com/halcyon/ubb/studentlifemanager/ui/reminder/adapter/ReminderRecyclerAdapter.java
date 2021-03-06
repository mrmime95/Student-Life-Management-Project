package com.halcyon.ubb.studentlifemanager.ui.reminder.adapter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.model.reminder.Reminder;
import com.halcyon.ubb.studentlifemanager.ui.reminder.notification.NotifyReceiver;
import com.halcyon.ubb.studentlifemanager.ui.reminder.update.ReminderUpdateData;
import com.halcyon.ubb.studentlifemanager.database.DatabaseProvider;
import com.halcyon.ubb.studentlifemanager.database.local.reminder.ReminderDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * Created by Szilard on 09.12.2016.
 */

public class ReminderRecyclerAdapter extends RecyclerView.Adapter<ReminderRecyclerAdapter.ContactViewHolder> {
    private ArrayList<Reminder> contacts = new ArrayList<>();
    private Context ctx;
    private RecyclerView recyclerView;
    private ReminderUpdateData reminderControl;
    private Calendar mCalendar;
    public ReminderRecyclerAdapter(ArrayList<Reminder> contacts, Context ctx, RecyclerView recyclerView, ReminderUpdateData reminderControl){
        this.contacts = contacts;
        this.ctx = ctx;
        this.recyclerView = recyclerView;
        this.reminderControl = reminderControl;
        this.mCalendar = Calendar.getInstance();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_card_view, parent, false);
        return new ContactViewHolder(view, contacts, ctx, recyclerView, reminderControl);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(mCalendar.getTime());
        mCalendar.add(Calendar.DAY_OF_YEAR, 1 );
        String tomorrowDate = df.format(mCalendar.getTime());
        mCalendar.add(Calendar.DAY_OF_YEAR, -1);
        Reminder reminderContact = contacts.get(position);
        holder.rem_time.setText(reminderContact.getTime());
        //Log.d("INFO", formattedDate + " " + reminderContact.getDate());
        if (reminderContact.getDate().toLowerCase().equals(formattedDate.replace(".",""))) holder.rem_date.setText("Today");
        else if (reminderContact.getDate().toLowerCase().equals(tomorrowDate.replace(".",""))) holder.rem_date.setText("Tomorrow");
        else  holder.rem_date.setText(reminderContact.getDate());
        holder.rem_name.setText(reminderContact.getName());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder
    {
        private ArrayList<Reminder> contacts = new ArrayList<>();
        private TextView rem_name, rem_date, rem_time;
        private ReminderDatabase db;

        ContactViewHolder(View view, ArrayList<Reminder> arrayList, final Context ctx, final RecyclerView recyclerView, final ReminderUpdateData reminderControl) {
            super(view);
            this.contacts = arrayList;

            db = DatabaseProvider.getInstance().getReminderDatabase(view.getContext());
            view.findViewById(R.id.dropBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.dropBtn) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        int position = getAdapterPosition();
                                        Reminder contact = contacts.get(position);
                                        Reminder deleteReminder = new Reminder(1, contact .getName(), contact.getDate(), contact.getTime());
                                        try {

                                            int deletingID = db.findIdByOthers(deleteReminder);
                                            Intent my_intient = new Intent(ctx, NotifyReceiver.class);

                                            db.delete(deleteReminder);
                                            PendingIntent.getBroadcast(ctx, deletingID, my_intient, PendingIntent.FLAG_UPDATE_CURRENT).cancel();

                                            reminderControl.updateReminders(recyclerView, db);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.AlertDialogCustom);
                        builder.setMessage("Deleting your reminder will permanently remove it.").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener)
                                .setTitle("Delete reminder?").show();

                    }
                }
            });
            view.findViewById(R.id.remCardData).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            rem_name = (TextView) view.findViewById(R.id.remName);
            rem_date = (TextView) view.findViewById(R.id.choosedDate);
            rem_time = (TextView) view.findViewById(R.id.choosedTime);
        }
    }
}
