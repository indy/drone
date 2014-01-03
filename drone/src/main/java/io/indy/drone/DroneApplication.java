package io.indy.drone;

import android.app.Application;
import android.util.Log;

public class DroneApplication extends Application {

    private static final String TAG = "DroneApplication";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }
}
