package com.halcyon.ubb.studentlifemanager.viewmodel;

//TODO CR: Remove unused imports. [Peter]
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.icu.text.SimpleDateFormat;
import android.text.format.DateFormat;

import com.halcyon.ubb.studentlifemanager.model.timetable.Event;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */

public class EventStartViewModel implements ICEventViewModel{
    //TODO CR: The MVVM pattern is a bit more complex than naming three packages respectively. I strongly suggest you master MVC first.
    // Using mixed architecture (what you're doing right now) is probably the least maintainable thing you can do. [Peter]
    public ObservableField<String> startingTime;

    public EventStartViewModel() {
        startingTime=new ObservableField<>();
    }

    public EventStartViewModel(Event event) {
        this();
        setEvent(event);
    }

    @Override
    public void setEvent(Event event) {
        startingTime.set(DateFormat.format("HH:mm",event.getStartingTime()).toString());
    }
}
