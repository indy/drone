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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
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

import de.greenrobot.event.EventBus;
import io.indy.drone.AppConfig;
import io.indy.drone.R;
import io.indy.drone.event.StrikeMoveEvent;
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
public class StrikeDetailActivity extends ActionBarActivity {

    private StrikeMapHelper mStrikeMapHelper;

    private SQLDatabase mDatabase;

    private String mStrikeId;

    private String mRegion;
    private Cursor mStrikeLocations;

    private SpinnerAdapter mSpinnerAdapter;
    private ActionBar.OnNavigationListener mOnNavigationListener;

    /**
     * large screen devices in portrait mode will always have enough room to show all
     * the information in a StrikeDetailFragment without having to use the
     * fragment_strike_detail_half.xml layout
     */
    private boolean shouldAlwaysUseFlexLayout() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width > 900;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strike_detail);

        mDatabase = new SQLDatabase(this);

        configureActionBar();

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

            mRegion = getIntent().getStringExtra(SQLDatabase.REGION);
            mStrikeId = getIntent().getStringExtra(SQLDatabase.KEY_ID);

            mStrikeLocations = mDatabase.getStrikeLocationsInRegion(mRegion);


            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            StrikeDetailFragment fragment = new StrikeDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putString(SQLDatabase.KEY_ID, mStrikeId);
            bundle.putString(SQLDatabase.REGION, mRegion);
            bundle.putBoolean(StrikeDetailFragment.ALWAYS_FLEX_VIEW, shouldAlwaysUseFlexLayout());
            fragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.strike_detail_container, fragment)
                    .commit();

            mStrikeMapHelper = new StrikeMapHelper();
            showStrikeOnMap(mStrikeId, 400);
        }
    }

    private void configureActionBar() {
        // Show the Up button in the action bar.
        ActionBar actionBar = getActionBar();

        mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.locations_array,
                android.R.layout.simple_spinner_dropdown_item);

        mOnNavigationListener = new ActionBar.OnNavigationListener() {
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                return true;
            }
        };

        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
    }

    private void showStrikeOnMap(String strikeId, int detailsHeight) {

        ifd("showStrikeOnMap " + strikeId + " " + detailsHeight);

        Fragment mapFragment = (getSupportFragmentManager().findFragmentById(R.id.map));

        Strike strike = mDatabase.getStrike(strikeId);
        LatLng strikeLocation = new LatLng(strike.getLat(), strike.getLon());

        if(mStrikeMapHelper.configureMap((SupportMapFragment) mapFragment, strikeLocation, detailsHeight)) {
            mStrikeMapHelper.clearMap()
                            .showMainMarker(strike)
                            .showSurroundingMarkers(mStrikeLocations);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ifd("onStart");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        ifd("onStop");
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void onEvent(StrikeMoveEvent event) {
        ifd("received StrikeMoveEvent");
        showStrikeOnMap(event.getStrikeId(), event.getDetailsHeight());
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
                NavUtils.navigateUpTo(this, new Intent(this, StrikeListActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.strike_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    static private final boolean D = true;
    static private final String TAG = StrikeDetailActivity.class.getSimpleName();

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }
}
