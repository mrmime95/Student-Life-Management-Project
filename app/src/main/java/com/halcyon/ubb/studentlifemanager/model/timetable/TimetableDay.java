package com.halcyon.ubb.studentlifemanager.model.timetable;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */

public class TimetableDay implements Serializable {
    @Retention(SOURCE)
    @IntDef({MON,TUE,WED,THU,FRI,SAT})
    public @interface Days {}
    public static final int MON=0;
    public static final int TUE=1;
    public static final int WED=2;
    public static final int THU=3;
    public static final int FRI=4;
    public static final int SAT=5;

    private ArrayList<Event> mEvents;

    public static Comparator<Event> mStartingTimeComparator =new Comparator<Event>() {
        @Override
        public int compare(Event a, Event b) {
            return a.getStartingTime().compareTo(b.getStartingTime());
        }
    };

    public TimetableDay(ArrayList<Event> events) {
        mEvents=events;
    }

    public ArrayList<Event> sortEventsInStartingTime() {
        Collections.sort(mEvents, mStartingTimeComparator);
        return mEvents;
    }
}
