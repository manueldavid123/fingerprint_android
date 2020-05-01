package com.mdgarcia.android.utils.manager;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mdgarcia.android.utils.dao.FingerprintDao;
import com.mdgarcia.android.utils.model.Fingerprint;
import com.mdgarcia.android.utils.repository.FingerprintRepository;

import java.util.List;

public class FingerprintViewModel extends AndroidViewModel {

    private LiveData<List<Fingerprint>> myAllItems;

    public FingerprintViewModel(Application application) {
        super(application);
        CustomFingerprintManager fingerprintManager = new CustomFingerprintManager(application);
        myAllItems = fingerprintManager.getAllLive();
    }

    public LiveData<List<Fingerprint>> getAllItems() { return myAllItems; }

}
