package com.halcyon.ubb.studentlifemanager;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.halcyon.ubb.studentlifemanager.database.DatabaseProvider;

/**
 * Created by matyas on 2016.12.17..
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseProvider.setDatabaseType(this,DatabaseProvider.FIREBASE_FILE);
    }
}
