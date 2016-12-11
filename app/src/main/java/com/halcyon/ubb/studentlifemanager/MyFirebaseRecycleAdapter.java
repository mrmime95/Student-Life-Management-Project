package com.example.matyas.eddd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by matyas on 2016.12.11..
 */

public class MyFirebaseRecycleAdapter extends  FirebaseRecyclerAdapter<Course,CourseViewHolder>  {

    private Query query;
    private Context context;

    public MyFirebaseRecycleAdapter(Query query, final Context context) {
        super(Course.class, R.layout.course_row, CourseViewHolder.class, query);
        this.query = query;
        this.context = context;
    }

    @Override
    protected void populateViewHolder(CourseViewHolder viewHolder, Course model, int position) {
        viewHolder.setTitle(model.getTitle());
        viewHolder.seFile(model.getFileName());
        viewHolder.setDescription(model.getDescription());
        viewHolder.setPicture(context, model.getPictureName());
    }
}
