package com.halcyon.ubb.studentlifemanager.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.ui.course.fragment.CourseFragment;
import com.halcyon.ubb.studentlifemanager.ui.reminder.fragment.ReminderFragment;
import com.halcyon.ubb.studentlifemanager.ui.timetable.fragment.TimetableFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private int mCurrentlySelected;
    private CourseFragment mCourseFragment;
    private ReminderFragment mReminderFragment;
    private TimetableFragment mTimetableFragment;
    private int mCurrentPos;

    private static String NAV_POS="nav_pos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView mNav = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        //nav
        mNav.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState!=null) {
            mCourseFragment= (CourseFragment) getSupportFragmentManager().findFragmentByTag("course");
            mReminderFragment= (ReminderFragment) getSupportFragmentManager().findFragmentByTag("reminder");
            mTimetableFragment= (TimetableFragment) getSupportFragmentManager().findFragmentByTag("timetable");
            mCurrentPos=savedInstanceState.getInt(NAV_POS,0);
        }
        else
            mCurrentPos=0;

        mNav.getMenu().getItem(mCurrentPos).setChecked(true);
        onNavigationItemSelected(mNav.getMenu().getItem(mCurrentPos));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_POS,mCurrentPos);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        int pos=0;
        if (mCurrentlySelected==id) return false;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        switch (id) {
            case R.id.tab_courses:
                if (mReminderFragment!=null && mReminderFragment.isVisible())
                    transaction.hide(mReminderFragment);
                else if (mTimetableFragment!=null && mTimetableFragment.isVisible())
                    transaction.hide(mTimetableFragment);

                if (mCourseFragment==null) {
                    mCourseFragment=new CourseFragment();
                    transaction.add(R.id.main_frame,mCourseFragment,"course");
                }

                transaction.show(mCourseFragment);
                break;
            case R.id.tab_reminders:
                if (mCourseFragment!=null && mCourseFragment.isVisible())
                    transaction.hide(mCourseFragment);
                else if (mTimetableFragment!=null && mTimetableFragment.isVisible())
                    transaction.hide(mTimetableFragment);

                if (mReminderFragment==null) {
                    mReminderFragment = new ReminderFragment();
                    transaction.add(R.id.main_frame,mReminderFragment,"reminder");
                }
                transaction.show(mReminderFragment);
                pos=1;
                break;
            case R.id.tab_timetable:
                if (mReminderFragment!=null && mReminderFragment.isVisible())
                    transaction.hide(mReminderFragment);
                else if (mCourseFragment!=null && mCourseFragment.isVisible())
                    transaction.hide(mCourseFragment);

                if (mTimetableFragment==null) {
                    mTimetableFragment = new TimetableFragment();
                    transaction.add(R.id.main_frame,mTimetableFragment,"timetable");
                }
                transaction.show(mTimetableFragment);
                pos=2;
                break;
        }
        transaction.commit();

        mCurrentlySelected=id;
        mCurrentPos=pos;
        return true;
    }
}
