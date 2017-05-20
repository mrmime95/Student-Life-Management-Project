package com.halcyon.ubb.studentlifemanager.ui.reminder.notification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Szilu on 2017. 05. 16..
 */
public class AppendNotificationsTest {
    private AppendNotifications notifications;
    @Before
    public void setUp() throws Exception {
        notifications = new AppendNotifications();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void append() throws Exception {
        String str1 = "alma", str2 = "korte", str3 = "almakorte";
        String returnValue = notifications.append(str1, str2);
        assertEquals(returnValue, str3);
    }

}