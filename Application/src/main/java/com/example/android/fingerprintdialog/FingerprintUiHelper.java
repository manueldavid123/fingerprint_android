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

import android.app.Application;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.mdgarcia.android.utils.manager.CustomFingerprintManager;
import com.mdgarcia.android.utils.model.Fingerprint;


/**
 * Small helper class to manage text/icon around fingerprint authentication UI.
 */
public class FingerprintUiHelper extends FingerprintManager.AuthenticationCallback {

    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;

    private final FingerprintManager mFingerprintManager;
    private final ImageView mIcon;
    private final TextView mErrorTextView;
    private final Callback mCallback;
    private final Switch aSwitch;
    private final Button aButton;
    private CancellationSignal mCancellationSignal;
    private CustomFingerprintManager customFingerprintManager;

    private boolean mSelfCancelled;

    /**
     * Constructor for {@link FingerprintUiHelper}.
     */
    FingerprintUiHelper(FingerprintManager fingerprintManager,
                        ImageView icon,
                        TextView errorTextView,
                        Callback callback,
                        Application application,
                        Switch switchView,
                        Button button
    ) {
        mFingerprintManager = fingerprintManager;
        mIcon = icon;
        mErrorTextView = errorTextView;
        mCallback = callback;
        customFingerprintManager = new CustomFingerprintManager(application);
        aSwitch = switchView;
        aButton = button;
    }

    public boolean isFingerprintAuthAvailable() {
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }

    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        if (!isFingerprintAuthAvailable()) {
            return;
        }
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        mFingerprintManager
                .authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null);
        mIcon.setImageResource(R.drawable.ic_fp_40px);
    }

    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (!mSelfCancelled) {
            showError(errString);
            showFinalButtons(false, false, false);
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        if (FingerprintManager.FINGERPRINT_ERROR_LOCKOUT != helpMsgId) {
            showError(helpString);
            showFinalButtons(false, false, false);
        }

    }

    @Override
    public void onAuthenticationFailed() {
         showError(mIcon.getResources().getString(
                R.string.fingerprint_not_recognized));

        showFinalButtons(false, true, true);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mIcon.setImageResource(R.drawable.ic_fingerprint_success);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.success_color, null));
        mErrorTextView.setText(
                mErrorTextView.getResources().getString(R.string.fingerprint_success));
        showFinalButtons(true, true, true);
    }

    private void showFinalButtons(final boolean isAuthenticated, final boolean read, final boolean showSwitch) {
        this.stopListening();
        aButton.setVisibility(View.VISIBLE);
        if (showSwitch) {
            aSwitch.setVisibility(View.VISIBLE);
        }
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean switchValue = showSwitch && aSwitch.isChecked();
                customFingerprintManager.addFingerprint(isAuthenticated, switchValue, read);

                mCallback.onAuthenticated();
            }
        });
    }

    private void showError(CharSequence error) {
        mIcon.setImageResource(R.drawable.ic_fingerprint_error);
        mErrorTextView.setText(error);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.warning_color, null));
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
    }

    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            mErrorTextView.setTextColor(
                    mErrorTextView.getResources().getColor(R.color.hint_color, null));
            mErrorTextView.setText(
                    mErrorTextView.getResources().getString(R.string.fingerprint_hint));
            mIcon.setImageResource(R.drawable.ic_fp_40px);
        }
    };

    public interface Callback {
        void onAuthenticated();
    }
}
