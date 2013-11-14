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

package io.indy.drone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.indy.drone.Flags;
import io.indy.drone.R;
import io.indy.drone.model.Strike;

/**
 * A fragment representing a single Strike detail screen.
 * This fragment is either contained in a {@link io.indy.drone.activity.StrikeListActivity}
 * in two-pane mode (on tablets) or a {@link io.indy.drone.activity.StrikeDetailActivity}
 * on handsets.
 */
public class StrikeDetailFragment extends Fragment {

    static private final boolean D = true;
    static private final String TAG = "StrikeDetailFragment";

    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }

    private String mSummary;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StrikeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Strike.BIJ_SUMMARY_SHORT)) {
            mSummary = getArguments().getString(Strike.BIJ_SUMMARY_SHORT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_strike_detail, container, false);

        ((TextView) rootView.findViewById(R.id.strike_detail)).setText(mSummary);

        return rootView;
    }
}
