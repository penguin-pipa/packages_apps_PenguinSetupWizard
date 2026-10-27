/*
 * SPDX-FileCopyrightText: 2016 The CyanogenMod Project
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.setupwizard;

import static org.lineageos.setupwizard.SetupWizardApp.ACTION_EMERGENCY_DIAL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.setupcompat.util.SystemBarHelper;

import org.lineageos.setupwizard.util.Illustrations;
import org.lineageos.setupwizard.util.SetupWizardUtils;
import org.lineageos.setupwizard.widget.TypewriterText;

public class WelcomeActivity extends SubBaseActivity {

    private static final String ACTION_ACCESSIBILITY_SETTINGS =
            "android.settings.ACCESSIBILITY_SETTINGS_FOR_SUW";
    private static final long REVEAL_DELAY_MS = 1500;
    private static final long REVEAL_MS = 600;
    private static final long GREETING_DELAY_MS = 1100;

    private TypewriterText mGreeting;

    @Override
    protected void onStartSubactivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onSetupStart();
        SystemBarHelper.setBackButtonVisible(getWindow(), false);
        setNextText(R.string.start);
        Button startButton = findViewById(R.id.start);
        Button emergButton = findViewById(R.id.emerg_dialer);
        Button skipButton = findViewById(R.id.skip);
        startButton.setOnClickListener(view -> onNextPressed());
        findViewById(R.id.launch_accessibility)
                .setOnClickListener(
                        view -> startSubactivity(new Intent(ACTION_ACCESSIBILITY_SETTINGS)));

        if (SetupWizardUtils.hasTelephony(this)) {
            setSkipText(R.string.emergency_call);
            emergButton.setOnClickListener(
                    view -> startSubactivity(new Intent(ACTION_EMERGENCY_DIAL)));
        } else {
            emergButton.setVisibility(View.GONE);
        }

        LottieAnimationView illustration = findViewById(R.id.welcome_illustration);
        if (illustration != null) {
            Illustrations.play(illustration, R.raw.welcome_logo, false);
        }
        TextView greetingView = findViewById(R.id.welcome_greeting);
        if (greetingView != null) {
            mGreeting = new TypewriterText(greetingView,
                    getResources().getStringArray(R.array.welcome_greetings));
            mGreeting.start(GREETING_DELAY_MS);
        }
        reveal(findViewById(R.id.welcome_title), REVEAL_DELAY_MS);
        View codenameView = findViewById(R.id.welcome_codename);
        if (codenameView != null) {
            reveal(codenameView, REVEAL_DELAY_MS + 120);
        }

        TextView welcomeTitle = findViewById(R.id.welcome_title);
        if (SetupWizardUtils.isManagedProfile(this)) {
            welcomeTitle.setText(getString(R.string.setup_managed_profile_welcome_message));
        } else {
            welcomeTitle.setText(getString(R.string.setup_welcome_message,
                    getString(R.string.os_name)));
        }

        if (Build.TYPE.equals("eng")) {
            skipButton.setVisibility(View.VISIBLE);
            skipButton.setOnClickListener(v -> {
                SetupWizardUtils.finishSetupWizard(WelcomeActivity.this);
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (mGreeting != null) {
            mGreeting.stop();
        }
        super.onDestroy();
    }

    private void reveal(View view, long delayMs) {
        view.setTranslationY(getResources().getDisplayMetrics().density * 16);
        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(delayMs)
                .setDuration(REVEAL_MS)
                .setInterpolator(new PathInterpolator(0.2f, 0f, 0f, 1f));
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.welcome_activity;
    }

    @Override
    protected int getTitleResId() {
        return -1;
    }
}
