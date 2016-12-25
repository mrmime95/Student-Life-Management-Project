package com.halcyon.ubb.studentlifemanager.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.halcyon.ubb.studentlifemanager.database.Database;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private List<TimetableDayEventListener> mListeners;
    private boolean mSubscribed = false;

    public TimetableDayEventViewModel(Database database, List<String> coursesKey, @TimetableDay.Days int day) {
        mDay = day;
        mCourseKey = coursesKey;
        mDatabase = database;
        events = new ObservableArrayList<>();
        mListeners = new ArrayList<>();
        mListener = new CoursesEventValueListener() {

            @Override
            public void onEventsListChange(final Map<String, List<Event>> eventsDB, boolean loading) {
                if (!loading)
                    for (TimetableDayEventListener listener : mListeners)
                        listener.onDayChanged();

                events.clear();
                if (eventsDB == null) return;
                ArrayList<Event> noNullEventsDay = new ArrayList<>();
                for (List<Event> list : eventsDB.values())
                    if (list != null) noNullEventsDay.addAll(list);

                events.addAll(noNullEventsDay);
            }

            @Override
            public void onCancelled(Exception e) {
                for (TimetableDayEventListener listener : mListeners)
                    listener.onSubscriptionFailed();
            }
        };
    }

    public void subscribe(TimetableDayEventListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
            if (!mSubscribed)
                mDatabase.addCoursesEventValueListener(mCourseKey, mDay, mListener);
        }
    }

    public void unSubscribe(TimetableDayEventListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
            if (mSubscribed)
                mDatabase.removeCoursesEventValueListener(mCourseKey, mDay, mListener);
        }
    }
}
