package com.halcyon.ubb.studentlifemanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
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
import android.text.format.DateFormat;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mNav;
    private Spinner mSpinner;
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
    int[] images = {R.drawable.pic1, R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.pic5,R.drawable.pic6, R.drawable.pic7,R.drawable.pic8};
    private String[] descriptions, titles;
    private ArrayList<CourseContact> courseContacts = new ArrayList<CourseContact>();

    //Reminders
    private ImageView reminderPipe, reminderClose;
    private TextView reminderDate, reminderTime;
    private Calendar myCalendar = Calendar.getInstance();
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
        setContentView(R.layout.activity_main);
        //database
            dbHandler = new MyDBHandler(this);
        //resolve references
        mSpinner = (Spinner) findViewById(R.id.main_spinner);
        mNav = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        mToolbar= (Toolbar) findViewById(R.id.toolbars);
        mTabs= (TabLayout) findViewById(R.id.main_tabs);
        mFrame = (FrameLayout) findViewById(R.id.main_frame);
        mTabsLayout=findViewById(R.id.main_tabs_layout);
        mSpinnerLayout=findViewById(R.id.main_spinner_layout);

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
        mItems[0]="First";
        mItems[1]="Second";
        mSpinnerAdapter.notifyDataSetChanged();
        mFrame.addView(mCourses);

        //course items

        descriptions = getResources().getStringArray(R.array.course_description);
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
        courseRecyclerView.setHasFixedSize(true);
        courseAdapter = new ContactAdapter(courseContacts, this);
        courseRecyclerView.setAdapter(courseAdapter);

        // reminder's list

        reminderControl = new ReminderControl(this);
        reminderRecyclerView = (RecyclerView)mReminders.findViewById(R.id.reminders_recycler);
        reminderControl.updateReminders(reminderRecyclerView,dbHandler);
        //supportBar options

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
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateReminderDate();
            }

        };
    }

    public void addNewReminderOnClick(View view){
        setNewReminderIcons(true, false);
        setNewRemindersData(view, "", "01/01/1970", "00:00");
    }

    public void setNewRemindersData(View view, String name, String date, String time){
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
        View viewPipe = findViewById(R.id.pipeBtn);
        View viewDate = findViewById(R.id.date);
        View viewTime = findViewById(R.id.time);
        ImageView img = (ImageView) findViewById(R.id.pipeBtn);
        reminderName = (EditText)((Activity)this).findViewById(R.id.reminderName);
        if (visability){
            viewClose.setVisibility(View.VISIBLE);
            viewPipe.setVisibility(View.VISIBLE);
            viewDate.setVisibility(View.VISIBLE);
            viewTime.setVisibility(View.VISIBLE);
            reminderName.setEnabled(visability);
        }else{
            viewClose.setVisibility(View.GONE);
            viewPipe.setVisibility(View.GONE);
            viewDate.setVisibility(View.GONE);
            viewTime.setVisibility(View.GONE);
            reminderName.setEnabled(visability);
        }
        if (pipe) img.setImageResource(R.drawable.ic_check_ok);
        else img.setImageResource(R.drawable.ic_check);
    }

    public void  closeAddingNewReminderOnClick(View view){
        setNewReminderIcons(false, false);
        //reminderControl.setNewReminderIcons(false, false, view);
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

        mFrame.removeAllViews();
        mSpinnerTextView=mSpinner.findViewById(R.id.spinner_textView);
        mSpinnerTextView.setVisibility(View.GONE);

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
