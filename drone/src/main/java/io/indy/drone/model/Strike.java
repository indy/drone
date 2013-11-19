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
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.indy.drone.Flags;
import io.indy.drone.utils.DateFormatHelper;

public class Strike {

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
    public static final String DRONE_SUMMARY = "drone_summary";

    private String mJsonId;
    private int mNumber;
    private String mCountry;
    private Date mHappened;
    private String mTown;
    private String mLocation;

    private String mDeaths;
    private boolean mHasValidDeathsRange;
    private int mDeathsMin;
    private int mDeathsMax;

    private String mCivilians;
    private boolean mHasValidCivilianRange;
    private int mCiviliansMin;
    private int mCiviliansMax;

    private String mInjuries;
    private boolean mHasValidInjuriesRange;
    private int mInjuriesMin;
    private int mInjuriesMax;

    private String mChildren;
    private boolean mHasValidChildrenRange;
    private int mChildrenMin;
    private int mChildrenMax;

    private String mTweetId;
    private String mBureauId;
    private String mBijSummaryShort;
    private String mBijLink;
    private String mTarget;

    private double mLat;
    private double mLon;

    private String mNames;

    private String mDroneSummary;

    public boolean hasValidDeathsRange() {
        return mHasValidDeathsRange;
    }

    public void confirmValidDeathsRange(boolean hasValidDeathsRange) {
        mHasValidDeathsRange = hasValidDeathsRange;
    }

    public boolean hasValidCivilianRange() {
        return mHasValidCivilianRange;
    }

    public void confirmValidCivilianRange(boolean hasValidCivilianRange) {
        mHasValidCivilianRange = hasValidCivilianRange;
    }

    public boolean hasValidInjuriesRange() {
        return mHasValidInjuriesRange;
    }

    public void confirmValidInjuriesRange(boolean hasValidInjuriesRange) {
        mHasValidInjuriesRange = hasValidInjuriesRange;
    }

    public boolean hasValidChildrenRange() {
        return mHasValidChildrenRange;
    }

    public void confirmValidChildrenRange(boolean hasValidChildrenRange) {
        mHasValidChildrenRange = hasValidChildrenRange;
    }

    public int getNumber() {
        return mNumber;
    }

    public Strike setNumber(int number) {
        mNumber = number;
        return this;
    }

    public String getJsonId() {
        return mJsonId;
    }

    public Strike setJsonId(String jsonId) {
        mJsonId = jsonId;
        return this;
    }

    public String getCountry() {
        return mCountry;
    }

    public Strike setCountry(String country) {
        mCountry = country;
        return this;
    }

    public Date getHappened() {
        return mHappened;
    }

    public Strike setHappened(Date happened) {
        mHappened = happened;
        return this;
    }

    public String getTown() {
        return mTown;
    }

    public Strike setTown(String town) {
        mTown = town;
        return this;
    }

    public String getLocation() {
        return mLocation;
    }

    public Strike setLocation(String location) {
        mLocation = location;
        return this;
    }

    public String getDeaths() {
        return mDeaths;
    }

    public Strike setDeaths(String deaths) {
        mDeaths = deaths;
        return this;
    }

    public int getDeathsMin() {
        return mDeathsMin;
    }

    public Strike setDeathsMin(int deathsMin) {
        mDeathsMin = deathsMin;
        return this;
    }

    public int getDeathsMax() {
        return mDeathsMax;
    }

    public Strike setDeathsMax(int deathsMax) {
        mDeathsMax = deathsMax;
        return this;
    }

    public String getCivilians() {
        return mCivilians;
    }

    public Strike setCivilians(String civilians) {
        mCivilians = civilians;
        return this;
    }

    public int getCiviliansMin() {
        return mCiviliansMin;
    }

    public Strike setCiviliansMin(int civiliansMin) {
        mCiviliansMin = civiliansMin;
        return this;
    }

    public int getCiviliansMax() {
        return mCiviliansMax;
    }

    public Strike setCiviliansMax(int civiliansMax) {
        mCiviliansMax = civiliansMax;
        return this;
    }

    public String getInjuries() {
        return mInjuries;
    }

