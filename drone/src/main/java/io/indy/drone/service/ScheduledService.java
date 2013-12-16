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

package io.indy.drone.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import de.greenrobot.event.EventBus;
import io.indy.drone.Flags;
import io.indy.drone.event.UpdatedDatabaseEvent;
import io.indy.drone.model.SQLDatabase;
import io.indy.drone.model.Strike;
import io.indy.drone.utils.DateFormatHelper;

public class ScheduledService extends IntentService {

    private static final String TAG = "ScheduledService";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }

    public static final String PREFS_FILENAME = "MyPrefsFile";
    public static final String ALARM_SET_AT = "alarm_set_at";

    private final String SERVER_URL = "http://indy.io/drone/cache/";

    private SQLDatabase mDatabase;


    public ScheduledService() {
        super("ScheduledService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ifd("onHandleIntent");

        mDatabase = new SQLDatabase(this);

        // called by a repeating alarm so update sharedpreferences with the current time
        updateAlarmTime();

        try {
            int serverCount = fetchStrikesCount();
            int localCount = mDatabase.getNumStrikes();

            if (localCount == 0) {
                ifd("localCount == 0");
                return;
            }

            if (localCount < serverCount) {
                // there's new strike data in the server
                ifd("new strike data found on server");
                JSONArray jsonStrikes = fetchNewStrikes(localCount);
                if (jsonStrikes == null) {
                    ifd("fetching new strike data returned null");
                    return;
                }

                ifd("adding strike data to local db");
                addStrikes(jsonStrikes);

                // event to inform list that new strike data is in the db
                EventBus.getDefault().post(new UpdatedDatabaseEvent());

            } else {
                ifd("serverCount (" + serverCount + ") not greater than localCount (" + localCount + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAlarmTime() {
        Context ctx = getApplicationContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
        Date today = new Date();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(ScheduledService.ALARM_SET_AT, DateFormatHelper.dateToSQLite(today));
        editor.commit();
    }

    private String httpGet(String path) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(path);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();

            c.setRequestMethod("GET");
            c.setReadTimeout(15000);
            c.connect();

            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buf.append(line + "\n");
            }

            return buf.toString();

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private int fetchStrikesCount() throws Exception {
        try {
            String data = httpGet(SERVER_URL + "/strikes-count.json");
            return Integer.parseInt(data.replaceAll("\\s+", "")); // strip whitespace
        } catch (IOException e) {
            e.printStackTrace();
            throw (e);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw (e);
        }
    }

    private JSONArray fetchNewStrikes(int localCount) {
        final String STRIKE = "strike";

        try {
            String url = SERVER_URL + "/strikes-id-" + localCount + ".json";
            String data = httpGet(url);
            JSONObject json = new JSONObject(data);

            // TODO: check that status == "OK"
            return json.getJSONArray(STRIKE);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addStrikes(JSONArray jsonStrikes) {
        try {
            int len = jsonStrikes.length();
            Strike strike;

            // list will show newest strikes first,
            // so populate the db in newest->oldest order
            for (int i = len - 1; i >= 0; i--) {
                strike = Strike.fromJson(jsonStrikes.getJSONObject(i));
                mDatabase.addStrike(strike);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
