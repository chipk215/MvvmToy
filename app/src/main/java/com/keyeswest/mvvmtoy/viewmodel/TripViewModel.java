package com.keyeswest.mvvmtoy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.keyeswest.mvvmtoy.db.entity.TripEntity;

public class TripViewModel  extends AndroidViewModel {

    private final MediatorLiveData<TripEntity> mObservableTrip;

    //checkbox select state
    private boolean mSelected;

    public TripViewModel(@NonNull Application application, TripEntity trip){

        super(application);
        mObservableTrip = new MediatorLiveData<>();
        mObservableTrip.setValue(trip);
    }

    public void setSelected(){
        mSelected = true;
    }

    public boolean isSelected(){
        return mSelected;
    }

}
