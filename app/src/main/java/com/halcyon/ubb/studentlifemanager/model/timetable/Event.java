package com.halcyon.ubb.studentlifemanager.model.timetable;

import android.support.annotation.IntDef;

import com.google.firebase.database.Exclude;
import com.halcyon.ubb.studentlifemanager.model.course.Course_t;

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

    private String mDescription;
    private @EventType int mType;
    private int mDay;
    private Location mLocation;
    private Course_t mCourse;
    private String mKey;


    //public String location;

    //firebase doesn't like date
    public long startingTime;
    public long endingTime;

    //Firebase needs empty constructor
    public Event() {
        mCourse=new Course_t();
    }

    public Event(Course_t course, Date startingTime) {
        this.startingTime=startingTime.getTime();
        mCourse=course;
    }

    public Event(String key, Course_t course, @EventType int type, Location location) {
        mKey=key;
        mCourse=course;
        mLocation=location;
        mType=type;
    }

    public Event(String key, Course_t course, @EventType int type, @Timetable.Days int day, Location location, Date startingTime, Date endingTime) {
        this(key,course,type,location);
        this.mDay=day;
        this.startingTime =startingTime==null?0:startingTime.getTime();
        this.endingTime =endingTime==null?0:endingTime.getTime();
    }

    public void setDay(@Timetable.Days int day) {
        mDay=day;
    }

    public @Timetable.Days int getDay() {
        return mDay;
    }

    public String getKey() {
        return mKey;
    }

    public void setCourse(Course_t course) {
        mCourse = course;
    }

    public Course_t getCourse() {
        return mCourse;
    }

    public void setKey(String key) {
        mKey=key;
    }

    public void setCourseKey(Course_t course) {
        mCourse=course;
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

    public Location getLocation() {
        return mLocation;
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
        Event newEvent = new Event(mKey,mCourse, mType, mDay, mLocation, getStartingDate(), getEndingDate());
        newEvent.setDescription(mDescription);
        return newEvent;
    }
}
