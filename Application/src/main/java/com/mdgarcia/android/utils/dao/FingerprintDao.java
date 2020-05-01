package com.mdgarcia.android.utils.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mdgarcia.android.utils.model.Fingerprint;

@Dao
public interface FingerprintDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertFingerprints(Fingerprint... fingerprints);
    @Delete
    public void deleteFingerprints(Fingerprint... fingerprints);
    @Query("SELECT * FROM fingerprints")
    public Fingerprint[] getAll();
    @Query("SELECT * FROM fingerprints where accepted = 1")
    public Fingerprint[] getAccepted();
    @Query("SELECT * FROM fingerprints where accepted = 0")
    public Fingerprint[] getNotAccepted();
    @Query("SELECT * FROM fingerprints where accepted = 1 and valid = 1")
    public Fingerprint[] getAcceptedValid();
    @Query("SELECT * FROM fingerprints where accepted = 1 and valid = 0")
    public Fingerprint[] getAcceptedNotValid();
    @Query("SELECT * FROM fingerprints where accepted = 0 and valid = 0")
    public Fingerprint[] getNotAcceptedNotValid();
    @Query("SELECT * FROM fingerprints where accepted = 0 and valid = 1")
    public Fingerprint[] getNotAcceptedValid();
    @Query("SELECT * FROM fingerprints where read = 0")
    public Fingerprint[] getNotRead();

}
