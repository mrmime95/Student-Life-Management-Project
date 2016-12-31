package com.halcyon.ubb.studentlifemanager.database.remote;

import com.halcyon.ubb.studentlifemanager.database.listener.GroupsValueEventListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;

import java.util.Set;

/**
 *
 * Created by Baroti Csaba on 12/13/2016.
 */
public interface RemoteDatabase {
    void addEventValueEventListener(Set<Group> groups, @TimetableDay.Days int day, final CoursesEventValueListener listener);
    void removeEventValueEventListener(CoursesEventValueListener listener);

    void fetchGroups(GroupsValueEventListener groupsValueEventListener);

    void addGroupsValueEventListener(GroupsValueEventListener groupsValueEventListener);
    void removeGroupsValueEventListener(GroupsValueEventListener groupsValueEventListener);


    void createTestData();
    void deleteTestData();

    //same group reference returned
    void validateKeysOnGroups(Set<Group> table, GroupsValueEventListener validationListener);
}