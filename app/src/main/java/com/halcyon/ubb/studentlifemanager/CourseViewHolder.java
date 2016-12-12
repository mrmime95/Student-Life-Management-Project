package com.halcyon.ubb.studentlifemanager;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by matyas on 2016.12.11..
 */

public class CourseViewHolder extends  RecyclerView.ViewHolder{
    View mView;

    public CourseViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setTitle(String title){
        TextView titleTextView = (TextView) mView.findViewById(R.id.courseTitle);
        titleTextView.setText(title);
    }

    public  void setPicture(android.content.Context context, String image){
        ImageView imageView = (ImageView) mView.findViewById(R.id.contact_image);
        Picasso.with(context).load(image).into(imageView);
    }

    public View getmView() {
        return mView;
    }
}
