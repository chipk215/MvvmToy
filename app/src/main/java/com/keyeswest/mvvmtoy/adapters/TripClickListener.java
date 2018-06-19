package com.keyeswest.mvvmtoy.adapters;


import android.view.View;

import com.keyeswest.mvvmtoy.db.entity.TripEntity;
import com.keyeswest.mvvmtoy.model.Trip;

public interface TripClickListener {

    void onDeleteClick(TripEntity trip);

    void onTripClicked(TripEntity trip);

    void onFavoriteClick(TripEntity trip);


}
