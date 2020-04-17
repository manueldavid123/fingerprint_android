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
    @Query("SELECT * FROM fingerprints")
    public Fingerprint[] getAll();
}
