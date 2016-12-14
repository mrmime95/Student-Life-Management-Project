package com.halcyon.ubb.studentlifemanager.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.halcyon.ubb.studentlifemanager.database.Database;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;

import java.util.ArrayList;

/**
 *
 * Created by Baroti Csaba on 12/11/2016.
 */

public class TimetableDayViewModel {
    public ObservableList<Event> events;
    private int mDay;
    private String mCourse;
    private Database mDatabase;
    private TimetableDayValueListener mListener;
    private Context mContext;

    public TimetableDayViewModel(Context context, Database database, String course, @TimetableDay.Days int day) {
        mDay=day;
        mCourse=course;
        mDatabase=database;
        mContext=context;
        events=new ObservableArrayList<>();
        mListener=new TimetableDayValueListener() {
            @Override
            public void onDayChange(TimetableDay day) {
                events.clear();
                if (day==null) return;
                ArrayList<Event> noNullEventsDay=new ArrayList<>();
                for (Event i:day.events)
                    if (i!=null) noNullEventsDay.add(i);

                events.addAll(noNullEventsDay);
            }

            @Override
            public void onCancelled(Exception e) {
                Toast.makeText(mContext,"There was a problem loading your timetable on day "+mDay+".",Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void subscribe() {
        mDatabase.addTimetableDayValueListener(mCourse,mDay,mListener);
    }

    public void unSubscribe() {
        mDatabase.removeTimetableDayValueListener(mCourse,mDay,mListener);
    }
}
