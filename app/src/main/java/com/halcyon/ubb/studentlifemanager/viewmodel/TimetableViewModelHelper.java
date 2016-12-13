package com.halcyon.ubb.studentlifemanager.viewmodel;

import android.provider.CalendarContract;

import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */

//TODO CR: Helper classes must not be instantiated: consider making it abstract or adding a private constructor. [Peter]
public class TimetableViewModelHelper {

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
        ArrayList<ICEventViewModel> vm=new ArrayList<>();
        ArrayList<Event> events=table.sortEventsInStartingTime();

        Calendar calendar=Calendar.getInstance();
        int hour=0;

        //TODO CR: Consider using for (Event event : events). [Peter]
        for (int i=0;i<events.size();++i) {
            Event event=events.get(i);
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
