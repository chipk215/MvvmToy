package com.keyeswest.mvvmtoy.db;

import com.keyeswest.mvvmtoy.db.entity.TripEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {

    private static final int NUMBER_RECORDS = 5;

    private static final Double MIN_LAT = 45.0;
    private static final Double MAX_LAT = 47.0;
    private static final Double MIN_LON = -116.0;
    private static final Double MAX_LON = -115.0;


    public static List<TripEntity> generateTrips(){
        List<TripEntity> trips = new ArrayList<>(5);
        // now in seconds
        long now = new java.util.Date().getTime() / 1000;
        // last week
        long past = now - 604800L;

        double increment = 0.0d;
        for (int i=0; i< NUMBER_RECORDS; i++){
            TripEntity trip = new TripEntity();
            trip.setId(UUID.randomUUID());
            trip.setTimeStamp(ThreadLocalRandom.current().nextLong(past, now));
            trip.setFavorite(false);
            trip.setMinLatitude(MIN_LAT + increment);
            trip.setMaxLatitude(MAX_LAT + increment);
            trip.setMinLongitude(MIN_LON + increment);
            trip.setMaxLongitude(MAX_LON + increment);
            trip.setDuration(500L);
            trip.setDistance((double) ThreadLocalRandom.current().nextLong(100, 90000));
            increment += 0.1d;

            trips.add(trip);
        }

        return trips;
    }
}