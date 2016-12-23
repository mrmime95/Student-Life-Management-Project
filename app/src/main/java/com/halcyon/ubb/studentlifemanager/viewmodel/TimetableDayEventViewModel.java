package com.halcyon.ubb.studentlifemanager.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.halcyon.ubb.studentlifemanager.database.Database;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Baroti Csaba on 12/11/2016.
 */

public class TimetableDayEventViewModel {
    public ObservableList<Event> events;
    private int mDay;
    private List<String> mCourseKey;
    private Database mDatabase;
    private CoursesEventValueListener mListener;
    private Context mContext;

    public TimetableDayEventViewModel(Context context, Database database, List<String> coursesKey , @TimetableDay.Days int day) {
        mDay=day;
        mCourseKey=coursesKey;
        mDatabase=database;
        mContext=context;
        events=new ObservableArrayList<>();
        mListener=new CoursesEventValueListener() {
            @Override
            public void onEventsChange(List<Event> eventsDB) {
                events.clear();
                if (eventsDB==null) return;
                ArrayList<Event> noNullEventsDay=new ArrayList<>();
                for (Event i:eventsDB)
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
        mDatabase.addCoursesEventValueListener(mCourseKey,mDay,mListener);
    }

    public void unSubscribe() {
        mDatabase.removeCoursesEventValueListener(mCourseKey,mDay,mListener);
    }
}