    public Strike setInjuries(String injuries) {
        mInjuries = injuries;
        return this;
    }

    public int getInjuriesMin() {
        return mInjuriesMin;
    }

    public Strike setInjuriesMin(int injuriesMin) {
        mInjuriesMin = injuriesMin;
        return this;
    }

    public int getInjuriesMax() {
        return mInjuriesMax;
    }

    public Strike setInjuriesMax(int injuriesMax) {
        mInjuriesMax = injuriesMax;
        return this;
    }

    public String getChildren() {
        return mChildren;
    }

    public Strike setChildren(String children) {
        mChildren = children;
        return this;
    }

    public int getChildrenMin() {
        return mChildrenMin;
    }

    public Strike setChildrenMin(int childrenMin) {
        mChildrenMin = childrenMin;
        return this;
    }

    public int getChildrenMax() {
        return mChildrenMax;
    }

    public Strike setChildrenMax(int childrenMax) {
        mChildrenMax = childrenMax;
        return this;
    }

    public String getTweetId() {
        return mTweetId;
    }

    public Strike setTweetId(String tweetId) {
        mTweetId = tweetId;
        return this;
    }

    public String getBureauId() {
        return mBureauId;
    }

    public Strike setBureauId(String bureauId) {
        mBureauId = bureauId;
        return this;
    }

    public String getBijSummaryShort() {
        return mBijSummaryShort;
    }

    public Strike setBijSummaryShort(String bijSummaryShort) {
        mBijSummaryShort = bijSummaryShort;
        return this;
    }

    public String getBijLink() {
        return mBijLink;
    }

    public Strike setBijLink(String bijLink) {
        mBijLink = bijLink;
        return this;
    }

    public String getTarget() {
        return mTarget;
    }

    public Strike setTarget(String target) {
        mTarget = target;
        return this;
    }

    public double getLat() {
        return mLat;
    }

    public Strike setLat(double lat) {
        mLat = lat;
        return this;
    }

    public double getLon() {
        return mLon;
    }

    public Strike setLon(double lon) {
        mLon = lon;
        return this;
    }

    public String getNames() {
        return mNames;
    }

    public Strike setNames(String names) {
        mNames = names;
        return this;
    }

    public String getDroneSummary() {
        return mDroneSummary;
    }

    public void buildDroneSummary() {
        String res = "";
        // 12-25 deaths, 1-2 children killed, 4-3 civilians, 10 injuries

        if(mHasValidDeathsRange) {
            if(mDeathsMin != mDeathsMax) {
                res += mDeathsMin + "-" + mDeathsMax + " deaths, ";
            } else {
                if(mDeathsMin == 0) {
                } else if(mDeathsMin == 1) {
                    res += mDeathsMin + " death, ";
                } else {
                    res += mDeathsMin + " deaths, ";
                }
            }
        }

        if(mHasValidChildrenRange) {
            if(mChildrenMin != mChildrenMax) {
                res += mChildrenMin + "-" + mChildrenMax + " children killed, ";
            } else {
                if(mChildrenMin == 0) {
                } else if(mChildrenMin == 1) {
                    res += mChildrenMin + " child killed, ";
                } else {
                    res += mChildrenMin + " children killed, ";
                }
            }
        }

        if(mHasValidCivilianRange) {
            if(mCiviliansMin != mCiviliansMax) {
                res += mCiviliansMin + "-" + mCiviliansMax + " civilians, ";
            } else {
                if(mCiviliansMin == 0) {
                } else if(mCiviliansMin == 1) {
                    res += mCiviliansMin + " civilian, ";
                } else {
                    res += mCiviliansMin + " civilians, ";
                }
            }
        }

        if(mHasValidInjuriesRange) {
            if(mInjuriesMin != mInjuriesMax) {
                res += mInjuriesMin + "-" + mInjuriesMax + " injuries, ";
            } else {
                if(mInjuriesMin == 0) {
                } else if(mInjuriesMin == 1) {
                    res += mInjuriesMin + " injury, ";
                } else {
                    res += mInjuriesMin + " injuries, ";
                }
            }
        }

        if(res.length() == 0) {
            mDroneSummary = "";
        } else {
            mDroneSummary = res.substring(0, res.length() - 2);
        }
    }

