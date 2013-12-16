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

package io.indy.drone.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.indy.drone.Flags;
import io.indy.drone.model.Strike;

public class StrikeMapHelper {

    private static final String TAG = "StrikeMapHelper";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }

    protected GoogleMap mMap;
    protected View mMapView;
    protected LatLng mStrikeLocation;

    public boolean showStrikeOnMap(SupportMapFragment supportMapFragment, Strike strike) {

        mStrikeLocation = new LatLng(strike.getLat(), strike.getLon());

        if (!configureMap(supportMapFragment)) {
            return false;
        }

        showMarker(strike);
        // todo: also show nearby markers

        return true;
    }

    private boolean configureMap(SupportMapFragment supportMapFragment) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            // SupportMapFragment xf = (SupportMapFragment)(getSupportFragmentManager().findFragmentById(R.id.map));

            mMap = supportMapFragment.getMap();
            //mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            // Check if we were successful in obtaining the map.
            if (mMap == null) {
                return false;
            }

            mMapView = supportMapFragment.getView();
            //mMapView = findViewById(R.id.map);
            //mMapView = (getSupportFragmentManager().findFragmentById(R.id.map)).getView();

            ViewTreeObserver vto = mMapView.getViewTreeObserver();
            ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // bottom half of map will have an information overlay
                    //
                    mMap.setPadding(0, 0, 0, mMapView.getHeight() / 2);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mStrikeLocation, 8));

                    ViewTreeObserver vto = mMapView.getViewTreeObserver();
                    vto.removeGlobalOnLayoutListener(this);
                }
            };
            vto.addOnGlobalLayoutListener(listener);
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(mStrikeLocation), 3000, null);
        }
        return true;
    }


    private void showMarker(Strike strike) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(strike.getLat(), strike.getLon()))
                .title(strike.getTown())
                .snippet(strike.getLocation())
                .alpha(0.8f)
                .flat(false));
    }

    public Bundle strikeIntoBundle(Strike strike) {
        Bundle bundle = new Bundle();

        bundle.putString(Strike.BIJ_SUMMARY_SHORT, strike.getBijSummaryShort());

        ifd(strike.getInformationUrl());

        if (strike.getInformationUrl() != null) {
            bundle.putString(Strike.INFORMATION_URL, strike.getInformationUrl());
        }

        return bundle;
    }
}
