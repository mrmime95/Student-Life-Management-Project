package com.halcyon.ubb.studentlifemanager.ui.timetable.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.ui.timetable.fragment.TimetableDayFragment;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Created by Baroti Csaba on 12/13/2016.
 */

public class DayPagerAdapter extends FragmentStatePagerAdapter {
    private Set<Group> mGroups;
    private String[] mDays;
    private ConcurrentHashMap<Integer, Fragment> mRegisteredFragments;

    public DayPagerAdapter(Context context, FragmentManager fm, Set<Group> groups) {
        super(fm);
        mDays=context.getResources().getStringArray(R.array.days);
        mGroups =groups;
        mRegisteredFragments=new ConcurrentHashMap<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //http://stackoverflow.com/questions/8785221/retrieve-a-fragment-from-a-viewpager
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return TimetableDayFragment.newInstance(mGroups,position);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDays[position];
    }

    public void setSelectedGroups(Set<Group> selectedGroups) {
        mGroups=selectedGroups;

        for (Fragment fragment:mRegisteredFragments.values())
                ( (TimetableDayFragment)fragment).setGroups(mGroups);

    }

    public void setSelectedGroups(int position, Set<Group> groups) {
        if (position>-1 && position<groups.size())
        mGroups=groups;

        Fragment fragment=mRegisteredFragments.get(position);
        if (fragment==null) return;

        TimetableDayFragment timetableFragment=(TimetableDayFragment)fragment;
        if (timetableFragment.getGroups()!=groups)
            timetableFragment.setGroups(groups);
    }
}
