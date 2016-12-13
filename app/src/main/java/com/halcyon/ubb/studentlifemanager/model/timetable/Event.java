package com.halcyon.ubb.studentlifemanager.model.timetable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */

public class Event{
    public String name;
    public String location;
    public String description;

    public Date startingTime;
    public Date endingTime;

    public Event() {
    }

    public Event(String name,String description) {
        this.name =name;
        this.description =description;
    }

    public Event(String name,String description,Date startingTime,Date endingTime) {
        this(name,description);
        this.startingTime =startingTime;
        this.endingTime =endingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //TODO CR: Remove unused methods. [Peter]
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }

    public Date getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Date endingTime) {
        this.endingTime = endingTime;
    }
}
