package com.halcyon.ubb.studentlifemanager.database;

import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.viewmodel.TimetableDayValueListener;

/**
 *
 * Created by Baroti Csaba on 12/13/2016.
 */
public interface Database {
    void addTimetableDayValueListener(String course, @TimetableDay.Days final int day, final TimetableDayValueListener listener);
    void removeTimetableDayValueListener(String course,@TimetableDay.Days final int day,TimetableDayValueListener listener);
}
