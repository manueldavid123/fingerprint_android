package com.mdgarcia.android.utils.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.mdgarcia.android.utils.dao.FingerprintDao;
import com.mdgarcia.android.utils.model.Fingerprint;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Fingerprint.class}, version = 3, exportSchema = false)
public abstract class FingerprintDatabase extends RoomDatabase {

    public abstract FingerprintDao fingerprintDao();

    private static volatile FingerprintDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FingerprintDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FingerprintDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FingerprintDatabase.class, "fingerprint_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}