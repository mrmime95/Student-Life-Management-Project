package com.halcyon.ubb.studentlifemanager.database;

/**
 *
 * Created by Baroti Csaba on 1/15/2017.
 */

public interface ConnectionListener {
    void onConnected();
    void onDisconnected();
}
