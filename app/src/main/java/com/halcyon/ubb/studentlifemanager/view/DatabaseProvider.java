package com.halcyon.ubb.studentlifemanager.view;

import com.halcyon.ubb.studentlifemanager.database.Database;

/**
 * Created by Baroti Csaba on 12/23/2016.
 */

public interface DatabaseProvider {
    Database getDatabase();
}
