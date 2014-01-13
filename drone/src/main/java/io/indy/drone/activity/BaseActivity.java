/*
 * Copyright 2014 Inderjit Gill
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

import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import io.indy.drone.AppConfig;
import io.indy.drone.R;
import io.indy.drone.fragment.StrikeDetailFragment;
import io.indy.drone.model.SQLDatabase;
import io.indy.drone.model.Strike;
import io.indy.drone.view.StrikeMapHelper;

public class BaseActivity extends ActionBarActivity implements
        StrikeDetailFragment.OnStrikeInfoListener,
        StrikeMapHelper.OnMarkerClickListener {

    private static final String TAG = "BaseActivity";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    protected StrikeDetailFragment mStrikeDetailFragment;

    protected StrikeMapHelper mStrikeMapHelper;
    protected Cursor mStrikeLocations;

    protected SQLDatabase mDatabase;
    protected String mStrikeId;
    protected String mRegion;

    // for interface OnMarkerClickListener
    public void onMarkerClick(String strikeId) {
        // this will update the StrikeDetailFragment
        mStrikeDetailFragment.showStrikeDetail(strikeId);
        showStrikeOnMap(strikeId);
    }


    private void changeMapPadding(int padding) {
        Fragment mapFragment = (getSupportFragmentManager().findFragmentById(R.id.map));
        mStrikeMapHelper.setMapPadding((SupportMapFragment) mapFragment, padding);
    }

    // for interface OnStrikeInfoListener
    public void onStrikeInfoNavigated(String strikeId) {
        showStrikeOnMap(strikeId);
    }
    public void onStrikeInfoResized(int height) {
        ifd("height");
        changeMapPadding(height);
    }


    /**
     * user has selected a region from the spinner on the action bar
     *
     */
    protected void onRegionSelected(int itemPosition) {

        ifd("onRegionSelected");

        try {
            // new region == old region -> do nothing
            if(itemPosition == SQLDatabase.indexFromRegion(mRegion)) {
                return;
            }
            mRegion = SQLDatabase.regionFromIndex(itemPosition);

            // is mStrikeId also in the new region?
            // true if we're switching to worldwide view (itemPosition == 0)
            // or if we're going from worldwide to a region that the current strike is from
            Strike strike = mDatabase.getStrike(mStrikeId);
            boolean isCurrentStrikeInNewRegion = itemPosition == 0 || strike.getCountry().equals(mRegion);

            // show markers for the new region
            mStrikeLocations = mDatabase.getStrikeLocationsInRegion(mRegion);

            if(!isCurrentStrikeInNewRegion) {
                // set strikeId to the most recent strike in the new region
                ifd("getting most recent strike in " + mRegion);
                mStrikeId = mDatabase.getRecentStrikeIdInRegion(mRegion);
            } else {
                ifd("existing strike already took place in the new region: " + mRegion);
            }

            // create a new StrikeDetailFragment and StrikeMapHelper
            StrikeDetailFragment fragment = new StrikeDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putString(SQLDatabase.KEY_ID, mStrikeId);
            bundle.putString(SQLDatabase.REGION, mRegion);
            bundle.putBoolean(StrikeDetailFragment.ALWAYS_FLEX_VIEW, shouldAlwaysUseFlexLayout());
            fragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.strike_detail_container, fragment)
                    .commit();

            mStrikeMapHelper = new StrikeMapHelper(this);

            showStrikeOnMap(mStrikeId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void showStrikeOnMap(String strikeId) {
        ifd("showStrikeOnMap");

        ifd("strikeId = " + strikeId);
        mStrikeId = strikeId;

        if(mStrikeLocations == null) {
            ifd("null strikeLocations");
        } else {
            ifd("non null strikelocations");
        }

        Fragment mapFragment = (getSupportFragmentManager().findFragmentById(R.id.map));

        Strike strike = mDatabase.getStrike(strikeId);
        LatLng strikeLocation = new LatLng(strike.getLat(), strike.getLon());

        if(mStrikeMapHelper.configureMap((SupportMapFragment) mapFragment, strikeLocation)) {
            mStrikeMapHelper.clearMap()
                    .showMainMarker(strike)
                    .showSurroundingMarkers(mStrikeLocations);
        }
    }

    /**
     * large screen devices in portrait mode will always have enough room to show all
     * the information in a StrikeDetailFragment without having to use the
     * fragment_strike_detail_half.xml layout
     */
    protected boolean shouldAlwaysUseFlexLayout() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width > 900;
    }
}
