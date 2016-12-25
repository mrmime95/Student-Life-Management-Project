package com.halcyon.ubb.studentlifemanager.model.timetable;

import android.support.annotation.IntDef;

import com.google.firebase.database.Exclude;

import java.lang.annotation.Retention;
import java.util.Date;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 *
 * Created by Baroti Csaba on 12/11/2016.
 */

@SuppressWarnings({"WeakerAccess","unused"})
public class Event {
    @Retention(SOURCE)
    @IntDef({COURSE,LAB})
    public @interface EventType {}
    public static final int COURSE=0;
    public static final int LAB=1;

    private String mCourseKey;
    private String mLocationKey;
    private String mDescription;
    private @EventType int mType;
    private int mDay;
    private Location mLocation;
    private Course mCourse;


    //public String location;

    //firebase doesn't like date
    public long startingTime;
    public long endingTime;

    //Firebase needs empty constructor
    public Event() {
    }

    public Event(String courseKey,Date startingTime) {
        this.startingTime=startingTime.getTime();
        mCourseKey=courseKey;
    }

    public Event(String courseKey,@EventType int type, String location) {
        this.mCourseKey =courseKey;
        this.mLocationKey =location;
        mType=type;
    }

    public Event(String courseKey, @EventType int type, @TimetableDay.Days int day, String locationKey, Date startingTime, Date endingTime) {
        this(courseKey,type,locationKey);
        this.mDay=day;
        this.startingTime =startingTime==null?0:startingTime.getTime();
        this.endingTime =endingTime==null?0:endingTime.getTime();
    }

    public void setDay(@TimetableDay.Days int day) {
        mDay=day;
    }

    public @TimetableDay.Days int getDay() {
        return mDay;
    }

    public String getCourseKey() {
        return mCourseKey;
    }

    public void setCourseKey(String key) {
        mCourseKey=key;
    }

    public String getLocationKey() {
        return mLocationKey;
    }

    public void setLocationKey(String locationKey) {
        mLocationKey =locationKey;
    }

    public @EventType int getType() {
        return mType;
    }

    public void setType(@EventType int type) {
        mType=type;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription=description;
    }

    @Exclude
    public Date getStartingDate() {
        return startingTime==0?null:new Date(startingTime);
    }

    @Exclude
    public Date getEndingDate() {
        return endingTime==0?null:new Date(endingTime);
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    @Exclude
    public Location getLocation() {
        return mLocation;
    }

    public void setCourse(Course course) {
        mCourse = course;
    }

    @Exclude
    public Course getCourse() {
        return mCourse;
    }

    @Exclude
    public String getTypeString() {
        switch (mType) {
            case COURSE:
                return "Course";
            case LAB:
                return "Laboratory";
        }
        return null;
    }

    @Exclude
    public Event copy() {
        Event newEvent = new Event(mCourseKey, mType, mDay, mLocationKey, getStartingDate(), getEndingDate());
        newEvent.setDescription(mDescription);
        newEvent.setLocation(mLocation);
        newEvent.setCourse(mCourse);
        return newEvent;
    }
}
