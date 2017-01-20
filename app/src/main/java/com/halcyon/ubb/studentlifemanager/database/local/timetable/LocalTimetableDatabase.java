package com.halcyon.ubb.studentlifemanager.database.local.timetable;

import android.content.Context;

import com.halcyon.ubb.studentlifemanager.database.listener.LocalTimetableListener;
import com.halcyon.ubb.studentlifemanager.database.listener.OperationCompleteListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */
public interface LocalTimetableDatabase {
    String PREF_DIVIDER="_DIVIDER_";
    void readWantedTimetables(Context context, LocalTimetableListener listener);
    void readLocalTimetables(Context context, LocalTimetableListener listener);
    void writeLocalTimetable(Context context, Timetable table, OperationCompleteListener operationCompleteListener);

    void removeWantedTimetable(Context context, Timetable table);
}
