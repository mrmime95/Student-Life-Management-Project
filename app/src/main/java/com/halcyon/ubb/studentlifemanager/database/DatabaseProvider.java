package com.halcyon.ubb.studentlifemanager.database;

import android.content.Context;
import android.support.annotation.IntDef;

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
    Database mDatabase;
    LocalTimetableDatabase mTimetableDatabase;

    public static void setDatabaseType(Context context, @DatabaseType int type) {
        if (mInstance==null)
            switch (type) {
                case FIREBASE_FILE:
                    mInstance=new FirebaseFileDatabaseProvider(context);
            }
    }

    public static DatabaseProvider getInstance() {
        if (mInstance==null)
            throw new IllegalStateException("Set database type first.");
        return mInstance;
    }

    DatabaseProvider() {
    }

    public Database getRemoteDatabase() {
        return mDatabase;
    }

    public LocalTimetableDatabase getLocalTimetableDatabase() {
        return mTimetableDatabase;
    }
}
