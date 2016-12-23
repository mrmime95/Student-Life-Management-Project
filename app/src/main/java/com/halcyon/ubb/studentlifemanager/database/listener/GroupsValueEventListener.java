package com.halcyon.ubb.studentlifemanager.database.listener;

import com.halcyon.ubb.studentlifemanager.model.timetable.Group;

import java.util.List;

/**
 * Created by Baroti Csaba on 12/23/2016.
 */

public interface GroupsValueEventListener {
    void onGroupsChange(List<Group> groups);
    void onCancelled(Exception e);
}
