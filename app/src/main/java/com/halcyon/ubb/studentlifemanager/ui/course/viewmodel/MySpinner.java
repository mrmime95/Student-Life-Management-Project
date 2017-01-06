package com.halcyon.ubb.studentlifemanager.ui.course.viewmodel;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.ui.course.viewmodel.MyItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matyas on 2016.12.11..
 */

public class MySpinner extends AppCompatSpinner{

    private Query queryCourses;
    private Context context;

    public MySpinner(Context context) {
        super(context);
        init(context);
    }

    public MySpinner(Context context, int mode) {
        super(context, mode);
        init(context);
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init(context);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        init(context);
    }

    public void init(Context context){
        this.context = context;
    }

    public void courseSpinnerUpdate(Query query){
        final List<String> list = new ArrayList<String>();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                list.add("All");
                Log.d("INFO", "added " + dataSnapshot.getChildrenCount());
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    String name = ds.getValue(String.class);
                    list.add(name);
                    Log.d("INFO", "added " + name);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.main_spinner_textview, list);
                arrayAdapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
                setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(valueEventListener);

    }

    public void setMyItemSelectedListener(RecyclerView recyclerView, Context context){
        setOnItemSelectedListener(new MyItemSelectedListener(recyclerView, context));
    }
}
