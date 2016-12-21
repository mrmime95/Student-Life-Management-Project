package com.halcyon.ubb.studentlifemanager.view;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.halcyon.ubb.studentlifemanager.NotifyReceiver;
import com.halcyon.ubb.studentlifemanager.database.SQLiteDB;
import com.halcyon.ubb.studentlifemanager.MySpinner;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.ReminderContact;
import com.halcyon.ubb.studentlifemanager.ReminderControl;
import com.halcyon.ubb.studentlifemanager.database.FirebaseDB;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //TODO CR: This class need some serious refactoring. First of all, there are lots of unused fields or fields that can be made public. Secondly, all inner
    // classes (Fragments) should be defined in separate files and communicate with the Activity through interfaces. [Peter]
    private BottomNavigationView mNav;
    private MySpinner mSpinner;
    private Toolbar mToolbar;
    private FrameLayout mFrame;
    private View mCourses;
    private View mReminders;
    private View mTimeTable;
    private TabLayout mTabsLayout;
    private View mSpinnerLayout;
    private View mSpinnerTextView;
    private int mCurrentlySelected;

    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager courseLayoutManager;
    //Reminders
    //TODO CR: Don't initialize the values here. Also, the code would be nicer if you defined everything in a new line. [Peter]
    private Calendar myCalendar;
    private ImageView reminderCheckBtn, reminderCloseBtn, reminderAddBtn;
    private TextView reminderDate, reminderTime;
    private DatePickerDialog.OnDateSetListener date;
    private TimePickerDialog.OnTimeSetListener time;
    private EditText reminderName;
    private boolean timeChoosed, dateChoosed;
    private RecyclerView reminderRecyclerView;
    private SQLiteDB dbHandler;
    private ReminderControl reminderControl;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCalendar = Calendar.getInstance();
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

    public void addNewReminderOnClick(View view){
        setNewReminderIcons(true, false);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
        String formattedDate = df.format(c.getTime());
        String formattedTime = tf.format(c.getTime());
        setNewRemindersData("", formattedDate.replace(".",""), formattedTime);
    }

    public void setNewRemindersData(String name, String date, String time){
        reminderName.setText(name);
        reminderDate.setText(date);
        reminderTime.setText(time);
    }

    public void setNewReminderIcons(boolean visability, boolean pipe)
    {
        reminderCloseBtn = (ImageView) findViewById(R.id.closeBtn);
        reminderCheckBtn = (ImageView) findViewById(R.id.pipeBtn);
        reminderDate = (TextView) findViewById(R.id.date);
        reminderTime = (TextView) findViewById(R.id.time);
        reminderName = (EditText) findViewById(R.id.reminderName);
        reminderAddBtn = (ImageView) findViewById(R.id.addBtn);
        if (visability){
            reminderCloseBtn.setVisibility(View.VISIBLE);
            reminderCheckBtn.setVisibility(View.VISIBLE);
            reminderDate.setVisibility(View.VISIBLE);
            reminderTime.setVisibility(View.VISIBLE);
            reminderAddBtn.setVisibility(View.GONE);
            reminderName.setEnabled(visability);
        }else{
            reminderCloseBtn.setVisibility(View.GONE);
            reminderCheckBtn.setVisibility(View.GONE);
            reminderDate.setVisibility(View.GONE);
            reminderTime.setVisibility(View.GONE);
            reminderAddBtn.setVisibility(View.VISIBLE);
            reminderName.setEnabled(visability);
        }
        //TODO CR: You can use the ternary operator here: img.setImageResource(pipe ? R.drawable.ic_check_ok : R.drawable.ic_check); [Peter]
        //TODO CR: If you import PNG-s, make sure all 5 densities are in the project. However, you can use vector assets which are MUCH better for icons. [Peter]
        //TODO CR: Pipe...? [Peter]
        if (pipe) reminderCheckBtn.setImageResource(R.drawable.ic_check_ok);
        else reminderCheckBtn.setImageResource(R.drawable.ic_check);
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
            reminderCheckBtn.setImageResource(R.drawable.ic_check_ok);
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
            reminderCheckBtn.setImageResource(R.drawable.ic_check_ok);
        }
    }

    public void pipeBtnOnClick(View view){
        if (timeChoosed && dateChoosed){
            timeChoosed =  false;
            dateChoosed = false;
            ReminderContact temp = new ReminderContact(1, reminderName.getText().toString().replace("\n","")
                    , reminderDate.getText().toString().replace("\n","")
                    , reminderTime.getText().toString().replace("\n",""));

            //make an intent to NotifyReceiver
            Intent my_intient = new Intent(this, NotifyReceiver.class);

            //with extras i'm giving away the Tittle and the Date/Time
            my_intient.putExtra("Title", temp.getName());
            my_intient.putExtra("DateTime", "Tomorrow: " + temp.getDate() + " at " + temp.getTime());
            AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            //setting up a calendar to date and time's of the new reminder,this will show us when
            //will be the notify
            Calendar calendar = Calendar.getInstance();
            Date date = null;
            Date time = null;
            try {
                date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(temp.getDate().toLowerCase());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                time = new SimpleDateFormat("h:mm a", Locale.getDefault()).parse(temp.getTime());
            } catch (ParseException e) {
                Log.e("ERROR", e.toString());
            }

            calendar.setTimeInMillis(date.getTime());
            calendar.add(calendar.DAY_OF_YEAR, -1);
            calendar.set(Calendar.MINUTE, time.getMinutes());
            calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
            calendar.set(Calendar.SECOND, 00);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (int)System.currentTimeMillis(), my_intient, PendingIntent.FLAG_UPDATE_CURRENT);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


            setNewReminderIcons(false, false);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
            String formattedDate = df.format(c.getTime());
            String formattedTime = tf.format(c.getTime());
            setNewRemindersData("Enter reminder title", formattedDate, formattedTime);
            dbHandler.insert(temp);
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
                mReminders=getLayoutInflater().inflate(R.layout.main_tab_reminders,mFrame,false);
                dbHandler = new SQLiteDB(this);
                reminderControl = new ReminderControl(this);
                reminderRecyclerView = (RecyclerView)mReminders.findViewById(R.id.reminders_recycler);
                reminderControl.updateReminders(reminderRecyclerView,dbHandler);
                //Reminders
                reminderCloseBtn = (ImageView) findViewById(R.id.closeBtn);
                reminderCheckBtn = (ImageView) findViewById(R.id.pipeBtn);
                reminderAddBtn = (ImageView) findViewById(R.id.addBtn);
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
