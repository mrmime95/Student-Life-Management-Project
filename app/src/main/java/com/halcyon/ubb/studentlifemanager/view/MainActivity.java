package com.halcyon.ubb.studentlifemanager.view;

import android.content.DialogInterface;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
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
import com.halcyon.ubb.studentlifemanager.database.FirebaseDB;
import com.halcyon.ubb.studentlifemanager.ReminderContact;
import com.halcyon.ubb.studentlifemanager.ReminderControl;
import com.halcyon.ubb.studentlifemanager.model.timetable.Event;
import com.halcyon.ubb.studentlifemanager.model.timetable.TimetableDay;
import com.halcyon.ubb.studentlifemanager.view.fragment.TimetableDayFragment;

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
    private ArrayAdapter<String> mSpinnerAdapter;
    private String[] mItems;
    private FrameLayout mFrame;
    private View mCourses;
    private View mReminders;
    private View mTimeTable;

    private TabLayout mTabsLayout;
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
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO CR: Initializing Firebase should be done in the Application class, not within an Activity. [Peter]
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        //database
            dbHandler = new MyDBHandler(this);
        //resolve references
        //TODO CR: Don't initialize variables until you need them, it makes your code more difficult to follow. [Peter]
        mSpinner = (MySpinner) findViewById(R.id.main_spinner);
        mNav = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        mToolbar= (Toolbar) findViewById(R.id.toolbars);
        mFrame = (FrameLayout) findViewById(R.id.main_frame);
        mTabsLayout=(TabLayout)  findViewById(R.id.main_tabs);
        mSpinnerLayout=findViewById(R.id.main_spinner_layout);

        //nav
        mNav.setOnNavigationItemSelectedListener(this);

        //spinner
        Query query = FirebaseDatabase.getInstance().getReference().child("Courses").orderByValue();
        mSpinner.courseSpinnerUpdate(query);

        /*//TODO CR: Don't inflate all three Fragments if you're only going to display one. The FragmentTransaction's replace() method would be much more optimal. [Peter]
        // Answer : these are not fragments ( but i will try to inflate them when selected ) [Csaba] */
        //fist tab
        mCourses=getLayoutInflater().inflate(R.layout.main_tab_courses,mFrame,false);
        //second tab
        /*mReminders=getLayoutInflater().inflate(R.layout.main_tab_reminders,mFrame,false);
        //third tab
        mTimeTable=getLayoutInflater().inflate(R.layout.main_tab_timetable,mFrame,false);*/

        courseRecyclerView = (RecyclerView) mCourses.findViewById(R.id.recyclerCourse);
        courseLayoutManager  = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);
        courseRecyclerView.setHasFixedSize(true);

        /*ViewPager pager=(ViewPager) mTimeTable.findViewById(R.id.tab_timetable_viewpager);
        mTabsLayout.setupWithViewPager(pager);

        pager.setAdapter(new DayPagerAdapter(this,getSupportFragmentManager(), "testCourse"));*/

        //TODO CR: You're doing way too much Visibility toggling for this to be considered a nice implementation. When you look at a design and see three different pages
        // with completely different action bars, consider making them part of each Fragment: right now there is too much hidden content in your View hierarchy that consumes
        // resources and reduces performance. [Peter]
        // Answer: i think toolbar etc cannot be part of fragment. These elements are in toolbar and they similar between pages [Csaba]
        //select first tab
        mSpinnerLayout.setVisibility(View.GONE);
        mTabsLayout.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        mSpinnerLayout.setVisibility(View.VISIBLE);
        /*mItems[0]="First";
        mItems[1]="Second";
        mSpinnerAdapter.notifyDataSetChanged();*/
        mSpinner.courseSpinnerUpdate(query);
        mFrame.addView(mCourses);

        //course items

        /*descriptions = getResources().getStringArray(R.array.course_description);
        titles = getResources().getStringArray(R.array.course_titles);
        int count = 0;
        for (String str: descriptions){
            CourseContact courseContact = new CourseContact(images[count],titles[count], str);
            count++;
            courseContacts.add(courseContact);
        }
        courseRecyclerView = (RecyclerView) findViewById(R.id.recyclerCourse);
        courseLayoutManager  = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);
        courseRecyclerView.setHasFixedSize(true);*/
        //courseAdapter = new ContactAdapter(courseContacts, this);
        //courseRecyclerView.setAdapter(courseAdapter);


        mSpinner.setMyItemSelectedListener(courseRecyclerView, this);
        // reminder's list

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

    public void addNewReminderOnClick(View view){
        setNewReminderIcons(true, false);
        //TODO CR: Why do you hardcode these values? Think about the users, do they really want to see the default date at 1970? [Peter]
        setNewRemindersData(view, "", "01/01/1970", "00:00");
    }

    public void setNewRemindersData(View view, String name, String date, String time){
        //TODO CR: Remove useless parameter, and useless casts. Don't use findViewById calls multiple times if it can be avoided. I'm sure the
        // high-level flow could be optimized as well but I won't bother looking at it until the code is readable. [Peter]
        EditText reminderName = (EditText)((Activity)this).findViewById(R.id.reminderName);
        reminderName.setText(name);
        TextView textDate = (TextView) ((Activity)this).findViewById(R.id.date);
        textDate.setText(date);
        TextView textTime = (TextView) ((Activity)this).findViewById(R.id.time);
        textTime.setText(time);
    }

    public void setNewReminderIcons(boolean visability, boolean pipe)
    {
        View viewClose = findViewById(R.id.closeBtn);
        //TODO CR: Views can be down-casted any time but still, if you really want to keep separate variables for the different types (why...?),
        // re-use and cast the reference that was already saved. The findViewById() method is a relatively heavy one as it goes through all of
        // the layout hierarchy, be careful not to use it too often.. [Peter]
        View viewPipe = findViewById(R.id.pipeBtn);
        View viewDate = findViewById(R.id.date);
        View viewTime = findViewById(R.id.time);
        ImageView img = (ImageView) findViewById(R.id.pipeBtn);
        reminderName = (EditText)((Activity)this).findViewById(R.id.reminderName);
        //TODO CR: Watch out for typos. [Peter]
        if (visability){
            viewClose.setVisibility(View.VISIBLE);
            viewPipe.setVisibility(View.VISIBLE);
            viewDate.setVisibility(View.VISIBLE);
            viewTime.setVisibility(View.VISIBLE);
            //TODO CR: Watch out for Lint warnings. [Peter]
            reminderName.setEnabled(visability);
        }else{
            viewClose.setVisibility(View.GONE);
            viewPipe.setVisibility(View.GONE);
            viewDate.setVisibility(View.GONE);
            viewTime.setVisibility(View.GONE);
            reminderName.setEnabled(visability);
        }
        //TODO CR: You can use the ternary operator here: img.setImageResource(pipe ? R.drawable.ic_check_ok : R.drawable.ic_check); [Peter]
        //TODO CR: If you import PNG-s, make sure all 5 densities are in the project. However, you can use vector assets which are MUCH better for icons. [Peter]
        //TODO CR: Pipe...? [Peter]
        if (pipe) img.setImageResource(R.drawable.ic_check_ok);
        else img.setImageResource(R.drawable.ic_check);
    }

    public void  closeAddingNewReminderOnClick(View view){
        setNewReminderIcons(false, false);
        //reminderControl.setNewReminderIcons(false, false, view);
        //TODO CR: Don't hardcode values. Use constants for Strings that appear multiple times. Also, as mentioned before, come up with better default values. [Peter]
        setNewRemindersData(view, "Enter reminder title", "01/01/1970", "00:00");
        timeChoosed = dateChoosed = false;
    }

    public void dateChooserOnClick(View view){
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void updateReminderDate(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String str = sdf.format(myCalendar.getTime());
        TextView txtView = (TextView) ((Activity)this).findViewById(R.id.date);
        txtView.setText(str);
        dateChoosed = true;
        //TODO CR: Pay attention to Lint warnings. Don't write logic that doesn't make sense. [Peter]
        if ((timeChoosed && dateChoosed)){
            ImageView img= (ImageView)findViewById(R.id.pipeBtn);
            img.setImageResource(R.drawable.ic_check_ok);
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
                        if (minute<10)updateTimeInReminder(hourOfDay + ":0" + minute);
                        else updateTimeInReminder(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    public void updateTimeInReminder(String str){
        TextView txtView = (TextView) ((Activity)this).findViewById(R.id.time);
        txtView.setText(str);
        timeChoosed = true;
        //TODO CR: As a general note, you should move duplicated code to a common method. In this case, however, delete it (bad UX). [Peter]
        if ((timeChoosed && dateChoosed)){
            ImageView img= (ImageView) findViewById(R.id.pipeBtn);
            img.setImageResource(R.drawable.ic_check_ok);
        }
    }

    public void pipeBtnOnClick(View view){
        if (timeChoosed && dateChoosed){
            timeChoosed =  false;
            dateChoosed = false;
            TextView txtViewDate = (TextView) ((Activity)this).findViewById(R.id.date);
            TextView txtViewTime = (TextView) ((Activity)this).findViewById(R.id.time);
            ReminderContact temp = new ReminderContact(1, reminderName.getText().toString().replace("\n","")
                    , txtViewDate.getText().toString().replace("\n","").replace(" ", "")
                    , txtViewTime.getText().toString().replace("\n","").replace(" ", ""));
            setNewReminderIcons(false, false);
            setNewRemindersData(view, "Enter reminder title", "01/01/1970", "00:00");
            dbHandler.insertContact(temp);
            reminderControl.updateReminders(reminderRecyclerView, dbHandler);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (mCurrentlySelected==id)
            return false;

        mCourses=null;
        mTimeTable=null;
        mReminders=null;

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
//                mSpinnerTextView.setVisibility(View.VISIBLE);
                /*mItems[0]="First";
                mItems[1]="Second";
                mSpinnerAdapter.notifyDataSetChanged();*/
                Query query = FirebaseDatabase.getInstance().getReference().child("Courses").orderByValue();
                mSpinner.courseSpinnerUpdate(query);

                mCourses=getLayoutInflater().inflate(R.layout.main_tab_courses,mFrame,false);

                courseRecyclerView = (RecyclerView) mCourses.findViewById(R.id.recyclerCourse);
                courseLayoutManager  = new LinearLayoutManager(this);
                courseRecyclerView.setLayoutManager(courseLayoutManager);
                courseRecyclerView.setHasFixedSize(true);

                mFrame.addView(mCourses);
                break;
            case R.id.tab_reminders:

                mTabsLayout.setVisibility(View.GONE);

                mToolbar.setVisibility(View.VISIBLE);
                mToolbar.setTitle(item.getTitle());

                mReminders=getLayoutInflater().inflate(R.layout.main_tab_reminders,mFrame,false);

                reminderControl = new ReminderControl(this);
                reminderRecyclerView = (RecyclerView)mReminders.findViewById(R.id.reminders_recycler);
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

                mFrame.addView(mReminders);

                break;
            case R.id.tab_timetable:
                mTabsLayout.setVisibility(View.VISIBLE);
//                mSpinnerTextView.setVisibility(View.VISIBLE);
                mSpinnerLayout.setVisibility(View.VISIBLE);

             /*    mItems[0]="Third";
                mItems[1]="Fourth";
                mSpinnerAdapter.notifyDataSetChanged();
                */

                mTimeTable=getLayoutInflater().inflate(R.layout.main_tab_timetable,mFrame,false);

                mPager=(ViewPager) mTimeTable.findViewById(R.id.tab_timetable_viewpager);
                mTabsLayout.setupWithViewPager(mPager);
                mPager.setOffscreenPageLimit(6);

                mPager.setAdapter(new DayPagerAdapter(this,getSupportFragmentManager(), "testCourse"));
                mFrame.addView(mTimeTable);
                break;
        }
        mCurrentlySelected=id;

        return true;
    }
}
