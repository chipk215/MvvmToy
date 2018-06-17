package com.keyeswest.mvvmtoy;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.keyeswest.mvvmtoy.db.TripDatabase;

import timber.log.Timber;

public class MainApp extends Application {

    private AppExecutors mAppExecutors;
    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();

        Timber.plant(new Timber.DebugTree() {
            // include line numbers
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ':' + element.getLineNumber();
            }
        });

        Stetho.initializeWithDefaults(this);
    }

    public TripDatabase getDatabase() {
        return TripDatabase.getInstance(this, mAppExecutors);
    }


    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
