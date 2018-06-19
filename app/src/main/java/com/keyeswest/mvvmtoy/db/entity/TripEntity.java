package com.keyeswest.mvvmtoy.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

@Entity(tableName="trip")
public class TripEntity  {

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

    public TripEntity(TripEntity trip){
        id = UUID.fromString(trip.id.toString());
        timeStamp = trip.timeStamp;
        favorite = trip.favorite;
        minLatitude = trip.minLatitude;
        maxLatitude = trip.maxLatitude;
        minLongitude = trip.minLongitude;
        maxLongitude = trip.maxLongitude;
        distance = trip.distance;
        duration = trip.duration;

    }

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


    public Double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(Double minLatitude) {
        this.minLatitude = minLatitude;
    }


    public Double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(Double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }


    public Double getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(Double minLongitude) {
        this.minLongitude = minLongitude;
    }


    public Double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(Double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }


    public long getDuration() {
        return duration;
    }

    public void setDuration(long elapsedTime) {
        duration = elapsedTime;
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

}
