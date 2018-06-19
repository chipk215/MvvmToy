package com.keyeswest.mvvmtoy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.keyeswest.mvvmtoy.db.entity.TripEntity;

public class TripViewModel  extends AndroidViewModel {

    private TripEntity mTrip;

    //checkbox select state
    private MutableLiveData<Boolean> mSelected;

    public TripViewModel(@NonNull Application application, TripEntity trip){

        super(application);
        mTrip = trip;

    }

    public void setSelected(Boolean value){
        mSelected.setValue(value);
    }

    public MutableLiveData<Boolean> getSelected(){
        return mSelected;
    }

    public void setTrip(TripEntity trip){
        this.mTrip = trip;
    }

    public TripEntity getTrip(){
        return mTrip;
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private TripEntity mTrip;


        public Factory(@NonNull Application application, TripEntity trip) {
            mApplication = application;
            mTrip = trip;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TripViewModel(mApplication, mTrip);
        }
    }

}
