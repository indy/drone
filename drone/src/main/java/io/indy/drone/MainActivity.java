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

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.indy.drone.adapter.TabsAdapter;
import io.indy.drone.fragment.MapFragment;
import io.indy.drone.fragment.NewsFragment;
import io.indy.drone.fragment.StatsFragment;
import io.indy.drone.model.SQLDatabase;

public class MainActivity extends ActionBarActivity {

    static private final boolean D = true;
    static private final String TAG = MainActivity.class.getSimpleName();

    private SQLDatabase mDatabase;

    static void ifd(final String message) {
        if (D) Log.d(TAG, message);
    }

    TabsAdapter mTabsAdapter;

    public SQLDatabase getDatabase() {
        return mDatabase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_main);

        mDatabase = new SQLDatabase(getApplicationContext());

        ifd("MainActivity hashcode " + System.identityHashCode(this));

        setupTabs();
    }

    @Override
    public void onDestroy() {
        ifd("onDestroy");
        mDatabase.closeDatabase();
        super.onDestroy();
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ifd("clicked " + item);

        switch (item.getItemId()) {
            case R.id.action_settings:
                ifd("clicked on settings");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}