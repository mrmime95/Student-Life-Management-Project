package com.halcyon.ubb.studentlifemanager.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.text.format.DateFormat;

import com.halcyon.ubb.studentlifemanager.database.FirebaseDB;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */

public class EventViewModel implements ICEventViewModel {
    public ObservableField<String> title;
    public ObservableField<String> desc;

    public EventViewModel() {
        title=new ObservableField<>();
        desc=new ObservableField<>();
    }

    public EventViewModel(Event event) {
        this();
        setEvent(event);
    }

    @Override
    public void setEvent(Event event) {
        title.set(event.getName());
        desc.set(event.getDescription());
    }

}
