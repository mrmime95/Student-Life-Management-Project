package com.halcyon.ubb.studentlifemanager.model.timetable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */

public class Timetable implements Parcelable {
    private String mName;
    private HashMap<Group, List<Course>> mTimetable;

    public Timetable(String name) {
        mTimetable=new HashMap<>();
        mName=name;
    }

    Timetable(Parcel in) {
        this(in.readString());
        int size = in.readInt();
        for (int i=0;i<size;i++) {
            Group group=in.readParcelable(Group.class.getClassLoader());
            List<Course> courses=new ArrayList<>();
            in.readList(courses,Course.class.getClassLoader());
            mTimetable.put(group,courses);
        }
    }

    public void addGroup(Group group) {
        mTimetable.put(group,null);
    }

    public String getName() {
        return mName;
    }

    public void addGroupWithCourses(Group group,List<Course> courses) {
        List<Course> list = mTimetable.get(group);
        if (list!=null)
            list.addAll(courses);
        else
            list=courses;
        mTimetable.put(group,list);
    }

    public List<Course> getCourses(Group group) {
        return mTimetable.get(group);
    }

    public Set<Group> getGroups() {
        return mTimetable.keySet();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mName);
        parcel.writeInt(mTimetable.size());
        for (Group group :mTimetable.keySet()) {
            parcel.writeParcelable(group,flags);
            parcel.writeList(mTimetable.get(group));
        }
    }

    public static final Parcelable.Creator<Timetable> CREATOR
            = new Parcelable.Creator<Timetable>() {
        public Timetable createFromParcel(Parcel in) {
            return new Timetable(in);
        }

        public Timetable[] newArray(int size) {
            return new Timetable[size];
        }
    };
}
