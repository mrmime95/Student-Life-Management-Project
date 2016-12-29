package com.halcyon.ubb.studentlifemanager.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.database.DatabaseProvider;
import com.halcyon.ubb.studentlifemanager.database.listener.GroupsValueEventListener;
import com.halcyon.ubb.studentlifemanager.database.listener.LocalTimetableListener;
import com.halcyon.ubb.studentlifemanager.database.listener.OperationCompleteListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;
import com.halcyon.ubb.studentlifemanager.model.timetable.Timetable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.halcyon.ubb.studentlifemanager.database.LocalTimetableDatabase.PREF_DIVIDER;

public class TimetableSettingsActivity extends AppCompatPreferenceActivity {

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBar();

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new TimetablePreferenceFragment())
                .commit();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || TimetablePreferenceFragment.class.getName().equals(fragmentName);
    }

    public static void initSettings(Context context) {
            initVisibleTimetables(context);
    }

    private static void initVisibleTimetables(final Context context) {
        DatabaseProvider.getInstance().getRemoteDatabase()
                .fetchGroups(new GroupsValueEventListener() {
                    @Override
                    public void onGroupsChange(Set<Group> groups) {
                        Set<String> entriesSet=new HashSet<>();

                        for (Group group:groups)
                            entriesSet.add(group.getKey()+PREF_DIVIDER+group.getName());

                            PreferenceManager.getDefaultSharedPreferences(context)
                                    .edit().putStringSet("visible_timetables",entriesSet).apply();
                    }

                    @Override
                    public void onCancelled(Exception e) {

                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class TimetablePreferenceFragment extends PreferenceFragment {
        private GroupsValueEventListener mRemoteGroupsListener=new GroupsValueEventListener() {
            @Override
            public void onGroupsChange(Set<Group> groups) {
                MultiSelectListPreference preference= (MultiSelectListPreference) findPreference("visible_timetables");
                CharSequence[] entries=new CharSequence[groups.size()];
                CharSequence[] entrieValues=new CharSequence[groups.size()];

                int i=0;
                for (Group group:groups) {
                    entries[i]=group.getName();
                    entrieValues[i++]=group.getKey()+PREF_DIVIDER+group.getName();
                }

                preference.setPersistent(true);
                preference.setEntries(entries);
                preference.setEntryValues(entrieValues);
            }

            @Override
            public void onCancelled(Exception e) {

            }
        };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_timetable_main);
            setHasOptionsMenu(true);

            DatabaseProvider.getInstance().getRemoteDatabase()
                    .addGroupsValueEventListener(mRemoteGroupsListener);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            DatabaseProvider.getInstance().getRemoteDatabase()
                    .removeGroupsValueEventListener(mRemoteGroupsListener);
        }


        @Override
        public void onResume() {
            super.onResume();

            DatabaseProvider.getInstance().getLocalTimetableDatabase()
                    .readLocalTimetables(getActivity(),
                            new LocalTimetableListener() {
                                @Override
                                public void onLocalTimetablesLoaded(List<Timetable> list) {
                                    PreferenceCategory localTimetables = (PreferenceCategory)findPreference("local_timetables");

                                    if (list.size()==0) {
                                        PlaceHolderPreference pref = new PlaceHolderPreference(getActivity());
                                        pref.setTitle("No local timetable");
                                        pref.setSummary("Please create a local timetable.");
                                        pref.setSelectable(false);
                                        localTimetables.addPreference(pref);
                                    }
                                    else{
                                        for (Timetable table : list) {
                                            Preference preference=new Preference(getActivity());
                                            preference.setTitle(table.getName());
                                            Intent intent=new Intent(getActivity(),LocalTimetableSettingsActivity.class);
                                            intent.putExtra(LocalTimetableSettingsActivity.PARAM_TIMETABLE,table);
                                            preference.setIntent(intent);
                                            localTimetables.addPreference(preference);
                                        }
                                    }

                                }
                            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = super.onCreateView(inflater, container, savedInstanceState);

            FloatingActionButton fab=new FloatingActionButton(v.getContext());
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addNewLocalTimetable();
                }
            });

            return v;
        }

        private void addNewLocalTimetable() {
            final Timetable table=new Timetable("Default timetable title");
            DatabaseProvider.getInstance().getLocalTimetableDatabase()
                    .writeLocalTimetable(getActivity(),table,
                            new OperationCompleteListener() {
                                @Override
                                public void onComplete() {
                                    Intent intent=new Intent(getActivity(),LocalTimetableSettingsActivity.class);
                                    intent.putExtra(LocalTimetableSettingsActivity.PARAM_TIMETABLE,table);

                                }

                                @Override
                                public void onFaliure() {

                                }
                            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), TimetableSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
