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

package io.indy.drone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

// class based on TabsAdapter in UltimateStopwatch app
// https://code.google.com/p/android-ultimatestopwatch

public class TabsAdapter extends FragmentPagerAdapter
    implements ActionBar.TabListener {

    private final ActionBarActivity mActivity;
    private final ActionBar mActionBar;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private int mCurrentTab = 0;

    static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }
    }

    public TabsAdapter(ActionBarActivity activity, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        mActivity = activity;
        mActionBar = activity.getSupportActionBar();
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(MyViewPagerListener);
    }

    public void addTab(String tabName, Class<?> clss) {

        Bundle args = null;
        TabInfo info = new TabInfo(clss, args);

        ActionBar.Tab tab = mActionBar.newTab();
        tab.setText(tabName);
        tab.setTag(info);
        tab.setTabListener(this);

        mTabs.add(info);
        mActionBar.addTab(tab);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        return Fragment.instantiate(mActivity, info.clss.getName(), info.args);
    }

    ViewPager.SimpleOnPageChangeListener MyViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Find the ViewPager Position
                mActionBar.setSelectedNavigationItem(position);
            }
        };

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        mViewPager.setCurrentItem(tab.getPosition());
        /*
        Object tag = tab.getTag();
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i) == tag) {
                mViewPager.setCurrentItem(i,true);
                mCurrentTab = i;
            }
        }

        mActivity.invalidateOptionsMenu();
        */
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public int getCurrentTabNum()
    {
        return mCurrentTab;
    }
}
