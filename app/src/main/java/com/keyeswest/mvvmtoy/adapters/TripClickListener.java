package com.keyeswest.mvvmtoy.adapters;


import com.keyeswest.mvvmtoy.db.entity.TripEntity;
import com.keyeswest.mvvmtoy.model.Trip;

public interface TripClickListener {
    void onItemChecked(TripEntity trip);

    void onItemUnchecked(TripEntity trip);

    void onDeleteClick(TripEntity trip);

    void onFavoriteClick(TripEntity trip);
}
