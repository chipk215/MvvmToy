package com.keyeswest.mvvmtoy.adapters;

import com.keyeswest.mvvmtoy.db.entity.TripEntity;

import com.keyeswest.mvvmtoy.viewmodel.TripViewModel;

public interface TripClickListener {

    void onDeleteClick(TripEntity trip);

    void onTripClicked(TripViewModel trip);

    void onFavoriteClick(TripEntity trip);


}
