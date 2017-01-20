package com.halcyon.ubb.studentlifemanager.ui.reminder.fragment;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.halcyon.ubb.studentlifemanager.ui.reminder.notification.NotifyReceiver;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.model.reminder.Reminder;
import com.halcyon.ubb.studentlifemanager.ui.reminder.update.ReminderUpdateData;
import com.halcyon.ubb.studentlifemanager.database.DatabaseProvider;
import com.halcyon.ubb.studentlifemanager.database.local.reminder.ReminderDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import static android.content.Context.ALARM_SERVICE;

/**
 *
 * Created by Szilard on 2016. 12. 21..
 */

//TODO Fix recycler to see last item when list is full.
public class ReminderFragment extends android.support.v4.app.Fragment {
    private Calendar myCalendar;
    private ImageView reminderCheckBtn, reminderCloseBtn, reminderAddBtn;
    private TextView reminderDate, reminderTime;
    private DatePickerDialog.OnDateSetListener date;
    private TimePickerDialog.OnTimeSetListener time;
    private EditText reminderName;
    private boolean timeChoosed, dateChoosed;
    private RecyclerView reminderRecyclerView;
    private ReminderDatabase dbHandler;
    private ReminderUpdateData updateReminders;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_tab_reminders, container, false);
        reminderCloseBtn = (ImageView) rootView.findViewById(R.id.closeBtn);
        reminderCheckBtn = (ImageView)rootView.findViewById(R.id.checkBtn);
        reminderDate = (TextView)rootView.findViewById(R.id.date);
        reminderTime = (TextView)rootView.findViewById(R.id.time);
        reminderName = (EditText)rootView.findViewById(R.id.reminderName);
        reminderAddBtn = (ImageView)rootView.findViewById(R.id.addBtn);

        myCalendar = Calendar.getInstance();
        dbHandler = DatabaseProvider.getInstance().getReminderDatabase(getContext());
        updateReminders = new ReminderUpdateData(getActivity());
        reminderRecyclerView = (RecyclerView)rootView.findViewById(R.id.reminders_recycler);
        updateReminders.updateReminders(reminderRecyclerView,dbHandler);

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

        reminderAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewReminderIcons(true, false);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
                String formattedDate = df.format(c.getTime());
                String formattedTime = tf.format(c.getTime());
                setNewRemindersData("", formattedDate.replace(".",""), formattedTime);
            }
        });
        reminderCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewReminderIcons(false, false);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
                String formattedDate = df.format(c.getTime());
                String formattedTime = tf.format(c.getTime());
                setNewRemindersData("Enter reminder title", formattedDate, formattedTime);
                timeChoosed = dateChoosed = false;
            }
        });
        reminderCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long tm = myCalendar.getTimeInMillis();
                long current = System.currentTimeMillis();
                if (tm < current){
                    Toast.makeText(getActivity(),"The choosed time is invalid!", Toast.LENGTH_LONG).show();
                }
                else if (timeChoosed && dateChoosed){
                    timeChoosed =  false;
                    dateChoosed = false;
                    Reminder temp = new Reminder(1, reminderName.getText().toString().replace("\n","")
                            , reminderDate.getText().toString().replace("\n","")
                            , reminderTime.getText().toString().replace("\n",""));

                    //make an intent to NotifyReceiver
                    Intent my_intient = new Intent(getActivity(), NotifyReceiver.class);

                    //with extras i'm giving away the Tittle and the Date/Time
                    my_intient.putExtra("Title", temp.getName());
                    my_intient.putExtra("DateTime", temp.getDate() + " at " + temp.getTime());
                    AlarmManager mAlarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

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
                    /*calendar.add(calendar.DAY_OF_YEAR, -1);*/
                    calendar.set(Calendar.MINUTE, time.getMinutes());
                    calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
                    calendar.set(Calendar.SECOND, 0);

                    setNewReminderIcons(false, false);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
                    String formattedDate = df.format(c.getTime());
                    String formattedTime = tf.format(c.getTime());
                    setNewRemindersData("Enter reminder title", formattedDate, formattedTime);
                    dbHandler.insert(temp);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), dbHandler.findIdByOthers(temp), my_intient, PendingIntent.FLAG_UPDATE_CURRENT);
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                    updateReminders.updateReminders(reminderRecyclerView, dbHandler);
                }
            }
        });
        reminderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   DatePickerDialog datePickerDialog =  new DatePickerDialog(getActivity(), R.style.AlertDialogCustom ,date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                    datePickerDialog.show();
            }
        });

        reminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.AlertDialogCustom,
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
        });
        return rootView;
    }

    public void setNewRemindersData(String name, String date, String time){
        reminderName.setText(name);
        reminderDate.setText(date);
        reminderTime.setText(time);
    }

    public void setNewReminderIcons(boolean visability, boolean check)
    {
        if (visability){
            reminderCloseBtn.setVisibility(View.VISIBLE);
            reminderCheckBtn.setVisibility(View.VISIBLE);
            reminderDate.setVisibility(View.VISIBLE);
            reminderTime.setVisibility(View.VISIBLE);
            reminderAddBtn.setVisibility(View.GONE);
            reminderName.setEnabled(true);
        }else{
            reminderCloseBtn.setVisibility(View.GONE);
            reminderCheckBtn.setVisibility(View.GONE);
            reminderDate.setVisibility(View.GONE);
            reminderTime.setVisibility(View.GONE);
            reminderAddBtn.setVisibility(View.VISIBLE);
            reminderName.setEnabled(false);
        }
        if (check) reminderCheckBtn.setImageResource(R.drawable.iccheckok);
        else reminderCheckBtn.setImageResource(R.drawable.iccheck);
    }

    public void updateReminderDate(){
        String myFormat = "dd-MMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String str = sdf.format(myCalendar.getTime());
        reminderDate.setText(str);
        dateChoosed = true;
        //TODO CR: Pay attention to Lint warnings. Don't write logic that doesn't make sense. [Peter]
        if ( timeChoosed ){
            reminderCheckBtn.setImageResource(R.drawable.iccheckok);
        }
    }

    public void updateTimeInReminder(){
        SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
        String str = tf.format(myCalendar.getTime());
        reminderTime.setText(str);
        timeChoosed = true;
        if (dateChoosed){
            reminderCheckBtn.setImageResource(R.drawable.iccheckok);
        }
    }
}
