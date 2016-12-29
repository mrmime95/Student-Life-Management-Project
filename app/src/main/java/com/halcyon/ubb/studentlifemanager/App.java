package com.halcyon.ubb.studentlifemanager;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import com.halcyon.ubb.studentlifemanager.database.DatabaseProvider;
import com.halcyon.ubb.studentlifemanager.ui.timetable.usersettings.TimetableSettingsActivity;

/**
 *
 * Created by matyas on 2016.12.17..
 */

public class App extends Application {
    public static String INITIAL_LUNCH="initial_lunch";

    public static boolean isInitial(Context context) {
        boolean init=PreferenceManager.getDefaultSharedPreferences(context).getBoolean(INITIAL_LUNCH,true);
        if (init) {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(INITIAL_LUNCH,false).apply();
            return true;
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseProvider.setDatabaseType(this,DatabaseProvider.FIREBASE_FILE);

        if (App.isInitial(this)) {
            TimetableSettingsActivity.initSettings(this);
        }
    }
}
