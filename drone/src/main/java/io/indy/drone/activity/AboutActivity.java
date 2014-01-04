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

package io.indy.drone.activity;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.indy.drone.AppConfig;
import io.indy.drone.R;

public class AboutActivity extends ActionBarActivity {

    static private final boolean D = true;
    static private final String TAG = "AboutActivity";

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.action_about));

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new AboutFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static class AboutFragment extends Fragment {

        public AboutFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_about, container, false);

            TextView version = (TextView)rootView.findViewById(R.id.version);
            version.setText("v" + AppConfig.VERSION + (AppConfig.DEBUG ? "d" : "r"));

            TextView source = (TextView)rootView.findViewById(R.id.source_link);
            asLink(source, "https://github.com/indy/drone");


            CharSequence res = "";
            Acknowledgement[] thirdParties = {
                    new Acknowledgement("EventBus",
                            "https://github.com/greenrobot/EventBus",
                            "Licensed under the Apache License, Version 2.0")
            };
            for (Acknowledgement a : thirdParties) {
                res = TextUtils.concat(res, TextUtils.concat(a.asFormatted()));
            }
            TextView ackView = (TextView)rootView.findViewById(R.id.acknowledgements);
            ackView.setText(res);
            ackView.setMovementMethod(LinkMovementMethod.getInstance());

            TextView email = (TextView)rootView.findViewById(R.id.textViewEmail);
            asLink(email, "mailto://drone@indy.io", "drone@indy.io");
            TextView google = (TextView)rootView.findViewById(R.id.textViewGooglePlus);
            asLink(google, "https://google.com/+InderjitGill", "google.com/+InderjitGill");
            TextView twitter = (TextView)rootView.findViewById(R.id.textViewTwitter);
            asLink(twitter, "https://twitter.com/InderjitGill", "@InderjitGill");

            return rootView;
        }

        private void asLink(TextView source, String url, String urlText) {
            SpannableString ssURL = new SpannableString(urlText);
            ssURL.setSpan(new URLSpan(url), 0, urlText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            source.setText(ssURL, TextView.BufferType.SPANNABLE);
            source.setMovementMethod(LinkMovementMethod.getInstance());
        }

        private void asLink(TextView source, String url) {
            SpannableString ssURL = new SpannableString(url);
            ssURL.setSpan(new URLSpan(url), 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            source.setText(ssURL, TextView.BufferType.SPANNABLE);
            source.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }


    private static class Acknowledgement {
        public final String mLibraryName;
        public final String mUrl;
        public final String mLicense;

        public Acknowledgement(String libraryName, String url, String license) {
            mLibraryName = libraryName;
            mUrl = url;
            mLicense = license;
        }

        CharSequence asFormatted() {
            SpannableString libraryName = new SpannableString(mLibraryName);
            libraryName.setSpan(new StyleSpan(Typeface.BOLD), 0, mLibraryName.length(), 0);

            SpannableString url = new SpannableString(mUrl);
            url.setSpan(new URLSpan(mUrl), 0, mUrl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableString ssLicense = new SpannableString(mLicense);

            return TextUtils.concat(libraryName, "\n", url, "\n", ssLicense);
        }
    }

    private static class Contact {
        public final String mType;
        public final String mUrl;
        public final String mUrlText;

        public Contact(String type, String url, String urlText) {
            mType = type;
            mUrl = url;
            mUrlText = urlText;
        }

        CharSequence asFormatted() {
            SpannableString type = new SpannableString(mType + ":");
            type.setSpan(new StyleSpan(Typeface.BOLD), 0, mType.length() + 1, 0);

            SpannableString url = new SpannableString(mUrlText);
            url.setSpan(new URLSpan(mUrl), 0, mUrlText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return TextUtils.concat(type, " ", url);
        }
    }

}
