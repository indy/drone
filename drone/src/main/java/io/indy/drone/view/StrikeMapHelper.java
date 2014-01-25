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

import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.indy.drone.AppConfig;
import io.indy.drone.R;
import io.indy.drone.model.SQLDatabase;
import io.indy.drone.model.Strike;

public class StrikeMapHelper {

    private static final String SNIPPET_ID_PREFIX = "id:";

    private static final String TAG = "StrikeMapHelper";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public interface OnMarkerClickListener {
        public abstract void onMarkerClick(String strikeId);
    }

    protected GoogleMap mMap;
    protected View mMapView;
    protected LatLng mStrikeLocation;

    private int mDetailsHeight;

    private OnMarkerClickListener mOnMarkerClickListener;


    public StrikeMapHelper(OnMarkerClickListener onMarkerClickListener) {
        mOnMarkerClickListener = onMarkerClickListener;
    }

    public StrikeMapHelper clearMap() {
        mMap.clear();
        return this;
    }

    public StrikeMapHelper showSurroundingMarkers(Cursor cursor) {

        int keyIdIndex = cursor.getColumnIndex(SQLDatabase.KEY_ID);
        int latIndex = cursor.getColumnIndex(Strike.LAT);
        int lonIndex = cursor.getColumnIndex(Strike.LON);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.aux_map_marker);

        cursor.moveToFirst();
        do {
            mMap.addMarker(new MarkerOptions()
                    .icon(icon)
                    .anchor(0.5f, 0.5f)
                    .snippet(SNIPPET_ID_PREFIX + cursor.getString(keyIdIndex))
                    .position(new LatLng(cursor.getDouble(latIndex),
                            cursor.getDouble(lonIndex)))
                    .alpha(0.6f));

        } while(cursor.moveToNext());

        return this;
    }

    public StrikeMapHelper showMainMarker(Strike strike) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(strike.getLat(), strike.getLon()))
                .title(strike.getTown())
                .snippet(strike.getLocation())
                .alpha(0.8f)
                .flat(false));
        return this;
    }

    public void setMapPadding(SupportMapFragment supportMapFragment, int detailsHeight) {

        mDetailsHeight = detailsHeight;

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            mMap = supportMapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap == null) {
                return;
            }

            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);

            mMapView = supportMapFragment.getView();

            ViewTreeObserver vto = mMapView.getViewTreeObserver();
            ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // bottom half of map will have an information overlay
                    //
                    mMap.setPadding(0, 0, 0, mDetailsHeight);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mStrikeLocation, 8));


                    ViewTreeObserver vto = mMapView.getViewTreeObserver();
                    vto.removeGlobalOnLayoutListener(this);
                }
            };
            vto.addOnGlobalLayoutListener(listener);
        } else {
            mMap.setPadding(0, 0, 0, mDetailsHeight);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(mStrikeLocation), 3000, null);


            /*
            ViewTreeObserver vto = mMapView.getViewTreeObserver();
            ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ifd("-------");
                    ifd("mapView.getHeight() = " + mMapView.getHeight());
                    ifd("mDetailsHeight = " + mDetailsHeight);

                    mMap.setPadding(0, 0, 0, mMapView.getHeight() - mDetailsHeight);
                    ViewTreeObserver vto = mMapView.getViewTreeObserver();
                    vto.removeGlobalOnLayoutListener(this);
                }
            };
            vto.addOnGlobalLayoutListener(listener);
            */
        }
    }

    public boolean configureMap(SupportMapFragment supportMapFragment, LatLng location) {

        mStrikeLocation = location;

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            mMap = supportMapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap == null) {
                return false;
            }

            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);

            mMapView = supportMapFragment.getView();

            ViewTreeObserver vto = mMapView.getViewTreeObserver();
            ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // bottom half of map will have an information overlay
                    //
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mStrikeLocation, 8));

                    ViewTreeObserver vto = mMapView.getViewTreeObserver();
                    vto.removeGlobalOnLayoutListener(this);
                }
            };
            vto.addOnGlobalLayoutListener(listener);
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(mStrikeLocation), 3000, null);
            /*
            ViewTreeObserver vto = mMapView.getViewTreeObserver();
            ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ifd("-------");
                    ifd("mapView.getHeight() = " + mMapView.getHeight());
                    ifd("mDetailsHeight = " + mDetailsHeight);

                    mMap.setPadding(0, 0, 0, mMapView.getHeight() - mDetailsHeight);
                    ViewTreeObserver vto = mMapView.getViewTreeObserver();
                    vto.removeGlobalOnLayoutListener(this);
                }
            };
            vto.addOnGlobalLayoutListener(listener);
            */
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ifd("clicked on a marker with snippet: " + marker.getSnippet());

                // the currently selected strike location will have the
                // 'classic' google maps marker on it. If the user
                // clicks on that this callback will be triggered
                // and the value of getSnippet() won't be the key id for
                // the strike.
                //
                String snippet = marker.getSnippet();
                int prefixLength = SNIPPET_ID_PREFIX.length();
                if(snippet.substring(0, prefixLength).equals(SNIPPET_ID_PREFIX)) {
                    String id = snippet.substring(prefixLength);
                    ifd("snippet contains id = " + id);
                    mOnMarkerClickListener.onMarkerClick(id);
                    return true;
                }
                return false;
            }
        });

        return true;
    }
}
