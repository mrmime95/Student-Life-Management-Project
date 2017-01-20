package com.halcyon.ubb.studentlifemanager.database.remote;

import com.halcyon.ubb.studentlifemanager.database.ConnectionListener;
import com.halcyon.ubb.studentlifemanager.database.listener.ValueEventListListener;
import com.halcyon.ubb.studentlifemanager.database.listener.ValueEventSetListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;

import java.util.List;
import java.util.Set;

/**
 *
 * Created by Baroti Csaba on 12/13/2016.
 */
public interface RemoteDatabase {
    void addEventValueEventListener(Set<Group> groups, @Timetable.Days int day, final CoursesEventValueListener listener);
    void removeEventValueEventListener(CoursesEventValueListener listener);

    void fetchGroups(ValueEventSetListener<Group> valueEventSetListener);

    void addGroupsValueEventListener(ValueEventSetListener<Group> valueEventSetListener);
    void removeGroupsValueEventListener(ValueEventSetListener<Group> valueEventSetListener);


    void createTestData();
    void deleteTestData();

    //same group reference returned
    void validateKeysOnGroups(Set<Group> table, ValueEventSetListener<Group> validationListener);
    void validateKeysOnTimetablesGroups(List<Timetable> timetables, ValueEventListListener<Timetable> validationListener);

    void addConnectionListener(ConnectionListener connectionListener);
    void removeConnectionListener(ConnectionListener connectionListener);
}
