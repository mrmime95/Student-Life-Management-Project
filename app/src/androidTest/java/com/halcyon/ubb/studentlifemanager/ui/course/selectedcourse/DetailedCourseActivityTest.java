package com.halcyon.ubb.studentlifemanager.ui.course.selectedcourse;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Szilu on 2017. 05. 16..
 */
public class DetailedCourseActivityTest {

    @Test
    public void isStoragePermissionGranted() throws Exception {
        boolean oke = true;
        DetailedCourseActivity activity = new DetailedCourseActivity();
        boolean temp = activity.isStoragePermissionGranted();
        assertEquals(oke, temp);
    }

}