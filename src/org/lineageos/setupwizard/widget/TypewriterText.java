/*
 * SPDX-FileCopyrightText: The PenguinOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.setupwizard.widget;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

public final class TypewriterText {

    private static final long TYPE_MS = 90;
    private static final long ERASE_MS = 45;
    private static final long HOLD_MS = 1600;
    private static final long GAP_MS = 260;

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final TextView mView;
    private final String[] mWords;
    private int mWord;
    private int mLength;
    private boolean mErasing;

    private final Runnable mStep = this::step;

    public TypewriterText(TextView view, String[] words) {
        mView = view;
        mWords = words;
    }

    public void start(long delayMs) {
        stop();
        mHandler.postDelayed(mStep, delayMs);
    }

    public void stop() {
        mHandler.removeCallbacks(mStep);
    }

    private void step() {
        String word = mWords[mWord];
        int count = word.codePointCount(0, word.length());
        if (!mErasing) {
            mLength++;
            mView.setText(word.substring(0, word.offsetByCodePoints(0, mLength)));
            if (mLength >= count) {
                mErasing = true;
                mHandler.postDelayed(mStep, HOLD_MS);
                return;
            }
            mHandler.postDelayed(mStep, TYPE_MS);
            return;
        }
        mLength--;
        mView.setText(word.substring(0, word.offsetByCodePoints(0, Math.max(mLength, 0))));
        if (mLength <= 0) {
            mErasing = false;
            mWord = (mWord + 1) % mWords.length;
            mHandler.postDelayed(mStep, GAP_MS);
            return;
        }
        mHandler.postDelayed(mStep, ERASE_MS);
    }
}
