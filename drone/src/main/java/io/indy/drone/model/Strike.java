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

import java.util.Date;

public class Strike {
    private static final String TAG = "Strike";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (D) Log.d(TAG, message);
    }

    private int mNumber;
    private int mJsonId;
    private String mCountry;
    private Date mHappened;
    private String mTown;
    private String mLocation;

    private String mDeaths;
    private int mDeathsMin;
    private int mDeathsMax;

    private String mCivilians;
    private int mCiviliansMin;
    private int mCiviliansMax;

    private String mInjuries;
    private int mInjuriesMin;
    private int mInjuriesMax;

    private String mChildren;
    private int mChildrenMin;
    private int mChildrenMax;

    private String mTweetId;
    private String mBureauId;
    private String mBijSummaryShort;
    private String mBijLink;
    private String mTarget;

    private float mLat;
    private float mLon;
        
    private String mNames;

    public int getNumber() {
        return mNumber;
    }

    public Strike setNumber(int number) {
        mNumber = number;
        return this;
    }

    public int getJsonId() {
        return mJsonId;
    }

    public Strike setJsonId(int jsonId) {
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

    public float getLat() {
        return mLat;
    }

    public Strike setLat(float lat) {
        mLat = lat;
        return this;
    }

    public float getLon() {
        return mLon;
    }

    public Strike setLon(float lon) {
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
}
