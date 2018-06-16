package com.keyeswest.mvvmtoy;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.keyeswest.mvvmtoy.db.TripDatabase;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;

import java.util.List;

public class DataRepository {
    private static DataRepository sInstance;

    private final TripDatabase mDatabase;
    private MediatorLiveData<List<TripEntity>> mObservableTrips;

    private DataRepository(final TripDatabase database) {
        mDatabase = database;
        mObservableTrips = new MediatorLiveData<>();

        mObservableTrips.addSource(mDatabase.tripDao().loadAllTrips(),
                tripEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableTrips.postValue(tripEntities);
                    }
                });
    }


    public static DataRepository getInstance(final TripDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<TripEntity>> getTrips() {
        return mObservableTrips;
    }
}
