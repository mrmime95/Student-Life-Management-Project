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

@SuppressWarnings("WeakerAccess")
public class Event {
    @Retention(SOURCE)
    @IntDef({COURSE,LAB})
    public @interface EventType {}
    public static final int COURSE=0;
    public static final int LAB=1;

    private String mCourseKey;
    private String mLocation;
    private String mDescription;
    private @EventType int mType;
    private int mDay;


    //public String location;

    //firebase doesn't like date
    public long startingTime;
    public long endingTime;

    //Firebase needs empty constructor
    public Event() {
    }

    public Event(Date startingTime) {
        this.startingTime=startingTime.getTime();
    }

    public Event(String courseKey,@EventType int type, String location) {
        this.mCourseKey =courseKey;
        this.mLocation=location;
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
        return mLocation;
    }

    public void setLocationKey(String locationKey) {
        mLocation=locationKey;
    }

    public @EventType int getType() {
        return mType;
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
    public String getTypeString() {
        switch (mType) {
            case COURSE:
                return "Course";
            case LAB:
                return "Laboratory";
        }
        return "";
    }
}
