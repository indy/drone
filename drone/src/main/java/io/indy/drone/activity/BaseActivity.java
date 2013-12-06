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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.indy.drone.Flags;
import io.indy.drone.R;
import io.indy.drone.model.Strike;

public class BaseActivity extends ActionBarActivity {

    private static final String TAG = "BaseActivity";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }

    protected GoogleMap mMap;

    protected void setUpMapIfNeeded(Strike strike) {
        LatLng location = new LatLng(strike.getLat(), strike.getLon());

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                // The Map is verified. It is now safe to manipulate the map.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 3000, null);

                showMarker(strike);
            }
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(location), 3000, null);
            showMarker(strike);
        }
    }

    protected void showMarker(Strike strike) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(strike.getLat(), strike.getLon()))
                .title(strike.getTown())
                .snippet(strike.getLocation())
                .alpha(0.8f)
                .flat(false));
    }

    protected Bundle strikeIntoBundle(Strike strike) {
        Bundle bundle = new Bundle();

        bundle.putString(Strike.BIJ_SUMMARY_SHORT, strike.getBijSummaryShort());

        ifd(strike.getInformationUrl());

        if(strike.getInformationUrl() != null) {
            bundle.putString(Strike.INFORMATION_URL, strike.getInformationUrl());
        }

        return bundle;
    }

}
