package com.halcyon.ubb.studentlifemanager.viewmodel;

import com.halcyon.ubb.studentlifemanager.model.timetable.Event;

//TODO CR: Why do you need an empty interface? There is no javadoc comment and the name of the interface isn't helping either. [Peter]
/**
 *  This interface connects UI elements so they can be differentiated in recyclerview adapter
 *  => Timetable has dots and lines and an endpoint.
 *
 *  Created by Baroti Csaba on 12/11/2016.
 */
public interface ICEventViewModel {
    void setEvent(Event event);
}
