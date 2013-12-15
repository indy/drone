/*
 * Copyright 2013 Inderjit Gill
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.indy.drone.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Date;

import io.indy.drone.Flags;
import io.indy.drone.R;
import io.indy.drone.fragment.StrikeDetailFragment;
import io.indy.drone.fragment.StrikeListFragment;
import io.indy.drone.model.SQLDatabase;
import io.indy.drone.model.Strike;
import io.indy.drone.service.ScheduledService;
import io.indy.drone.utils.DateFormatHelper;


/**
 * An activity representing a list of Strikes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link io.indy.drone.activity.StrikeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link io.indy.drone.fragment.StrikeListFragment} and the item details
 * (if present) is a {@link io.indy.drone.fragment.StrikeDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link io.indy.drone.fragment.StrikeListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class StrikeListActivity extends BaseActivity
        implements StrikeListFragment.Callbacks {

    static private final boolean D = true;
    static private final String TAG = StrikeListActivity.class.getSimpleName();

    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }

    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;


    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private SQLDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strike_list);

        mDatabase = new SQLDatabase(this);

        setupNavigationDrawer();
        setupAlarm();

        if (findViewById(R.id.strike_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((StrikeListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.strike_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    private void setupAlarm() {

        // check a SharedPreferences variable to see if we've already setup an inexact alarm
        // otherwise the time until the next alarm will reset every time StrikeListActivity
        // is used.
        // var: time that the alarm was last triggered
        // if this was 2-3 hours ago the alarm was cancelled and requires a reset

        boolean requireAlarm = false;
        Date today = new Date();

        SharedPreferences settings = getSharedPreferences(ScheduledService.PREFS_FILENAME, 0);
        String alarmSetAt = settings.getString(ScheduledService.ALARM_SET_AT, "");
        if(alarmSetAt.isEmpty()) {
            // first time of running, so check the server and set an alarm
            startService(new Intent(this, ScheduledService.class));
            ifd("no alarm prefs found, assuming first time run, setting alarm");
            requireAlarm = true;
        } else {
            Date d = DateFormatHelper.parseSQLiteDateString(alarmSetAt);
            long diffMs = today.getTime() - d.getTime();
            long threeHours = 1000 * 60 * 60 * 3;
            if (diffMs > threeHours) {
                ifd("alarm was set more than 3 hours ago, and so requires a reset");
                requireAlarm = true;
            }
        }

        if(!requireAlarm) {
            ifd("no need to set an alarm from StrikeListActivity");
            return;
        }

        Intent intent = new Intent(this, ScheduledService.class);
        pi = PendingIntent.getService(this, 0, intent, 0);

        am = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE));
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                AlarmManager.INTERVAL_HOUR,
                pi);

        ifd("set alarm");

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(ScheduledService.ALARM_SET_AT, DateFormatHelper.dateToSQLite(today));
        editor.commit();
        ifd("updated ALARM_SET_AT shared preference");
    }

    /* from MainActivity
    @Override
    public void onDestroy() {
        ifd("onDestroy");

        CursorAdapter ca = (CursorAdapter)(mListView.getAdapter());
        ca.getCursor().close();

        mDatabase.closeDatabase();
        super.onDestroy();
    }
    */

    protected void onDrawerItemClicked(int position) {
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles[position]);

        ((StrikeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.strike_list))
                .onRegionClicked(position);

        mDrawerLayout.closeDrawer(mDrawerList);
    }


    /**
     * Callback method from {@link StrikeListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {


        if (mTwoPane) {
            Strike strike = mDatabase.getStrike(id);

            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            Bundle arguments = strikeIntoBundle(strike);

            StrikeDetailFragment fragment = new StrikeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.strike_detail_container, fragment)
                    .commit();

            setUpMapIfNeeded(strike);

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, StrikeDetailActivity.class);
            detailIntent.putExtra(SQLDatabase.KEY_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ifd("clicked " + item);

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                ifd("clicked on settings");
                break;
            case R.id.temp_action_test:
                ifd("clicked on temp action test");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    protected void setupNavigationDrawer() {
        mTitle = mDrawerTitle = getTitle();
        mDrawerTitles = getResources().getStringArray(R.array.locations_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            ifd("selected " + position);
            onDrawerItemClicked(position);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
}
