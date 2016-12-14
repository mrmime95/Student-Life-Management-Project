package com.halcyon.ubb.studentlifemanager.model.timetable;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.util.ArrayList;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 *
 * Created by Baroti Csaba on 12/11/2016.
 */

@SuppressWarnings("WeakerAccess")
public class TimetableDay implements Serializable {
    //TODO CR: The IntDef declaration is incorrect, the constants should be between the curly braces following "@interface Days". [Peter]
    @Retention(SOURCE)
    @IntDef({MON,TUE,WED,THU,FRI,SAT})
    public @interface Days {}
    public static final int MON=0;
    public static final int TUE=1;
    public static final int WED=2;
    public static final int THU=3;
    public static final int FRI=4;
    public static final int SAT=5;

    public  ArrayList<Event> events;

    //Firebase needs empty constructor
    @SuppressWarnings("unused")
    public TimetableDay() {}

    public TimetableDay(ArrayList<Event> events) {
        this.events =events;
    }
}
