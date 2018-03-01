package com.example.baking;

import android.support.annotation.VisibleForTesting;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BakingSchedulers {

    private static Scheduler sDatabaseScheduler = Schedulers.io();
    private static Scheduler sNetworkScheduler = Schedulers.from(Executors.newFixedThreadPool(3));
    private static Scheduler sUiScheduler = AndroidSchedulers.mainThread();

    @VisibleForTesting
    public synchronized static void setSchedulers(Scheduler databaseScheduler, Scheduler networkScheduler, Scheduler uiScheduler) {
        sDatabaseScheduler = databaseScheduler;
        sNetworkScheduler = networkScheduler;
        sUiScheduler = uiScheduler;
    }

    public static Scheduler getDatabaseScheduler() {
        return sDatabaseScheduler;
    }

    public static Scheduler getNetworkScheduler() {
        return sNetworkScheduler;
    }

    public static Scheduler getUiScheduler() {
        return sUiScheduler;
    }

}
