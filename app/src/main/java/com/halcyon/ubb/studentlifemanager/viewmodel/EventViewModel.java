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
    public ObservableField<String> title;
    public ObservableField<String> description;

    public EventViewModel(Event event) {
        startingTime=new ObservableField<>();
        title=new ObservableField<>();
        description=new ObservableField<>();
        setEvent(event);
    }

    public void setEvent(Event event) {
        if (event==null) return;

        if (event.getStartingDate()!=null)
            startingTime.set(DateFormat.format("HH:mm",event.getStartingDate()).toString());
        title.set(event.getName());
        description.set(event.getDescription());
    }
}
