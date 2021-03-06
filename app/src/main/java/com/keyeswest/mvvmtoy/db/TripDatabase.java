package com.keyeswest.mvvmtoy.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.keyeswest.mvvmtoy.AppExecutors;
import com.keyeswest.mvvmtoy.db.converter.UUIDConverter;
import com.keyeswest.mvvmtoy.db.dao.TripDao;
import com.keyeswest.mvvmtoy.db.entity.TripEntity;

import java.util.List;


@Database(entities = {TripEntity.class}, version =1, exportSchema = false)
@TypeConverters(UUIDConverter.class)
public abstract class TripDatabase  extends RoomDatabase {

    private static final int NUMBER_SEED_RECORDS = 5;

    private static TripDatabase sInstance;

    private static final String DATABASE_NAME = "trip-database";
    
    public abstract TripDao tripDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();


    public static TripDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (TripDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static TripDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        return Room.databaseBuilder(appContext, TripDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            // Add a delay to simulate a long-running operation

                            // Generate the data for pre-population
                            TripDatabase database = TripDatabase.getInstance(appContext, executors);

                            List<TripEntity> trips = DataGenerator.generateTrips(NUMBER_SEED_RECORDS);

                            insertData(database, trips);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                }).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }


    private static void insertData(final TripDatabase database, final List<TripEntity> trips){
        database.runInTransaction(() -> {
            database.tripDao().insertAll(trips);
        });
    }




    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
    
}
