package com.halcyon.ubb.studentlifemanager.ui.timetable.usersettings;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.halcyon.ubb.studentlifemanager.R;

/**
 * Created by Baroti Csaba on 12/27/2016.
 */

public class PlaceHolderPreference extends Preference {
    public PlaceHolderPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PlaceHolderPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PlaceHolderPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceHolderPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        LayoutInflater li = (LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        return li.inflate( R.layout.pref_placeholder, parent, false);
    }
}
