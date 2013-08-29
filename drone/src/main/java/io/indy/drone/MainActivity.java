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

package io.indy.drone;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import de.greenrobot.event.EventBus;
import io.indy.drone.adapter.StrikeCursorAdapter;
import io.indy.drone.event.UpdatedDatabaseEvent;
import io.indy.drone.model.SQLDatabase;

public class MainActivity extends ActionBarActivity {

    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private SQLDatabase mDatabase;

    private StrikeCursorAdapter mStrikeCursorAdapter;
    private ListView mListView;

    public SQLDatabase getDatabase() {
        return mDatabase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        mDatabase = new SQLDatabase(getApplicationContext());

        setupNavigationDrawer();

        Cursor c = mDatabase.getStrikeCursor();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mStrikeCursorAdapter = new StrikeCursorAdapter(this, c, 0);
        } else {
            mStrikeCursorAdapter = new StrikeCursorAdapter(this, c);
        }

        mListView = (ListView) findViewById(R.id.listViewNews);
        mListView.setAdapter(mStrikeCursorAdapter);

        if(mDatabase.hasData()) {
            setSupportProgressBarIndeterminateVisibility(false);
        } else {
            setSupportProgressBarIndeterminateVisibility(true);
        }

    }

    private void setupNavigationDrawer() {
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
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles[position]);

        String[] countries = {"worldwide", "Pakistan", "Yemen", "Somalia"};
        if(position == 0) {
            updateStrikeCursor();
        } else {
            updateStrikeCursor(countries[position]);
        }

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onDestroy() {
        ifd("onDestroy");

        CursorAdapter ca = (CursorAdapter)(mListView.getAdapter());
        ca.getCursor().close();

        mDatabase.closeDatabase();
        super.onDestroy();
    }
/*
    private void setupTabs() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, pager);

        mTabsAdapter.addTab("News", NewsFragment.class);
        mTabsAdapter.addTab("Map", MapFragment.class);
        mTabsAdapter.addTab("Stats", StatsFragment.class);
    }
*/
    @Override
    public void onStart() {
        super.onStart();
        ifd("onStart");

        EventBus.getDefault().registerSticky(this);

        if(EventBus.getDefault().getStickyEvent(UpdatedDatabaseEvent.class) != null) {
            // the database has been created for the first time
            // the async task has loaded up the strikes in the asset's json file
            // so update the adapter
            // note: this code is here in case the async task has finished before onStart is called
            setSupportProgressBarIndeterminateVisibility(false);
            updateStrikeCursor();
            EventBus.getDefault().removeStickyEvent(UpdatedDatabaseEvent.class);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ifd("onStop");
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void onEvent(UpdatedDatabaseEvent event) {
        ifd("received UpdatedDatabaseEvent");

        setSupportProgressBarIndeterminateVisibility(false);
        updateStrikeCursor();
    }

    private void updateStrikeCursor() {
        Cursor cursor = mDatabase.getStrikeCursor();
        mStrikeCursorAdapter.changeCursor(cursor);
        mStrikeCursorAdapter.notifyDataSetChanged();
    }

    private void updateStrikeCursor(String country) {
        Cursor cursor = mDatabase.getStrikeCursor(country);
        mStrikeCursorAdapter.changeCursor(cursor);
        mStrikeCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
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
        }
        return super.onOptionsItemSelected(item);
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

    static private final boolean D = true;
    static private final String TAG = MainActivity.class.getSimpleName();
    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }
}
