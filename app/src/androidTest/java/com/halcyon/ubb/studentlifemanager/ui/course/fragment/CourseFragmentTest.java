package com.halcyon.ubb.studentlifemanager.ui.course.fragment;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.ui.TestActivity;
import com.halcyon.ubb.studentlifemanager.ui.reminder.fragment.ReminderFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.annotation.security.RolesAllowed;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by Szilu on 2017. 05. 16..
 */
public class CourseFragmentTest {
    @Rule
    public ActivityTestRule<TestActivity> mActivityTestRule = new ActivityTestRule<TestActivity>(TestActivity.class);
    private TestActivity mActivity = null;
    private RelativeLayout rlContainer = null;
    private CourseFragment courseFragment = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        rlContainer = (RelativeLayout) mActivity.findViewById(R.id.test_container);
        assertNotNull(rlContainer);
        courseFragment = new CourseFragment();
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.test_container, courseFragment, "course").commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
    }

    @Test
    public void testOnClickCourseDetails(){
        onView(withId(R.id.card_id)).perform(click());
        onView(withId(R.id.contact_image)).check(matches(isEnabled()));
    }

    @Test
    public void testLaunchCourseFragment(){
        View view = courseFragment.getView().findViewById(R.id.view_in_fragment);
        assertNotNull(view);
    }


    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

}