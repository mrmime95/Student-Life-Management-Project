package com.halcyon.ubb.studentlifemanager.database.local.reminder;

import com.halcyon.ubb.studentlifemanager.ReminderContact;

import java.util.ArrayList;

/**
 *
 * Created by Baroti Csaba on 12/29/2016.
 */

public interface ReminderDatabase {
    boolean insert(ReminderContact contact);
    boolean delete(ReminderContact contact);
    ArrayList<ReminderContact> getAllReminder();
}
