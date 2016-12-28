package com.halcyon.ubb.studentlifemanager.database;

import android.app.Activity;
import android.content.Context;

import com.halcyon.ubb.studentlifemanager.database.listener.GroupsValueEventListener;
import com.halcyon.ubb.studentlifemanager.database.listener.LocalTimetableListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;

import java.util.List;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */
public interface LocalTimetableDatabase {
    String PREF_DIVIDER="_DIVIDER_";
    void readWantedTimetables(Context context, LocalTimetableListener listener);
    void readLocalTimetables(Activity activity, LocalTimetableListener listener);
}
