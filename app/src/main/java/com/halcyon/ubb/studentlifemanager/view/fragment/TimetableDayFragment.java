package com.halcyon.ubb.studentlifemanager.view.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.halcyon.ubb.studentlifemanager.BR;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.database.Database;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.view.DatabaseProvider;
import com.halcyon.ubb.studentlifemanager.view.adapter.RecyclerViewEventBindingAdapter;
import com.halcyon.ubb.studentlifemanager.viewmodel.TimetableDayEventListener;
import com.halcyon.ubb.studentlifemanager.viewmodel.TimetableDayEventViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimetableDayFragment extends Fragment implements DatabaseProvider {
    public static String PARAMS_DAY = "timetableday_params_day";
    private static String PARAMS_COURSE = "timetableday_params_course";

    private DatabaseProvider mProvider;

    RecyclerView mRecyclerView;
    private int mDay;
    private TimetableDayEventViewModel mViewModel;
    private RecyclerViewEventBindingAdapter mAdapter;
    private TimetableDayEventListener mListener = new TimetableDayEventListener() {
        @Override
        public void onSubscriptionFailed() {
            if (getView() != null)
                Snackbar.make(getView(), "Sorry, couldn't load data.", Snackbar.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), "Sorry, couldn't load data.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDayChanged() {
            if (getView() != null)
                Snackbar.make(getView(), "Day " + (mDay + 1) + " has changed.", Snackbar.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), "Day " + (mDay + 1) + " has changed.", Toast.LENGTH_LONG).show();
        }
    };

    public TimetableDayFragment() {
        // Required empty public constructor
    }

    public static TimetableDayFragment newInstance(List<String> coursesKey, @TimetableDay.Days int day) {
        TimetableDayFragment fragment = new TimetableDayFragment();
        Bundle args = new Bundle();
        args.putInt(PARAMS_DAY, day);
        args.putStringArray(PARAMS_COURSE, coursesKey.toArray(new String[0]));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null)
            throw new IllegalStateException("Should instantiate with static method (no args found).");

        //noinspection WrongConstant
        mDay = getArguments().getInt(PARAMS_DAY);
        //noinspection ConstantConditions
        String[] courses = getArguments().getStringArray(PARAMS_COURSE);

        List<String> coursesKey;
        if (courses == null)
            coursesKey = new ArrayList<>();
        else
            coursesKey = Arrays.asList(courses);

        //making a connection between viewmodel - mAdapter :
        //  ViewModel.events is an ObservableList<Event>
        //  so RecyclerViewBindingAdapter can listen for changes
        //  When changes happen the mAdapter updates the recycler with new events in list;
        //noinspection WrongConstant
        mViewModel = new TimetableDayEventViewModel(mProvider.getDatabase(), coursesKey, mDay);
        mAdapter = new RecyclerViewEventBindingAdapter(BR.item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_timetable_day, container, false).getRoot();
        //return inflater.inflate(R.layout.fragment_timetable_day, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DatabaseProvider)
            mProvider = (DatabaseProvider) context;
        else
            throw new IllegalStateException("Parent activity must implement database provider.");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_timetable_day);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);


        //setting variable (data binding) of recyclerview
        DataBindingUtil.getBinding(mRecyclerView).setVariable(BR.item, mViewModel);

        mRecyclerView.setAdapter(mAdapter);

        //we tell the viewmodel to make connection with database ==> change mViewModel.events
        //      ==> RecyclerViewBindingAdapter listener ==> notify data changed
        // Notice that viewmodel doesn't need reference to mAdapter
        mViewModel.subscribe(mListener);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewModel.unSubscribe(mListener);
    }

    @Override
    public Database getDatabase() {
        return mProvider.getDatabase();
    }
}
