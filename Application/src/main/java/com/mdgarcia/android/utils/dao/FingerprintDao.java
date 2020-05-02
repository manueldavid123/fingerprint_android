package com.mdgarcia.android.utils.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mdgarcia.android.utils.model.Fingerprint;

import java.util.List;

@Dao
public interface FingerprintDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertFingerprints(Fingerprint... fingerprints);
    @Delete
    public void deleteFingerprints(Fingerprint... fingerprints);
    @Query("SELECT count(*) FROM fingerprints")
    public int getAll();
    @Query("SELECT * FROM fingerprints where accepted = 1 and valid = 1 and read = 1")
    public LiveData<List<Fingerprint>> getAcceptedValid();
    @Query("SELECT * FROM fingerprints where accepted = 0 and valid = 1 and read = 1")
    public LiveData<List<Fingerprint>> getNotAcceptedValid();
    @Query("SELECT * FROM fingerprints where accepted = 1 and valid = 0 and read = 1")
    public LiveData<List<Fingerprint>> getAcceptedNotValid();
    @Query("SELECT * FROM fingerprints where accepted = 0 and valid = 0 and read = 1")
    public LiveData<List<Fingerprint>> getNotAcceptedNotValid();
    @Query("SELECT * FROM fingerprints where read = 0")
    public LiveData<List<Fingerprint>> getNotRead();
    @Query("SELECT * FROM fingerprints")
    public LiveData<List<Fingerprint>> getAllLive();

}
