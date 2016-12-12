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
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        "( " + CONTACTS_COLUMN_ID  + " integer primary key, " + CONTACTS_COLUMN_NAME
                        + " text, " + CONTACTS_COLUMN_DATE + " text, " + CONTACTS_COLUMN_TIME + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact (ReminderContact contact) {
        String name = contact.getName();
        String date = contact.getDate();
        String time = contact.getTime();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_DATE, date);
        contentValues.put(CONTACTS_COLUMN_TIME, time);

        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean deleteContact(ReminderContact contact)
    {
        SQLiteDatabase database = getWritableDatabase();
        return  database.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_NAME + "= \"" + contact.getName() + "\"" +
                " and " + CONTACTS_COLUMN_DATE + "=\"" + contact.getDate() + "\"" + " and " +
                CONTACTS_COLUMN_TIME + "=\"" + contact.getTime() + "\"", null) > 0;
    }

    public ArrayList<ReminderContact> getAllReminderCotacts() {
        ArrayList<ReminderContact> array_list = new ArrayList<ReminderContact>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CONTACTS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ReminderContact temp = new ReminderContact(1,
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_DATE)),
                    res.getString(res.getColumnIndex(CONTACTS_COLUMN_TIME))
            );
            Log.d("INFO ------- ID = ", res.getString(res.getColumnIndex(CONTACTS_COLUMN_ID)));
            array_list.add(temp);
            res.moveToNext();
        }
        return array_list;
    }
}
