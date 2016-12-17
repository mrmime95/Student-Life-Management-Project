package com.halcyon.ubb.studentlifemanager.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.halcyon.ubb.studentlifemanager.CourseContact;
import com.halcyon.ubb.studentlifemanager.MyDBHandler;
import com.halcyon.ubb.studentlifemanager.MySpinner;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.ReminderContact;
import com.halcyon.ubb.studentlifemanager.ReminderControl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //TODO CR: This class need some serious refactoring. First of all, there are lots of unused fields or fields that can be made public. Secondly, all inner
    // classes (Fragments) should be defined in separate files and communicate with the Activity through interfaces. [Peter]
    private BottomNavigationView mNav;
    private MySpinner mSpinner;
    private Toolbar mToolbar;
    private TabLayout mTabs;
    private ArrayAdapter<String> mSpinnerAdapter;
    private String[] mItems;
    private FrameLayout mFrame;
    private View mCourses;
    private View mReminders;
    private View mTimeTable;

    private View mTabsLayout;
    private View mSpinnerLayout;
    private View mSpinnerTextView;
    private int mCurrentlySelected;

    private RecyclerView courseRecyclerView;
    private RecyclerView.Adapter courseAdapter;
    private RecyclerView.LayoutManager courseLayoutManager;
    //TODO CR: Separate mock data from the final implementation. [Peter]
    int[] images = {R.drawable.pic1, R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.pic5,R.drawable.pic6, R.drawable.pic7,R.drawable.pic8};
    private String[] descriptions, titles;
    private ArrayList<CourseContact> courseContacts = new ArrayList<CourseContact>();

    //Reminders
    //TODO CR: Don't initialize the values here. Also, the code would be nicer if you defined everything in a new line. [Peter]
    private Calendar myCalendar = Calendar.getInstance();
    private ImageView reminderPipe, reminderClose;
    private TextView reminderDate, reminderTime;
    private DatePickerDialog.OnDateSetListener date;
    private TimePickerDialog.OnTimeSetListener time;
    private EditText reminderName;
    private boolean timeChoosed, dateChoosed;

    private RecyclerView reminderRecyclerView;
    private  RecyclerView.Adapter reminderAdapter;
    private RecyclerView.LayoutManager reminderLayoutManager;

    private MyDBHandler dbHandler;
    private ReminderControl reminderControl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO CR: Initializing Firebase should be done in the Application class, not within an Activity. [Peter]
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        //TODO CR: Don't initialize variables until you need them, it makes your code more difficult to follow. [Peter]
        mSpinner = (MySpinner) findViewById(R.id.main_spinner);
        mNav = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        mToolbar= (Toolbar) findViewById(R.id.toolbars);
        //TODO CR: It's like you don't even care. Why are there two references to the same View? Don't ignore Lint warnings. [Peter]
        mTabs= (TabLayout) findViewById(R.id.main_tabs);
        mFrame = (FrameLayout) findViewById(R.id.main_frame);
        mTabsLayout=findViewById(R.id.main_tabs);
        mSpinnerLayout=findViewById(R.id.main_spinner_layout);

        //TODO CR: Very poor quality comments throughout the file. [Peter]
        //nav
        mNav.setOnNavigationItemSelectedListener(this);
        //spinner
        Query query = FirebaseDatabase.getInstance().getReference().child("Courses").orderByValue();
        mSpinner.courseSpinnerUpdate(query);
        //TODO CR: Don't inflate all three Fragments if you're only going to display one. The FragmentTransaction's replace() method would be much more optimal. [Peter]
        //fist tab
        mCourses=getLayoutInflater().inflate(R.layout.main_tab_courses,mFrame,false);
        //second tab
        mReminders=getLayoutInflater().inflate(R.layout.main_tab_reminders,mFrame,false);
        //third tab
        mTimeTable=getLayoutInflater().inflate(R.layout.main_tab_timetable,mFrame,false);

        ViewPager pager=(ViewPager) mTimeTable.findViewById(R.id.tab_timetable_viewpager);
        //mTabs.setupWithViewPager(pager);
        //pager.setAdapter(new DayPagerAdapter(getSupportFragmentManager(), getTestTimeTable()));


        //TODO CR: You're doing way too much Visibility toggling for this to be considered a nice implementation. When you look at a design and see three different pages
        // with completely different action bars, consider making them part of each Fragment: right now there is too much hidden content in your View hierarchy that consumes
        // resources and reduces performance. [Peter]
        //select first tab
        mSpinnerLayout.setVisibility(View.GONE);
        mTabsLayout.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        mSpinnerLayout.setVisibility(View.VISIBLE);
        /*mSpinnerAdapter.notifyDataSetChanged();*/
        mSpinner.courseSpinnerUpdate(query);
        mFrame.addView(mCourses);

        //course items

        courseRecyclerView = (RecyclerView) findViewById(R.id.recyclerCourse);
        courseLayoutManager  = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);
        courseRecyclerView.setHasFixedSize(true);


        mSpinner.setMyItemSelectedListener(courseRecyclerView, this);
        // reminder's list
        reminderControl = new ReminderControl(this);
        reminderRecyclerView = (RecyclerView)mReminders.findViewById(R.id.reminders_recycler);
        //upload reminders from database
        dbHandler = new MyDBHandler(this);
        reminderControl.updateReminders(reminderRecyclerView,dbHandler);
        //supportBar options

        //TODO CR: The status bar color can be set from the theme. Don't try to do everything in Java unless you have to. [Peter]
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.supportBarColor));

        //Reminders
        reminderClose = (ImageView) findViewById(R.id.closeBtn);
        reminderPipe = (ImageView) findViewById(R.id.pipeBtn);
        reminderDate = (TextView) findViewById(R.id.date);
        reminderTime = (TextView) findViewById(R.id.time);
        reminderName = (EditText) findViewById(R.id.reminderName);
        timeChoosed = dateChoosed = false;
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateReminderDate();
            }

        };
    }

    public void addNewReminderOnClick(View view){
        setNewReminderIcons(true, false);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
        String formattedDate = df.format(c.getTime());
        String formattedTime = tf.format(c.getTime());
        setNewRemindersData("", formattedDate, formattedTime);
    }

    public void setNewRemindersData(String name, String date, String time){
        reminderName.setText(name);
        reminderDate.setText(date);
        reminderTime.setText(time);
    }

    public void setNewReminderIcons(boolean visability, boolean pipe)
    {
        reminderClose = (ImageView) findViewById(R.id.closeBtn);
        reminderPipe = (ImageView) findViewById(R.id.pipeBtn);
        reminderDate = (TextView) findViewById(R.id.date);
        reminderTime = (TextView) findViewById(R.id.time);
        reminderName = (EditText) findViewById(R.id.reminderName);
        if (visability){
            reminderClose.setVisibility(View.VISIBLE);
            reminderPipe.setVisibility(View.VISIBLE);
            reminderDate.setVisibility(View.VISIBLE);
            reminderTime.setVisibility(View.VISIBLE);
            reminderName.setEnabled(visability);
        }else{
            reminderClose.setVisibility(View.GONE);
            reminderPipe.setVisibility(View.GONE);
            reminderDate.setVisibility(View.GONE);
            reminderTime.setVisibility(View.GONE);
            reminderName.setEnabled(visability);
        }
        //TODO CR: You can use the ternary operator here: img.setImageResource(pipe ? R.drawable.ic_check_ok : R.drawable.ic_check); [Peter]
        //TODO CR: If you import PNG-s, make sure all 5 densities are in the project. However, you can use vector assets which are MUCH better for icons. [Peter]
        //TODO CR: Pipe...? [Peter]
        if (pipe) reminderPipe.setImageResource(R.drawable.ic_check_ok);
        else reminderPipe.setImageResource(R.drawable.ic_check);
    }

    public void  closeAddingNewReminderOnClick(View view){
        setNewReminderIcons(false, false);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
        String formattedDate = df.format(c.getTime());
        String formattedTime = tf.format(c.getTime());
        setNewRemindersData("Enter reminder title", formattedDate, formattedTime);
        timeChoosed = dateChoosed = false;
    }

    public void dateChooserOnClick(View view){
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void updateReminderDate(){
        String myFormat = "dd-MMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String str = sdf.format(myCalendar.getTime());
        reminderDate.setText(str);
        dateChoosed = true;
        //TODO CR: Pay attention to Lint warnings. Don't write logic that doesn't make sense. [Peter]
        if ((timeChoosed && dateChoosed)){
            reminderPipe.setImageResource(R.drawable.ic_check_ok);
        }
    }
    public  void  timeChooserOnClick(View view){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        myCalendar.set(Calendar.HOUR, hourOfDay);
                        myCalendar.set(Calendar.MINUTE, minute);
                        updateTimeInReminder();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    public void updateTimeInReminder(){
        SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
        String str = tf.format(myCalendar.getTime());
        reminderTime.setText(str);
        timeChoosed = true;
        //TODO CR: As a general note, you should move duplicated code to a common method. In this case, however, delete it (bad UX). [Peter]
        if ((timeChoosed && dateChoosed)){
            reminderPipe.setImageResource(R.drawable.ic_check_ok);
        }
    }

    public void pipeBtnOnClick(View view){
        if (timeChoosed && dateChoosed){
            timeChoosed =  false;
            dateChoosed = false;
            ReminderContact temp = new ReminderContact(1, reminderName.getText().toString().replace("\n","")
                    , reminderDate.getText().toString().replace("\n","").replace(" ", "")
                    , reminderTime.getText().toString().replace("\n","").replace(" ", ""));
            setNewReminderIcons(false, false);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
            String formattedDate = df.format(c.getTime());
            String formattedTime = tf.format(c.getTime());
            setNewRemindersData("Enter reminder title", formattedDate, formattedTime);
            dbHandler.insertContact(temp);
            reminderControl.updateReminders(reminderRecyclerView, dbHandler);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (mCurrentlySelected==id) return false;
        mFrame.removeAllViews();
        mSpinnerTextView=mSpinner.findViewById(R.id.spinner_textView);
       // mSpinnerTextView.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        mTabsLayout.setVisibility(View.GONE);
        if (id!=R.id.tab_courses && id!=R.id.tab_timetable) mSpinnerLayout.setVisibility(View.GONE);
        //TODO CR: As mentioned before, you shouldn't just add already inflated Views: use replace() instead. [Peter]
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(android.R.id.content));

        if (id==R.id.tab_courses || id==R.id.tab_timetable) mSpinnerLayout.setVisibility(View.GONE);

        switch (id) {
            case R.id.tab_courses:
                mSpinnerLayout.setVisibility(View.VISIBLE);
                Query query = FirebaseDatabase.getInstance().getReference().child("Courses").orderByValue();
                mSpinner.courseSpinnerUpdate(query);
                mFrame.addView(mCourses);
                break;
            case R.id.tab_reminders:
                mTabsLayout.setVisibility(View.GONE);
                mToolbar.setVisibility(View.VISIBLE);
                mToolbar.setTitle(item.getTitle());
                mFrame.addView(mReminders);

                break;
            case R.id.tab_timetable:
                mTabsLayout.setVisibility(View.VISIBLE);
                mSpinnerLayout.setVisibility(View.VISIBLE);
                mFrame.addView(mTimeTable);
                break;
        }

        mCurrentlySelected=id;

        return true;
    }

    //TODO CR: Move the Fragment class to a separate file (or remove unused class). [Peter]
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
}
