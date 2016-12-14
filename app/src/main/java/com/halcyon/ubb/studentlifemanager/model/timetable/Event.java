package com.halcyon.ubb.studentlifemanager.model.timetable;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 *
 * Created by Baroti Csaba on 12/11/2016.
 */

@SuppressWarnings("WeakerAccess")
public class Event {
    private String mName;
    private String mDescription;


    //public String location;

    //firebase doesn't like date
    public long startingTime;
    public long endingTime;

    //Firebase needs empty constructor
    public Event() {
    }

    public Event(String name,String description) {
        this.mName=name;
        this.mDescription =description;
    }

    public Event(String name,String description,Date startingTime,Date endingTime) {
        this(name,description);
        this.startingTime =startingTime==null?0:startingTime.getTime();
        this.endingTime =endingTime==null?0:endingTime.getTime();
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {this.mDescription = mDescription;}

    /*public String getLocation() {
        return location;
    }

    public void setLocation(String mLocation) {
        this.location = mLocation;
    }*/

    @Exclude
    public Date getStartingDate() {
        return startingTime==0?null:new Date(startingTime);
    }
}
