package com.halcyon.ubb.studentlifemanager.view.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.halcyon.ubb.studentlifemanager.MySpinner;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.database.Database;
import com.halcyon.ubb.studentlifemanager.database.FirebaseDB;
import com.halcyon.ubb.studentlifemanager.database.listener.GroupsValueEventListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.view.DatabaseProvider;
import com.halcyon.ubb.studentlifemanager.view.DayPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimetableFragment extends Fragment implements DatabaseProvider, AdapterView.OnItemSelectedListener {
    private MySpinner mSpinner;
    private TabLayout mTabLayout;
    private android.support.v4.view.ViewPager mPager;
    private ArrayList<String> mGroupNames;
    private List<Group> mGroups;
    private ArrayAdapter<String> mAdapter;
    private DatabaseProvider mProvider;
    private GroupsValueEventListener mGroupsListener=new GroupsValueEventListener() {
        @Override
        public void onGroupsChange(List<Group> groups) {
            mGroups=groups;
            mGroupNames.clear();
            List<String> groupNames=new ArrayList<>();
            for (Group g:mGroups)
                groupNames.add(g.getName());
            mGroupNames.addAll(groupNames);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(Exception e) {
            mGroupNames.clear();
            mGroups.clear();
            mAdapter.notifyDataSetChanged();
            Snackbar.make(getActivity().findViewById(android.R.id.content),"Sorry couldn't load groups.",Snackbar.LENGTH_LONG).show();
        }
    };

    public TimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DatabaseProvider)
            mProvider= (DatabaseProvider) context;
        else
            throw new IllegalStateException("Parent activity must implement database provider.");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupNames =new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.main_tab_timetable, container, false);
        mTabLayout=(TabLayout)  view.findViewById(R.id.main_tabs);
        mSpinner = (MySpinner) view.findViewById(R.id.main_spinner);
        mPager=(ViewPager) view.findViewById(R.id.tab_timetable_viewpager);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mProvider.getDatabase().removeGroupsValueEventListener(mGroupsListener);
    }

    private boolean mAsked=false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mPager.setAdapter(new DayPagerAdapter(getContext(),getActivity().getSupportFragmentManager(), null));
        mTabLayout.setupWithViewPager(mPager);

        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mGroupNames);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(this);

        getDatabase().addGroupsValueEventListener(mGroupsListener);

        if (!mAsked) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
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
            mAsked=true;
        }
    }

    @Override
    public Database getDatabase() {
        return mProvider.getDatabase();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        mPager.setAdapter(new DayPagerAdapter(getContext(),getActivity().getSupportFragmentManager(),
                mGroups.get(pos).getCoursesNames()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mPager.setAdapter(new DayPagerAdapter(getContext(),getActivity().getSupportFragmentManager(),
                null));
    }
}
