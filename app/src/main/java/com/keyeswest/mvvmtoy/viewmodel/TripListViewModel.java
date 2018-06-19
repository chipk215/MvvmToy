package com.keyeswest.mvvmtoy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.keyeswest.mvvmtoy.DataRepository;
import com.keyeswest.mvvmtoy.MainApp;
import com.keyeswest.mvvmtoy.R;
import com.keyeswest.mvvmtoy.adapters.TripClickListener;
import com.keyeswest.mvvmtoy.db.DataGenerator;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class TripListViewModel extends AndroidViewModel implements TripClickListener{


    private static final int MAX_TRIPS_SELECTABLE = 4;

    private final MediatorLiveData<List<TripEntity>> mObservableTrips;

    private int mTripsSelected;

    private Application mApplication;

    private DataRepository mDataRepository;

    public TripListViewModel(@NonNull Application application) {
        super(application);

        mApplication = application;

        mObservableTrips = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableTrips.setValue(null);

        mDataRepository = ((MainApp) application).getRepository();
        LiveData<List<TripEntity>> trips = mDataRepository.getTrips();

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



    private void handleTripSelected(TripEntity trip){

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
            }else{
                Toast.makeText(mApplication.getApplicationContext(),
                        R.string.max_trips_message, Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onDeleteClick(TripEntity trip) {
        Timber.d("on delete clicked");

        mDataRepository.delete(trip);
    }

    @Override
    public void onTripClicked(TripEntity trip) {
        Timber.d("on trip clicked");
        handleTripSelected(trip);
    }

    @Override
    public void onFavoriteClick(TripEntity trip) {
        // flip the favorite status
        Timber.d("Favorite clicked");
        Timber.d("Current status %s", Boolean.toString(trip.isFavorite()));
        trip.setFavorite(! trip.isFavorite());
        Timber.d("New status %s", Boolean.toString(trip.isFavorite()));

        //update database which will update view
        mDataRepository.update(trip);

    }

    @Override
    public void onFabClicked() {
        Timber.d("Fab clicked insert random trip");
        List<TripEntity> trips = DataGenerator.generateTrips(1);

        mDataRepository.insert(trips.get(0));
    }
}
