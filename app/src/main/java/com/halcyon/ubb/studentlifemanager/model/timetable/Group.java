package com.halcyon.ubb.studentlifemanager.model.timetable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 *
 * Created by Baroti Csaba on 12/23/2016.
 */

@SuppressWarnings("unused")
public class Group implements Parcelable {
    private String mName;
    private int mYear;
    private String mKey;
    private HashMap<String,Boolean> mCoursesKey;
    private int mCoursesCount;

    public Group() { }

    public Group(String key,String name,int year) {
        mName =name;
        mYear =year;
        mKey  =key;
    }

    public Group(Parcel in) {
        mName=in.readString();
        mYear=in.readInt();
        mCoursesCount=in.readInt();
        mKey=in.readString();
        //noinspection unchecked
        mCoursesKey= (HashMap<String, Boolean>) in.readSerializable();
    }

    public int getCoursesCount() {
        return mCoursesKey==null?0:mCoursesKey.size();
    }

    public void setCoursesCount(int coursesCount) {
        mCoursesCount=coursesCount;
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

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey=key;
    }

    public HashMap<String,Boolean> getCoursesKey() {
        return mCoursesKey;
    }

    public void setCoursesKey(HashMap<String, Boolean> coursesKey) {
        mCoursesKey=coursesKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeInt(mYear);
        parcel.writeInt(mCoursesCount);
        parcel.writeString(mKey);
        parcel.writeSerializable(mCoursesKey);
    }

    public static final Parcelable.Creator<Group> CREATOR
            = new Parcelable.Creator<Group>() {
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            if (obj==null)
                return false;
            Group group= (Group) obj;
            return group.getName().compareTo(getName())==0
                    && group.getKey().compareTo(getKey())==0
                    && group.getCoursesCount() == getCoursesCount()
                    && group.getYear()==getYear();
        }
        return false;
    }

}
