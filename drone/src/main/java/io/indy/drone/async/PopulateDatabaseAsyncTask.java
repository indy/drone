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
import java.util.ArrayList;
import java.util.List;

import io.indy.drone.model.SQLDatabase;
import io.indy.drone.model.Strike;

public class PopulateDatabaseAsyncTask extends AsyncTask<Void, Void, List<Strike>> {
    private static final String TAG = "PopulateDatabaseAsyncTask";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (D) Log.d(TAG, message);
    }

    private final String STRIKE = "strike";

    private Context mContext;
    private SQLDatabase mDatabase;
    private String mPath;

    public PopulateDatabaseAsyncTask(Context context, SQLDatabase database, String path) {
        mContext = context;
        mDatabase = database;
        mPath = path;
    }

    private JSONObject loadJsonFromFile(String path) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = mContext.getAssets().open(path);

            BufferedReader in = new BufferedReader(new InputStreamReader(json));
            String str;
            while ((str=in.readLine()) != null) {
                buf.append(str);
            }
            in.close();

            return new JSONObject(buf.toString());

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }

        return null;
    }

    private List<Strike> parseJson(JSONObject json) {
        List<Strike> res = new ArrayList<Strike>();

        try {
            // TODO: check that status == "OK"

            JSONArray strikes = json.getJSONArray(STRIKE);
            Strike strike;
            for (int i = 0; i < strikes.length(); i++) {
                strike = Strike.fromJson(strikes.getJSONObject(i));
                res.add(strike);
            }
        } catch (JSONException e) {
            ifd("parseJson JSONException: " + e);
        }

        return res;
    }

    @Override
    protected List<Strike> doInBackground(Void... params) {
        JSONObject jsonObject = loadJsonFromFile(mPath);
        List<Strike> strikes = parseJson(jsonObject);
        // mDatabase.loadStrikes(strikes);
        return strikes;
    }

    @Override
    protected void onPostExecute(List<Strike> result) {
        super.onPostExecute(result);

        // fire an event

    }
}
