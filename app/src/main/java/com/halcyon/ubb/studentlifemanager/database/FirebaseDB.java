package com.halcyon.ubb.studentlifemanager.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.viewmodel.TimetableDayValueListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Baroti Csaba on 12/12/2016.
 */
public class FirebaseDB implements Database {
    private static FirebaseDB ourInstance = new FirebaseDB();
    private HashMap<TimetableDayValueListener,ValueEventListener> mMap;

    private FirebaseDB() {
        mMap=new HashMap<>();
    }

    public static FirebaseDB getInstance() {
        return ourInstance;
    }

    public void addTimetableDayValueListener(String course, @TimetableDay.Days final int day, final TimetableDayValueListener listener) {
        if (listener==null) return;
        ValueEventListener l=new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key=dataSnapshot.getKey();
                if (key.compareTo(String.valueOf(day))==0)
                    listener.onDayChange(dataSnapshot.getValue(TimetableDay.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    listener.onCancelled(databaseError.toException());
            }
        };
        FirebaseDatabase.getInstance().getReference().child("courses").child(course).child("timetable")
                .child("days").child(String.valueOf(day)).addValueEventListener(l);
        mMap.put(listener,l);
    }

    public void removeTimetableDayValueListener(String course,@TimetableDay.Days final int day,TimetableDayValueListener listener) {
        if (listener==null || mMap.get(listener)==null) return;
            FirebaseDatabase.getInstance().getReference().child("courses").child(course).child("timetable")
                    .child("days").child(String.valueOf(day)).removeEventListener(mMap.get(listener));
        mMap.remove(listener);
    }

    public void createTestData() {
        DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference().child("courses").child("testCourse");
        DatabaseReference daysRef=courseRef.child("timetable").child("days");


        for (int i=0;i<6;++i) {
            ArrayList<Event> events=new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY,8);
            events.add(new Event("Course • Matemathicum","Algebra of analytics",cal.getTime(),cal.getTime()));
            events.add(new Event("Laboratory • Central","C++ coding standards",cal.getTime(),cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY,14);
            events.add(new Event("Laboratory • FSEGA","Linux basics",cal.getTime(),cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY,19);
            events.add(new Event("Laboratory • FSEGA","C++ advanced",cal.getTime(),cal.getTime()));
            daysRef.child(String.valueOf(i)).setValue(new TimetableDay(events));
        }
    }

    public void deleteTestData() {
        FirebaseDatabase.getInstance().getReference().child("courses").child("testCourse").removeValue();
    }
}
