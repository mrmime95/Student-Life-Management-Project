package com.halcyon.ubb.studentlifemanager.database.listener;

import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;

import java.util.List;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */

public interface LocalTimetableListener {
    void onLocalTimetablesLoaded(List<Timetable> tables);
}
