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
import com.keyeswest.mvvmtoy.db.entity.TripEntity;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class TripListViewModel extends AndroidViewModel implements TripClickListener {


    private static final int MAX_TRIPS_SELECTABLE = 4;

    private final MediatorLiveData<List<TripEntity>> mObservableTrips;

    private List<TripViewModel> mModels;

    private int mTripsSelected;

    private Application mApplication;

    private DataRepository mDataRepository;

    public TripListViewModel(@NonNull Application application) {
        super(application);

        Timber.d("Constructing TripListViewModel");

        mApplication = application;

        mObservableTrips = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableTrips.setValue(null);

        mDataRepository = ((MainApp) application).getRepository();
        LiveData<List<TripEntity>> trips = mDataRepository.getTrips();

        // observe the changes of trips from the database and forward them
        mObservableTrips.addSource(trips, mObservableTrips::setValue);

        mTripsSelected = 0;

        mModels = new ArrayList<>();
    }

    /**
     * Expose the LiveData Trip query so the UI can observe it.
     */
    public LiveData<List<TripEntity>> getTrips() {
        return mObservableTrips;
    }


    public List<TripViewModel> updateTrips(List<TripEntity> trips) {
        List<TripViewModel> newList = new ArrayList<>();
        for (TripEntity trip : trips) {
            // determine if we have an existing model corresponding to a trip
            TripViewModel match = findModelMatch(trip);
            if (match != null) {
                newList.add(match);
            } else {
                TripViewModel model = new TripViewModel(trip, false);
                newList.add(model);
            }
        }

        mModels = newList;

        return newList;
    }


    private TripViewModel findModelMatch(TripEntity trip) {
        TripViewModel result = null;
        for (TripViewModel model : mModels) {
            if (model.tripEntity.getId().equals(trip.getId())) {
                result = model;
                break;
            }
        }

        return result;
    }

    private void handleTripSelected(TripViewModel model) {

        if (model.isSelected()) {
            // un-select the trip
            model.setSelected(false);
            if (mTripsSelected > 0) {
                mTripsSelected--;
            }
        } else {
            // select the trip unless trip limit has been reached
            if (mTripsSelected < MAX_TRIPS_SELECTABLE) {
                model.setSelected(true);
                mTripsSelected++;
            } else {
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
    public void onTripClicked(TripViewModel model) {
        Timber.d("on trip clicked");
        handleTripSelected(model);
    }

    @Override
    public void onFavoriteClick(TripEntity trip) {
        // flip the favorite status
        Timber.d("Favorite clicked");
        Timber.d("Current status %s", Boolean.toString(trip.isFavorite()));
        trip.setFavorite(!trip.isFavorite());
        Timber.d("New status %s", Boolean.toString(trip.isFavorite()));

        //update database which will update view
        mDataRepository.update(trip);

    }


}
