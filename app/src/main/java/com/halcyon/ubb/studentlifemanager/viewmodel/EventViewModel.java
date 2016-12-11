package com.halcyon.ubb.studentlifemanager.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.text.format.DateFormat;

import com.halcyon.ubb.studentlifemanager.model.timetable.Event;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */

public class EventViewModel implements ICEventViewModel {
    public ObservableField<String> title;
    public ObservableField<String> desc;
    private Event mEvent;

    public EventViewModel() {
        title=new ObservableField<>();
        desc=new ObservableField<>();
    }

    public EventViewModel(Event event) {
        this();
        setEvent(event);
    }

    public void setEvent(Event event) {
        mEvent=event;
        title.set(mEvent.getName());
        desc.set(mEvent.getDescription());
    }

}
