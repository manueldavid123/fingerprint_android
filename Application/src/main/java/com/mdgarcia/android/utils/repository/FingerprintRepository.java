package com.mdgarcia.android.utils.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.mdgarcia.android.utils.dao.FingerprintDao;
import com.mdgarcia.android.utils.database.FingerprintDatabase;
import com.mdgarcia.android.utils.model.Fingerprint;

import java.util.List;

public class FingerprintRepository {

    private FingerprintDao fingerprintDao;

    public FingerprintRepository(Application application) {
        FingerprintDatabase db = FingerprintDatabase.getDatabase(application);
        fingerprintDao = db.fingerprintDao();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public int getAllFingerprints() {
        return fingerprintDao.getAll();
    }

    public LiveData<List<Fingerprint>> getAllAcceptedAndValid() {
        return fingerprintDao.getAcceptedValid();
    }

    public LiveData<List<Fingerprint>> getAllNotAcceptedAndValid() {
        return fingerprintDao.getNotAcceptedValid();
    }

    public LiveData<List<Fingerprint>> getAllAcceptedAndNotValid() {
        return fingerprintDao.getAcceptedNotValid();
    }

    public LiveData<List<Fingerprint>> getNotAcceptedNotValid() {
        return fingerprintDao.getNotAcceptedNotValid();
    }

    public LiveData<List<Fingerprint>> getAllNotRead() {
        return fingerprintDao.getNotRead();
    }

    public void insert(final Fingerprint fingerprint) {
        FingerprintDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fingerprintDao.insertFingerprints(fingerprint);
            }
        });
    }

    public void remove(final Fingerprint fingerprint) {
        FingerprintDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fingerprintDao.deleteFingerprints(fingerprint);
            }
        });
    }

    public LiveData<List<Fingerprint>> getAllLive() {
        return fingerprintDao.getAllLive();
    }

    public LiveData<List<Fingerprint>> getNotRead() {
        return fingerprintDao.getNotRead();
    }
}