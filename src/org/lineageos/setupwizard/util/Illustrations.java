/*
 * SPDX-FileCopyrightText: The PenguinOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.setupwizard.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class Illustrations {

    private static final String TAG = "Illustrations";
    private static final String ACCENT = "[0.91, 0.45, 0.2, 1]";
    private static final String INK = "[0.1, 0.1, 0.1, 1]";

    private Illustrations() {
    }

    public static void play(LottieAnimationView view, int rawRes, boolean loop) {
        Context context = view.getContext();
        int accent = color(context, android.R.attr.colorAccent);
        int ink = color(context, android.R.attr.textColorPrimary);
        String json;
        try (InputStream in = context.getResources().openRawResource(rawRes)) {
            json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.w(TAG, "Cannot read illustration", e);
            return;
        }
        json = json.replace(ACCENT, lottieColor(accent)).replace(INK, lottieColor(ink));
        view.setAnimationFromJson(json, rawRes + "-" + accent + "-" + ink);
        view.setRepeatCount(loop ? LottieDrawable.INFINITE : 0);
        view.playAnimation();
    }

    private static String lottieColor(int color) {
        return String.format(Locale.ROOT, "[%.4f, %.4f, %.4f, 1]",
                Color.red(color) / 255f, Color.green(color) / 255f, Color.blue(color) / 255f);
    }

    private static int color(Context context, int attr) {
        TypedArray a = context.obtainStyledAttributes(new int[] {attr});
        try {
            return a.getColor(0, Color.BLACK);
        } finally {
            a.recycle();
        }
    }
}
