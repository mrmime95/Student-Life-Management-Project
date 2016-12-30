package com.halcyon.ubb.studentlifemanager.database.remote;

import android.content.Context;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.halcyon.ubb.studentlifemanager.database.listener.CoursesEventValueListener;
import com.halcyon.ubb.studentlifemanager.database.listener.GroupsValueEventListener;
import com.halcyon.ubb.studentlifemanager.model.course.Course;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.Location;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * Created by Baroti Csaba on 12/12/2016.
 */
public class FirebaseDB implements RemoteDatabase {
    private Map<CoursesEventValueListener, Map<DatabaseReference,ValueEventListener>> mCoursesEventMap;
    private Map<GroupsValueEventListener, ValueEventListener> mGroupsMap;

    public FirebaseDB(Context context) {
        mCoursesEventMap = new HashMap<>();
        mGroupsMap = new HashMap<>();
    }

    //TODO: Documentation...
    @Override
    public void addEventValueEventListener(Set<Group> groups, @TimetableDay.Days int day, final CoursesEventValueListener listener) {
        if (listener == null || groups==null || groups.size()==0) return;

        final AtomicInteger atom = new AtomicInteger(groups.size());
        final List<Event> events = new ArrayList<>();

        Map<DatabaseReference,ValueEventListener> listenerMap = mCoursesEventMap.get(listener);
        if (listenerMap == null) {
            listenerMap = new HashMap<>();
            mCoursesEventMap.put(listener, listenerMap);
        }

        DatabaseReference baseRef = FirebaseDatabase.getInstance().getReference().child("test1");

        for (Group group : groups) {
            HashMap<String, Boolean> coursesKeys = group.getCoursesKey();
            DatabaseReference ref;
            ValueEventListener listenerRef;
            if (coursesKeys==null || coursesKeys.size()==group.getCoursesCount()) {
                //get all events to given group
                ref = baseRef.child("group-eventsWcourse").child(group.getKey());
                listenerRef = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        events.clear();
                        for (DataSnapshot post:dataSnapshot.getChildren())
                            events.add(post.getValue(Event.class));
                        if (atom.decrementAndGet()<=0)
                            listener.onEventsListChange(events);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onCancelled(databaseError.toException());
                    }
                };
                ref.orderByChild("day").equalTo(day).addValueEventListener(listenerRef);
                listenerMap.put(ref,listenerRef);
            }
            else{
                final AtomicInteger atomCourse = new AtomicInteger(coursesKeys.size());
                for (String course : coursesKeys.keySet()) {
                    //join events in different courses but one group
                    ref = baseRef.child("test1").child("group-course-eventsWcourse")
                            .child(group.getKey()).child(course);
                    listenerRef = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //TODO TESTING, possible bug: course collision
                            String courseKey = dataSnapshot.getKey();
                            for (int i=events.size()-1;i>=0;--i)
                                if (events.get(i).getCourse().getKey().compareTo(courseKey)==0)
                                    events.remove(i);
                            for (DataSnapshot post:dataSnapshot.getChildren())
                                events.add(post.getValue(Event.class));
                            if (atomCourse.decrementAndGet()<=0 && atom.decrementAndGet()<=0)
                                listener.onEventsListChange(events);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listener.onCancelled(databaseError.toException());
                        }
                    };
                    ref.orderByChild("day").equalTo(day).addValueEventListener(listenerRef);
                    listenerMap.put(ref,listenerRef);
                }
            }
        }

        mCoursesEventMap.put(listener, listenerMap);
    }

    @Override
    public void removeEventValueEventListener(CoursesEventValueListener listener) {
        if (listener == null || mCoursesEventMap.get(listener) == null) return;

        Map<DatabaseReference, ValueEventListener> map = mCoursesEventMap.get(listener);

        for (DatabaseReference ref :map.keySet())
            ref.removeEventListener(map.get(ref));

        mCoursesEventMap.remove(listener);
    }

    private void loadGroups(final GroupsValueEventListener listener, boolean once) {
        if (listener == null) return;
        ValueEventListener l = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<Group> groups = new HashSet<>();
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
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("test1").child("groups");
        if (once)
            ref.addListenerForSingleValueEvent(l);
        else {
            ref.addValueEventListener(l);
            mGroupsMap.put(listener, l);
        }
    }

    @Override
    public void fetchGroups(GroupsValueEventListener listener) {
        loadGroups(listener,true);
    }

    @Override
    public void addGroupsValueEventListener(GroupsValueEventListener listener) {
        loadGroups(listener,false);
    }

    @Override
    public void removeGroupsValueEventListener(GroupsValueEventListener listener) {
        if (listener == null || mGroupsMap.get(listener) == null) return;
        FirebaseDatabase.getInstance().getReference().child("test1").child("groups").removeEventListener(mGroupsMap.get(listener));
        mGroupsMap.remove(listener);
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public void createTestData() {
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference().child("test1");
        DatabaseReference eventRef = firebaseRef.child("events");
        DatabaseReference courseRef = firebaseRef.child("courses");
        DatabaseReference groupRef = firebaseRef.child("groups");
        DatabaseReference groupEventsWcourseRef = firebaseRef.child("group-eventsWcourse");
        DatabaseReference groupCourseEventsWcourseRef = firebaseRef.child("group-course-eventsWcourse");

        //courses
        courseRef.removeValue();

        Map<Course, String> courseMap = new HashMap<>();

        String algebra = courseRef.push().getKey();
        String cstand = courseRef.push().getKey();
        String linux = courseRef.push().getKey();
        String cadvanced = courseRef.push().getKey();

        Course algebraCourse = new Course(algebra, "Algebra of analytics");
        Course linuxCourse = new Course(cadvanced, "Linux basics");
        Course cstandCourse = new Course(linux, "C++ coding standards");
        Course cadvancedCourse = new Course(cadvanced, "C++ advanced");

        courseMap.put(algebraCourse, algebra);
        courseMap.put(linuxCourse, linux);
        courseMap.put(cstandCourse, cstand);
        courseMap.put(cadvancedCourse, cadvanced);

        courseRef.child(algebra).setValue(algebraCourse);
        courseRef.child(cstand).setValue(cstandCourse);
        courseRef.child(linux).setValue(linuxCourse);
        courseRef.child(cadvanced).setValue(cadvancedCourse);

        //gonna be 3 groups 531 532 533
        groupRef.removeValue();

        String oneref = groupRef.push().getKey();
        String tworef = groupRef.push().getKey();
        String threeref = groupRef.push().getKey();

        Group one = new Group(oneref, "531", 3);
        HashMap<String,Boolean> mapCourse=new HashMap<>();
        mapCourse.put(algebraCourse.getKey(),true);
        mapCourse.put(cadvancedCourse.getKey(),true);
        mapCourse.put(cstandCourse.getKey(),true);
        mapCourse.put(linuxCourse.getKey(),true);
        one.setCoursesKey(mapCourse);
        one.setCoursesCount(mapCourse.size());

        Group two = new Group(tworef, "532", 3);
        mapCourse=new HashMap<>();
        mapCourse.put(cadvancedCourse.getKey(),true);
        mapCourse.put(cstandCourse.getKey(),true);
        two.setCoursesKey(mapCourse);
        two.setCoursesCount(mapCourse.size());

        Group three = new Group(threeref, "533", 3);
        mapCourse=new HashMap<>();
        mapCourse.put(algebraCourse.getKey(),true);
        mapCourse.put(linuxCourse.getKey(),true);
        three.setCoursesKey(mapCourse);
        three.setCoursesCount(mapCourse.size());


        groupRef.child(oneref).setValue(one);
        groupRef.child(tworef).setValue(two);
        groupRef.child(threeref).setValue(three);

        //events
        eventRef.removeValue();

        Map<Integer, Map<Event, String>> map = new HashMap<>();

        for (int i = 0; i < 6; ++i) {
            DatabaseReference dayRef = eventRef.child(String.valueOf(i));
            dayRef.removeValue();

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            ArrayList<Event> events = new ArrayList<>();
            cal.set(Calendar.HOUR_OF_DAY, 8);
            events.add(new Event(null, algebraCourse, Event.COURSE, 0, new Location("Matemathicum"), cal.getTime(), cal.getTime()));
            events.add(new Event(null, cstandCourse, Event.LAB, 0, new Location("Central building"), cal.getTime(), cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY, 14);
            events.add(new Event(null, linuxCourse, Event.LAB, 0, new Location("FSEGA"), cal.getTime(), cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY, 19);
            events.add(new Event(null, cadvancedCourse, Event.LAB, 0, new Location("FSEGA"), cal.getTime(), cal.getTime()));

            Map<Event, String> innerMap = new HashMap<>();
            for (Event e : events) {
                e.setDay(i);
                String ref = dayRef.push().getKey();
                innerMap.put(e, ref);
                Course c = e.getCourse();
                e.setCourse(null);
                e.setKey(ref);
                dayRef.child(ref).setValue(e);
                e.setCourse(c);
                e.setKey(null);
            }
            map.put(i, innerMap);
        }

        //group-eventsWcourse && group-course-eventsWcourse
        groupEventsWcourseRef.removeValue();
        groupCourseEventsWcourseRef.removeValue();

        DatabaseReference oneGroup = groupEventsWcourseRef
                .child(oneref);
        DatabaseReference twoGroup = groupEventsWcourseRef
                .child(tworef);
        DatabaseReference threeGroup = groupEventsWcourseRef
                .child(threeref);

        DatabaseReference oneGroupC = groupCourseEventsWcourseRef
                .child(oneref);
        DatabaseReference twoGroupC = groupCourseEventsWcourseRef
                .child(tworef);
        DatabaseReference threeGroupC = groupCourseEventsWcourseRef
                .child(threeref);

        for (Map<Event, String> m : map.values()) {
            for (Event e : m.keySet()) {
                oneGroup.child(m.get(e)).setValue(e);
                oneGroupC.child(courseMap.get(e.getCourse())).child(m.get(e)).setValue(e);
                if (e.getCourse() == cstandCourse || e.getCourse() == cadvancedCourse) {
                    twoGroup.child(m.get(e)).setValue(e);
                    twoGroupC.child(courseMap.get(e.getCourse())).child(m.get(e)).setValue(e);
                }
                if (e.getCourse() == algebraCourse || e.getCourse() == linuxCourse) {
                    threeGroup.child(m.get(e)).setValue(e);
                    threeGroupC.child(courseMap.get(e.getCourse())).child(m.get(e)).setValue(e);
                }
            }
        }

        //

    }

    @Override
    public void deleteTestData() {
        FirebaseDatabase.getInstance().getReference().child("test1").removeValue();
    }

    @Override
    public void validateKeysOnGroups(final Set<Group> groups, final GroupsValueEventListener validationListener) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("test1")
                .child("groups");
        final Set<Group> validatedGroups=new HashSet<>();
        final AtomicInteger atom=new AtomicInteger(groups.size());
        for (final Group group:groups) {
            ref.child(group.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null)
                        validatedGroups.add(dataSnapshot.getValue(Group.class));
                    if (atom.decrementAndGet()==0) {
                        validationListener.onGroupsChange(validatedGroups);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
