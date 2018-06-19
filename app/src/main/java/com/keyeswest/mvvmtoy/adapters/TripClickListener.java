package com.keyeswest.mvvmtoy.adapters;


import android.view.View;

import com.keyeswest.mvvmtoy.db.entity.TripEntity;
import com.keyeswest.mvvmtoy.model.Trip;
import com.keyeswest.mvvmtoy.viewmodel.TripViewModel;

public interface TripClickListener {

    void onDeleteClick(TripEntity trip);

    void onTripClicked(TripViewModel trip);

    void onFavoriteClick(TripEntity trip);


}
