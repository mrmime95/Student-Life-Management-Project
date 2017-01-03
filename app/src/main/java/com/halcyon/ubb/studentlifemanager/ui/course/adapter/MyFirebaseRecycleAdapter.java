package com.halcyon.ubb.studentlifemanager.ui.course.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

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

    private Query query;
    //TODO CR: Don't save the Context as a global variable unless you absolutely have to (here it can be avoided, see the comments in the CourseViewHolder class). [Peter]
    private Context context;

    public MyFirebaseRecycleAdapter(Query query, final Context context) {
        super(Course_c.class, R.layout.course_card, CourseViewHolder.class, query);
        this.query = query;
        this.context = context;
    }

    @Override
    protected void populateViewHolder(final CourseViewHolder viewHolder, final Course_c model, int position) {
        viewHolder.setTitle(model.getTitle());
        viewHolder.setPicture(context, model.getPictureName());
        viewHolder.getmView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(context, DetailedCourseActivity.class);
                intent.putExtra("img_id", model.getPictureName());
                intent.putExtra("courseTitle", model.getTitle());
                intent.putExtra("courseDescription", model.getDescription());
                intent.putExtra("attachmentName", model.getFileName());
                context.startActivity(intent);
            }
        });

    }
}
