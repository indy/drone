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

package io.indy.drone.event;

import android.util.Log;

import io.indy.drone.AppConfig;

public class StrikeMoveEvent {

    private static final String TAG = "StrikeMoveEvent";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private String mStrikeId;
    private String mRegion;

    public StrikeMoveEvent(String strikeId, String region) {
        mStrikeId = strikeId;
        mRegion = region;
    }

    public String getStrikeId() { return mStrikeId; }
    public String getRegion() {
        return mRegion;
    }
}
