package com.halcyon.ubb.studentlifemanager.ui.course.fragment;

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
import com.halcyon.ubb.studentlifemanager.database.DatabaseProvider;
import com.halcyon.ubb.studentlifemanager.ui.course.viewmodel.MySpinner;
import com.halcyon.ubb.studentlifemanager.R;

/**
 *
 * Created by Szilard on 2016. 12. 21..
 */


public class CourseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DatabaseProvider.getInstance().getRemoteDatabase();
        View rootView = inflater.inflate(R.layout.main_tab_courses, container, false);
        RecyclerView courseRecyclerView;
        RecyclerView.LayoutManager courseLayoutManager;
        MySpinner mSpinner;
        mSpinner = (MySpinner) rootView.findViewById(R.id.main_spinner);
        courseRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerCourse);
        courseLayoutManager  = new LinearLayoutManager(getActivity());
        courseRecyclerView.setLayoutManager(courseLayoutManager);
        courseRecyclerView.setHasFixedSize(true);
        Query query = FirebaseDatabase.getInstance().getReference().child("courseNames").orderByValue();
        mSpinner.courseSpinnerUpdate(query);
        mSpinner.setMyItemSelectedListener(courseRecyclerView);
        return rootView;
    }
}
