package com.keyeswest.mvvmtoy.ui;

import com.keyeswest.mvvmtoy.db.entity.TripEntity;

public interface TripClickCallback {
    void onClick(TripEntity trip);
}
