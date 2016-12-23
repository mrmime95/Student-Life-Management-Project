package com.halcyon.ubb.studentlifemanager.viewmodel;

import android.databinding.ObservableField;
import android.text.format.DateFormat;

import com.halcyon.ubb.studentlifemanager.model.timetable.Event;

/**
 *
 * Created by Baroti Csaba on 12/14/2016.
 */

public class EventViewModel {
    public ObservableField<String> startingTime;
    public ObservableField<String> location;
    public ObservableField<String> description;
    public ObservableField<String> course;
    public ObservableField<String> type;
    public ObservableField<String> shortTitle;

    public EventViewModel(Event event) {
        startingTime=new ObservableField<>();
        course=new ObservableField<>();
        location=new ObservableField<>();
        description=new ObservableField<>();
        type=new ObservableField<>();
        shortTitle=new ObservableField<>();
        setEvent(event);
    }

    public void setEvent(Event event) {
        if (event==null) return;

        if (event.getStartingDate()!=null)
            startingTime.set(DateFormat.format("HH:mm",event.getStartingDate()).toString());
        course.set(event.getCourseKey());
        description.set(event.getDescription());
        location.set(event.getLocationKey());
        type.set(event.getTypeString());
        shortTitle.set(type.get() + " • " + location.get());
    }
}
