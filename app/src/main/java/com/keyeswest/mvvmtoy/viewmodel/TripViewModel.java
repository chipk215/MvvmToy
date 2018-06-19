package com.keyeswest.mvvmtoy.viewmodel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.keyeswest.mvvmtoy.R;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;
import com.keyeswest.mvvmtoy.utilities.PluralHelpers;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import timber.log.Timber;

public class TripViewModel {

    public TripEntity tripEntity;

    public boolean selected;

    public TripViewModel(TripEntity trip, boolean selected) {
        tripEntity = trip;
        this.selected = selected;
    }

    public TripEntity getTripEntity() {
        return tripEntity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public String getDate() {
        Date date = new Date(tripEntity.getTimeStamp() * 1000);
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }


    public String getTime() {
        Date date = new Date(tripEntity.getTimeStamp() * 1000);
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(date);

    }


    public String getDistanceMiles() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double METERS_TO_MILES = 0.000621371;
        return decimalFormat.format(tripEntity.getDistance() * METERS_TO_MILES);
    }


    public String getMileOrMiles(Context context) {
        return context.getResources().getQuantityString(R.plurals.miles_plural,
                PluralHelpers.getPluralQuantity(tripEntity.getDistance()));

    }

    public Drawable getFavoriteImage(Context context) {

        Timber.d("Get Favorite Image");
        if (tripEntity.isFavorite()) {
            Timber.d("Returning favorite filled");
            return ContextCompat.getDrawable(context, R.drawable.fav_star_filled);
        } else {
            Timber.d("Returning (non)favorite border");
            return ContextCompat.getDrawable(context, R.drawable.fav_star_border);
        }


    }
}
