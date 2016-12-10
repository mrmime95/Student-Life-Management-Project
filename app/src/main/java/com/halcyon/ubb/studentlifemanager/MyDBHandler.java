package com.halcyon.ubb.studentlifemanager;

/**
 * Created by Szilard on 11.12.2016.
 */

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyDBHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "StudLifeManager.db";
    public static final String CONTACTS_TABLE_NAME = "reminders";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_DATE = "date";
    public static final String CONTACTS_COLUMN_TIME = "time";
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table reminders " +
                        "(id integer primary key, name text,date text,time text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS reminders");
        onCreate(db);
    }

    public boolean insertContact (ReminderContact contact) {
        String name = contact.getName();
        String date = contact.getDate();
        String time = contact.getTime();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("date", date);
        contentValues.put("time", time);

        db.insert("reminders", null, contentValues);
        return true;
    }
    public void deleteContact(ReminderContact contact)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + CONTACTS_TABLE_NAME + " WHERE " + CONTACTS_COLUMN_NAME  + "=\""
                +  contact.getName() + "\"" + " AND "+ CONTACTS_COLUMN_DATE + "=\"" + contact.getDate()
                + "\"" + " AND " + CONTACTS_COLUMN_TIME + "=\"" + contact.getTime() + "\"");
    }
    public ArrayList<ReminderContact> getAllReminderCotacts() {
        ArrayList<ReminderContact> array_list = new ArrayList<ReminderContact>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from reminders", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ReminderContact temp = new ReminderContact(1,
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_DATE)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_TIME))
            );
            array_list.add(temp);
            Log.d("Szar:", res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
