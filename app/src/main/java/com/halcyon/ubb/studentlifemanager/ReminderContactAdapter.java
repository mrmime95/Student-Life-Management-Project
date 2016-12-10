package com.halcyon.ubb.studentlifemanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Szilard on 09.12.2016.
 */

public class ReminderContactAdapter extends RecyclerView.Adapter<ReminderContactAdapter.ContactViewHolder> {
    private ArrayList<ReminderContact> contacts = new ArrayList<ReminderContact>();
    public ReminderContactAdapter(ArrayList<ReminderContact> contacts){
        this.contacts = contacts;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_card_view, parent, false);
        ContactViewHolder recyclerViewHolder = new ContactViewHolder(view);
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
        TextView rem_name, rem_date, rem_time;
        public ContactViewHolder(View view)
        {
            super(view);
            rem_name = (TextView) view.findViewById(R.id.remName);
            rem_date = (TextView) view.findViewById(R.id.choosedDate);
            rem_time = (TextView) view.findViewById(R.id.choosedTime);
        }
    }
}
