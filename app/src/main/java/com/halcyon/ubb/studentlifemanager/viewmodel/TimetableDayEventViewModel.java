package com.halcyon.ubb.studentlifemanager.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.halcyon.ubb.studentlifemanager.database.Database;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;
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
    private List<Group> mGroups;
    private Database mDatabase;
    private CoursesEventValueListener mListener;
    private List<TimetableDayEventListener> mListeners;
    private boolean mSubscribed = false;

    public TimetableDayEventViewModel(Database database, List<Group> groups, @TimetableDay.Days int day) {
        mDay = day;
        mGroups =groups;
        mDatabase = database;
        events = new ObservableArrayList<>();
        mListeners = new ArrayList<>();
        mListener = new CoursesEventValueListener() {
            @Override
            public void onEventsListChange(List<Event> eventsDB) {
                if (events.size()!=0)
                    for (TimetableDayEventListener listener : mListeners)
                        listener.onDayChanged();
                events.clear();
                if (eventsDB == null) return;
                ArrayList<Event> noNullEventsDay = new ArrayList<>();
                for (Event event : eventsDB)
                    if (event != null) noNullEventsDay.add(event);

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
            if (!mSubscribed) {
                mDatabase.addEventValueEventListener(mGroups, mDay, mListener);
                mSubscribed=true;
            }
        }
    }

    public void unSubscribe(TimetableDayEventListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
            if (mSubscribed) {
                mDatabase.removeEventValueEventListener(mListener);
                mSubscribed=false;
            }
        }
    }
}
