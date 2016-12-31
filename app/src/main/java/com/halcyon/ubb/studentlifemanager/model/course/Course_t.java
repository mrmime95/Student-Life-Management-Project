package com.halcyon.ubb.studentlifemanager.model.course;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Baroti Csaba on 12/23/2016.
 */

@SuppressWarnings("unused")
public class Course_t implements Parcelable {
    private String mName;
    private String mDescription;
    private String mKey;

    public Course_t() {}

    public Course_t(String key, String name) {
        mName = name;
        mKey= key;
    }

    public Course_t(String key, String name, String description) {
        this(key,name);
        this.mDescription = description;
    }

    public Course_t(Parcel in) {
        mName=in.readString();
        mKey=in.readString();
        mDescription=in.readString();
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

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey=key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mKey);
        parcel.writeString(mDescription);
    }

    public static final Parcelable.Creator<Course_t> CREATOR
            = new Parcelable.Creator<Course_t>() {
        public Course_t createFromParcel(Parcel in) {
            return new Course_t(in);
        }

        public Course_t[] newArray(int size) {
            return new Course_t[size];
        }
    };
}
