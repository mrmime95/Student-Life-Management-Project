package com.halcyon.ubb.studentlifemanager.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.halcyon.ubb.studentlifemanager.database.listener.GroupsValueEventListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Course;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.Location;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by Baroti Csaba on 12/12/2016.
 */
public class FirebaseDB implements Database {
    private static FirebaseDB ourInstance = new FirebaseDB();
    private HashMap<CoursesEventValueListener,ValueEventListener> mCoursesEventMap;
    private HashMap<GroupsValueEventListener,ValueEventListener> mGroupsMap;

    private FirebaseDB() {
        mCoursesEventMap =new HashMap<>();
        mGroupsMap=new HashMap<>();
    }

    public static FirebaseDB getInstance() {
        return ourInstance;
    }

    @Override
    public void addCoursesEventValueListener(final List<String> coursesKey, @TimetableDay.Days final int day, final CoursesEventValueListener listener) {
        if (listener==null) return;
        ValueEventListener l=new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Event> events=new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Event event=postSnapshot.getValue(Event.class);
                    if (coursesKey.contains(event.getCourseKey()))
                        events.add(event);
                }
                listener.onEventsChange(events);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError.toException());
            }
        };
        FirebaseDatabase.getInstance().getReference().child("test").child("events").child(String.valueOf(day)).addValueEventListener(l);
        mCoursesEventMap.put(listener,l);
    }

    @Override
    public void removeCoursesEventValueListener(List<String> coursesKey, @TimetableDay.Days int day, CoursesEventValueListener listener) {
        if (listener==null || mCoursesEventMap.get(listener)==null) return;
        FirebaseDatabase.getInstance().getReference().child("test").child("events").child(String.valueOf(day)).removeEventListener(mCoursesEventMap.get(listener));
        mCoursesEventMap.remove(listener);
    }

    @Override
    public void addGroupsValueEventListener(final GroupsValueEventListener listener) {
        if (listener==null) return;
        ValueEventListener l=new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Group> groups=new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    groups.add(postSnapshot.getValue(Group.class));

                }

                listener.onGroupsChange(groups);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError.toException());
            }
        };
        FirebaseDatabase.getInstance().getReference().child("test").child("groups").addValueEventListener(l);
        mGroupsMap.put(listener,l);
    }

    @Override
    public void removeGroupsValueEventListener(GroupsValueEventListener listener) {
        if (listener==null || mGroupsMap.get(listener)==null) return;
        FirebaseDatabase.getInstance().getReference().child("test").child("groups").removeEventListener(mGroupsMap.get(listener));
        mGroupsMap.remove(listener);
    }

    @SuppressWarnings("WrongConstant")
    public void createTestData() {
        DatabaseReference firebaseRef=FirebaseDatabase.getInstance().getReference().child("test");
        DatabaseReference eventRef=firebaseRef.child("events");
        DatabaseReference courseRef=firebaseRef.child("courses");
        DatabaseReference groupRef=firebaseRef.child("groups");
        DatabaseReference locationRef=firebaseRef.child("locations");

        //setting up locations
        locationRef.child("Matemathicum").setValue(new Location("Matemathicum"));
        locationRef.child("Central building").setValue(new Location("Central building"));
        locationRef.child("FSEGA").setValue(new Location("FSEGA"));

        //courses
        courseRef.child("Algebra of analytics").setValue(new Course("Algebra of analytics"));
        courseRef.child("C++ coding standards").setValue(new Course("C++ coding standards"));
        courseRef.child("Linux basics").setValue(new Course("Linux basics"));
        courseRef.child("C++ advanced").setValue(new Course("C++ advanced"));

        //events
        for (int i=0;i<6;++i) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            ArrayList<Event> events=new ArrayList<>();
            cal.set(Calendar.HOUR_OF_DAY,8);
            events.add(new Event("Algebra of analytics",Event.COURSE,i,"Matemathicum",cal.getTime(),cal.getTime()));
            events.add(new Event("C++ coding standards",Event.LAB,i,"Central building",cal.getTime(),cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY,14);
            events.add(new Event("Linux basics",Event.LAB,i,"FSEGA",cal.getTime(),cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY,19);
            events.add(new Event("C++ advanced",Event.LAB,i,"FSEGA",cal.getTime(),cal.getTime()));

            DatabaseReference dayRef=eventRef.child(String.valueOf(i));
            dayRef.removeValue();
            for (Event e:events) {
                dayRef.child(e.getCourseKey()+"_"+e.getDay()+"_"+e.getLocationKey()+"_"+e.getStartingDate().getTime()).setValue(e);
            }
        }

        //gonna be 3 groups 531 532 533
        HashMap<String,Boolean> map;
        Group one=new Group("531",3,null);
        map=new HashMap<>();
        map.put("Algebra of analytics",true);
        map.put("C++ coding standards",true);
        map.put("Linux basics",true);
        map.put("C++ advanced",true);
        one.setCourses(map);
        groupRef.child("531").setValue(one);

        Group two=new Group("532",3,null);
        map=new HashMap<>();
        map.put("Algebra of analytics",true);
        map.put("C++ advanced",true);
        two.setCourses(map);
        groupRef.child("532").setValue(two);

        Group three=new Group("533",3,null);
        map=new HashMap<>();
        map.put("Linux basics",true);
        map.put("C++ advanced",true);
        three.setCourses(map);
        groupRef.child("533").setValue(three);
    }

    public void deleteTestData() {
        FirebaseDatabase.getInstance().getReference().child("test").removeValue();
    }
}
