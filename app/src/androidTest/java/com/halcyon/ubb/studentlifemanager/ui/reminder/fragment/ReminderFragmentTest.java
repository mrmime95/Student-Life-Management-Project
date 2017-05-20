package com.halcyon.ubb.studentlifemanager.ui.reminder.fragment;

import android.databinding.adapters.CalendarViewBindingAdapter;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.ui.TestActivity;
import com.halcyon.ubb.studentlifemanager.ui.course.fragment.CourseFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import android.widget.DatePicker;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * Created by Szilu on 2017. 05. 16..
 */
public class ReminderFragmentTest {
    @Rule
    public ActivityTestRule<TestActivity> mActivityTestRule = new ActivityTestRule<TestActivity>(TestActivity.class);
    private TestActivity mActivity = null;
    private RelativeLayout rlContainer;
    private ReminderFragment reminderFragment = null;
    private View view = null;
    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        rlContainer = (RelativeLayout) mActivity.findViewById(R.id.test_container);
        assertNotNull(rlContainer);
        reminderFragment = new ReminderFragment();
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(rlContainer.getId(), reminderFragment, "reminder").commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
    }

    @Test
    public void testLaunchReminderFragment(){
        view = reminderFragment.getView().findViewById(R.id.view_in_fragment_rem);
        assertNotNull(view);
    }
    @Test
    public void testTimeTV(){
        onView(withId(R.id.time)).check(matches(withText("00:00")));
    }
    @Test
    public void testDateTV(){
        onView(withId(R.id.date)).check(matches(withText("01/01/1970")));
    }
    @Test
    public void testReminderNameTV(){
        onView(withId(R.id.reminderName)).check(matches(withText("Enter reminder title")));
    }
    @Test
    public void testAppearingReminderNameClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.reminderName)).check(matches(isEnabled()));
    }
    @Test
    public void testAppearingTimeClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.time)).check(matches(isEnabled()));
    }
    @Test
    public void testAppearingDateClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.date)).check(matches(isEnabled()));
    }
    @Test
    public void testAppearingCheckBtnClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.checkBtn)).check(matches(isEnabled()));
    }
    @Test
    public void testAppearingCloseBtnClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.closeBtn)).check(matches(isEnabled()));
    }
    @Test
    public void testAppearingAddBtnClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.closeBtn)).perform(click());
        onView(withId(R.id.addBtn)).check(matches(isEnabled()));
    }
    @Test
    public void testDisAppearingCheckBtnClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.closeBtn)).perform(click());
        onView(withId(R.id.checkBtn)).check(matches(not(isDisplayed())));
    }
    @Test
    public void testDisAppearingCloseBtnClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.closeBtn)).perform(click());
        onView(withId(R.id.closeBtn)).check(matches(not(isDisplayed())));
    }
    @Test
    public void testDisAppearingReminderNameClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.closeBtn)).perform(click());
        onView(withId(R.id.reminderName)).check(matches(withText("Enter reminder title")));
    }
    @Test
    public void testDisAppearingDateTVClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.closeBtn)).perform(click());
        onView(withId(R.id.date)).check(matches(not(isDisplayed())));
    }
    @Test
    public void testDisAppearingTimeTVClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.closeBtn)).perform(click());
        onView(withId(R.id.time)).check(matches(not(isDisplayed())));
    }
    @Test
    public void testAfterCheckDisappearingDateTVClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.time)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.date)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.checkBtn)).perform(click());
        onView(withId(R.id.date)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testAfterCheckDisappearingTimeTVClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.time)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.date)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.checkBtn)).perform(click());
        onView(withId(R.id.time)).check(matches(not(isDisplayed())));
    }
    @Test
    public void testAfterCheckDisappearingReminderNameTVClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.time)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.date)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.checkBtn)).perform(click());
        onView(withId(R.id.reminderName)).check(matches(withText("Enter reminder title")));
    }
    @Test
    public void testAfterCheckDisappearingCheckBtnTVClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.time)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.date)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.checkBtn)).perform(click());
        onView(withId(R.id.checkBtn)).check(matches(not(isDisplayed())));
    }
    @Test
    public void testAfterCheckDisappearingCloseBtnTVClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.time)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.date)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.checkBtn)).perform(click());
        onView(withId(R.id.closeBtn)).check(matches(not(isDisplayed())));
    }
    @Test
    public void testAfterCloseDisappearingCheckBtnTVClick(){
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.time)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.date)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.closeBtn)).perform(click());
        onView(withId(R.id.checkBtn)).check(matches(not(isDisplayed())));
    }
    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

}