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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.greenrobot.event.EventBus;
import io.indy.drone.Flags;
import io.indy.drone.R;
import io.indy.drone.fragment.StrikeDetailFragment;
import io.indy.drone.fragment.StrikeListFragment;
import io.indy.drone.event.UpdatedDatabaseEvent;


/**
 * An activity representing a list of Strikes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link io.indy.drone.activity.StrikeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link io.indy.drone.fragment.StrikeListFragment} and the item details
 * (if present) is a {@link io.indy.drone.fragment.StrikeDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link io.indy.drone.fragment.StrikeListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class StrikeListActivity extends BaseActivity
        implements StrikeListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strike_list);

        setupNavigationDrawer();

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

    @Override
    protected void onDrawerItemClicked(int position) {
        super.onDrawerItemClicked(position);

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
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(StrikeDetailFragment.ARG_ITEM_ID, id);
            StrikeDetailFragment fragment = new StrikeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.strike_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, StrikeDetailActivity.class);
            detailIntent.putExtra(StrikeDetailFragment.ARG_ITEM_ID, id);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    static private final boolean D = true;
    static private final String TAG = StrikeListActivity.class.getSimpleName();
    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }
}
