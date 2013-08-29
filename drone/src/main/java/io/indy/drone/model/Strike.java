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

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.indy.drone.utils.DateFormatHelper;
import io.indy.drone.Flags;

public class Strike {

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


    private static Pattern SINGLE_NUMBER = Pattern.compile("^\\s*(\\d+)\\s*$");
    private static Pattern RANGE = Pattern.compile("^\\s*(\\d+)\\s*-\\s*(\\d+)\\s*$");

    public static class ParseException extends Exception {
        public ParseException(String message){
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
        if(matcher.matches()) {
            int val = Integer.parseInt(matcher.group(1));
            return new int[]{val, val};
        }

        matcher = RANGE.matcher(s);
        if(matcher.matches()) {
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
        final String JSON_ID = "_id";
        final String NUMBER = "number";
        final String COUNTRY = "country";
        final String HAPPENED = "date";
        final String TOWN = "town";
        final String LOCATION = "location";
        final String DEATHS = "deaths";
        final String DEATHS_MIN = "deaths_min";
        final String DEATHS_MAX = "deaths_max";
        final String CIVILIANS = "civilians";
        final String INJURIES = "injuries";
        final String CHILDREN = "children";
        final String TWEET_ID = "tweet_id";
        final String BUREAU_ID = "bureau_id";
        final String BIJ_SUMMARY_SHORT = "bij_summary_short";
        final String BIJ_LINK = "bij_link";
        final String TARGET = "target";
        final String LAT = "lat";
        final String LON = "lon";
        final String NAMES = "names";

        Strike strike = new Strike();
        try {
            strike.setJsonId(jsonObject.getString(JSON_ID));
            strike.setNumber(jsonObject.getInt(NUMBER));
            strike.setCountry(jsonObject.getString(COUNTRY));
            strike.setHappened(DateFormatHelper.parseJsonDateString(jsonObject.getString(HAPPENED)));
            strike.setTown(jsonObject.getString(TOWN));
            strike.setLocation(jsonObject.getString(LOCATION));

            //strike.setDeathsMin(jsonObject.getString(DEATHS_MIN));
            //strike.setDeathsMax(jsonObject.getString(DEATHS_MAX));

            String deaths = jsonObject.getString(DEATHS);
            strike.setDeaths(deaths);
            if(hasValidMinMax(deaths)) {
                strike.confirmValidDeathsRange(true);
                try {
                    int[] minMax = parseMinMax(deaths);
                    strike.setDeathsMin(minMax[0]);
                    strike.setDeathsMax(minMax[1]);
                } catch(ParseException e) {
                    strike.confirmValidDeathsRange(false);
                    e.printStackTrace();
                }
            } else {
                strike.confirmValidDeathsRange(false);
            }

            String civilians = jsonObject.getString(CIVILIANS);
            strike.setCivilians(civilians);
            if(hasValidMinMax(civilians)) {
                strike.confirmValidCivilianRange(true);
                try {
                    int[] minMax = parseMinMax(civilians);
                    strike.setCiviliansMin(minMax[0]);
                    strike.setCiviliansMax(minMax[1]);
                } catch(ParseException e) {
                    strike.confirmValidCivilianRange(false);
                    e.printStackTrace();
                }
            } else {
                strike.confirmValidCivilianRange(false);
            }

            String injuries = jsonObject.getString(INJURIES);
            strike.setInjuries(injuries);
            if(hasValidMinMax(injuries)) {
                strike.confirmValidInjuriesRange(true);
                try {
                    int[] minMax = parseMinMax(injuries);
                    strike.setInjuriesMin(minMax[0]);
                    strike.setInjuriesMax(minMax[1]);
                } catch(ParseException e) {
                    strike.confirmValidInjuriesRange(false);
                    e.printStackTrace();
                }
            } else {
                strike.confirmValidInjuriesRange(false);
            }


            String children = jsonObject.getString(CHILDREN);
            strike.setChildren(children);
            if(hasValidMinMax(children)) {
                strike.confirmValidChildrenRange(true);
                try {
                    int[] minMax = parseMinMax(children);
                    strike.setChildrenMin(minMax[0]);
                    strike.setChildrenMax(minMax[1]);
                } catch(ParseException e) {
                    strike.confirmValidChildrenRange(false);
                    e.printStackTrace();
                }
            } else {
                strike.confirmValidChildrenRange(false);
            }

            strike.setTweetId(jsonObject.getString(TWEET_ID));
            strike.setBureauId(jsonObject.getString(BUREAU_ID));
            strike.setBijSummaryShort(jsonObject.getString(BIJ_SUMMARY_SHORT));
            strike.setBijLink(jsonObject.getString(BIJ_LINK));
            strike.setTarget(jsonObject.getString(TARGET));
            strike.setLat(Double.parseDouble(jsonObject.getString(LAT)));
            strike.setLon(Double.parseDouble(jsonObject.getString(LON)));

            JSONArray names = jsonObject.getJSONArray(NAMES);
            // the NAMES array should only have one entry
            for(int i=0;i<names.length();i++) {
                names.getString(i);
                strike.setNames(names.getString(i));
            }

            return strike;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static final String TAG = "Strike";
    private static final boolean D = true;
    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }
}
