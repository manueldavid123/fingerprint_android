package com.mdgarcia.android.utils.manager;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.mdgarcia.android.utils.model.Fingerprint;
import com.mdgarcia.android.utils.repository.FingerprintRepository;

import java.util.List;

public class CustomFingerprintManager {

    private FingerprintRepository fingerprintRepository;

    public CustomFingerprintManager(Application application) {
        this.fingerprintRepository = new FingerprintRepository(
                application
        );
    }

    public void addFingerprint(boolean isAccepted, boolean valid, boolean read) {
        Fingerprint fingerprint = new Fingerprint(isAccepted, valid, read);

        this.fingerprintRepository.insert(fingerprint);
    }

    public int getFingerprints() {
        return this.fingerprintRepository.getAllFingerprints();
    }

    public LiveData<List<Fingerprint>> getAllAcceptedAndValid() {
        return this.fingerprintRepository.getAllAcceptedAndValid();
    }

    public LiveData<List<Fingerprint>> getAllNotAcceptedAndValid() {
        return this.fingerprintRepository.getAllNotAcceptedAndValid();
    }

    public LiveData<List<Fingerprint>> getAllAcceptedAndNotValid() {
        return this.fingerprintRepository.getAllAcceptedAndNotValid();
    }

    public LiveData<List<Fingerprint>> getNotAcceptedNotValid() {
        return this.fingerprintRepository.getNotAcceptedNotValid();
    }

    public void removeFingerprint(Fingerprint fingerprint) {
        this.fingerprintRepository.remove(fingerprint);
    }

    public LiveData<List<Fingerprint>> getAllLive() {
        return this.fingerprintRepository.getAllLive();
    }

    public LiveData<List<Fingerprint>> getNotRead() {
        return this.fingerprintRepository.getNotRead();
    }
}
