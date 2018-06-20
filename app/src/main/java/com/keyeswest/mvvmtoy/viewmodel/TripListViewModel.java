package com.keyeswest.mvvmtoy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.keyeswest.mvvmtoy.DataRepository;
import com.keyeswest.mvvmtoy.MainApp;
import com.keyeswest.mvvmtoy.R;
import com.keyeswest.mvvmtoy.SortPreferenceEnum;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;
import com.keyeswest.mvvmtoy.utilities.FilterSharedPreferences;
import com.keyeswest.mvvmtoy.utilities.SortSharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

public class TripListViewModel extends AndroidViewModel {

    private static final int MAX_TRIPS_SELECTABLE = 4;

    private final MediatorLiveData<List<TripEntity>> mObservableTrips;

    //TODO - Is this a good idea to store POJO list item view models in the AndroidViewModel
    // The benefit is that the data is saved on config changes which is what is needed.

    //Collection of POJO Trip View Models  (not LiveData)
    private List<TripViewModel> mModels;

    // A count of the umber of selected (checked) trips
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

    /**
     * Creates a TripViewModel for each trip.
     * Stores the collection of POJO view models in the TripListVewModel.
     *
     * @param trips - trip entity data obtained from database
     * @return - List of TripViewModel wrapped trips
     */
    public List<TripViewModel> updateTrips(List<TripEntity> trips) {

        List<TripViewModel> newList = new ArrayList<>();
        List<TripViewModel> displayList;
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

        // Now that comparisons have been made between old list and new list, set the old
        // list to the new list
        mModels = newList;

        // filter the list for viewing if needed
        displayList = filterTrips();

        //sort the list for viewing if needed
        displayList = sort(displayList);

        return displayList;
    }



    public List<TripViewModel> filterTrips() {

        boolean dateFilter;
        boolean favoriteFilter;
        long startDate = 0L;
        long endDate = Integer.MAX_VALUE;

        List<TripViewModel> displayList = new ArrayList<>();

        Context context = mApplication.getApplicationContext();

        // determine filter types
        dateFilter = FilterSharedPreferences.isDateRangeSet(context);
        if (dateFilter) {
            startDate = FilterSharedPreferences.getStartDate(context);
            endDate = FilterSharedPreferences.getEndDate(context);
        }


        favoriteFilter = FilterSharedPreferences.getFavoriteFilterSetting(context);

        if (dateFilter || favoriteFilter) {
            for (TripViewModel tripModel : mModels) {
                TripEntity trip = tripModel.tripEntity;

                if (dateFilter && favoriteFilter) {
                    long tripTime = trip.getTimeStamp();
                    if ((tripTime >= startDate) && (tripTime <= endDate) && trip.isFavorite()) {
                        displayList.add(tripModel);
                    }
                } else if (dateFilter) {
                    long tripTime = trip.getTimeStamp();
                    if ((tripTime >= startDate) && (tripTime <= endDate)) {
                        displayList.add(tripModel);
                    }

                } else if (favoriteFilter && trip.isFavorite()) {
                    displayList.add(tripModel);

                }
            }
        } else {
            displayList = mModels;
        }

        return displayList;
    }




    public List<TripViewModel> getAllTrips() {
        return mModels;
    }

    private List<TripViewModel> sort(List<TripViewModel> sourceList) {
        Context context = mApplication.getApplicationContext();

        List<TripViewModel> sorted = sourceList;
        String sortByCode = SortSharedPreferences.getSortByCode(context);
        SortPreferenceEnum sortPreference = SortPreferenceEnum.lookupByCode(sortByCode);

        switch (sortPreference) {
            case NEWEST:
                Collections.sort(sorted, new SortTripViewModelByDate());
                break;
            case OLDEST:
                Collections.sort(sorted, Collections.reverseOrder(new SortTripViewModelByDate()));
                break;

            case LONGEST:
                Collections.sort(sorted, Collections.reverseOrder(new SortTripViewModelByDistance()));
                break;

            case SHORTEST:
                Collections.sort(sorted, new SortTripViewModelByDistance());
                break;

            default:
                Collections.sort(sorted, new SortTripViewModelByDate());

        }

        return sorted;
    }


    public List<TripViewModel> sort() {
        return sort(mModels);
    }


    // Determine if a trip is in the existing list of trip models
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


    // A trip had been checked or unchecked in the UI.
    public void handleTripSelected(TripViewModel model) {

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


}
