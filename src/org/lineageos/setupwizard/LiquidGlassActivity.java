/*
 * Copyright (C) 2026 The PenguinOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.setupwizard;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;

import org.lineageos.setupwizard.widget.Cards;

public class LiquidGlassActivity extends BaseSetupWizardActivity {

    private static final String SETTING_LIQUID_GLASS = "penguin_liquid_glass";
    private static final String SETTING_QS_PANEL_STYLE = "qs_panel_style";
    private static final String SETTING_DUAL_SHADE = "dual_shade";
    private static final String SETTING_COS_EXPERIENCE = "penguin_cos_experience";
    private static final String SETTING_LIQUID_GLASS_STYLE = "penguin_liquid_glass_style";
    private static final int LIQUID_GLASS_STYLE_COS = 1;
    private static final int QS_PANEL_STYLE_PENGUIN = 1;

    private CompoundButton mLiquidGlass;
    private CompoundButton mSeparateQs;
    private CompoundButton mCosExperience;
    private View mExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNextText(R.string.next);
        getGlifLayout().setDescriptionText(getString(R.string.liquid_glass_description));

        mLiquidGlass = findViewById(R.id.liquid_glass_switch);
        mSeparateQs = findViewById(R.id.separate_qs_switch);
        mCosExperience = findViewById(R.id.cos_experience_switch);
        mExtras = findViewById(R.id.liquid_glass_extras);

        findViewById(R.id.liquid_glass).setOnClickListener(v -> {
            mLiquidGlass.toggle();
            updateExtras();
        });
        findViewById(R.id.separate_qs).setOnClickListener(v -> mSeparateQs.toggle());
        findViewById(R.id.cos_experience).setOnClickListener(v -> mCosExperience.toggle());
        mCosExperience.setChecked(Settings.Secure.getInt(
                getContentResolver(), SETTING_COS_EXPERIENCE, 0) != 0);

        mLiquidGlass.setChecked(Settings.Secure.getInt(
                getContentResolver(), SETTING_LIQUID_GLASS, 0) != 0);
        updateExtras();
    }

    private void updateExtras() {
        mExtras.setVisibility(mLiquidGlass.isChecked() ? View.VISIBLE : View.GONE);
        Cards.apply(mExtras);
    }

    @Override
    protected void onNextPressed() {
        final boolean glass = mLiquidGlass.isChecked();
        Settings.Secure.putInt(getContentResolver(), SETTING_LIQUID_GLASS, glass ? 1 : 0);
        if (glass && mSeparateQs.isChecked()) {
            Settings.Secure.putInt(getContentResolver(), SETTING_QS_PANEL_STYLE,
                    QS_PANEL_STYLE_PENGUIN);
            Settings.Secure.putInt(getContentResolver(), SETTING_DUAL_SHADE, 1);
        }
        if (glass) {
            final boolean cos = mCosExperience.isChecked();
            Settings.Secure.putInt(getContentResolver(), SETTING_COS_EXPERIENCE, cos ? 1 : 0);
            if (cos) {
                Settings.Secure.putInt(getContentResolver(), SETTING_LIQUID_GLASS_STYLE,
                        LIQUID_GLASS_STYLE_COS);
            }
        }
        super.onNextPressed();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.setup_liquid_glass;
    }

    @Override
    protected int getTitleResId() {
        return R.string.liquid_glass_title;
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_liquid_glass;
    }
}
