package com.halcyon.ubb.studentlifemanager.database;

import com.halcyon.ubb.studentlifemanager.database.listener.GroupsValueEventListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;

import java.util.List;

/**
 *
 * Created by Baroti Csaba on 12/13/2016.
 */
public interface Database {
    void addCoursesEventValueListener(List<String> coursesKey, @TimetableDay.Days final int day, final CoursesEventValueListener listener);
    void removeCoursesEventValueListener(List<String> coursesKey, @TimetableDay.Days final int day, CoursesEventValueListener listener);

    void addGroupsValueEventListener(GroupsValueEventListener groupsValueEventListener);
    void removeGroupsValueEventListener(GroupsValueEventListener groupsValueEventListener);
}
