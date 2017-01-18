package com.halcyon.ubb.studentlifemanager.database.listener;

import com.halcyon.ubb.studentlifemanager.model.timetable.Event;

import java.util.List;

/**
 *
 * Created by Baroti Csaba on 12/12/2016.
 */

public interface CoursesEventValueListener {
    void onEventsListChange(List<Event> events);
    void onTimeout();
    void onCancelled(Exception e);
}
