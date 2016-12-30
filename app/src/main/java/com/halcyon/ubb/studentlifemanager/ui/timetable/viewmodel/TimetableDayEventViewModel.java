package com.halcyon.ubb.studentlifemanager.ui.timetable.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.halcyon.ubb.studentlifemanager.database.remote.RemoteDatabase;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by Baroti Csaba on 12/11/2016.
 */

public class TimetableDayEventViewModel {
    public ObservableList<Event> events;
    private int mDay;
    private Set<Group> mGroups;
    private RemoteDatabase mRemoteDatabase;
    private CoursesEventValueListener mListener;
    private List<TimetableDayEventListener> mListeners;
    private boolean mSubscribed = false;
    private boolean mChanged=false;

    public TimetableDayEventViewModel(RemoteDatabase remoteDatabase, Set<Group> groups, @TimetableDay.Days int day) {
        mDay = day;
        mGroups =groups;
        mRemoteDatabase = remoteDatabase;
        events = new ObservableArrayList<>();
        mListeners = new ArrayList<>();
        mListener = new CoursesEventValueListener() {
            @Override
            public void onEventsListChange(List<Event> eventsDB) {
                if (mChanged) {
                    for (TimetableDayEventListener listener : mListeners)
                        listener.onDayChanged();
                    mChanged=true;
                }
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
            addDatabaseListener();
        }
    }

    public void unSubscribe(TimetableDayEventListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
            removeDatabaseListener();
        }
    }

    public void addDatabaseListener() {
        if (!mSubscribed) {
            mRemoteDatabase.addEventValueEventListener(mGroups, mDay, mListener);
            mSubscribed=true;
        }
    }

    public void removeDatabaseListener() {
        if (mSubscribed) {
            mRemoteDatabase.removeEventValueEventListener(mListener);
            mSubscribed=false;
        }
    }

    public void setGroups(Set<Group> groups) {
        removeDatabaseListener();
        mChanged=false;
        mGroups=groups;
        addDatabaseListener();
    }
}
