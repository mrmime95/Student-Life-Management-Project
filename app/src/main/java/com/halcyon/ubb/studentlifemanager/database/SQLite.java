package com.halcyon.ubb.studentlifemanager.database;

import com.halcyon.ubb.studentlifemanager.ReminderContact;

import java.util.ArrayList;

/**
 * Created by Szilard on 2016. 12. 18..
 */

public interface SQLite {
    boolean insert(ReminderContact contact);
    boolean delete(ReminderContact contact);
    ArrayList<ReminderContact> getAllReminder();
}
