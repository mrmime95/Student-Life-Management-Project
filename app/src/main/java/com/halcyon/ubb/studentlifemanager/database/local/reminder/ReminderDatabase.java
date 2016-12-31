package com.halcyon.ubb.studentlifemanager.database.local.reminder;

import com.halcyon.ubb.studentlifemanager.model.reminder.Reminder;

import java.util.ArrayList;

/**
 *
 * Created by Baroti Csaba on 12/29/2016.
 */

public interface ReminderDatabase {
    boolean insert(Reminder contact);
    boolean delete(Reminder contact);
    ArrayList<Reminder> getAllReminder();
}
