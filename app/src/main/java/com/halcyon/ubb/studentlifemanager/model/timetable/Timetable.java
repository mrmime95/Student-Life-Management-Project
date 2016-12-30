package com.halcyon.ubb.studentlifemanager.model.timetable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */

public class Timetable implements Parcelable {
    private String mName;
    private String mID;
    private Set<Group> mGroups;

    public Timetable(String name) {
        mGroups =new HashSet<>();
        mName=name;
    }


    private Timetable(Parcel in) {
        this(in.readString());
        mID=in.readString();
        int size = in.readInt();
        //noinspection unchecked
        mGroups=new HashSet<>(in.readArrayList(Group.class.getClassLoader()));
    }

    public void setID(String id) {
        mID=id;
    }

    public String getID() {
        return mID;
    }

    public void addGroup(Group group) {
        mGroups.add(group);
    }

    public String getName() {
        return mName;
    }

    public Set<Group> getGroups() {
        return mGroups;
    }

    public void setGroups(Set<Group> groups) {
        mGroups=groups;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mName);
        parcel.writeString(mID);
        parcel.writeInt(mGroups.size());
        parcel.writeList(new ArrayList<>(mGroups));
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
