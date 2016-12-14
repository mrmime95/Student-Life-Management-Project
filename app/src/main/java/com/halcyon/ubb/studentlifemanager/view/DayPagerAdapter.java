package com.halcyon.ubb.studentlifemanager.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.view.fragment.TimetableDayFragment;

/**
 *
 * Created by Baroti Csaba on 12/13/2016.
 */

class DayPagerAdapter extends FragmentStatePagerAdapter {
    private final String mCourse;
    private String[] mDays;

    DayPagerAdapter(Context context, FragmentManager fm, String courseID) {
        super(fm);
        mDays=context.getResources().getStringArray(R.array.days);
        mCourse=courseID;
    }

    @Override
    public Fragment getItem(int position) {
        return TimetableDayFragment.newInstance(mCourse,position);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDays[position];
    }
}
