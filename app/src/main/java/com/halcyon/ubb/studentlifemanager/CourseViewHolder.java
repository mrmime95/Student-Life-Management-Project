package com.example.matyas.eddd;

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
        TextView titleTextView = (TextView) mView.findViewById(R.id.titleView);
        titleTextView.setText(title);
    }

    public void setDescription(String description) {
        TextView descTextView = (TextView) mView.findViewById(R.id.descView);
        descTextView.setText(description);
    }

    public  void setPicture(android.content.Context context, String image){
        ImageView imageView = (ImageView) mView.findViewById(R.id.imageView);
        Picasso.with(context).load(image).into(imageView);
    }

    public void seFile(String fileName){
        TextView textView = (TextView) mView.findViewById(R.id.fileView);
        textView.setText(fileName);
    }
}
