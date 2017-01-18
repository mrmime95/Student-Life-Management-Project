package com.halcyon.ubb.studentlifemanager.database.listener;

import java.util.Set;

/**
 *
 * Created by Baroti Csaba on 12/23/2016.
 */

public interface ValueEventSetListener<T> extends TimeoutListener,CancelattionListener{
    void onChange(Set<T> items);
}
