package com.halcyon.ubb.studentlifemanager.ui.timetable.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.ui.timetable.fragment.TimetableDayFragment;

import java.util.ArrayList;

/**
 *
 * Created by Baroti Csaba on 12/13/2016.
 */

public class DayPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Group> mGroups;
    private String[] mDays;

    public DayPagerAdapter(Context context, FragmentManager fm, ArrayList<Group> groups) {
        super(fm);
        mDays=context.getResources().getStringArray(R.array.days);
        mGroups =groups;
    }

    @Override
    public Fragment getItem(int position) {
        if (mGroups !=null && mGroups.size()!=0)
            return TimetableDayFragment.newInstance(mGroups,position);
        else
            return new Fragment();
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDays[position];
    }

    public void setSelectedGroups(ArrayList<Group> selectedGroups) {
        mGroups =selectedGroups;
    }
}
