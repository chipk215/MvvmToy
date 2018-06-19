package com.keyeswest.mvvmtoy.viewmodel;

import com.keyeswest.mvvmtoy.db.entity.TripEntity;

public  class TripViewModel {

    public  TripEntity tripEntity;

    public  boolean selected;

    public TripViewModel(TripEntity trip, boolean selected){
        tripEntity = trip;
        this.selected = selected;
    }

    public TripEntity getTripEntity() {
        return tripEntity;
    }

    public boolean isSelected() {
        return selected;
    }
}
