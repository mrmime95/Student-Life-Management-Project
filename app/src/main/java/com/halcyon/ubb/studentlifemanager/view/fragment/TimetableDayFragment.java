package com.halcyon.ubb.studentlifemanager.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nitrico.lastadapter.LastAdapter;
import com.halcyon.ubb.studentlifemanager.BR;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay.Days;
import com.halcyon.ubb.studentlifemanager.viewmodel.EventEndPoint;
import com.halcyon.ubb.studentlifemanager.viewmodel.EventStartViewModel;
import com.halcyon.ubb.studentlifemanager.viewmodel.EventViewModel;
import com.halcyon.ubb.studentlifemanager.viewmodel.ICEventViewModel;
import com.halcyon.ubb.studentlifemanager.viewmodel.TimetableViewModelHelper;

import java.util.ArrayList;

public class TimetableDayFragment extends Fragment {
    public static String PARAMS_DAY="timetableday_params_day";
    public static String PARAMS_TIMETABLE="timetableday_params_timetable";

    private TimetableDay mTimeTableDay;

    //TODO CR: Pay attention to Lint warnings, there is no need to keep a global reference to this field. [Peter]
    private RecyclerView mRecyclerView;

    public TimetableDayFragment() {
        // Required empty public constructor
    }

    public static TimetableDayFragment newInstance(@Days int day, TimetableDay table) {
        //TODO CR: Don't commit partially finished code: use the PARAMS_DAY constant to pass the selected day. [Peter]
        TimetableDayFragment fragment = new TimetableDayFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAMS_TIMETABLE, table);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //noinspection WrongConstant
            mTimeTableDay = (TimetableDay) getArguments().getSerializable(PARAMS_TIMETABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_timetable_day, container, false);
        //TODO CR: The onCreateView() method should only be responsible for inflating the View, move the rest of the logic to onViewCreated(). [Peter]
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recycler_timetable_day);

        //TODO CR: Try to avoid meaningless variable names. [Peter]
        ArrayList<ICEventViewModel> vm = TimetableViewModelHelper.createEventViewModels(mTimeTableDay);

        //TODO CR: Why do you need a third party library to set up the RecyclerView? Initializing the adapter and handling the ViewHolder pattern
        // is one of the basic skills you need to master as an Android developer, after you're 100% comfortable using it you might look into simplifications
        // but I really don't think using libraries for such simple tasks is a good idea. Furthermore this particular library uses some advanced solutions
        // (data binding, Kotlin) that significantly increase the complexity of your project and while it's a good thing you're interested in newer technologies,
        // there are still some basic stuff you need to be comfortable with before moving on. Consider rewriting this, it doesn't help you to learn anything. [Peter]
        LastAdapter.with(vm, BR.item)
                .map(EventStartViewModel.class,R.layout.timetableday_time)
                .map(EventViewModel.class,R.layout.timetableday_event)
                .map(EventEndPoint.class,R.layout.timetableday_endpoint)
                .into(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }


}
