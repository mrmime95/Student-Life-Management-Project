package com.halcyon.ubb.studentlifemanager.ui.timetable.usersettings;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.database.DatabaseProvider;
import com.halcyon.ubb.studentlifemanager.database.listener.ValueEventSetListener;
import com.halcyon.ubb.studentlifemanager.model.timetable.Group;

import java.util.HashSet;
import java.util.Set;

import static com.halcyon.ubb.studentlifemanager.database.local.timetable.LocalTimetableDatabase.PREF_DIVIDER;

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
                .fetchGroups(new ValueEventSetListener<Group>() {
                    @Override
                    public void onChange(Set<Group> groups) {
                        Set<String> entriesSet=new HashSet<>();

                        for (Group group:groups)
                            entriesSet.add(group.getKey()+PREF_DIVIDER+group.getName());

                            PreferenceManager.getDefaultSharedPreferences(context)
                                    .edit().putStringSet("visible_timetables",entriesSet).apply();
                    }

                    @Override
                    public void onTimeout() {

                    }

                    @Override
                    public void onCancelled(Exception e) {

                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class TimetablePreferenceFragment extends PreferenceFragment {
        CharSequence[] mEntries =new CharSequence[0];
        CharSequence[] mEntrieValues =new CharSequence[0];
        MultiSelectListPreference preference;

        private ValueEventSetListener<Group> mRemoteGroupsListener=new ValueEventSetListener<Group>() {
            @Override
            public void onChange(Set<Group> groups) {
                if (groups.size()==0) return;
                mEntries =new CharSequence[groups.size()];
                mEntrieValues =new CharSequence[groups.size()];

                int i=0;
                for (Group group:groups) {
                    mEntries[i]=group.getName();
                    mEntrieValues[i++]=group.getKey()+PREF_DIVIDER+group.getName();
                }

                preference.setEntries(mEntries);
                preference.setEntryValues(mEntrieValues);

            }

            @Override
            public void onTimeout() {

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

            preference=(MultiSelectListPreference) findPreference("visible_timetables");
            preference.setPersistent(true);
            preference.setEntries(mEntries);
            preference.setEntryValues(mEntrieValues);

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
