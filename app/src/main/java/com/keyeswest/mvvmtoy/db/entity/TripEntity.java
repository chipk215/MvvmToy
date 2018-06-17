package com.keyeswest.mvvmtoy.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.gson.annotations.SerializedName;
import com.keyeswest.mvvmtoy.R;
import com.keyeswest.mvvmtoy.model.Trip;
import com.keyeswest.mvvmtoy.utilities.PluralHelpers;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

@Entity(tableName="trip")
public class TripEntity implements Trip {

    @PrimaryKey
    @NonNull
    @SerializedName("ID")
    private UUID id;

    @SerializedName("TimeStamp")
    private long timeStamp;

    private boolean favorite;

    @SerializedName("MinLat")
    private Double minLatitude ;

    @SerializedName("MaxLat")
    private Double maxLatitude ;

    @SerializedName("MinLon")
    private Double minLongitude;

    @SerializedName("MaxLon")
    private Double maxLongitude;

    @SerializedName("Distance")
    private Double distance;

    @SerializedName("Duration")
    private long duration ;

    public TripEntity(){}

    public TripEntity(UUID id, long timeStamp, boolean favorite, double minLatitude,
                         double maxLatitude, double minLongitude, double maxLongitude,
                         double distance, long elapsedTime){
        this.id = id;
        this.timeStamp = timeStamp;
        this.favorite = favorite;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.distance = distance;
        duration = elapsedTime;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public Double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(Double minLatitude) {
        this.minLatitude = minLatitude;
    }

    @Override
    public Double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(Double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    @Override
    public Double getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(Double minLongitude) {
        this.minLongitude = minLongitude;
    }

    @Override
    public Double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(Double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public void setDuration(long elapsedTime) {
        duration = elapsedTime;
    }

    @Override
    public String getDate(){
        Date date = new Date(timeStamp * 1000);
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);

    }

    @Override
    public String getTime(){
        Date date = new Date(timeStamp * 1000);
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(date);

    }

    @Override
    public String getDistanceMiles(){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double METERS_TO_MILES = 0.000621371;
        return decimalFormat.format(distance * METERS_TO_MILES);
    }

    @Override
    public boolean equals(Object o){

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Segment or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof TripEntity)) {
            return false;
        }

        TripEntity segment = (TripEntity) o;

        return id.equals(segment.getId());

    }

    @Override
    public String getMileOrMiles(Context context){
        return context.getResources().getQuantityString(R.plurals.miles_plural,
                PluralHelpers.getPluralQuantity(getDistance()));

    }

    @Override
    public Drawable getFavoriteImage(Context context){

        return favorite ? ContextCompat.getDrawable(context,R.drawable.fav_star_filled)
                : ContextCompat.getDrawable(context,R.drawable.fav_star_border);

    }
}
