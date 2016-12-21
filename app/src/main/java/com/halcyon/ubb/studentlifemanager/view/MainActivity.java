package com.halcyon.ubb.studentlifemanager.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.halcyon.ubb.studentlifemanager.MySpinner;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.database.FirebaseDB;
import com.halcyon.ubb.studentlifemanager.view.fragment.ReminderFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //TODO CR: This class need some serious refactoring. First of all, there are lots of unused fields or fields that can be made public. Secondly, all inner
    // classes (Fragments) should be defined in separate files and communicate with the Activity through interfaces. [Peter]
    private BottomNavigationView mNav;
    private MySpinner mSpinner;
    private Toolbar mToolbar;
    private FrameLayout mFrame;
    private View mCourses;
    private View mTimeTable;
    private TabLayout mTabsLayout;
    private View mSpinnerLayout;
    private View mSpinnerTextView;
    private int mCurrentlySelected;


    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager courseLayoutManager;
    //Reminders
    //TODO CR: Don't initialize the values here. Also, the code would be nicer if you defined everything in a new line. [Peter]
    private ViewPager mPager;

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
        //spinner
        Query query = FirebaseDatabase.getInstance().getReference().child("courseNames").orderByValue();
        mSpinner.courseSpinnerUpdate(query);
        //fist tab
        mCourses=getLayoutInflater().inflate(R.layout.main_tab_courses,mFrame,false);
        courseRecyclerView = (RecyclerView) mCourses.findViewById(R.id.recyclerCourse);
        courseLayoutManager  = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);
        courseRecyclerView.setHasFixedSize(true);

        mSpinnerLayout.setVisibility(View.GONE);
        mTabsLayout.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        mSpinnerLayout.setVisibility(View.VISIBLE);
        mSpinner.courseSpinnerUpdate(query);
        mFrame.addView(mCourses);
        mSpinner.setMyItemSelectedListener(courseRecyclerView, this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to upload test data?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseDB.getInstance().createTestData();
            }
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Do you want to delete test data?");
                alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDB.getInstance().deleteTestData();
                    }
                });
                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialogBuilder.show();
            }
        });
        alertDialogBuilder.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (mCurrentlySelected==id)
            return false;
        mCourses=null;
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
                Query query = FirebaseDatabase.getInstance().getReference().child("courseNames").orderByValue();
                mSpinner.courseSpinnerUpdate(query);
                mCourses=getLayoutInflater().inflate(R.layout.main_tab_courses,mFrame,false);
                courseRecyclerView = (RecyclerView) mCourses.findViewById(R.id.recyclerCourse);
                courseLayoutManager  = new LinearLayoutManager(this);
                courseRecyclerView.setLayoutManager(courseLayoutManager);
                courseRecyclerView.setHasFixedSize(true);
                mFrame.addView(mCourses);
                mSpinner.setMyItemSelectedListener(courseRecyclerView, this);
                break;
            case R.id.tab_reminders:
                mTabsLayout.setVisibility(View.GONE);
                mToolbar.setVisibility(View.VISIBLE);
                mToolbar.setTitle(item.getTitle());
                ReminderFragment reminderFragment = new ReminderFragment();
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
