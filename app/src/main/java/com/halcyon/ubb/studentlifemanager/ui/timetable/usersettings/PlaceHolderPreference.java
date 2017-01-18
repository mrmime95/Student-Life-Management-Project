package com.halcyon.ubb.studentlifemanager.ui.timetable.usersettings;

import android.content.Context;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.halcyon.ubb.studentlifemanager.R;

/**
 *
 * Created by Baroti Csaba on 12/27/2016.
 */

class PlaceHolderPreference extends Preference {
    PlaceHolderPreference(Context context) {
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
