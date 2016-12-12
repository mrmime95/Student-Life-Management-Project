package com.halcyon.ubb.studentlifemanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Szilard on 01.12.2016.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private ArrayList<CourseContact> contacts = new ArrayList<>();
    private Context ctx;
    public ContactAdapter(ArrayList<CourseContact> contacts, Context ctx){
        this.ctx = ctx;
        this.contacts = contacts;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card, parent, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view, ctx, contacts);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        CourseContact courseContact = contacts.get(position);
        holder.courseImg.setImageResource(courseContact.getImage_id());
        holder.courseTitle.setText(courseContact.getTitle());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView courseImg;
        TextView courseTitle;
        TextView courseDescription;
        ArrayList<CourseContact> contacts = new ArrayList<CourseContact>();
        Context ctx;
        public ContactViewHolder(View view, Context ctx, ArrayList<CourseContact> contacts){
            super(view);
            view.setOnClickListener(this);
            this.contacts = contacts;
            this.ctx = ctx;
            courseImg = (ImageView)view.findViewById(R.id.contact_image);
            courseTitle = (TextView) view.findViewById(R.id.courseTitle);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
                CourseContact contact = this.contacts.get(position);
            Intent intent = new Intent(this.ctx, CourseDetail.class);
            intent.putExtra("img_id", contact.getImage_id());
            intent.putExtra("courseTitle", contact.getTitle());
            intent.putExtra("courseDescription", contact.getDescription());
            this.ctx.startActivity(intent);
        }
    }
}
