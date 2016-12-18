package com.halcyon.ubb.studentlifemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.halcyon.ubb.studentlifemanager.database.SQLiteDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Szilard on 09.12.2016.
 */

public class ReminderContactAdapter extends RecyclerView.Adapter<ReminderContactAdapter.ContactViewHolder> {
    private ArrayList<ReminderContact> contacts = new ArrayList<ReminderContact>();
    private Context ctx;
    private RecyclerView recyclerView;
    private ReminderControl reminderControl;
    private Calendar mCalendar;
    public ReminderContactAdapter(ArrayList<ReminderContact> contacts, Context ctx, RecyclerView recyclerView, ReminderControl reminderControl){
        this.contacts = contacts;
        this.ctx = ctx;
        this.recyclerView = recyclerView;
        this.reminderControl = reminderControl;
        this.mCalendar = Calendar.getInstance();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_card_view, parent, false);
        ContactViewHolder recyclerViewHolder = new ContactViewHolder(view, contacts, ctx, recyclerView, reminderControl);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(mCalendar.getTime());
        mCalendar.add(Calendar.DAY_OF_YEAR, 1 );
        String tomorrowDate = df.format(mCalendar.getTime());
        mCalendar.add(Calendar.DAY_OF_YEAR, -1);
        ReminderContact reminderContact = contacts.get(position);
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

    public static class ContactViewHolder extends RecyclerView.ViewHolder
    {
        private ArrayList<ReminderContact> contacts = new ArrayList<ReminderContact>();
        private Context ctx;
        private TextView rem_name, rem_date, rem_time;
        private SQLiteDB db;
        private RecyclerView recyclerView;
        private ReminderControl reminderControl;
        public ContactViewHolder(View view, ArrayList<ReminderContact> arrayList, final Context ctx, final RecyclerView recyclerView, final ReminderControl reminderControl) {
            super(view);
            this.contacts = arrayList;
            this.ctx = ctx;
            this.recyclerView = recyclerView;
            this.reminderControl = reminderControl;
            db = new SQLiteDB(this.ctx);
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
                                            ok = db.delete(deleteReminder);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setMessage("Deleting your reminder will permanently remove it.").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).setTitle("Delete reminder?").show();

                    }
                }
            });
            view.findViewById(R.id.remCardData).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.remCardData) {

                    }
                }
            });
            rem_name = (TextView) view.findViewById(R.id.remName);
            rem_date = (TextView) view.findViewById(R.id.choosedDate);
            rem_time = (TextView) view.findViewById(R.id.choosedTime);
        }
    }
}
