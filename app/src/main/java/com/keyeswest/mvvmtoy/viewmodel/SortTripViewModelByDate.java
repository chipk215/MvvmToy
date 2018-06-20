package com.keyeswest.mvvmtoy.viewmodel;


import com.keyeswest.mvvmtoy.viewmodel.TripViewModel;

import java.util.Comparator;

public class SortTripByDate implements Comparator<TripViewModel> {

    @Override
    public int compare(TripViewModel tripModel1, TripViewModel tripModel2) {
        if (tripModel1.tripEntity.getTimeStamp() < tripModel2.tripEntity.getTimeStamp()){
            return -1;
        }else if (tripModel1.tripEntity.getTimeStamp() > tripModel2.tripEntity.getTimeStamp()){
            return 1;
        }

        return 0;
    }


}
