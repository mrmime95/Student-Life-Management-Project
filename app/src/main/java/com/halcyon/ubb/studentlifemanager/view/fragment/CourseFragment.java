package com.halcyon.ubb.studentlifemanager.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.halcyon.ubb.studentlifemanager.MySpinner;
import com.halcyon.ubb.studentlifemanager.R;

/**
 * Created by Szilard on 2016. 12. 21..
 */

public class CourseFragment extends Fragment {
    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager courseLayoutManager;
    private MySpinner mSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_tab_courses, container, false);
        View activity_main = inflater.inflate(R.layout.activity_main, container, false);
        mSpinner = (MySpinner) activity_main.findViewById(R.id.main_spinner);
        courseRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerCourse);
        courseLayoutManager  = new LinearLayoutManager(getActivity());
        courseRecyclerView.setLayoutManager(courseLayoutManager);
        courseRecyclerView.setHasFixedSize(true);
        Query query = FirebaseDatabase.getInstance().getReference().child("courseNames").orderByValue();
        mSpinner.courseSpinnerUpdate(query);
        mSpinner.setMyItemSelectedListener(courseRecyclerView, getActivity());
        return rootView;
    }
}