package com.halcyon.ubb.studentlifemanager.database.local.reminder;

/**
 *
 * Created by Szilard on 11.12.2016.
 */

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.halcyon.ubb.studentlifemanager.Reminder;

public class SQLiteReminderDatabase extends SQLiteOpenHelper implements ReminderDatabase{

    public static final String DATABASE_NAME = "StudLifeManager.db";
    public static final String CONTACTS_TABLE_NAME = "reminders";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_DATE = "date";
    public static final String CONTACTS_COLUMN_TIME = "time";
    public SQLiteReminderDatabase(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        "( " + CONTACTS_COLUMN_ID  + " integer primary key, " + CONTACTS_COLUMN_NAME
                        + " text, " + CONTACTS_COLUMN_DATE + " text, " + CONTACTS_COLUMN_TIME + " text)"
        );
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public boolean insert(Reminder contact) {
        String name = contact.getName();
        String date = contact.getDate();
        String time = contact.getTime();
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_DATE, date);
        contentValues.put(CONTACTS_COLUMN_TIME, time);

        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    @Override
    public boolean delete(Reminder contact) {
        android.database.sqlite.SQLiteDatabase database = getWritableDatabase();
        return  database.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_NAME + "= \"" + contact.getName() + "\"" +
                " and " + CONTACTS_COLUMN_DATE + "=\"" + contact.getDate() + "\"" + " and " +
                CONTACTS_COLUMN_TIME + "=\"" + contact.getTime() + "\"", null) > 0;
    }

    @Override
    public ArrayList<Reminder> getAllReminder() {
        ArrayList<Reminder> array_list = new ArrayList<Reminder>();
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CONTACTS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Reminder temp = new Reminder(1,
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_DATE)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_TIME))
            );
            //Log.d("INFO ------- ID = ", res.getString(res.getColumnIndex(CONTACTS_COLUMN_ID)));
            array_list.add(temp);
            res.moveToNext();
        }
        return array_list;
    }
}
