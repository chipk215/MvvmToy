package com.keyeswest.mvvmtoy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.keyeswest.mvvmtoy.DataRepository;
import com.keyeswest.mvvmtoy.MainApp;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;

import java.util.List;

public class TripListViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<TripEntity>> mObservableTrips;

    public TripListViewModel(@NonNull Application application) {
        super(application);

        mObservableTrips = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableTrips.setValue(null);

        LiveData<List<TripEntity>> trips = ((MainApp) application).getRepository()
                .getTrips();

        // observe the changes of trips from the database and forward them
        mObservableTrips.addSource(trips, mObservableTrips::setValue);
    }

    /**
     * Expose the LiveData Trip query so the UI can observe it.
     */
    public LiveData<List<TripEntity>> getTrips() {
        return mObservableTrips;
    }


}