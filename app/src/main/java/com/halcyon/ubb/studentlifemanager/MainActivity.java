package com.halcyon.ubb.studentlifemanager;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //resolve references
        mSpinner = (Spinner) findViewById(R.id.main_spinner);
        mNav = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        mToolbar= (Toolbar) findViewById(R.id.toolbars);
        mTabs= (TabLayout) findViewById(R.id.main_tabs);
        mFrame = (FrameLayout) findViewById(R.id.main_frame);
        mTabsLayout=findViewById(R.id.main_tabs_layout);
        mSpinnerLayout=findViewById(R.id.main_spinner_layout);
        mFab= (FloatingActionButton) findViewById(R.id.main_fab);

        //nav
        mNav.setOnNavigationItemSelectedListener(this);


        //spinner
        mItems=new String[]{ "First","Second" };
        mSpinnerAdapter= new ArrayAdapter<>(this, R.layout.main_spinner_textview, mItems);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);

        //fist tab
        mCourses=getLayoutInflater().inflate(R.layout.main_tab_courses,mFrame,false);
        //second tab
        mReminders=getLayoutInflater().inflate(R.layout.main_tab_reminders,mFrame,false);
        //third tab
        mTimeTable=getLayoutInflater().inflate(R.layout.main_tab_timetable,mFrame,false);

        ViewPager pager=(ViewPager) mTimeTable.findViewById(R.id.tab_timetable_viewpager);
        mTabs.setupWithViewPager(pager);
        pager.setAdapter(new DayPagerAdapter(getSupportFragmentManager()));


        //select first tab
        mSpinnerLayout.setVisibility(View.GONE);
        mTabsLayout.setVisibility(View.GONE);

        mToolbar.setVisibility(View.GONE);

        mSpinnerLayout.setVisibility(View.VISIBLE);

        mFab.hide();

        mItems[0]="First";
        mItems[1]="Second";
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
        mTabsLayout.setVisibility(View.GONE);

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

                mTabsLayout.setVisibility(View.GONE);

                mToolbar.setVisibility(View.VISIBLE);
                mToolbar.setTitle(item.getTitle());

                mFrame.addView(mReminders);

                mFab.show();
                break;
            case R.id.tab_timetable:
                mTabsLayout.setVisibility(View.VISIBLE);
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

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
            MainActivity.PlaceholderFragment fragment = new MainActivity.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class DayPagerAdapter extends FragmentPagerAdapter {
        private String[] mDays;


        public DayPagerAdapter(FragmentManager fm) {
            super(fm);
            mDays=getBaseContext().getResources().getStringArray(R.array.days);
        }

        @Override
        public Fragment getItem(int position) {
            return MainActivity.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return mDays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDays[position];
        }
    }
}
