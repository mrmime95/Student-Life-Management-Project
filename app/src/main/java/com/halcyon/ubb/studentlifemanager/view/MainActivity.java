package com.halcyon.ubb.studentlifemanager.view;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.view.fragment.TimetableDayFragment;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mNav;
    private Spinner mSpinner;
    private Toolbar mToolbar;
    private TabLayout mTabs;
    private ArrayAdapter<String> mSpinnerAdapter;
    private String[] mItems;
    private FrameLayout mFrame;
    private FloatingActionButton mFab;
    private View mCourses;
    private View mReminders;
    private View mTimeTable;

    private View mTabsLayout;
    private View mSpinnerLayout;
    private View mSpinnerTextView;

    private int mCurrentlySelected;
    private Object testTimeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //resolve references
        mSpinner = (Spinner) findViewById(R.id.main_spinner);
        mNav = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        mToolbar = (Toolbar) findViewById(R.id.toolbars);
        mTabs = (TabLayout) findViewById(R.id.main_tabs);
        mFrame = (FrameLayout) findViewById(R.id.main_frame);
        //mTabsLayout = findViewById(R.id.main_tabs_layout);
        mSpinnerLayout = findViewById(R.id.main_spinner_layout);
        mFab = (FloatingActionButton) findViewById(R.id.main_fab);

        //nav
        mNav.setOnNavigationItemSelectedListener(this);


        //spinner
        mItems = new String[]{"First", "Second"};
        mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.main_spinner_textview, mItems);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);

        //fist tab
        mCourses = getLayoutInflater().inflate(R.layout.main_tab_courses, mFrame, false);
        //second tab
        mReminders = getLayoutInflater().inflate(R.layout.main_tab_reminders, mFrame, false);
        //third tab
        mTimeTable = getLayoutInflater().inflate(R.layout.main_tab_timetable, mFrame, false);

        ViewPager pager = (ViewPager) mTimeTable.findViewById(R.id.tab_timetable_viewpager);
        mTabs.setupWithViewPager(pager);
        pager.setAdapter(new DayPagerAdapter(getSupportFragmentManager(), getTestTimeTable()));


        //select first tab
        mSpinnerLayout.setVisibility(View.GONE);
       // mTabsLayout.setVisibility(View.GONE);
        mTabs.setVisibility(View.GONE);

        mToolbar.setVisibility(View.GONE);

        mSpinnerLayout.setVisibility(View.VISIBLE);

        mFab.hide();

        mItems[0] = "First";
        mItems[1] = "Second";
        mSpinnerAdapter.notifyDataSetChanged();

        mFrame.addView(mCourses);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (mCurrentlySelected==id)
            return false;

        mFab.hide();
        mFrame.removeAllViews();
        mSpinnerTextView=mSpinner.findViewById(R.id.spinner_textView);
        mSpinnerTextView.setVisibility(View.GONE);

        /*if (id==R.id.tab_reminders || id==R.id.tab_courses)
            mSpinnerLayout.setVisibility(View.GONE);

        if (id == R.id.tab_courses)
            mToolbar.setVisibility(View.GONE);


        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(android.R.id.content));

        if (id!=R.id.tab_reminders && id!=R.id.tab_courses)  mSpinnerLayout.setVisibility(View.GONE);
        if (id != R.id.tab_courses)  mToolbar.setVisibility(View.GONE);
        mTabsLayout.setVisibility(View.GONE);*/
        mToolbar.setVisibility(View.GONE);

        mToolbar.setVisibility(View.GONE);
        mTabs.setVisibility(View.GONE);

        if (id!=R.id.tab_courses && id!=R.id.tab_timetable) mSpinnerLayout.setVisibility(View.GONE);

        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(android.R.id.content));

        if (id==R.id.tab_courses || id==R.id.tab_timetable) mSpinnerLayout.setVisibility(View.GONE);

        switch (id) {
            case R.id.tab_courses:
                mSpinnerLayout.setVisibility(View.VISIBLE);
                mSpinnerTextView.setVisibility(View.VISIBLE);
                mItems[0]="First";
                mItems[1]="Second";
                mSpinnerAdapter.notifyDataSetChanged();

                mFrame.addView(mCourses);
                break;
            case R.id.tab_reminders:

                mTabs.setVisibility(View.GONE);

                mToolbar.setVisibility(View.VISIBLE);
                mToolbar.setTitle(item.getTitle());

                mFrame.addView(mReminders);

                mFab.show();
                break;
            case R.id.tab_timetable:
                mTabs.setVisibility(View.VISIBLE);
                mSpinnerTextView.setVisibility(View.VISIBLE);
                mSpinnerLayout.setVisibility(View.VISIBLE);

                mItems[0]="Third";
                mItems[1]="Fourth";
                mSpinnerAdapter.notifyDataSetChanged();

                mFrame.addView(mTimeTable);
                break;
        }

        mCurrentlySelected=id;

        return true;
    }



    public class DayPagerAdapter extends FragmentPagerAdapter {
        private String[] mDays;
        private Timetable mTimetable;

        public DayPagerAdapter(FragmentManager fm, Timetable timetable) {
            super(fm);
            mDays=getBaseContext().getResources().getStringArray(R.array.days);
            mTimetable=timetable;
        }

        @Override
        public Fragment getItem(int position) {
            return TimetableDayFragment.newInstance(position,mTimetable.getTimetableOnDay(position));
        }

        @Override
        public int getCount() {
            return mTimetable.getDayCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDays[position];
        }
    }

    public Timetable getTestTimeTable() {
        ArrayList<TimetableDay> days=new ArrayList<>();

        for (int i=0;i<6;++i) {
            ArrayList<Event> events=new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY,8);
            events.add(new Event("Course • Matemathicum","Algebra of analytics",cal.getTime(),cal.getTime()));
            events.add(new Event("Laboratory • Central","C++ coding standards",cal.getTime(),cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY,14);
            events.add(new Event("Laboratory • FSEGA","Linux basics",cal.getTime(),cal.getTime()));
            cal.set(Calendar.HOUR_OF_DAY,19);
            events.add(new Event("Laboratory • FSEGA","C++ advanced",cal.getTime(),cal.getTime()));
            days.add(new TimetableDay(events));
        }

        Log.d("hi","hi");

        return new Timetable(days);
    }
}
