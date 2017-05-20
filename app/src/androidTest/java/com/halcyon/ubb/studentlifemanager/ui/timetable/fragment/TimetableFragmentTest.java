package com.halcyon.ubb.studentlifemanager.ui.timetable.fragment;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.ui.TestActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Created by Szilu on 2017. 05. 17..
 */
public class TimetableFragmentTest {
    @Rule
    public ActivityTestRule<TestActivity> mActivityTestRule = new ActivityTestRule<TestActivity>(TestActivity.class);
    private TestActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }


    @Test
    public void testLaunchTimetableFragment(){
        RelativeLayout rlContainer = (RelativeLayout) mActivity.findViewById(R.id.test_container);
        assertNotNull(rlContainer);
        TimetableFragment timetableDayFragment = new TimetableFragment();
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.test_container, timetableDayFragment, "timetable").commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        View view = timetableDayFragment.getView().findViewById(R.id.view_in_fragment_timetable);
        assertNotNull(view);
    }


    @After
    public void tearDown() throws Exception {
        mActivity = null;

        mActivity = null;
    }

}