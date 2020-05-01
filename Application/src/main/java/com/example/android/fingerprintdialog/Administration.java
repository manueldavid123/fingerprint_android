package com.example.android.fingerprintdialog;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mdgarcia.android.utils.manager.CustomFingerprintManager;
import com.mdgarcia.android.utils.manager.FingerprintViewModel;
import com.mdgarcia.android.utils.model.Fingerprint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Administration extends AppCompatActivity {
    private ListView listview;
    private CustomFingerprintManager customFingerprintManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);
        customFingerprintManager = new CustomFingerprintManager(getApplication());
        listview = (ListView) findViewById(R.id.list);
        FingerprintViewModel fingerprintViewModel = ViewModelProviders.of(this).get(FingerprintViewModel.class);

        fingerprintViewModel.getAllItems().observe(this, new Observer<List<Fingerprint>>() {
            @Override
            public void onChanged(@Nullable List<Fingerprint> fingerprints) {
                if(fingerprints != null) {
                    ListAdapterView adapterView = new ListAdapterView(getApplication(), R.layout.list_item, fingerprints, customFingerprintManager);
                    listview.setAdapter(adapterView);
                    adapterView.notifyDataSetChanged();
                }
            }
        });
    }
}
