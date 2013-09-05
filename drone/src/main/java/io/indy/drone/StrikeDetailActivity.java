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
import android.os.Bundle;
import android.util.Log;

import io.indy.drone.model.SQLDatabase;

public class StrikeDetailActivity extends BaseActivity {

    public static final String STRIKE_ID = "STRIKE_ID";

    private SQLDatabase mDatabase;
    private String mStrikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // call this in onStart?
        Intent intent = getIntent();
        mStrikeId = intent.getStringExtra(STRIKE_ID);

        setupNavigationDrawer();
    }

    @Override
    public void onStart() {
        super.onStart();
        ifd("onStart");

        mDatabase = new SQLDatabase(getApplicationContext());

        Cursor c = mDatabase.getDetailedStrikeCursor(mStrikeId);
        c.moveToFirst();

        // get the data from the cursor
        int ci = c.getColumnIndex(SQLDatabase.BIJ_SUMMARY_SHORT);
        String s = c.getString(ci);

        ifd(s);

        c.close();
    }

    @Override
    protected void onDrawerItemClicked(int position) {
        super.onDrawerItemClicked(position);

        mDrawerLayout.closeDrawer(mDrawerList);
    }


    private static final String TAG = "StrikeDetailActivity";
    private static final boolean D = true;
    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }
}
