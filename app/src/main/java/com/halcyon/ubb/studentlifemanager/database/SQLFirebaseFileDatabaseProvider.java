package com.halcyon.ubb.studentlifemanager.database;

import android.content.Context;

import com.halcyon.ubb.studentlifemanager.database.local.timetable.FileLocalTimetableDatabase;
import com.halcyon.ubb.studentlifemanager.database.local.reminder.SQLiteReminderDatabase;
import com.halcyon.ubb.studentlifemanager.database.remote.FirebaseDB;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */
public class SQLFirebaseFileDatabaseProvider extends DatabaseProvider {

    SQLFirebaseFileDatabaseProvider(Context context) {
        super();
        mRemoteDatabase =new FirebaseDB(context);
        mLocalTimetableDatabase =new FileLocalTimetableDatabase(context);
        mRemainderDatabase=new SQLiteReminderDatabase(context);
    }

}
