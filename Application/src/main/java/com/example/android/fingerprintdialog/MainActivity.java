/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.example.android.fingerprintdialog;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mdgarcia.android.utils.manager.CustomFingerprintManager;
import com.mdgarcia.android.utils.manager.FingerprintViewModel;
import com.mdgarcia.android.utils.model.Fingerprint;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Main entry point for the sample, showing a backpack and "Purchase" button.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String SECRET_MESSAGE = "Very secret message";
    private static final String KEY_NAME_NOT_INVALIDATED = "key_not_invalidated";
    static final String DEFAULT_KEY_NAME = "default_key";

    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private SharedPreferences mSharedPreferences;
    private CustomFingerprintManager customFingerprintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }
        try {
            mKeyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
        }
        Cipher defaultCipher;
        Cipher cipherNotInvalidated;
        try {
            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipherNotInvalidated = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
        Button purchaseButton = (Button) findViewById(R.id.purchase_button);
        Button adminButton = (Button) findViewById(R.id.admin_fingerprints);

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            purchaseButton.setEnabled(false);
            // This happens when no fingerprints are registered.
            Toast.makeText(this,
                    "Al menos una huella tiene que estar configurada. Por favor, diríjase a su configuración y configure una.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        createKey(DEFAULT_KEY_NAME, true);
        createKey(KEY_NAME_NOT_INVALIDATED, false);
        purchaseButton.setEnabled(true);
        purchaseButton.setOnClickListener(
                new PurchaseButtonClickListener(defaultCipher, DEFAULT_KEY_NAME));

        adminButton.setEnabled(true);
        adminButton.setOnClickListener(new AdminButtonClickListener());
        customFingerprintManager = new CustomFingerprintManager(getApplication());

        showConfirmation();

    }

    /**
     * Initialize the {@link Cipher} instance with the created key in the
     * {@link #createKey(String, boolean)} method.
     *
     * @param keyName the key name to init the cipher
     * @return {@code true} if initialization is successful, {@code false} if the lock screen has
     * been disabled or reset after the key was generated, or if a fingerprint got enrolled after
     * the key was generated.
     */
    private boolean initCipher(Cipher cipher, String keyName) {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    /**
     * Proceed the purchase operation
     *
     * @param withFingerprint {@code true} if the purchase was made by using a fingerprint
     * @param cryptoObject the Crypto object
     */
    public void onPurchased(boolean withFingerprint,
            @Nullable FingerprintManager.CryptoObject cryptoObject) {
        if (withFingerprint) {
            // If the user has authenticated with fingerprint, verify that using cryptography and
            // then show the confirmation message.
            assert cryptoObject != null;
            tryEncrypt(cryptoObject.getCipher());
        } else {
            // Authentication happened with backup password. Just show the confirmation message.
            showConfirmation();
        }
    }

    // Show confirmation, if fingerprint was used show crypto information.
    private void showConfirmation() {
        final TextView totalWidget = (TextView) findViewById(R.id.total);
        final TextView totalAcceptedWidget = (TextView) findViewById(R.id.totalAccepted);
        final TextView totalNotAcceptedWidget = (TextView) findViewById(R.id.totalNotAccepted);
        final TextView totalErrors = (TextView) findViewById(R.id.totalError);
        final TextView farWidget = (TextView) findViewById(R.id.far);
        final TextView frrWidget = (TextView) findViewById(R.id.frr);
        final TextView notRead = (TextView) findViewById(R.id.fta);

        FingerprintViewModel fingerprintViewModel = ViewModelProviders.of(this).get(FingerprintViewModel.class);

        fingerprintViewModel.getAllItems().observe(this, new Observer<List<Fingerprint>>() {
            @Override
            public void onChanged(@Nullable List<Fingerprint> fingerprints) {
                totalWidget.setText(fingerprints.size() + "");
            }
        });

        fingerprintViewModel.getAllAcceptedAndValid().observe(this, new Observer<List<Fingerprint>>() {
            @Override
            public void onChanged(@Nullable List<Fingerprint> fingerprints) {
                totalAcceptedWidget.setText(fingerprints.size() + "");
            }
        });

        fingerprintViewModel.getAllNotAcceptedAndValid().observe(this, new Observer<List<Fingerprint>>() {
            @Override
            public void onChanged(@Nullable List<Fingerprint> fingerprints) {
                totalNotAcceptedWidget.setText(fingerprints.size() + "");
            }
        });

        fingerprintViewModel.getAllAcceptedAndNotValid().observe(this, new Observer<List<Fingerprint>>() {
            @Override
            public void onChanged(@Nullable List<Fingerprint> fingerprints) {
                int allAcceptedAndNotValid = fingerprints.size();
                int allFingerprintsCount = customFingerprintManager.getFingerprints();
                float percentage = 0;
                if (allFingerprintsCount > 0) {
                    percentage = (float) allAcceptedAndNotValid / allFingerprintsCount * 100;
                }

                farWidget.setText(percentage + " %");
            }
        });

        fingerprintViewModel.getNotAcceptedNotValid().observe(this, new Observer<List<Fingerprint>>() {
            @Override
            public void onChanged(@Nullable List<Fingerprint> fingerprints) {
                int notAcceptedAndValid = fingerprints.size();
                int allFingerprintsCount = customFingerprintManager.getFingerprints();

                float percentage = 0;
                if (allFingerprintsCount > 0) {
                    percentage = (float) notAcceptedAndValid / allFingerprintsCount * 100;
                }

                frrWidget.setText(percentage + " %");
            }
        });

        fingerprintViewModel.getNotRead().observe(this, new Observer<List<Fingerprint>>() {
            @Override
            public void onChanged(@Nullable List<Fingerprint> fingerprints) {
                int notReadCount = fingerprints.size();
                int allFingerprintsCount = customFingerprintManager.getFingerprints();

                float percentage = 0;
                if (allFingerprintsCount > 0) {
                    percentage = (float) notReadCount / allFingerprintsCount * 100;
                }
                notRead.setText(percentage + " %");
                totalErrors.setText(notReadCount + "");
            }
        });
    }

    /**
     * Tries to encrypt some data with the generated key in {@link #createKey} which is
     * only works if the user has just authenticated via fingerprint.
     */
    private void tryEncrypt(Cipher cipher) {
        try {
            byte[] encrypted = cipher.doFinal(SECRET_MESSAGE.getBytes());
            showConfirmation();
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            showConfirmation();
        }
    }

    /**
     * Creates a symmetric key in the Android Key Store which can only be used after the user has
     * authenticated with fingerprint.
     *
     * @param keyName the name of the key to be created
     * @param invalidatedByBiometricEnrollment if {@code false} is passed, the created key will not
     *                                         be invalidated even if a new fingerprint is enrolled.
     *                                         The default value is {@code true}, so passing
     *                                         {@code true} doesn't change the behavior
     *                                         (the key will be invalidated if a new fingerprint is
     *                                         enrolled.). Note that this parameter is only valid if
     *                                         the app works on Android N developer preview.
     *
     */
    public void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore.load(null);
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder

            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            // This is a workaround to avoid crashes on devices whose API level is < 24
            // because KeyGenParameterSpec.Builder#setInvalidatedByBiometricEnrollment is only
            // visible on API level +24.
            // Ideally there should be a compat library for KeyGenParameterSpec.Builder but
            // which isn't available yet.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
            }
            mKeyGenerator.init(builder.build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private class PurchaseButtonClickListener implements View.OnClickListener {

        Cipher mCipher;
        String mKeyName;

        PurchaseButtonClickListener(Cipher cipher, String keyName) {
            mCipher = cipher;
            mKeyName = keyName;
        }

        @Override
        public void onClick(View view) {

            // Set up the crypto object for later. The object will be authenticated by use
            // of the fingerprint.
            if (initCipher(mCipher, mKeyName)) {

                // Show the fingerprint dialog. The user has the option to use the fingerprint with
                // crypto, or you can fall back to using a server-side verified password.
                FingerprintAuthenticationDialogFragment fragment
                        = new FingerprintAuthenticationDialogFragment();
                fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
                boolean useFingerprintPreference = mSharedPreferences
                        .getBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                                true);
                if (useFingerprintPreference) {
                    fragment.setStage(
                            FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
                } else {
                    fragment.setStage(
                            FingerprintAuthenticationDialogFragment.Stage.PASSWORD);
                }
                fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            } else {
                // This happens if the lock screen has been disabled or or a fingerprint got
                // enrolled. Thus show the dialog to authenticate with their password first
                // and ask the user if they want to authenticate with fingerprints in the
                // future
                FingerprintAuthenticationDialogFragment fragment
                        = new FingerprintAuthenticationDialogFragment();
                fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
                fragment.setStage(
                        FingerprintAuthenticationDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
                fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            }
        }
    }

    private static class AdminButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), Administration.class);
            v.getContext().startActivity(intent);
        }
    }
}
