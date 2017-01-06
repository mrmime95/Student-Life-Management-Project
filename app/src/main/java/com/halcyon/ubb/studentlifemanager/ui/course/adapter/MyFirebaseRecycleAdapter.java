package com.halcyon.ubb.studentlifemanager.ui.course.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.halcyon.ubb.studentlifemanager.ui.course.selectedcourse.DetailedCourseActivity;
import com.halcyon.ubb.studentlifemanager.ui.course.adapter.recycler.CourseViewHolder;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.model.course.Course_c;

/**
 * Created by matyas on 2016.12.11..
 */

public class MyFirebaseRecycleAdapter extends  FirebaseRecyclerAdapter<Course_c,CourseViewHolder> {

    public MyFirebaseRecycleAdapter(Query query) {
        super(Course_c.class, R.layout.course_card, CourseViewHolder.class, query);
    }

    @Override
    protected void populateViewHolder(final CourseViewHolder viewHolder, final Course_c model, int position) {
        viewHolder.setTitle(model.getTitle());
        viewHolder.setPicture(model.getPictureName());
        if (model.getFileName().equals("none")){
            viewHolder.setFileIconVisibility(false);
        } else {
            viewHolder.setFileIconVisibility(true);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailedCourseActivity.class);
                intent.putExtra("img_id", model.getPictureName());
                intent.putExtra("courseTitle", model.getTitle());
                intent.putExtra("courseDescription", model.getDescription());
                intent.putExtra("attachmentName", model.getFileName());
                intent.putExtra("attachmentURL", model.getFileUrl());
                view.getContext().startActivity(intent);
            }
        });
    }
}
