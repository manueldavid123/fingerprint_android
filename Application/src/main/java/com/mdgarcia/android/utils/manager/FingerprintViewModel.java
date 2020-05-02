package com.mdgarcia.android.utils.manager;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.mdgarcia.android.utils.model.Fingerprint;

import java.util.List;

public class FingerprintViewModel extends AndroidViewModel {

    private LiveData<List<Fingerprint>> myAllItems;
    private LiveData<List<Fingerprint>> acceptedValid;
    private LiveData<List<Fingerprint>> acceptedNotValid;
    private LiveData<List<Fingerprint>> notAcceptedValid;
    private LiveData<List<Fingerprint>> notAcceptedNotValid;
    private LiveData<List<Fingerprint>> notRead;

    public FingerprintViewModel(Application application) {
        super(application);
        CustomFingerprintManager fingerprintManager = new CustomFingerprintManager(application);
        myAllItems = fingerprintManager.getAllLive();
        acceptedValid = fingerprintManager.getAllAcceptedAndValid();
        notAcceptedValid = fingerprintManager.getAllNotAcceptedAndValid();
        acceptedNotValid = fingerprintManager.getAllAcceptedAndNotValid();
        notAcceptedNotValid = fingerprintManager.getNotAcceptedNotValid();
        notRead = fingerprintManager.getNotRead();
    }

    public LiveData<List<Fingerprint>> getAllItems() { return myAllItems; }

    public LiveData<List<Fingerprint>> getAllAcceptedAndValid() { return acceptedValid; }

    public LiveData<List<Fingerprint>> getAllNotAcceptedAndValid() { return notAcceptedValid; }

    public LiveData<List<Fingerprint>> getAllAcceptedAndNotValid() { return acceptedNotValid; }

    public LiveData<List<Fingerprint>> getNotAcceptedNotValid() { return notAcceptedNotValid; }

    public LiveData<List<Fingerprint>> getNotRead() { return notRead; }


}
