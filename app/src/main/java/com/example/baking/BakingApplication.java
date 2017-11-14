package com.example.baking;

import android.app.Application;
import android.os.StrictMode;

public class BakingApplication extends Application {

    private static BakingApplication sInstance;

    public static BakingApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }

}