package com.halcyon.ubb.studentlifemanager.database.listener;

import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;

import java.util.List;
import java.util.Set;

/**
 *
 * Created by Baroti Csaba on 1/16/2017.
 */

public interface ValueEventListListener<T> extends TimeoutListener,CancelattionListener{
    void onChange(List<T> items);
}
