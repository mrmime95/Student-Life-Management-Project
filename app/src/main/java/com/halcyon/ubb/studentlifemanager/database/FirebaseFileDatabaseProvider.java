package com.halcyon.ubb.studentlifemanager.database;

import android.content.Context;

/**
 * Created by Baroti Csaba on 12/27/2016.
 */
public class FirebaseFileDatabaseProvider extends DatabaseProvider {

    FirebaseFileDatabaseProvider(Context context) {
        super();
        mDatabase=new FirebaseDB(context);
        mTimetableDatabase=new FileLocalTimetableDB(context);
    }

}
