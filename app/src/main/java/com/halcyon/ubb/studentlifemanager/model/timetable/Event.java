package com.halcyon.ubb.studentlifemanager.model.timetable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */

public class Event implements Serializable{
    private String mName;
    private String mLocation;
    private String mDescription;

    private Date mStartingTime;
    private Date mEndingTime;

    public Event(String name,String description) {
        mName=name;
        mDescription=description;
    }

    public Event(String name,String description,Date startingTime,Date endingTime) {
        this(name,description);
        mStartingTime=startingTime;
        mEndingTime=endingTime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Date getStartingTime() {
        return mStartingTime;
    }

    public void setStartingTime(Date startingTime) {
        this.mStartingTime = startingTime;
    }

    public Date getEndingTime() {
        return mEndingTime;
    }

    public void setEndingTime(Date endingTime) {
        this.mEndingTime = endingTime;
    }
}
