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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.view.ViewPager;

import io.indy.drone.fragment.MapFragment;
import io.indy.drone.fragment.NewsFragment;
import io.indy.drone.fragment.StatsFragment;

public class MainActivity extends ActionBarActivity {
 
    TabsAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_main);

        setupTabs();
    }

    private void setupTabs() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, pager);

        mTabsAdapter.addTab("News", NewsFragment.class);
        mTabsAdapter.addTab("Map", MapFragment.class);
        mTabsAdapter.addTab("Stats", StatsFragment.class);
    }

}
