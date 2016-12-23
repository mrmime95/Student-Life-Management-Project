package com.halcyon.ubb.studentlifemanager.model.timetable;

/**
 * Created by Baroti Csaba on 12/23/2016.
 */

public class Location {
    private String mAddress;

    public Location() {}

    public Location(String address) {
        mAddress=address;
    }

    public String getLocation() {
        return mAddress;
    }
}
