package com.halcyon.ubb.studentlifemanager.database.listener;

import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;

/**
 *
 * Created by Baroti Csaba on 12/28/2016.
 */

public interface ValidationListener {
    void onSuccess();
    void onChangesDetected(Timetable newTimetable);
    void onFaliure(Exception e);
}
