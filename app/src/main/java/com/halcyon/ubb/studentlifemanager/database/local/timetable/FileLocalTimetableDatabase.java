package com.halcyon.ubb.studentlifemanager.database.local.timetable;

import android.content.Context;
import android.preference.PreferenceManager;

import com.halcyon.ubb.studentlifemanager.database.listener.LocalTimetableListener;
import com.halcyon.ubb.studentlifemanager.database.listener.OperationCompleteListener;
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

public class FileLocalTimetableDatabase implements LocalTimetableDatabase {

    public FileLocalTimetableDatabase(Context context) {

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
    public void readLocalTimetables(Context context, LocalTimetableListener listener) {
        listener.onLocalTimetablesLoaded(new ArrayList<Timetable>());
    }

    @Override
    public void writeLocalTimetable(Context context, Timetable table, OperationCompleteListener operationCompleteListener) {

    }
}
