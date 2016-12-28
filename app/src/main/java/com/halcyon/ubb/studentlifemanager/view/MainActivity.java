package com.halcyon.ubb.studentlifemanager.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.halcyon.ubb.studentlifemanager.MySpinner;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.database.FirebaseDB;
import com.halcyon.ubb.studentlifemanager.view.fragment.CourseFragment;
import com.halcyon.ubb.studentlifemanager.view.fragment.ReminderFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mNav;
    private MySpinner mSpinner;
    private Toolbar mToolbar;
    private FrameLayout mFrame;
    private View mTimeTable;
    private TabLayout mTabsLayout;
    private View mSpinnerLayout;
    private View mSpinnerTextView;
    private int mCurrentlySelected;
    private ViewPager mPager;
    private CourseFragment courseFragment;
    private ReminderFragment reminderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = (MySpinner) findViewById(R.id.main_spinner);
        mNav = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        mToolbar= (Toolbar) findViewById(R.id.toolbars);
        mFrame = (FrameLayout) findViewById(R.id.main_frame);
        mTabsLayout=(TabLayout)  findViewById(R.id.main_tabs);
        mSpinnerLayout=findViewById(R.id.main_spinner_layout);
        //nav
        mNav.setOnNavigationItemSelectedListener(this);
        mSpinnerLayout.setVisibility(View.GONE);
        mTabsLayout.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        mSpinnerLayout.setVisibility(View.VISIBLE);

        courseFragment = new CourseFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, courseFragment).commit();
        FirebaseDB.getInstance().createTestData();
        //FirebaseDB.getInstance().deleteTestData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (mCurrentlySelected==id) return false;

        mTimeTable=null;
        mFrame.removeAllViews();
        mSpinnerTextView=mSpinner.findViewById(R.id.spinner_textView);

        mToolbar.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        mTabsLayout.setVisibility(View.GONE);

        if (id!=R.id.tab_courses && id!=R.id.tab_timetable) mSpinnerLayout.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(android.R.id.content));
        if (id==R.id.tab_courses || id==R.id.tab_timetable) mSpinnerLayout.setVisibility(View.GONE);

        switch (id) {
            case R.id.tab_courses:
                mSpinnerLayout.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, courseFragment).commit();
                break;
            case R.id.tab_reminders:
                mTabsLayout.setVisibility(View.GONE);
                mToolbar.setVisibility(View.VISIBLE);
                mToolbar.setTitle(item.getTitle());
                reminderFragment = new ReminderFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, reminderFragment).commit();
                break;
            case R.id.tab_timetable:
                mTabsLayout.setVisibility(View.VISIBLE);
                mSpinnerLayout.setVisibility(View.VISIBLE);
                mTimeTable=getLayoutInflater().inflate(R.layout.main_tab_timetable,mFrame,false);
                mPager=(ViewPager) mTimeTable.findViewById(R.id.tab_timetable_viewpager);
                mTabsLayout.setupWithViewPager(mPager);
                mPager.setAdapter(new DayPagerAdapter(this,getSupportFragmentManager(), "testCourse"));
                mFrame.addView(mTimeTable);
                break;
        }
        mCurrentlySelected=id;
        return true;
    }
}
