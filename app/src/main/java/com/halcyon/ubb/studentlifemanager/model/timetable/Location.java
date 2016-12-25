package com.halcyon.ubb.studentlifemanager.model.timetable;

/**
 *
 * Created by Baroti Csaba on 12/23/2016.
 */

@SuppressWarnings("unused")
public class Location {
    private String mShortName;

    public Location() {}

    public Location(String shortName) {
        mShortName =shortName;
    }

    public void setShortName(String shortName) {
        mShortName=shortName;
    }

    public String getShortName() {
        return mShortName;
    }
}
