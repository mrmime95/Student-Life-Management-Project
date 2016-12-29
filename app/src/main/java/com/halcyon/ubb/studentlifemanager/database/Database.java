package com.halcyon.ubb.studentlifemanager.database;

import com.halcyon.ubb.studentlifemanager.database.listener.GroupsValueEventListener;
import com.halcyon.ubb.studentlifemanager.database.listener.ValidationListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Course;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * Created by Baroti Csaba on 12/13/2016.
 */
public interface Database {
    void addEventValueEventListener(List<Group> groups, @TimetableDay.Days int day, final CoursesEventValueListener listener);
    void removeEventValueEventListener(CoursesEventValueListener listener);

    void fetchGroups(GroupsValueEventListener groupsValueEventListener);

    void addGroupsValueEventListener(GroupsValueEventListener groupsValueEventListener);
    void removeGroupsValueEventListener(GroupsValueEventListener groupsValueEventListener);


    void createTestData();
    void deleteTestData();

    void validateKeysOnGroups(Set<Group> table, GroupsValueEventListener validationListener);
}
