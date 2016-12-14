package com.halcyon.ubb.studentlifemanager.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.halcyon.ubb.studentlifemanager.BR;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.database.FirebaseDB;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.view.adapter.RecyclerViewEventBindingAdapter;
import com.halcyon.ubb.studentlifemanager.viewmodel.TimetableDayViewModel;

public class TimetableDayFragment extends Fragment {
    public static String PARAMS_DAY="timetableday_params_day";
    private static String PARAMS_COURSE="timetableday_params_course";

    RecyclerView mRecyclerView;
    private int mDay;
    private String mCourse;
    private TimetableDayViewModel mViewModel;
    private RecyclerViewEventBindingAdapter mAdapter;

    public TimetableDayFragment() {
        // Required empty public constructor
    }

    public static TimetableDayFragment newInstance(String course,@TimetableDay.Days int day) {
        TimetableDayFragment fragment = new TimetableDayFragment();
        Bundle args = new Bundle();
        args.putInt(PARAMS_DAY, day);
        args.putString(PARAMS_COURSE,course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) throw new IllegalStateException("Should instantiate with static method (no args found).");

        //noinspection WrongConstant
        mDay =getArguments().getInt(PARAMS_DAY);
        mCourse=getArguments().getString(PARAMS_COURSE);

        //setting variable (data binding) of recyclerview
        //noinspection WrongConstant
        mViewModel=new TimetableDayViewModel(getContext(),FirebaseDB.getInstance(),mCourse,mDay);
        mAdapter = new RecyclerViewEventBindingAdapter(BR.item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater,R.layout.fragment_timetable_day,container,false).getRoot();
        //return inflater.inflate(R.layout.fragment_timetable_day, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recycler_timetable_day);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        DataBindingUtil.getBinding(mRecyclerView).setVariable(BR.item,mViewModel);

        //making a connection between viewmodel - mAdapter :
        //  ViewModel.events is an ObservableList<Event>
        //  so RecyclerViewBindingAdapter can listen for changes
        //  When changes happen the mAdapter updates the recycler with new events in list;
        mRecyclerView.setAdapter(mAdapter);

        //we tell the viewmodel to make connection with database ==> change mViewModel.events
        //      ==> RecyclerViewBindingAdapter listener ==> notify data changed
        // Notice that viewmodel doesn't need reference to mAdapter
        mViewModel.subscribe();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewModel.unSubscribe();
    }
}
