package com.halcyon.ubb.studentlifemanager.database;

import android.content.Context;
import android.support.annotation.IntDef;

import com.halcyon.ubb.studentlifemanager.database.local.timetable.LocalTimetableDatabase;
import com.halcyon.ubb.studentlifemanager.database.local.reminder.ReminderDatabase;
import com.halcyon.ubb.studentlifemanager.database.remote.RemoteDatabase;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */
public abstract class DatabaseProvider {
    @Retention(SOURCE)
    @IntDef({FIREBASE_FILE})
    public @interface DatabaseType {}
    public static final int FIREBASE_FILE =1;

    private static DatabaseProvider mInstance;
    RemoteDatabase mRemoteDatabase;
    LocalTimetableDatabase mLocalTimetableDatabase;
    ReminderDatabase mRemainderDatabase;

    public static void setDatabaseType(Context context, @DatabaseType int type) {
        if (mInstance==null)
            switch (type) {
                case FIREBASE_FILE:
                    mInstance=new SQLFirebaseFileDatabaseProvider(context);
            }
    }

    public static DatabaseProvider getInstance() {
        if (mInstance==null)
            throw new IllegalStateException("Set database type first.");
        return mInstance;
    }

    DatabaseProvider() {
    }

    public RemoteDatabase getRemoteDatabase() {
        return mRemoteDatabase;
    }

    public LocalTimetableDatabase getLocalTimetableDatabase() {
        return mLocalTimetableDatabase;
    }

    public ReminderDatabase getReminderDatabase() {
        return mRemainderDatabase;
    }
}
