package com.mdgarcia.android.utils.manager;

import android.app.Application;

import com.mdgarcia.android.utils.model.Fingerprint;
import com.mdgarcia.android.utils.repository.FingerprintRepository;

public class FingerprintManager {

    private FingerprintRepository fingerprintRepository;

    public FingerprintManager(Application application) {
        this.fingerprintRepository = new FingerprintRepository(
                application
        );
    }

    public void addFingerprint(boolean isAccepted) {
        Fingerprint fingerprint = new Fingerprint(isAccepted);

        this.fingerprintRepository.insert(fingerprint);
    }

    public Fingerprint[] getFingerprints() {
        return this.fingerprintRepository.getAllFingerprints();
    }
}
