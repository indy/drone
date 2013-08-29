package io.indy.drone.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Date;

import io.indy.drone.Flags;
import io.indy.drone.R;
import io.indy.drone.model.SQLDatabase;
import io.indy.drone.utils.DateFormatHelper;

public class StrikeCursorAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;

    private int mStrikeIDIndex;
    private int mCountryIndex;
    private int mTownIndex;
    private int mLocationIndex;
    private int mSummaryIndex;
    private int mHappenedIndex;

    public StrikeCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        init(context);
    }

    @SuppressWarnings("deprecation")
    public StrikeCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        init(context);
    }

    private void init(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        updateColumnIndices();
    }

    private void updateColumnIndices() {
        Cursor c = getCursor();
        mStrikeIDIndex = c.getColumnIndex(SQLDatabase.KEY_ID);
        mCountryIndex = c.getColumnIndex(SQLDatabase.COUNTRY);
        mTownIndex = c.getColumnIndex(SQLDatabase.TOWN);
        mLocationIndex = c.getColumnIndex(SQLDatabase.LOCATION);
        mSummaryIndex = c.getColumnIndex(SQLDatabase.BIJ_SUMMARY_SHORT);
        mHappenedIndex = c.getColumnIndex(SQLDatabase.HAPPENED);
    }

    @Override
    public void changeCursor(Cursor cursor) {
        ifd("changeCursor");

        super.changeCursor(cursor);
        updateColumnIndices();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView strike = (TextView)view.findViewById(R.id.strikeid);
        strike.setText(cursor.getString(mStrikeIDIndex));

        TextView happened = (TextView) view.findViewById(R.id.happened);
        String timeString = cursor.getString(mHappenedIndex);
        Date time = DateFormatHelper.parseSQLiteDateString(timeString);
        happened.setText(DateFormatHelper.dateToDisplay(time));

        TextView country = (TextView)view.findViewById(R.id.country);
        country.setText(cursor.getString(mCountryIndex));

        TextView location = (TextView)view.findViewById(R.id.location);
        String loc = cursor.getString(mTownIndex);
        if(loc.equals("")) {
            loc = cursor.getString(mLocationIndex);
        } else if(!cursor.getString(mLocationIndex).equals("")) {
            loc = cursor.getString(mTownIndex) + " - " + cursor.getString(mLocationIndex);
        }
        location.setText(loc);

        TextView summary = (TextView)view.findViewById(R.id.summary);
        summary.setText(cursor.getString(mSummaryIndex));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.row_news, parent, false);
        bindView(v, context, cursor);
        return v;
    }

    private static final String TAG = "StrikeCursorAdapter";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (Flags.DEBUG && D) Log.d(TAG, message);
    }
}
