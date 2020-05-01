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

    public Fingerprint[] getFingerprints() {
        return this.fingerprintRepository.getAllFingerprints();
    }

    public void removeFingerprint(Fingerprint fingerprint) {
        this.fingerprintRepository.remove(fingerprint);
    }

    public LiveData<List<Fingerprint>> getAllLive() {
        return this.fingerprintRepository.getAllLive();
    }
}
