package com.halcyon.ubb.studentlifemanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by matyas on 2016.12.11..
 */



public class MyItemSelectedListener implements AdapterView.OnItemSelectedListener {

    private RecyclerView courseList;
    private Context context;

    public MyItemSelectedListener(RecyclerView recyclerView, Context context) {
        this.courseList = recyclerView;
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        courseList.getRecycledViewPool().clear();
        if (i!=0) {
            Query queryAnousments = FirebaseDatabase.getInstance().getReference().child("courses").
                    child("testCourse").child("CoursesAnousments").orderByChild("course").
                    equalTo(adapterView.getSelectedItem().toString());
            MyFirebaseRecycleAdapter myFirebaseRecycleAdapter = new MyFirebaseRecycleAdapter(queryAnousments, context);
            courseList.setAdapter(myFirebaseRecycleAdapter);
            myFirebaseRecycleAdapter.notifyDataSetChanged();
        } else {
            Query queryAnousments = FirebaseDatabase.getInstance().getReference().child("CoursesAnousments");
            MyFirebaseRecycleAdapter myFirebaseRecycleAdapter = new MyFirebaseRecycleAdapter(queryAnousments, context);
            courseList.setAdapter(myFirebaseRecycleAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
