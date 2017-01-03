package com.halcyon.ubb.studentlifemanager.ui.course.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.halcyon.ubb.studentlifemanager.R;
import com.squareup.picasso.Picasso;

/**
 * Created by matyas on 2016.12.11..
 */

public class CourseViewHolder extends RecyclerView.ViewHolder {
    //TODO CR: The inherited itemView variable is global, there is no need to save another reference to it. [Peter]
    View mView;

    public CourseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setTitle(String title) {
        //TODO CR: To make the ViewHolder pattern optimal, you should save references to the Views you need to update in the constructor, that way
        //TODO CR: avoiding the relatively heavy findViewById() calls each time the data is updated. [Peter]
        TextView titleTextView = (TextView) mView.findViewById(R.id.courseTitle);
        titleTextView.setText(title);
    }

    public void setPicture(Context context, String image) {
        ImageView imageView = (ImageView) mView.findViewById(R.id.contact_image);
        //TODO CR: You can get the Context from the itemView, don't expect it as a parameter. [Peter]
        Picasso.with(context).load(image).into(imageView);
    }

    public View getmView() {
        return mView;
    }

}
