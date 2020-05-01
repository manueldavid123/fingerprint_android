package com.mdgarcia.android.utils.manager;

import android.app.Application;

import com.mdgarcia.android.utils.model.Fingerprint;
import com.mdgarcia.android.utils.repository.FingerprintRepository;

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
}
