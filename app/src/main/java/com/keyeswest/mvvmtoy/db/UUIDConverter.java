package com.keyeswest.mvvmtoy.db;

import android.arch.persistence.room.TypeConverter;

import java.util.UUID;

public class UUIDConverter {

    @TypeConverter
    public static UUID toUUID(String identifier){
        return identifier == null ? null : UUID.fromString(identifier);
    }

    @TypeConverter
    public static String toString(UUID uuid){
        return uuid == null ? null : uuid.toString();
    }
}