package com.halcyon.ubb.studentlifemanager;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.halcyon.ubb.studentlifemanager.database.DatabaseProvider;
import com.halcyon.ubb.studentlifemanager.ui.timetable.usersettings.TimetableSettingsActivity;

/**
 *
 * Created by matyas on 2016.12.17..
 */

public class StudentLifeManagerApplication extends Application {
    public static String INITIAL_LAUNCH = "initial_launch";

    public static boolean isInitial(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getBoolean(INITIAL_LAUNCH, true)) {
            sharedPreferences.edit().putBoolean(INITIAL_LAUNCH, false).apply();
            return true;
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseProvider.setDatabaseType(DatabaseProvider.FIREBASE_FILE);

        if (isInitial(this)) {
            TimetableSettingsActivity.initSettings(this);
        }
    }
}