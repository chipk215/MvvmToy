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


    private static final int MAX_TRIPS_SELECTABLE = 4;

    private final MediatorLiveData<List<TripEntity>> mObservableTrips;

    private int mTripsSelected;

    public TripListViewModel(@NonNull Application application) {
        super(application);

        mObservableTrips = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableTrips.setValue(null);

        LiveData<List<TripEntity>> trips = ((MainApp) application).getRepository()
                .getTrips();

        // observe the changes of trips from the database and forward them
        mObservableTrips.addSource(trips, mObservableTrips::setValue);

        mTripsSelected = 0;
    }

    /**
     * Expose the LiveData Trip query so the UI can observe it.
     */
    public LiveData<List<TripEntity>> getTrips() {
        return mObservableTrips;
    }



    public void handleTripSelected(TripEntity trip){

        if (trip.isSelected()){
            // un-select the trip
            trip.setSelected(false);
            if (mTripsSelected > 0){
                mTripsSelected--;
            }
        }else{
            // select the trip unless trip limit has been reached
            if (mTripsSelected < MAX_TRIPS_SELECTABLE ){
                trip.setSelected(true);
                mTripsSelected++;
            }
        }

    }




}
