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

package io.indy.drone.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.indy.drone.Flags;
import io.indy.drone.async.PopulateDatabaseAsyncTask;
import io.indy.drone.utils.DateFormatHelper;

public class SQLDatabase {

    public static final String[] LOCATIONS = {"worldwide", "Pakistan", "Yemen", "Somalia"};

    // The index (key) column name for use in where clauses.
    public static final String KEY_ID = "_id";

    // SQLDatabase open/upgrade helper
    private ModelHelper mModelHelper;

    public SQLDatabase(Context context) {
        ifd("constructor");

        mModelHelper = new ModelHelper(context,
                ModelHelper.DATABASE_NAME,
                null,
                ModelHelper.DATABASE_VERSION);
    }

    // Called when you no longer need access to the database.
    public void closeDatabase() {
        mModelHelper.close();
    }

    private String stringVal(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private int intVal(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    private double doubleVal(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    private Strike strikeFromCursor(Cursor cursor) {
        Strike strike = new Strike();

        String happenedString = stringVal(cursor, Strike.HAPPENED);
        strike.setHappened(DateFormatHelper.parseSQLiteDateString(happenedString));

        strike.setCountry(stringVal(cursor, Strike.COUNTRY));
        strike.setTown(stringVal(cursor, Strike.TOWN));
        strike.setLocation(stringVal(cursor, Strike.LOCATION));

        strike.setDeaths(stringVal(cursor, Strike.DEATHS));
        int bool = intVal(cursor, Strike.HAS_DEATHS_RANGE);
        if (bool == 1) {
            strike.confirmValidDeathsRange(true);
            strike.setDeathsMax(intVal(cursor, Strike.DEATHS_MAX));
            strike.setDeathsMin(intVal(cursor, Strike.DEATHS_MIN));
        } else {
            strike.confirmValidDeathsRange(false);
        }

        strike.setCivilians(stringVal(cursor, Strike.CIVILIANS));
        bool = intVal(cursor, Strike.HAS_CIVILIANS_RANGE);
        if (bool == 1) {
            strike.confirmValidCivilianRange(true);
            strike.setCiviliansMax(intVal(cursor, Strike.CIVILIANS_MAX));
            strike.setCiviliansMin(intVal(cursor, Strike.CIVILIANS_MIN));
        } else {
            strike.confirmValidCivilianRange(false);
        }

        strike.setInjuries(stringVal(cursor, Strike.INJURIES));
        bool = intVal(cursor, Strike.HAS_INJURIES_RANGE);
        if (bool == 1) {
            strike.confirmValidInjuriesRange(true);
            strike.setInjuriesMax(intVal(cursor, Strike.INJURIES_MAX));
            strike.setInjuriesMin(intVal(cursor, Strike.INJURIES_MIN));
        } else {
            strike.confirmValidInjuriesRange(false);
        }

        strike.setChildren(stringVal(cursor, Strike.CHILDREN));
        bool = intVal(cursor, Strike.HAS_CHILDREN_RANGE);
        if (bool == 1) {
            strike.confirmValidChildrenRange(true);
            strike.setChildrenMax(intVal(cursor, Strike.CHILDREN_MAX));
            strike.setChildrenMin(intVal(cursor, Strike.CHILDREN_MIN));
        } else {
            strike.confirmValidChildrenRange(false);
        }

        strike.setTweetId(stringVal(cursor, Strike.TWEET_ID));
        strike.setBureauId(stringVal(cursor, Strike.BUREAU_ID));
        strike.setBijSummaryShort(stringVal(cursor, Strike.BIJ_SUMMARY_SHORT));
        strike.setBijLink(stringVal(cursor, Strike.BIJ_LINK));
        strike.setTarget(stringVal(cursor, Strike.TARGET));

        strike.setLat(doubleVal(cursor, Strike.LAT));
        strike.setLon(doubleVal(cursor, Strike.LON));

        strike.setDroneSummary(stringVal(cursor, Strike.DRONE_SUMMARY));
        strike.setInformationUrl(stringVal(cursor, Strike.INFORMATION_URL));

        return strike;
    }

    public Strike getStrike(String strikeId) {
        String[] result_columns = new String[]{
                KEY_ID, Strike.HAPPENED, Strike.COUNTRY, Strike.TOWN,
                Strike.LOCATION, Strike.DEATHS, Strike.HAS_DEATHS_RANGE,
                Strike.DEATHS_MIN, Strike.DEATHS_MAX, Strike.CIVILIANS,
                Strike.HAS_CIVILIANS_RANGE, Strike.CIVILIANS_MIN,
                Strike.CIVILIANS_MAX, Strike.INJURIES,
                Strike.HAS_INJURIES_RANGE, Strike.INJURIES_MIN,
                Strike.INJURIES_MAX, Strike.CHILDREN,
                Strike.HAS_CHILDREN_RANGE, Strike.CHILDREN_MIN,
                Strike.CHILDREN_MAX, Strike.TWEET_ID, Strike.BUREAU_ID,
                Strike.BIJ_SUMMARY_SHORT, Strike.BIJ_LINK, Strike.TARGET,
                Strike.LAT, Strike.LON, Strike.DRONE_SUMMARY, Strike.INFORMATION_URL
        };

        String where = KEY_ID + "=?";
        String[] whereArgs = {strikeId};
        String groupBy = null;
        String having = null;
        String order = null;

        Strike strike = null;

        SQLiteDatabase db = mModelHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(ModelHelper.STRIKE_TABLE, result_columns,
                    where, whereArgs,
                    groupBy, having, order);

            cursor.moveToNext();
            strike = strikeFromCursor(cursor);
            cursor.close();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return strike;
    }

    public Cursor getStrikeCursor(String country) {
        if (country.equals(LOCATIONS[0])) { // worldwide
            return getStrikeCursor(null, null);
        }
        return getStrikeCursor(Strike.COUNTRY + "=?", new String[]{country});
    }

    private Cursor getStrikeCursor(String where, String[] whereArgs) {
        // Specify the result column projection. Return the minimum set
        // of columns required to satisfy your requirements.
        String[] result_columns = new String[]{
                KEY_ID,
                Strike.COUNTRY,
                Strike.TOWN,
                Strike.LOCATION,
                Strike.BIJ_SUMMARY_SHORT,
                Strike.HAPPENED,
                Strike.DRONE_SUMMARY,
                Strike.INFORMATION_URL
        };

        String groupBy = null;
        String having = null;
        String order = Strike.HAPPENED + " desc";

        SQLiteDatabase db = mModelHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(ModelHelper.STRIKE_TABLE, result_columns, where, whereArgs, groupBy,
                    having, order);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return cursor;
    }

    public static class ModelHelper extends SQLiteOpenHelper {

        private static final String TAG = "ModelHelper";
        private static final boolean D = true;

        static void ifd(final String message) {
            if (D) Log.d(TAG, message);
        }

        private static final String DATABASE_NAME = "drone.db";

        private static final int DATABASE_VERSION = 6;

        private static final String STRIKE_TABLE = "strike";

        private static Context mContext;

        public ModelHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
            mContext = context;
            ifd("constructor");
        }

        // Called when no database exists in disk and the helper class needs
        // to create a new one.
        @Override
        public void onCreate(SQLiteDatabase db) {
            ifd("onCreate");

            String table = new SQLTableStatement(STRIKE_TABLE)
                    .integer(KEY_ID, "primary key autoincrement")
                    .text(Strike.JSON_ID)
                    .integer(Strike.NUMBER)
                    .text(Strike.COUNTRY)
                    .timestamp(Strike.HAPPENED)
                    .text(Strike.TOWN)
                    .text(Strike.LOCATION)
                    .text(Strike.DEATHS)
                    .integer(Strike.HAS_DEATHS_RANGE)
                    .integer(Strike.DEATHS_MIN)
                    .integer(Strike.DEATHS_MAX)
                    .text(Strike.CIVILIANS)
                    .integer(Strike.HAS_CIVILIANS_RANGE)
                    .integer(Strike.CIVILIANS_MIN)
                    .integer(Strike.CIVILIANS_MAX)
                    .text(Strike.INJURIES)
                    .integer(Strike.HAS_INJURIES_RANGE)
                    .integer(Strike.INJURIES_MIN)
                    .integer(Strike.INJURIES_MAX)
                    .text(Strike.CHILDREN)
                    .integer(Strike.HAS_CHILDREN_RANGE)
                    .integer(Strike.CHILDREN_MIN)
                    .integer(Strike.CHILDREN_MAX)
                    .text(Strike.TWEET_ID)
                    .text(Strike.BUREAU_ID)
                    .text(Strike.BIJ_SUMMARY_SHORT)
                    .text(Strike.BIJ_LINK)
                    .text(Strike.TARGET)
                    .real(Strike.LAT)
                    .real(Strike.LON)
                    .text(Strike.NAMES)
                    .text(Strike.DRONE_SUMMARY)
                    .text(Strike.INFORMATION_URL)
                    .create();

            db.execSQL(table);

            PopulateDatabaseAsyncTask task = new PopulateDatabaseAsyncTask(mContext,
                    this,
                    "strikes/strikes-complete.json");
            task.execute();
        }

        // Called when there is a database version mismatch meaning that
        // the version of the database on disk needs to be upgraded to
        // the current version.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Log the version upgrade.
            Log.w("SQLDatabase", "Upgrading from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");

            // Upgrade the existing database to conform to the new
            // version. Multiple previous versions can be handled by
            // comparing oldVersion and newVersion values.

            // The simplest case is to drop the old table and create a new one.
            String dropStrikeTable = new SQLTableStatement(STRIKE_TABLE).drop();
            db.execSQL(dropStrikeTable);

            // Create a new one.
            onCreate(db);
        }

        public void addStrike(Strike strike) {

            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = strike.asContentValues();

            db.insert(STRIKE_TABLE, null, cv);
        }

    }

    private static final String TAG = "SQLDatabase";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }
}
