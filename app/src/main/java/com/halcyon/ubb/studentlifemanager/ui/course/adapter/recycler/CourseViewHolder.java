package com.halcyon.ubb.studentlifemanager.ui.course.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.halcyon.ubb.studentlifemanager.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by matyas on 2016.12.11..
 */

public class CourseViewHolder extends RecyclerView.ViewHolder {

    private final ProgressBar mProgressBar;
    private TextView titleTextView;
    private ImageView imageView;
    private ImageView fileView;

    public CourseViewHolder(View itemView) {
        super(itemView);
        titleTextView = (TextView) itemView.findViewById(R.id.courseTitle);
        imageView = (ImageView) itemView.findViewById(R.id.contact_image);
        fileView = (ImageView) itemView.findViewById(R.id.backBtn);
        mProgressBar= (ProgressBar) itemView.findViewById(R.id.progress_bar);
    }

    public void checkPictureExisting(Boolean exist){
        if (exist){
            imageView.setVisibility(View.VISIBLE);
        }else{
            imageView.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setPicture(String image) {
        Picasso.with(itemView.getContext()).load(image)
                //TODO refresh error img
                .error(android.R.drawable.ic_dialog_alert)
                .into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                if (mProgressBar!=null)
                    mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                if (mProgressBar!=null)
                    mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void setFileIconVisibility(Boolean b){
        if (b){
            fileView.setVisibility(View.VISIBLE);
        } else {
            fileView.setVisibility(View.INVISIBLE);
        }
    }
}
