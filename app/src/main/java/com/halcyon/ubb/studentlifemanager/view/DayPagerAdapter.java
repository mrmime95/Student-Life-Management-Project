package com.halcyon.ubb.studentlifemanager.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.view.fragment.TimetableDayFragment;

import java.util.List;

/**
 *
 * Created by Baroti Csaba on 12/13/2016.
 */

public class DayPagerAdapter extends FragmentStatePagerAdapter {
    private final List<String> mCoursesKey;
    private String[] mDays;

    public DayPagerAdapter(Context context, FragmentManager fm, List<String> courses) {
        super(fm);
        mDays=context.getResources().getStringArray(R.array.days);
        mCoursesKey =courses;
    }

    @Override
    public Fragment getItem(int position) {
        if (mCoursesKey !=null)
            return TimetableDayFragment.newInstance(mCoursesKey,position);
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
}
