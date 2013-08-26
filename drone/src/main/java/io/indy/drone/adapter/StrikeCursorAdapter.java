package io.indy.drone.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import io.indy.drone.R;
import io.indy.drone.model.SQLDatabase;

public class StrikeCursorAdapter extends CursorAdapter {
    private static final String TAG = "StrikeCursorAdapter";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (D) Log.d(TAG, message);
    }

    private LayoutInflater mLayoutInflater;

    private int mStrikeIDIndex;
    private int mCountryIndex;
    private int mTownIndex;
    private int mLocationIndex;
    private int mSummaryIndex;

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

        TextView country = (TextView)view.findViewById(R.id.country);
        country.setText(cursor.getString(mCountryIndex));

        TextView town = (TextView)view.findViewById(R.id.town);
        town.setText(cursor.getString(mTownIndex));

        TextView location = (TextView)view.findViewById(R.id.location);
        location.setText(cursor.getString(mLocationIndex));

        TextView summary = (TextView)view.findViewById(R.id.summary);
        summary.setText(cursor.getString(mSummaryIndex));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.row_news, parent, false);
        bindView(v, context, cursor);
        return v;
    }

}
