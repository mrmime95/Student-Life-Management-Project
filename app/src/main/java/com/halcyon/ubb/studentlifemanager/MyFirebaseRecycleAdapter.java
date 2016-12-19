package com.halcyon.ubb.studentlifemanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by matyas on 2016.12.11..
 */

public class MyFirebaseRecycleAdapter extends  FirebaseRecyclerAdapter<Course,CourseViewHolder> {

    private Query query;
    private Context context;

    public MyFirebaseRecycleAdapter(Query query, final Context context) {
        super(Course.class, R.layout.course_card, CourseViewHolder.class, query);
        this.query = query;
        this.context = context;
    }

    @Override
    protected void populateViewHolder(final CourseViewHolder viewHolder, final Course model, int position) {
        viewHolder.setTitle(model.getTitle());
        viewHolder.setPicture(context, model.getPictureName());
        viewHolder.getmView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(context, CourseDetail.class);
                intent.putExtra("img_id", model.getPictureName());
                intent.putExtra("courseTitle", model.getTitle());
                intent.putExtra("courseDescription", model.getDescription());
                intent.putExtra("attachmentName", model.getFileName());
                context.startActivity(intent);
            }
        });

    }
}
