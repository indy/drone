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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

public class SQLDatabase {

    private static final String TAG = "SQLDatabase";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (D) Log.d(TAG, message);
    }

    // The index (key) column name for use in where clauses.
    public static final String KEY_ID = "id";

    public static final String JSON_ID = "json_id";
    public static final String NUMBER = "number";
    public static final String COUNTRY = "country";
    public static final String HAPPENED = "happened";
    public static final String TOWN = "town";
    public static final String LOCATION = "location";
    public static final String DEATHS = "deaths";
    public static final String HAS_DEATHS_RANGE = "has_deaths_range";
    public static final String DEATHS_MIN = "deaths_min";
    public static final String DEATHS_MAX = "deaths_max";
    public static final String CIVILIANS = "civilians";
    public static final String HAS_CIVILIANS_RANGE = "has_civilians_range";
    public static final String CIVILIANS_MIN = "civilians_min";
    public static final String CIVILIANS_MAX = "civilians_max";
    public static final String INJURIES = "injuries";
    public static final String HAS_INJURIES_RANGE = "has_injuries_range";
    public static final String INJURIES_MIN = "injuries_min";
    public static final String INJURIES_MAX = "injuries_max";
    public static final String CHILDREN = "children";
    public static final String HAS_CHILDREN_RANGE = "has_children_range";
    public static final String CHILDREN_MIN = "children_min";
    public static final String CHILDREN_MAX = "children_max";
    public static final String TWEET_ID = "tweet_id";
    public static final String BUREAU_ID = "bureau_id";
    public static final String BIJ_SUMMARY_SHORT = "bij_summary_short";
    public static final String BIJ_LINK = "bij_link";
    public static final String TARGET = "target";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String NAMES = "names";
    
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

    public boolean isEmpty() {
        return true;
    }

    public void addStrikes(List<Strike> strikes) {

        SQLiteDatabase db = mModelHelper.getWritableDatabase();
        ContentValues cv;

        for(Strike strike: strikes) {
            cv = new ContentValues();

            cv.put(JSON_ID, strike.getJsonId());
            cv.put(NUMBER, strike.getNumber());
            cv.put(COUNTRY, strike.getCountry());
            cv.put(HAPPENED, strike.getHappened().toString());
            cv.put(TOWN, strike.getTown());
            cv.put(LOCATION, strike.getLocation());

            cv.put(DEATHS, strike.getDeaths());
            cv.put(HAS_DEATHS_RANGE, strike.hasValidDeathsRange());
            if(strike.hasValidDeathsRange()) {
                cv.put(DEATHS_MIN, strike.getDeathsMin());
                cv.put(DEATHS_MAX, strike.getDeathsMax());
            }

            cv.put(CIVILIANS, strike.getCivilians());
            cv.put(HAS_CIVILIANS_RANGE, strike.hasValidCivilianRange());
            if(strike.hasValidCivilianRange()) {
                cv.put(CIVILIANS_MIN, strike.getCiviliansMin());
                cv.put(CIVILIANS_MAX, strike.getCiviliansMax());
            }

            cv.put(INJURIES, strike.getInjuries());
            cv.put(HAS_INJURIES_RANGE, strike.hasValidInjuriesRange());
            if(strike.hasValidInjuriesRange()) {
                cv.put(INJURIES_MIN, strike.getInjuriesMin());
                cv.put(INJURIES_MAX, strike.getInjuriesMax());
            }

            cv.put(CHILDREN, strike.getChildren());
            cv.put(HAS_CHILDREN_RANGE, strike.hasValidChildrenRange());
            if(strike.hasValidChildrenRange()) {
                cv.put(CHILDREN_MIN, strike.getChildrenMin());
                cv.put(CHILDREN_MAX, strike.getChildrenMax());
            }

            cv.put(TWEET_ID, strike.getTweetId());
            cv.put(BUREAU_ID, strike.getBureauId());
            cv.put(BIJ_SUMMARY_SHORT, strike.getBijSummaryShort());
            cv.put(BIJ_LINK, strike.getBijLink());
            cv.put(TARGET, strike.getTarget());
            cv.put(LAT, strike.getLat());
            cv.put(LON, strike.getLon());
            cv.put(NAMES, strike.getNames());
            
            db.insert(ModelHelper.STRIKE_TABLE, null, cv);
        }
    }

