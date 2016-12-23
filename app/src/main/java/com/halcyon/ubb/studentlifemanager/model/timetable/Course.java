package com.halcyon.ubb.studentlifemanager.model.timetable;

/**
 * Created by Baroti Csaba on 12/23/2016.
 */

public class Course {
    private String mName;
    private String mDescription;

    public Course() {}

    public Course(String name) {
        this.mName = name;
    }

    public Course(String name, String description) {
        this(name);
        this.mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
