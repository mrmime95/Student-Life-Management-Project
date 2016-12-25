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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * Created by Baroti Csaba on 12/12/2016.
 */
public class FirebaseDB implements Database {
    private static FirebaseDB ourInstance = new FirebaseDB();
    private Map<CoursesEventValueListener, List<ValueEventListener>> mCoursesEventMap;
    private Map<GroupsValueEventListener, ValueEventListener> mGroupsMap;

    private FirebaseDB() {
        mCoursesEventMap = new HashMap<>();
        mGroupsMap = new HashMap<>();
    }

    public static FirebaseDB getInstance() {
        return ourInstance;
    }


    //TODO: Documentation...
    @Override
    public void addCoursesEventValueListener(final List<String> coursesKey, @TimetableDay.Days final int day, final CoursesEventValueListener listener) {
        if (listener == null) return;

        final AtomicInteger atom = new AtomicInteger();
        atom.set(coursesKey.size());
        final Map<String, List<Event>> events = new HashMap<>();

        final AtomicInteger atomInner = new AtomicInteger(coursesKey.size());

        List<ValueEventListener> listenerList = mCoursesEventMap.get(listener);
        if (listenerList == null) listenerList = new ArrayList<>();
        for (final String courseKey : coursesKey) {
            ValueEventListener l = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Event> list = new ArrayList<>();

                    synchronized (atomInner) {
                        atomInner.set((int) (atomInner.decrementAndGet()+dataSnapshot.getChildrenCount()*2));
                    }


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        final Event e=postSnapshot.getValue(Event.class);
                        list.add(e);

                        //join wiht location
                        FirebaseDatabase.getInstance().getReference().child("test").child("locations")
                                .orderByKey().equalTo(e.getLocationKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                e.setLocation(dataSnapshot.child(e.getLocationKey()).getValue(Location.class));
                                int loading;
                                if ((loading=atomInner.decrementAndGet())<=0)
                                listener.onEventsListChange(events, loading == 0);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                e.setLocation(null);
                                int loading;
                                if ((loading=atomInner.decrementAndGet())<=0)
                                listener.onEventsListChange(events, loading == 0);
                            }
                        });

                        //join with courses
                        FirebaseDatabase.getInstance().getReference().child("test").child("courses")
                                .orderByKey().equalTo(e.getCourseKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                e.setCourse(dataSnapshot.child(e.getCourseKey()).getValue(Course.class));
                                //boolean loading=atomInner.decrementAndGet()>0;
                                int loading;
                                if ((loading=atomInner.decrementAndGet())<=0)
                                    listener.onEventsListChange(events, loading == 0);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                e.setCourse(null);
                                int loading;
                                if ((loading=atomInner.decrementAndGet())<=0)
                                   listener.onEventsListChange(events,loading==0);
                            }
                        });
                    }
                    synchronized (events) {
                        events.put(courseKey, list);
                    }

                    if (atom.decrementAndGet() <= 0)
                        listener.onEventsListChange(events,true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onCancelled(databaseError.toException());
                }
            };

            FirebaseDatabase.getInstance().getReference().child("test").child("events").child(String.valueOf(day))
                    .orderByChild("courseKey").equalTo(courseKey).addValueEventListener(l);
            listenerList.add(l);
        }
        mCoursesEventMap.put(listener, listenerList);
    }

    @Override
    public void removeCoursesEventValueListener(List<String> coursesKey, @TimetableDay.Days int day, CoursesEventValueListener listener) {
        if (listener == null || mCoursesEventMap.get(listener) == null) return;

        for (ValueEventListener l : mCoursesEventMap.get(listener))
            FirebaseDatabase.getInstance().getReference().child("test").child("events").child(String.valueOf(day)).removeEventListener(l);
        mCoursesEventMap.remove(listener);
    }

    @Override
    public void addGroupsValueEventListener(final GroupsValueEventListener listener) {
        if (listener == null) return;
        ValueEventListener l = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Group> groups = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
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
        mGroupsMap.put(listener, l);
    }

    @Override
    public void removeGroupsValueEventListener(GroupsValueEventListener listener) {
        if (listener == null || mGroupsMap.get(listener) == null) return;
        FirebaseDatabase.getInstance().getReference().child("test").child("groups").removeEventListener(mGroupsMap.get(listener));
        mGroupsMap.remove(listener);
    }

    @SuppressWarnings("WrongConstant")
    public void createTestData() {
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference().child("test");
        DatabaseReference eventRef = firebaseRef.child("events");
        DatabaseReference courseRef = firebaseRef.child("courses");
        DatabaseReference groupRef = firebaseRef.child("groups");
        DatabaseReference locationRef = firebaseRef.child("locations");

        //setting up locations
        locationRef.removeValue();
        String mateKey=locationRef.push().getKey();
        locationRef.child(mateKey).setValue(new Location("Matemathicum"));
        String cbKey=locationRef.push().getKey();
        locationRef.child(cbKey).setValue(new Location("Central building"));
        String fsegaKey=locationRef.push().getKey();
        locationRef.child(fsegaKey).setValue(new Location("FSEGA"));

        //courses
        courseRef.removeValue();
        String algebra=courseRef.push().getKey();
        courseRef.child(algebra).setValue(new Course("Algebra of analytics"));
        String cstand=courseRef.push().getKey();
        courseRef.child(cstand).setValue(new Course("C++ coding standards"));
        String linux=courseRef.push().getKey();
        courseRef.child(linux).setValue(new Course("Linux basics"));
        String cadvanced=courseRef.push().getKey();
        courseRef.child(cadvanced).setValue(new Course("C++ advanced"));

        //events
        eventRef.removeValue();
        for (int i = 0; i < 6; ++i) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            ArrayList<Event> events = new ArrayList<>();
            cal.set(Calendar.HOUR_OF_DAY, 8);
            events.add(new Event(algebra, Event.COURSE, i, mateKey, cal.getTime(), cal.getTime()));
            events.add(new Event(cstand, Event.LAB, i, cbKey, cal.getTime(), cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY, 14);
            events.add(new Event(linux, Event.LAB, i, fsegaKey, cal.getTime(), cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY, 19);
            events.add(new Event(cadvanced, Event.LAB, i, fsegaKey, cal.getTime(), cal.getTime()));

            DatabaseReference dayRef = eventRef.child(String.valueOf(i));
            dayRef.removeValue();
            for (Event e : events) {
                dayRef.push().setValue(e);
            }
        }

        //gonna be 3 groups 531 532 533
        groupRef.removeValue();
        HashMap<String, Boolean> map;
        Group one = new Group("531", 3, null);
        map = new HashMap<>();
        map.put(algebra, true);
        map.put(cstand, true);
        map.put(linux, true);
        map.put(cadvanced, true);
        one.setCourses(map);
        groupRef.push().setValue(one);

        Group two = new Group("532", 3, null);
        map = new HashMap<>();
        map.put(algebra, true);
        map.put(cadvanced, true);
        two.setCourses(map);
        groupRef.push().setValue(two);

        Group three = new Group("533", 3, null);
        map = new HashMap<>();
        map.put(linux, true);
        map.put(cadvanced, true);
        three.setCourses(map);
        groupRef.push().setValue(three);
    }

    public void deleteTestData() {
        FirebaseDatabase.getInstance().getReference().child("test").removeValue();
    }
}
