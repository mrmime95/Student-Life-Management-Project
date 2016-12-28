package com.halcyon.ubb.studentlifemanager.model.timetable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Baroti Csaba on 12/23/2016.
 */

public class Group {
    private String mName;
    private int mYear;
    private HashMap<String,Boolean> mCourses;

    public Group() { }

    public Group(String name,int year,HashMap<String,Boolean> courses) {
        this.mCourses =courses;
        this.mName =name;
        this.mYear =year;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }

    public HashMap<String, Boolean> getCourses() {
        return mCourses;
    }

    public void setCourses(HashMap<String, Boolean> courses) {
        this.mCourses = courses;
    }

    @Exclude
    public List<String> getCoursesNames() {
        List<String> names=new ArrayList<>();
        for (String name:mCourses.keySet())
            names.add(name);
        return names;
    }
}
