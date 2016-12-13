package com.halcyon.ubb.studentlifemanager.model.timetable;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */

public class Timetable implements Serializable {
    public  List<TimetableDay> mDays;

    public Timetable() {
        this(new ArrayList<TimetableDay>());
    }

    public Timetable(ArrayList<TimetableDay> days) {
        mDays=days;
    }

    public int getDayCount() {
        return mDays.size();
    }

    //TODO CR: Static methods should not be part of a model class (consider moving it into a separate helper class). [Peter]
    public static Timetable union(Collection<Timetable> timetables) {
        return null;
    }

    /*public static  Timetable unionScheduleables(Collection<Scheduleable> scheduleables) {
        ArrayList<Timetable> list=new ArrayList<>();

        for (Scheduleable scheduleable:scheduleables)
            list.add(scheduleable.addTimetableDayValueListener());

        return Timetable.union(list);
    }*/

}
