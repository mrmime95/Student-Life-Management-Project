package com.halcyon.ubb.studentlifemanager.model.timetable;

import java.util.Comparator;

/**
 * Created by Baroti Csaba on 12/13/2016.
 */

public abstract class EventComparators {
    public static Comparator<Event> mStartingTimeComparator=new Comparator<Event>() {
        @Override
        public int compare(Event a, Event b) {
            return a.getStartingTime().compareTo(b.getStartingTime());
        }
    };
}
