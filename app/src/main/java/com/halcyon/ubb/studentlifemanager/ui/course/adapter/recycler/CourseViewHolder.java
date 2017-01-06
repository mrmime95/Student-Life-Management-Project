package com.halcyon.ubb.studentlifemanager.ui.course.adapter.recycler;

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

    private TextView titleTextView;
    private ImageView imageView;
    private ImageView fileView;

    public CourseViewHolder(View itemView) {
        super(itemView);
        titleTextView = (TextView) itemView.findViewById(R.id.courseTitle);
        imageView = (ImageView) itemView.findViewById(R.id.contact_image);
        fileView = (ImageView) itemView.findViewById(R.id.backBtn);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setPicture(String image) {
        Picasso.with(itemView.getContext()).load(image).into(imageView);
    }

    public void setFileIconVisibility(Boolean b){
        if (b){
            fileView.setVisibility(View.VISIBLE);
        } else {
            fileView.setVisibility(View.INVISIBLE);
        }
    }
}
