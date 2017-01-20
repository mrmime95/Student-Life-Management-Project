package com.halcyon.ubb.studentlifemanager.database.listener;

import java.util.List;

/**
 *
 * Created by Baroti Csaba on 1/16/2017.
 */

public interface ValueEventListListener<T> extends TimeoutListener,CancelattionListener{
    void onChange(List<T> items);
}
