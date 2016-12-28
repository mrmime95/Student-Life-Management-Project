package com.halcyon.ubb.studentlifemanager.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.SharedPreferencesCompat;

import com.halcyon.ubb.studentlifemanager.database.listener.GroupsValueEventListener;
import com.halcyon.ubb.studentlifemanager.database.listener.LocalTimetableListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */

class FileLocalTimetableDB implements LocalTimetableDatabase {

    FileLocalTimetableDB(Context context) {

    }

    @Override
    public void readWantedTimetables(Context context, LocalTimetableListener listener) {
        List<Timetable> tables=new ArrayList<>();
        Set<String> groupKeys=PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet("visible_timetables",new HashSet<String>());

        for (String groupKey:groupKeys) {
            String[] divided = groupKey.split(PREF_DIVIDER);
            Timetable table=new Timetable(divided[1]);
            Group group=new Group();
            group.setName(divided[1]);
            group.setKey(divided[0]);
            table.addGroup(group);
            tables.add(table);
        }

        listener.onLocalTimetablesLoaded(tables);

    }

    @Override
    public void readLocalTimetables(Activity activity, LocalTimetableListener listener) {
        listener.onLocalTimetablesLoaded(new ArrayList<Timetable>());
    }
}
