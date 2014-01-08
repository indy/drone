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

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.indy.drone.AppConfig;
import io.indy.drone.R;
import io.indy.drone.model.SQLDatabase;
import io.indy.drone.model.Strike;
import io.indy.drone.utils.DateFormatHelper;
import io.indy.drone.view.FrameLayoutDetails;

/**
 * A fragment representing a single Strike detail screen.
 * This fragment is either contained in a {@link io.indy.drone.activity.StrikeListActivity}
 * in two-pane mode (on tablets) or a {@link io.indy.drone.activity.StrikeDetailActivity}
 * on handsets.
 */
public class StrikeDetailFragment extends Fragment
        implements FrameLayoutDetails.OnSizeChangeListener {

    static private final boolean D = false;
    static private final String TAG = "StrikeDetailFragment";

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    // on larger screen devices
    public static final String ALWAYS_FLEX_VIEW = "always_flex_view";

    private SQLDatabase mDatabase;
    private Cursor mCursor; // cursor to all strikes in a particular region
    private String mStrikeId;

    private Strike mStrike;

    private LinearLayout mRootView;

    private View mHalfLayout;
    private View mFlexLayout;

    private Button mPrevButton;
    private Button mNextButton;
    private Button mInfoButton;

    private boolean mAtEnd;
    private boolean mAtStart;

    private boolean mAlwaysUseFlex;

    private boolean mShouldFireMoveEvent;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StrikeDetailFragment(OnStrikeInfoListener onStrikeInfoListener) {
        mOnStrikeInfoListener = onStrikeInfoListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = new SQLDatabase(getActivity());
    }

    // cursor has just been returned by the database, so set it to the current
    private boolean setupCursor(String region, String strikeId) {
        // get the strikes for this region
        mCursor = mDatabase.getStrikeCursor(region, false);

        mAtStart = true;
        mAtEnd = false;

        // position the cursor at the current strikeId
        int index = mCursor.getColumnIndex(SQLDatabase.KEY_ID);
        while (mCursor.moveToNext()) {
            String id = mCursor.getString(index);
            if (id.equals(strikeId)) {

                // are we at the end of the cursor?
                if (mCursor.moveToNext()) {
                    // no
                    mCursor.moveToPrevious();
                } else {
                    // yes
                    mAtEnd = true;
                    mCursor.moveToLast();
                }
                return true;
            }
            mAtStart = false;
        }
        return false;
    }

    private String moveToPreviousStrike() {
        int index = mCursor.getColumnIndex(SQLDatabase.KEY_ID);
        if (mCursor.moveToPrevious()) {
            mAtEnd = false;

            // could be at the start of the cursor
            if (mCursor.moveToPrevious()) {
                // no
                mAtStart = false;
                mCursor.moveToNext();
            } else {
                // yes
                mAtStart = true;
                mCursor.moveToFirst();
            }

            return mCursor.getString(index);
        } else {
            // moved before start of results, so position cursor at start
            mAtStart = true;
            mCursor.moveToFirst();
        }
        return "";
    }

    private String moveToNextStrike() {
        int index = mCursor.getColumnIndex(SQLDatabase.KEY_ID);
        if (mCursor.moveToNext()) {
            mAtStart = false;

            // could be at the end of the cursor
            if (mCursor.moveToNext()) {
                // no
                mAtEnd = false;
                mCursor.moveToPrevious();
            } else {
                // yes
                mAtEnd = true;
                mCursor.moveToLast();
            }

            return mCursor.getString(index);
        } else {
            // moved past the end of results, so position cursor at end
            mAtEnd = true;
            mCursor.moveToLast();
        }

        return "";
    }

    private void changeStrikeClicked(String newId) {
        if (!newId.equals("")) {
            mStrikeId = newId;
            mStrike = mDatabase.getStrike(mStrikeId);
            mShouldFireMoveEvent = true;
            updateUI(mRootView);
        }
    }

    public void onHeightChanged(int h) {
        mOnStrikeInfoListener.onStrikeInfoResized(h);
    }

    private void onStrikeInfoNavigated() {
        if (mShouldFireMoveEvent) {
            mOnStrikeInfoListener.onStrikeInfoNavigated(mStrikeId);
        }
    }

    private OnStrikeInfoListener mOnStrikeInfoListener;

    public interface OnStrikeInfoListener {
        public abstract void onStrikeInfoNavigated(String strikeId);

        public abstract void onStrikeInfoResized(int height);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mStrikeId = getArguments().getString(SQLDatabase.KEY_ID);
        mStrike = mDatabase.getStrike(mStrikeId);

        mShouldFireMoveEvent = false;

        mAlwaysUseFlex = getArguments().getBoolean(ALWAYS_FLEX_VIEW, false);

        // rootView should be a layout (linearLayout) - can then add/remove child views
        mRootView = (LinearLayout) inflater.inflate(R.layout.fragment_strike_detail, container, false);

        mHalfLayout = inflater.inflate(R.layout.fragment_strike_detail_half, mRootView, false);
        mFlexLayout = inflater.inflate(R.layout.fragment_strike_detail_flex, mRootView, false);


        FrameLayoutDetails fld = (FrameLayoutDetails) (mHalfLayout.findViewById(R.id.details));
        fld.setOnSizeChangeListener(this);
        fld = (FrameLayoutDetails) (mFlexLayout.findViewById(R.id.details));
        fld.setOnSizeChangeListener(this);

        String region = getArguments().getString(SQLDatabase.REGION);
        setupCursor(region, mStrikeId);

        updateUI(mRootView);

        return mRootView;
    }

    private void updateUI(LinearLayout rootView) {

        attachAppropriateView(rootView);

        ((TextView) rootView.findViewById(R.id.strike_detail)).setText(mStrike.getBijSummaryShort());

        ((TextView) rootView.findViewById(R.id.location)).setText(mStrike.getLocation());

        String displayDate = DateFormatHelper.dateToDisplay(mStrike.getHappened());
        ((TextView) rootView.findViewById(R.id.happened)).setText(displayDate);

        ((TextView) rootView.findViewById(R.id.drone_summary)).setText(mStrike.getDroneSummary());

        configureButtons();
    }

    private void attachAppropriateView(LinearLayout rootView) {
        rootView.removeAllViews();

        // choose layout based on amount of text in strike

        String summary = mStrike.getBijSummaryShort();

        if (summary.length() < 280 || mAlwaysUseFlex) {
            ifd("full view " + summary.length());
            rootView.addView(mFlexLayout);
        } else {
            ifd("half view " + summary.length());
            rootView.addView(mHalfLayout);
        }

        // fire off a change size event
        View detailsView = mRootView.findViewById(R.id.details);

        onHeightChanged(detailsView.getHeight());
        onStrikeInfoNavigated();
    }


    private void configureButtons() {
        mPrevButton = (Button) mRootView.findViewById(R.id.btn_previous_strike);
        mNextButton = (Button) mRootView.findViewById(R.id.btn_next_strike);

        mPrevButton.setEnabled(!mAtStart);
        mNextButton.setEnabled(!mAtEnd);

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get a strike object that represents the previous strike to view
                ifd("clicked prev");
                changeStrikeClicked(moveToPreviousStrike());
                mPrevButton.setEnabled(!mAtStart);
                mNextButton.setEnabled(!mAtEnd);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get a strike object that represents the next strike to view
                ifd("clicked next");
                changeStrikeClicked(moveToNextStrike());
                mPrevButton.setEnabled(!mAtStart);
                mNextButton.setEnabled(!mAtEnd);
            }
        });


        mInfoButton = (Button) mRootView.findViewById(R.id.btn_info_link);
        mInfoButton.setEnabled(!mStrike.getInformationUrl().isEmpty());
        mInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String url = mStrike.getInformationUrl();
                ifd("clicked a button for : " + url);
                if (!url.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            }
        });
    }
}
