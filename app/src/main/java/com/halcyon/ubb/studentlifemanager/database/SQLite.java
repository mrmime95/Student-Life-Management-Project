package com.halcyon.ubb.studentlifemanager.database;

import com.halcyon.ubb.studentlifemanager.Reminder;

import java.util.ArrayList;

/**
 * Created by Szilard on 2016. 12. 18..
 */

public interface SQLite {
    boolean insert(Reminder contact);
    boolean delete(Reminder contact);
    ArrayList<Reminder> getAllReminder();
}
