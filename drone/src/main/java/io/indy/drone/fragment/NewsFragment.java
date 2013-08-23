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

package io.indy.drone.fragment;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import io.indy.drone.MainActivity;
import io.indy.drone.R;
import io.indy.drone.model.SQLDatabase;

public class NewsFragment extends Fragment {
    private final boolean D = true;
    private final String TAG = NewsFragment.class.getSimpleName();

//    private SQLDatabase mDatabase;

    private ListView mListView;

    private void ifd(final String message) {
        if (D) Log.d(TAG, message);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        MainActivity mainActivity = (MainActivity)getActivity();
        SQLDatabase sqlDatabase = mainActivity.getDatabase();
        Cursor cursor = sqlDatabase.getStrikeCursor();

        SimpleCursorAdapter adapter;

        String[] fieldsDB = { SQLDatabase.KEY_ID,
                SQLDatabase.COUNTRY,
                SQLDatabase.TOWN,
                SQLDatabase.LOCATION,
                SQLDatabase.BIJ_SUMMARY_SHORT};

        int[] fieldsView = {R.id.strikeid, R.id.country, R.id.town, R.id.location, R.id.summary};

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            adapter = new SimpleCursorAdapter(getActivity(), R.layout.row_news,
                    cursor, fieldsDB, fieldsView, 0);
        } else {
            adapter = new SimpleCursorAdapter(getActivity(), R.layout.row_news,
                    cursor, fieldsDB, fieldsView);
        }

        mListView = (ListView) view.findViewById(R.id.listViewNews);
        mListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        CursorAdapter ca = (CursorAdapter)(mListView.getAdapter());
        ca.getCursor().close();
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
 
}
