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

package io.indy.drone.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.indy.drone.Flags;

public class DateFormatHelper {

    private static final SimpleDateFormat sDisplayFormat = new SimpleDateFormat("d MMMM yyyy");
    private static final SimpleDateFormat sSQLiteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sJSONFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static Date parseSQLiteDateString(String dateString) {
        Date date = null;
        try {
            date = sSQLiteFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date parseJsonDateString(String dateString) {
        Date date = null;
        try {
            date = sJSONFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String dateToSQLite(Date date) {
        return sSQLiteFormat.format(date);
    }

    public static String dateToDisplay(Date date) {
        return sDisplayFormat.format(date);
    }

    static private final boolean D = true;
    static private final String TAG = "DateFormatHelper";
    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }
}
