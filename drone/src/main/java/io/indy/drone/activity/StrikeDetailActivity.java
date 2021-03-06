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

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import io.indy.drone.AppConfig;
import io.indy.drone.R;
import io.indy.drone.fragment.StrikeDetailFragment;
import io.indy.drone.model.SQLDatabase;
import io.indy.drone.model.Strike;
import io.indy.drone.view.StrikeMapHelper;

/**
 * An activity representing a single Strike detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StrikeListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link io.indy.drone.fragment.StrikeDetailFragment}.
 */
public class StrikeDetailActivity extends BaseActivity {

    static private final boolean D = true;
    static private final String TAG = StrikeDetailActivity.class.getSimpleName();

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private SpinnerAdapter mSpinnerAdapter;
    private ActionBar.OnNavigationListener mOnNavigationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ifd("onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strike_detail);

        mDatabase = new SQLDatabase(this);


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            ifd("savedInstanceState is null");

            mRegion = getIntent().getStringExtra(SQLDatabase.REGION);
            mStrikeId = getIntent().getStringExtra(SQLDatabase.KEY_ID);


            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            mStrikeDetailFragment = new StrikeDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putString(SQLDatabase.KEY_ID, mStrikeId);
            bundle.putString(SQLDatabase.REGION, mRegion);
            bundle.putBoolean(StrikeDetailFragment.ALWAYS_FLEX_VIEW, shouldAlwaysUseFlexLayout());
            mStrikeDetailFragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.strike_detail_container, mStrikeDetailFragment)
                    .commit();

        } else {
            ifd("savedInstanceState is not null");
            mStrikeId = savedInstanceState.getString(SQLDatabase.KEY_ID);
            mRegion = savedInstanceState.getString(SQLDatabase.REGION);
        }

        mStrikeLocations = mDatabase.getStrikeLocationsInRegion(mRegion);

        mStrikeMapHelper = new StrikeMapHelper(this);
        showStrikeOnMap(mStrikeId);

        configureActionBar();
    }

    @Override
    public void onStart() {
        ifd("onStart");
        super.onStart();
    }

    @Override
    public void onRestart() {
        ifd("onRestart");
        super.onRestart();
    }

    @Override
    public void onResume() {
        ifd("onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        ifd("onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        ifd("onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        ifd("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ifd("onSaveInstanceState");

        super.onSaveInstanceState(outState);
        outState.putString(SQLDatabase.KEY_ID, mStrikeId);
        outState.putString(SQLDatabase.REGION, mRegion);
    }

    @Override
    public void finish() {
        super.finish();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.strike_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ifd("clicked " + item);

        switch (item.getItemId()) {
            case R.id.action_settings:
                ifd("clicked on settings");
                break;
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //

                Intent upIntent = new Intent(this, StrikeListActivity.class);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(upIntent);
                } else {
                    Bundle translateBundle = ActivityOptions.makeCustomAnimation(
                            StrikeDetailActivity.this,
                            R.anim.slide_in_right, R.anim.slide_out_right).toBundle();
                    startActivity(upIntent, translateBundle);
                }

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void configureActionBar() {
        // Show the Up button in the action bar.
        ActionBar actionBar = getActionBar();

        mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.locations_array,
                android.R.layout.simple_spinner_dropdown_item);

        mOnNavigationListener = new ActionBar.OnNavigationListener() {
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                onRegionSelected(itemPosition);
                return true;
            }
        };

        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);

        try {
            actionBar.setSelectedNavigationItem(SQLDatabase.indexFromRegion(mRegion));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
