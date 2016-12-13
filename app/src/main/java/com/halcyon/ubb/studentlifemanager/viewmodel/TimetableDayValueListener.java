package com.halcyon.ubb.studentlifemanager.viewmodel;

import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;

import java.util.List;

/**
 * Created by Baroti Csaba on 12/12/2016.
 */

public interface TimetableDayValueListener {
    void onDayChange(TimetableDay day);
    void onCancelled(Exception e);
}
