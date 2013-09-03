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

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import de.greenrobot.event.EventBus;
import io.indy.drone.adapter.StrikeCursorAdapter;
import io.indy.drone.event.UpdatedDatabaseEvent;
import io.indy.drone.model.SQLDatabase;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private SQLDatabase mDatabase;

    private StrikeCursorAdapter mStrikeCursorAdapter;
    private ListView mListView;

    public SQLDatabase getDatabase() {
        return mDatabase;
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, StrikeDetailActivity.class);
        intent.putExtra("STRIKE_ID", (String) v.getTag());
        startActivity(intent);
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

    @Override
    protected void onDrawerItemClicked(int position) {
        super.onDrawerItemClicked(position);

        String[] countries = {"worldwide", "Pakistan", "Yemen", "Somalia"};
        if(position == 0) {
            updateStrikeCursor();
        } else {
            updateStrikeCursor(countries[position]);
        }

        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public void onDestroy() {
        ifd("onDestroy");

        CursorAdapter ca = (CursorAdapter)(mListView.getAdapter());
        ca.getCursor().close();

        mDatabase.closeDatabase();
        super.onDestroy();
    }

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

    static private final boolean D = true;
    static private final String TAG = MainActivity.class.getSimpleName();
    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }
}
