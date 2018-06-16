package com.keyeswest.mvvmtoy.model;

import android.content.Context;

import java.util.UUID;

//Used for data binding
public interface Trip {
    String getTime();
    String getDate();
    String getDistanceMiles();
    String getMileOrMiles(Context context);

    UUID getId();

    Double getMinLatitude();
    Double getMaxLatitude();
    Double getMinLongitude();
    Double getMaxLongitude();

    boolean isFavorite();
    long getDuration();



}
