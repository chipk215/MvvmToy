package com.keyeswest.mvvmtoy;

import android.app.Application;

import com.keyeswest.mvvmtoy.db.TripDatabase;

import timber.log.Timber;

public class MainApp extends Application {

    private AppExecutors mAppExecutors;
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree() {
            // include line numbers
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ':' + element.getLineNumber();
            }
        });
    }

    public TripDatabase getDatabase() {
        return TripDatabase.getInstance(this, mAppExecutors);
    }


}