/*
    // return all the tasks associated with the list
    public List<Task> getTasks(int taskListId) {

        Cursor cursor = getTasksCursor(taskListId);

        int ID_INDEX = cursor.getColumnIndexOrThrow(KEY_ID);
        int STATE_INDEX = cursor.getColumnIndexOrThrow(STATE);
        int STARTED_AT_INDEX = cursor.getColumnIndexOrThrow(STARTED_AT);
        int FINISHED_AT_INDEX = cursor.getColumnIndexOrThrow(FINISHED_AT);
        int CONTENT_INDEX = cursor.getColumnIndexOrThrow(CONTENT);

        List<Task> res = new ArrayList<Task>();
        Task task;
        while (cursor.moveToNext()) {

            task = new Task.Builder().id(cursor.getInt(ID_INDEX))//.listId(taskListId)
                    .content(cursor.getString(CONTENT_INDEX)).state(cursor.getInt(STATE_INDEX))
                    .startedAt(cursor.getString(STARTED_AT_INDEX))
                    .finishedAt(cursor.getString(FINISHED_AT_INDEX)).build();
            res.add(task);
        }

        cursor.close();

        return res;
    }

    public Cursor getTasksCursor(int taskListId) {

        // Specify the result column projection. Return the minimum set
        // of columns required to satisfy your requirements.
        String[] result_columns = new String[] {
                KEY_ID, LIST_ID, STATE, STARTED_AT, FINISHED_AT, CONTENT
        };

        String where = LIST_ID + "=? and " + STATE + "<?";
        String whereArgs[] = {
                Integer.toString(taskListId), Integer.toString(Task.STATE_CLOSED)
        };

        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = mModelHelper.getReadableDatabase();
        Cursor cursor = db.query(ModelHelper.TASK_TABLE, result_columns, where, whereArgs, groupBy,
                having, order);

        return cursor;
    }
*/

    private static class ModelHelper extends SQLiteOpenHelper {

        private static final String TAG = "ModelHelper";
        private static final boolean D = true;

        static void ifd(final String message) {
            if (D) Log.d(TAG, message);
        }

        private static final String DATABASE_NAME = "drone.db";

        private static final int DATABASE_VERSION = 1;

        private static final String STRIKE_TABLE = "strike";

        public ModelHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // Called when no database exists in disk and the helper class needs
        // to create a new one.
        @Override
        public void onCreate(SQLiteDatabase db) {
            ifd("onCreate");

            String table = new SQLTableStatement(STRIKE_TABLE)
                .integer(KEY_ID, "primary key autoincrement")
                .text(JSON_ID)
                .integer(NUMBER)
                .text(COUNTRY)
                .timestamp(HAPPENED)
                .text(TOWN)
                .text(LOCATION)
                .text(DEATHS)
                .integer(HAS_DEATHS_RANGE)
                .integer(DEATHS_MIN)
                .integer(DEATHS_MAX)
                .text(CIVILIANS)
                .integer(HAS_CIVILIANS_RANGE)
                .integer(CIVILIANS_MIN)
                .integer(CIVILIANS_MAX)
                .text(INJURIES)
                .integer(HAS_INJURIES_RANGE)
                .integer(INJURIES_MIN)
                .integer(INJURIES_MAX)
                .text(CHILDREN)
                .integer(HAS_CHILDREN_RANGE)
                .integer(CHILDREN_MIN)
                .integer(CHILDREN_MAX)
                .text(TWEET_ID)
                .text(BUREAU_ID)
                .text(BIJ_SUMMARY_SHORT)
                .text(BIJ_LINK)
                .text(TARGET)
                .real(LAT)
                .real(LON)
                .text(NAMES)
                .create();

            db.execSQL(table);
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
    }
}
