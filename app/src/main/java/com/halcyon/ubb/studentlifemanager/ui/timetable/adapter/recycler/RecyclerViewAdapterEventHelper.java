package com.halcyon.ubb.studentlifemanager.ui.timetable.adapter.recycler;

import android.databinding.ObservableList;

import com.halcyon.ubb.studentlifemanager.model.course.Course;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.EventComparators;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.ui.timetable.viewmodel.EventViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 *
 * Created by Baroti Csaba on 12/14/2016.
 */

abstract class RecyclerViewAdapterEventHelper {
    /**
     *  Converts ObservableList<Event> to ArrayList<Event> through making fake events
     *  to be able to show different viewholders.
     *
     *  Types : Time, Event
     *
     * <pre>        Time:  *   9:00
     * Event:  |      | Event1  |
     *         |      | Event2  |</pre>
     * @param events events that can be displayed
     * @return a ViewModel representation of {@link TimetableDay}
     */
    static ArrayList<EventViewModel> createEventsForRecycler(ObservableList<Event> events) {
        if (events==null) return new ArrayList<>();

        ArrayList<EventViewModel> viewModels=new ArrayList<>();
        Collections.sort(events, EventComparators.mStartingTimeHourComparator);

        Calendar calendar=Calendar.getInstance();
        int hour=0;

        for (Event event:events) {
            if (event.getStartingDate()!=null) {
                calendar.setTime(event.getStartingDate());
                int ihour = calendar.get(Calendar.HOUR_OF_DAY);
                if (ihour != hour) {
                    calendar.set(Calendar.MINUTE, 0);
                    viewModels.add(new EventViewModel(new Event(new Course("#FAKE",null),calendar.getTime())));
                }
                viewModels.add(new EventViewModel(event));
                hour=ihour;
            }
        }

        if (events.size()>0)
            viewModels.add(new EventViewModel(new Event()));

        return viewModels;
    }
}
