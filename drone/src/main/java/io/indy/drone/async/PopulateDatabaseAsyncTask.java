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

package io.indy.drone.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.greenrobot.event.EventBus;
import io.indy.drone.AppConfig;
import io.indy.drone.event.UpdatedDatabaseEvent;
import io.indy.drone.model.SQLDatabase;
import io.indy.drone.model.Strike;

public class PopulateDatabaseAsyncTask extends AsyncTask<Void, Integer, Void> {
    private final String STRIKE = "strike";

    private Context mContext;
    private SQLDatabase.ModelHelper mModelHelper;
    private String mPath;

    public PopulateDatabaseAsyncTask(Context context, SQLDatabase.ModelHelper modelHelper, String path) {
        mContext = context;
        mModelHelper = modelHelper;
        mPath = path;
    }

    private JSONObject loadJsonFromFile(String path) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = mContext.getAssets().open(path);

            BufferedReader in = new BufferedReader(new InputStreamReader(json));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();

            return new JSONObject(buf.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected Void doInBackground(Void... params) {

        JSONObject json = loadJsonFromFile(mPath);

        try {
            // TODO: check that status == "OK"
            JSONArray strikes = json.getJSONArray(STRIKE);
            int len = strikes.length();
            Strike strike;

            // list will show newest strikes first,
            // so populate the db in newest->oldest order
            for (int i = len - 1; i >= 0; i--) {
                strike = Strike.fromJson(strikes.getJSONObject(i));
                mModelHelper.addStrike(strike);
                if (i == len - 5) {
                    // get an UpdatedDatabaseEvent fired as soon as there's enough
                    // data in the db to have a screen of strike information
                    publishProgress(i);
                } else if (i == len - 20) {
                    // continue early population of list
                    publishProgress(i);
                }
                if (i % 100 == 0) {
                    // now populate at regular intervals
                    publishProgress(i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onProgressUpdate(Integer... i) {
        EventBus.getDefault().post(new UpdatedDatabaseEvent());
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // final event to inform list that all strike data is in the db
        EventBus.getDefault().post(new UpdatedDatabaseEvent());
    }

    private static final String TAG = "PopulateDatabaseAsyncTask";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }
}
