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
import android.os.Bundle;
import android.util.Log;

public class StrikeDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String s = intent.getStringExtra("STRIKE_ID");
        ifd("StrikeDetailActivity received " + s);

        setupNavigationDrawer();
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
