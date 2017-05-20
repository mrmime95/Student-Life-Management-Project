package com.halcyon.ubb.studentlifemanager.ui;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.halcyon.ubb.studentlifemanager.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Szilu on 2017. 05. 16..
 */
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = mActivityRule.getActivity();
    }

    @Test
    public void testLaunchMainFrameLayout(){
        View view = mainActivity.findViewById(R.id.main_frame);
        assertNotNull(view);
    }
    @Test
    public void testLaunchNavigation(){
        View view = mainActivity.findViewById(R.id.main_bottom_navigation);
        assertNotNull(view);
    }
    @After
    public void tearDown() throws Exception {

    }

}