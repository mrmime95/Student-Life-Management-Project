package com.halcyon.ubb.studentlifemanager.viewmodel;

import com.halcyon.ubb.studentlifemanager.model.timetable.Event;

/**
 * Created by Baroti Csaba on 12/11/2016.
 */
//TODO CR: Why do you need an empty interface? There is no javadoc comment and the name of the interface isn't helping either. [Peter]
public interface ICEventViewModel {
    void setEvent(Event event);
}
