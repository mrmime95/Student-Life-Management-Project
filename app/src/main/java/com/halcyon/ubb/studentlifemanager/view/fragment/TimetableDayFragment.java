package com.halcyon.ubb.studentlifemanager.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.nitrico.lastadapter.LastAdapter;
import com.halcyon.ubb.studentlifemanager.BR;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.database.FirebaseDB;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.viewmodel.TimetableDayValueListener;
import com.halcyon.ubb.studentlifemanager.viewmodel.EventEndPoint;
import com.halcyon.ubb.studentlifemanager.viewmodel.EventStartViewModel;
import com.halcyon.ubb.studentlifemanager.viewmodel.EventViewModel;
import com.halcyon.ubb.studentlifemanager.viewmodel.ICEventViewModel;
import com.halcyon.ubb.studentlifemanager.viewmodel.TimetableViewModelHelper;

import org.antlr.v4.automata.ATNFactory;

import java.util.ArrayList;

public class TimetableDayFragment extends Fragment {
    public static String PARAMS_DAY="timetableday_params_day";
    private static String PARAMS_COURSE="timetableday_params_course";

    RecyclerView mRecyclerView;

    ArrayList<ICEventViewModel> mList;
    private LastAdapter mAdapter;

    private TimetableDayValueListener mListener;
    private int mDay;
    private String mCourse;

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

            mListener=new TimetableDayValueListener() {
                @Override
                public void onDayChange(TimetableDay day) {
                    int size=mList.size();
                    mList.clear();
                    mAdapter.notifyItemRangeRemoved(0,size);
                    ArrayList<ICEventViewModel> list = TimetableViewModelHelper.createEventViewModels(day);
                    for (ICEventViewModel i : list)
                        mList.add(i);
                    mAdapter.notifyItemRangeInserted(0,mList.size());
                    if (size!=0)
                        Snackbar.make(getActivity().findViewById(R.id.main_coord),"Timetable day "+String.valueOf(mDay+1)+" has been updated.",Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(Exception e) {
                    Toast.makeText(getContext(),"There was a problem loading your timetable on day "+mDay+".",Toast.LENGTH_SHORT).show();
                }
            };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetable_day, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recycler_timetable_day);

        //TODO CR: Why do you need a third party library to set up the RecyclerView? Initializing the adapter and handling the ViewHolder pattern
        // is one of the basic skills you need to master as an Android developer, after you're 100% comfortable using it you might look into simplifications
        // but I really don't think using libraries for such simple tasks is a good idea. Furthermore this particular library uses some advanced solutions
        // (data binding, Kotlin) that significantly increase the complexity of your project and while it's a good thing you're interested in newer technologies,
        // there are still some basic stuff you need to be comfortable with before moving on. Consider rewriting this, it doesn't help you to learn anything. [Peter]
        mList = TimetableViewModelHelper.createEventViewModels(new TimetableDay());
        mAdapter=LastAdapter.with(mList, BR.item)
                .map(EventStartViewModel.class,R.layout.timetableday_time)
                .map(EventViewModel.class,R.layout.timetableday_event)
                .map(EventEndPoint.class,R.layout.timetableday_endpoint)
                .into(mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseDB.getInstance().addTimetableDayValueListener(mCourse,mDay,mListener);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FirebaseDB.getInstance().removeTimetableDayValueListener(mCourse,mDay,mListener);
    }
}
