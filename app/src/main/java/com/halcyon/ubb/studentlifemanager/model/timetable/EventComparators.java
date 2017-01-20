package com.halcyon.ubb.studentlifemanager.model.timetable;

import java.util.Calendar;
import java.util.Comparator;

/**
 *
 * Created by Baroti Csaba on 12/13/2016.
 */

public abstract class EventComparators {

    @SuppressWarnings("unused")
    public static Comparator<Event> mStartingTimeComparator=new Comparator<Event>() {
        @Override
        public int compare(Event a, Event b) {
            return a.getStartingDate().compareTo(b.getStartingDate());
        }
    };

    public static Comparator<Event> mStartingTimeHourComparator=new Comparator<Event>() {
        @Override
        public int compare(Event a, Event b) {
            Calendar calendarA=Calendar.getInstance();
            Calendar calendarB=Calendar.getInstance();
            calendarA.setTime(a.getStartingDate());
            calendarB.setTime(b.getStartingDate());
            return calendarA.get(Calendar.HOUR_OF_DAY)-calendarB.get(Calendar.HOUR_OF_DAY);
        }
    };
}