    private static Pattern SINGLE_NUMBER = Pattern.compile("^\\s*(\\d+)\\s*$");
    private static Pattern RANGE = Pattern.compile("^\\s*(\\d+)\\s*-\\s*(\\d+)\\s*$");

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }

    public static boolean hasValidMinMax(String s) {

        Matcher matchSingle = SINGLE_NUMBER.matcher(s);
        Matcher matchRange = RANGE.matcher(s);

        return matchSingle.matches() || matchRange.matches();
    }

    public static int[] parseMinMax(String s) throws ParseException {

        Matcher matcher = SINGLE_NUMBER.matcher(s);
        if (matcher.matches()) {
            int val = Integer.parseInt(matcher.group(1));
            return new int[]{val, val};
        }

        matcher = RANGE.matcher(s);
        if (matcher.matches()) {
            int min = Integer.parseInt(matcher.group(1));
            int max = Integer.parseInt(matcher.group(2));
            return new int[]{min, max};
        }

        ifd("wat " + s);
        throw new ParseException("cannot parse min/max from: " + s);
    }


    public static Strike fromJson(JSONObject jsonObject) {

        // the strings used as keys in the dronestre.am json feed
        //
        final String D_JSON_ID = "_id";
        final String D_NUMBER = "number";
        final String D_COUNTRY = "country";
        final String D_HAPPENED = "date";
        final String D_TOWN = "town";
        final String D_LOCATION = "location";
        final String D_DEATHS = "deaths";
        final String D_DEATHS_MIN = "deaths_min";
        final String D_DEATHS_MAX = "deaths_max";
        final String D_CIVILIANS = "civilians";
        final String D_INJURIES = "injuries";
        final String D_CHILDREN = "children";
        final String D_TWEET_ID = "tweet_id";
        final String D_BUREAU_ID = "bureau_id";
        final String D_BIJ_SUMMARY_SHORT = "bij_summary_short";
        final String D_BIJ_LINK = "bij_link";
        final String D_TARGET = "target";
        final String D_LAT = "lat";
        final String D_LON = "lon";
        final String D_NAMES = "names";

        Strike strike = new Strike();
        try {
            strike.setJsonId(jsonObject.getString(D_JSON_ID));
            strike.setNumber(jsonObject.getInt(D_NUMBER));
            strike.setCountry(jsonObject.getString(D_COUNTRY));
            strike.setHappened(DateFormatHelper.parseJsonDateString(jsonObject.getString(D_HAPPENED)));
            strike.setTown(jsonObject.getString(D_TOWN));
            strike.setLocation(jsonObject.getString(D_LOCATION));

            //strike.setDeathsMin(jsonObject.getString(D_DEATHS_MIN));
            //strike.setDeathsMax(jsonObject.getString(D_DEATHS_MAX));

            String deaths = jsonObject.getString(D_DEATHS);
            strike.setDeaths(deaths);
            if (hasValidMinMax(deaths)) {
                strike.confirmValidDeathsRange(true);
                try {
                    int[] minMax = parseMinMax(deaths);
                    strike.setDeathsMin(minMax[0]);
                    strike.setDeathsMax(minMax[1]);
                } catch (ParseException e) {
                    strike.confirmValidDeathsRange(false);
                    e.printStackTrace();
                }
            } else {
                strike.confirmValidDeathsRange(false);
            }

            String civilians = jsonObject.getString(D_CIVILIANS);
            strike.setCivilians(civilians);
            if (hasValidMinMax(civilians)) {
                strike.confirmValidCivilianRange(true);
                try {
                    int[] minMax = parseMinMax(civilians);
                    strike.setCiviliansMin(minMax[0]);
                    strike.setCiviliansMax(minMax[1]);
                } catch (ParseException e) {
                    strike.confirmValidCivilianRange(false);
                    e.printStackTrace();
                }
            } else {
                strike.confirmValidCivilianRange(false);
            }

            String injuries = jsonObject.getString(D_INJURIES);
            strike.setInjuries(injuries);
            if (hasValidMinMax(injuries)) {
                strike.confirmValidInjuriesRange(true);
                try {
                    int[] minMax = parseMinMax(injuries);
                    strike.setInjuriesMin(minMax[0]);
                    strike.setInjuriesMax(minMax[1]);
                } catch (ParseException e) {
                    strike.confirmValidInjuriesRange(false);
                    e.printStackTrace();
                }
            } else {
                strike.confirmValidInjuriesRange(false);
            }


            String children = jsonObject.getString(D_CHILDREN);
            strike.setChildren(children);
            if (hasValidMinMax(children)) {
                strike.confirmValidChildrenRange(true);
                try {
                    int[] minMax = parseMinMax(children);
                    strike.setChildrenMin(minMax[0]);
                    strike.setChildrenMax(minMax[1]);
                } catch (ParseException e) {
                    strike.confirmValidChildrenRange(false);
                    e.printStackTrace();
                }
            } else {
                strike.confirmValidChildrenRange(false);
            }

            strike.setTweetId(jsonObject.getString(D_TWEET_ID));
            strike.setBureauId(jsonObject.getString(D_BUREAU_ID));
            strike.setBijSummaryShort(jsonObject.getString(D_BIJ_SUMMARY_SHORT));
            strike.setBijLink(jsonObject.getString(D_BIJ_LINK));
            strike.setTarget(jsonObject.getString(D_TARGET));
            strike.setLat(Double.parseDouble(jsonObject.getString(D_LAT)));
            strike.setLon(Double.parseDouble(jsonObject.getString(D_LON)));

            JSONArray names = jsonObject.getJSONArray(D_NAMES);
            // the NAMES array should only have one entry
            for (int i = 0; i < names.length(); i++) {
                names.getString(i);
                strike.setNames(names.getString(i));
            }
            
            strike.buildDroneSummary();

            return strike;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ContentValues asContentValues() {

        ContentValues cv = new ContentValues();

        cv.put(JSON_ID, getJsonId());
        cv.put(NUMBER, getNumber());
        cv.put(COUNTRY, getCountry());
        cv.put(HAPPENED, DateFormatHelper.dateToSQLite(getHappened()));
        cv.put(TOWN, getTown());
        cv.put(LOCATION, getLocation());

        cv.put(DEATHS, getDeaths());
        cv.put(HAS_DEATHS_RANGE, hasValidDeathsRange());
        if (hasValidDeathsRange()) {
            cv.put(DEATHS_MIN, getDeathsMin());
            cv.put(DEATHS_MAX, getDeathsMax());
        }

        cv.put(CIVILIANS, getCivilians());
        cv.put(HAS_CIVILIANS_RANGE, hasValidCivilianRange());
        if (hasValidCivilianRange()) {
            cv.put(CIVILIANS_MIN, getCiviliansMin());
            cv.put(CIVILIANS_MAX, getCiviliansMax());
        }

        cv.put(INJURIES, getInjuries());
        cv.put(HAS_INJURIES_RANGE, hasValidInjuriesRange());
        if (hasValidInjuriesRange()) {
            cv.put(INJURIES_MIN, getInjuriesMin());
            cv.put(INJURIES_MAX, getInjuriesMax());
        }

        cv.put(CHILDREN, getChildren());
        cv.put(HAS_CHILDREN_RANGE, hasValidChildrenRange());
        if (hasValidChildrenRange()) {
            cv.put(CHILDREN_MIN, getChildrenMin());
            cv.put(CHILDREN_MAX, getChildrenMax());
        }

        cv.put(TWEET_ID, getTweetId());
        cv.put(BUREAU_ID, getBureauId());
        cv.put(BIJ_SUMMARY_SHORT, getBijSummaryShort());
        cv.put(BIJ_LINK, getBijLink());
        cv.put(TARGET, getTarget());
        cv.put(LAT, getLat());
        cv.put(LON, getLon());
        cv.put(NAMES, getNames());

        cv.put(DRONE_SUMMARY, getDroneSummary());

        return cv;
    }

    private static final String TAG = "Strike";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }
}
