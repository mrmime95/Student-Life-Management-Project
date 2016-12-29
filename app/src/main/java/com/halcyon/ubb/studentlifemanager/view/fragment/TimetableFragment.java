package com.halcyon.ubb.studentlifemanager.view.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.halcyon.ubb.studentlifemanager.App;
import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.database.DatabaseProvider;
import com.halcyon.ubb.studentlifemanager.database.listener.GroupsValueEventListener;
import com.halcyon.ubb.studentlifemanager.database.listener.LocalTimetableListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;
import com.halcyon.ubb.studentlifemanager.view.DayPagerAdapter;
import com.halcyon.ubb.studentlifemanager.view.TimetableSettingsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class TimetableFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private AppCompatSpinner mSpinner;
    private TabLayout mTabLayout;
    private android.support.v4.view.ViewPager mPager;
    private ArrayList<String> mGroupNames;
    private List<Timetable> mTables;
    private ArrayAdapter<String> mAdapter;
    private DayPagerAdapter mDayPager;

    public TimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupNames = new ArrayList<>();
        mTables = new ArrayList<>();
        mTables = new ArrayList<>();
        mDayPager = new DayPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_tab_timetable, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.main_tabs);
        mSpinner = (AppCompatSpinner) view.findViewById(R.id.main_spinner);
        mPager = (ViewPager) view.findViewById(R.id.tab_timetable_viewpager);
        mPager.setOffscreenPageLimit(2);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.timetable_toolbar);

        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPager.setAdapter(mDayPager);
        mTabLayout.setupWithViewPager(mPager);

        mAdapter = new ArrayAdapter<>(getContext(), R.layout.main_spinner_textview, mGroupNames);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();

        DatabaseProvider.getInstance().getLocalTimetableDatabase()
                .readWantedTimetables(getContext(),
                        new LocalTimetableListener() {
                            @Override
                            public void onLocalTimetablesLoaded(final List<Timetable> tables) {
                                loadTables(tables);
                            }
                        });
    }

    private void loadTables(List<Timetable> tables) {
        mGroupNames.clear();

        mTables=tables;

        Collections.sort(mTables, new Comparator<Timetable>() {
            @Override
            public int compare(Timetable a, Timetable b) {
                return a.getName().compareTo(b.getName());
            }
        });

        for (Timetable table : tables) {
            mGroupNames.add(table.getName());
        }

        mAdapter.notifyDataSetChanged();

        int pos = mSpinner.getSelectedItemPosition();
        if (pos > -1 && pos < mGroupNames.size())
            onItemSelected(null, null, mSpinner.getSelectedItemPosition(), 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        final Timetable table = mTables.get(pos);
        DatabaseProvider.getInstance().getRemoteDatabase()
                .validateKeysOnGroups(table.getGroups(),
                        new GroupsValueEventListener() {
                            @Override
                            public void onGroupsChange(Set<Group> groups) {
                                if (groups==null) {
                                    //timtable is deleted
                                    mTables.remove(table);
                                    mGroupNames.remove(table.getName());
                                    mDayPager.setSelectedGroups(null);
                                }
                                else
                                    mDayPager.setSelectedGroups(new ArrayList<>(groups));

                                mPager.setAdapter(mDayPager);
                            }

                            @Override
                            public void onCancelled(Exception e) {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), "Sorry couldn't sync local groups.", Snackbar.LENGTH_LONG).show();
                            }
                        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mPager.setAdapter(new DayPagerAdapter(getContext(), getActivity().getSupportFragmentManager(),
                null));
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (getContext() instanceof AppCompatActivity)
            ((AppCompatActivity) getContext()).setSupportActionBar(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.timetable_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_timetable_action_test:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Do you want to upload test data?");
                alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseProvider.getInstance().getRemoteDatabase().createTestData();
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
                                DatabaseProvider.getInstance().getRemoteDatabase().deleteTestData();
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
                break;
            case R.id.menu_timetable_action_settings:
                Intent intent = new Intent(getContext(), TimetableSettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

}
