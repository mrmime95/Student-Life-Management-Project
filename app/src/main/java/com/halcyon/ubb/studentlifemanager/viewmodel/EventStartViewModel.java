package com.halcyon.ubb.studentlifemanager.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.icu.text.SimpleDateFormat;
import android.text.format.DateFormat;

import com.halcyon.ubb.studentlifemanager.model.timetable.Event;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */

public class EventStartViewModel implements ICEventViewModel{
    public ObservableField<String> startingTime;
    private Event mEvent;

    public EventStartViewModel() {
        startingTime=new ObservableField<>();
    }

    public EventStartViewModel(Event event) {
        this();
        setEvent(event);
    }

    private String formatStartingTime() {
        return DateFormat.format("HH:mm",mEvent.getStartingTime()).toString();
    }

    public void setEvent(Event event) {
        mEvent=event;
        startingTime.set(formatStartingTime());
    }
}
