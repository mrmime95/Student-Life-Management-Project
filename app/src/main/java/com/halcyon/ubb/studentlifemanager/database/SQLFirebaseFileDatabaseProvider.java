package com.halcyon.ubb.studentlifemanager.database;

import android.content.Context;

import com.halcyon.ubb.studentlifemanager.database.local.reminder.ReminderDatabase;
import com.halcyon.ubb.studentlifemanager.database.local.timetable.FileLocalTimetableDatabase;
import com.halcyon.ubb.studentlifemanager.database.local.reminder.SQLiteReminderDatabase;
import com.halcyon.ubb.studentlifemanager.database.local.timetable.LocalTimetableDatabase;
import com.halcyon.ubb.studentlifemanager.database.remote.FirebaseDB;
import com.halcyon.ubb.studentlifemanager.database.remote.RemoteDatabase;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */
public class SQLFirebaseFileDatabaseProvider extends DatabaseProvider {
    private volatile RemoteDatabase mRemoteDatabase;
    private volatile LocalTimetableDatabase mLocalTimetableDatabase;
    private volatile ReminderDatabase mRemainderDatabase;

    SQLFirebaseFileDatabaseProvider() {
        super();
    }

    @Override
    public RemoteDatabase getRemoteDatabase() {
            if (mRemoteDatabase==null)
                synchronized (this) {
                    if (mRemoteDatabase==null)
                        mRemoteDatabase=new FirebaseDB();
                }
            return mRemoteDatabase;
    }

    @Override
    public LocalTimetableDatabase getLocalTimetableDatabase(Context context) {
            if (mLocalTimetableDatabase==null)
                synchronized (this) {
                    if (mLocalTimetableDatabase==null)
                        mLocalTimetableDatabase=new FileLocalTimetableDatabase(context);
                }
            return mLocalTimetableDatabase;
    }

    @Override
    public ReminderDatabase getReminderDatabase(Context context) {
            if (mRemainderDatabase==null)
                synchronized (this) {
                    if (mRemainderDatabase==null)
                        mRemainderDatabase=new SQLiteReminderDatabase(context);
                }
            return mRemainderDatabase;
    }
}
