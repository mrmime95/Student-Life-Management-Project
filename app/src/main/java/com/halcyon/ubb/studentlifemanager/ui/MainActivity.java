package com.halcyon.ubb.studentlifemanager.ui;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.ui.course.CourseFragment;
import com.halcyon.ubb.studentlifemanager.ui.reminder.ReminderFragment;
import com.halcyon.ubb.studentlifemanager.ui.timetable.fragment.TimetableFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private int mCurrentlySelected;
    private CourseFragment mCourseFragment;
    private ReminderFragment mReminderFragment;
    private TimetableFragment mTimetableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set windowsDecor white => prevent overdraw
        // tried setting from style, didn't work
        //TODO: Figuring out how to set decorview white in theme files.
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        //nav
        nav.setOnNavigationItemSelectedListener(this);
        mCourseFragment=new CourseFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, mCourseFragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (mCurrentlySelected==id) return false;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        switch (id) {
            case R.id.tab_courses:
                transaction.replace(R.id.main_frame, mCourseFragment);
                break;
            case R.id.tab_reminders:
                if (mReminderFragment==null)
                    mReminderFragment = new ReminderFragment();
                transaction.replace(R.id.main_frame, mReminderFragment);
                break;
            case R.id.tab_timetable:
                if (mTimetableFragment==null)
                    mTimetableFragment = new TimetableFragment();
                transaction.replace(R.id.main_frame, mTimetableFragment);
                break;
        }
        transaction.commit();

        //TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.main_frame));
        mCurrentlySelected=id;
        return true;
    }
}
