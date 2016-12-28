package com.halcyon.ubb.studentlifemanager.view;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.database.Database;
import com.halcyon.ubb.studentlifemanager.database.FirebaseDB;
import com.halcyon.ubb.studentlifemanager.view.fragment.CourseFragment;
import com.halcyon.ubb.studentlifemanager.view.fragment.ReminderFragment;
import com.halcyon.ubb.studentlifemanager.view.fragment.TimetableFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,DatabaseProvider {
    private int mCurrentlySelected;
    private CourseFragment mCourseFragment;
    private ReminderFragment mReminderFragment;
    private TimetableFragment mTimetableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set windowsDecor white => prevent overdraw
        // tried setting from style, didn't work
        //TODO: Figuring out how to set decorview white in theme files.
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        //nav
        nav.setOnNavigationItemSelectedListener(this);

        mReminderFragment = new ReminderFragment();
        mCourseFragment = new CourseFragment();
        mTimetableFragment = new TimetableFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, mCourseFragment).commit();
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
        if (mCurrentlySelected==id)
            return false;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        switch (id) {
            case R.id.tab_courses:
                transaction.replace(R.id.main_frame, mCourseFragment);
                break;
            case R.id.tab_reminders:
                transaction.replace(R.id.main_frame, mReminderFragment);
                break;
            case R.id.tab_timetable:
                transaction.replace(R.id.main_frame, mTimetableFragment);
                break;
        }
        transaction.commit();

        //TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.main_frame));
        mCurrentlySelected=id;
        return true;
    }

    @Override
    public Database getDatabase() {
        return FirebaseDB.getInstance();
    }
}
