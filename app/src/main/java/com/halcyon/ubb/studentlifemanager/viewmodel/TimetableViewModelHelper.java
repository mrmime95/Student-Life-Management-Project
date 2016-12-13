package com.halcyon.ubb.studentlifemanager.viewmodel;

import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.EventComparators;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */

public abstract class TimetableViewModelHelper {

    /**
     *  Converts TimeTableDay to ICEventViewModels so it can be put into recycler
     *  Types : Time, Event
     *
     * <pre>        Time:  *   9:00
     * Event:  |      | Event1  |
     *         |      | Event2  |</pre>
     * @param table
     * @return a ViewModel representation of {@link TimetableDay}
     */
    public static ArrayList<ICEventViewModel> createEventViewModels(TimetableDay table) {
        if (table==null) return new ArrayList<>();

        ArrayList<ICEventViewModel> vm=new ArrayList<>();
        ArrayList<Event> events=table.events;
        Collections.sort(events, EventComparators.mStartingTimeComparator);

        Calendar calendar=Calendar.getInstance();
        int hour=0;

        for (Event event:events) {
            if (event.getStartingTime()!=null) {
                calendar.setTime(event.getStartingTime());
                int ihour = calendar.get(Calendar.HOUR_OF_DAY);
                if (ihour != hour) {
                    calendar.set(Calendar.MINUTE, 0);
                    vm.add(new EventStartViewModel(new Event("", "", calendar.getTime(), calendar.getTime())));
                }
                vm.add(new EventViewModel(event));
                hour=ihour;
            }
        }

        if (events.size()>0)
            vm.add(new EventEndPoint());

        return vm;
    }
}
