package com.halcyon.ubb.studentlifemanager.ui.timetable.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.halcyon.ubb.studentlifemanager.database.remote.RemoteDatabase;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by Baroti Csaba on 12/11/2016.
 */

public class TimetableDayEventViewModel extends BaseObservable{
    public ObservableList<Event> events;
    public ObservableField<Integer> mIsOfflineMessageVisible;

    private int mDay;
    private Set<Group> mGroups;
    private RemoteDatabase mRemoteDatabase;
    private CoursesEventValueListener mListener;
    private List<TimetableDayEventListener> mListeners;
    private boolean mSubscribed = false;
    private boolean mChanged;

    public TimetableDayEventViewModel(RemoteDatabase remoteDatabase, Set<Group> groups, @Timetable.Days int day) {
        mDay = day;
        mGroups =groups;
        mChanged=false;
        mIsOfflineMessageVisible=new ObservableField<>(View.GONE);
        mRemoteDatabase = remoteDatabase;
        events = new ObservableArrayList<>();
        mListeners = new ArrayList<>();
        mListener = new CoursesEventValueListener() {
            @Override
            public void onEventsListChange(List<Event> eventsDB) {
                if (mChanged) {
                    for (TimetableDayEventListener listener : mListeners)
                        listener.onDayChanged();
                }
                mChanged=true;
                events.clear();
                ArrayList<Event> noNullEventsDay = new ArrayList<>();
                for (Event event : eventsDB)
                    if (event != null) noNullEventsDay.add(event);

                mIsOfflineMessageVisible.set(View.GONE);
                notifyChange();
                events.addAll(noNullEventsDay);
            }

            @Override
            public void onTimeout() {
                if (events.size()==0) mIsOfflineMessageVisible.set(View.VISIBLE);
                notifyChange();
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
        mChanged=false;
        removeDatabaseListener();
        mGroups=groups;
        events.clear();
        notifyChange();
        addDatabaseListener();
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(RecyclerView view, ObservableList<Event> events) {
        view.setVisibility(events.isEmpty()?View.GONE:View.VISIBLE);
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(ProgressBar view, TimetableDayEventViewModel model) {
        view.setVisibility(model.events.isEmpty() && model.mIsOfflineMessageVisible.get()==View.GONE
                ?View.VISIBLE:View.GONE);
    }
}
