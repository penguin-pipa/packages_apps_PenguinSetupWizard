/*
 * Copyright (C) 2026 The PenguinOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.setupwizard.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import org.lineageos.setupwizard.R;

import java.util.ArrayList;
import java.util.List;

public final class Cards {

    private static final String TAG_GROUP = "cards";
    private static final String TAG_SINGLE = "card";

    private Cards() {}

    public static void apply(View view) {
        if (TAG_GROUP.equals(view.getTag())) {
            styleGroup((ViewGroup) view);
        } else if (TAG_SINGLE.equals(view.getTag())) {
            styleRow(view, true, true, false, false);
        }
        if (view instanceof ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                apply(group.getChildAt(i));
            }
        }
    }

    private static void styleGroup(ViewGroup group) {
        List<View> rows = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child.getVisibility() != View.GONE) rows.add(child);
        }
        for (int i = 0; i < rows.size(); i++) {
            styleRow(rows.get(i), i == 0, i == rows.size() - 1, i > 0, true);
        }
    }

    private static void styleRow(
            View row, boolean first, boolean last, boolean gapAbove, boolean grouped) {
        Context context = row.getContext();
        float large = context.getResources().getDimension(R.dimen.setup_card_radius);
        float small = context.getResources().getDimension(R.dimen.setup_card_radius_inner);
        float top = first ? large : small;
        float bottom = last ? large : small;
        float[] radii = {top, top, top, top, bottom, bottom, bottom, bottom};

        GradientDrawable fill = new GradientDrawable();
        fill.setCornerRadii(radii);
        fill.setColor(context.getColor(R.color.setup_card_background));
        GradientDrawable mask = new GradientDrawable();
        mask.setCornerRadii(radii);
        mask.setColor(0xFFFFFFFF);
        row.setBackground(new RippleDrawable(highlight(context), fill, mask));

        int horizontal = context.getResources().getDimensionPixelSize(R.dimen.setup_card_padding);
        int vertical = context.getResources().getDimensionPixelSize(R.dimen.setup_card_padding_vertical);
        row.setPaddingRelative(horizontal, vertical, horizontal, vertical);
        row.setMinimumHeight(
                context.getResources().getDimensionPixelSize(R.dimen.setup_card_min_height));
        if (row instanceof CompoundButton button) {
            button.setMinHeight(
                    context.getResources().getDimensionPixelSize(R.dimen.setup_card_min_height));
            Drawable radio = button.getButtonDrawable();
            if (row instanceof RadioButton && radio != null && !(radio instanceof InsetDrawable)) {
                button.setButtonDrawable(new InsetDrawable(radio, horizontal, 0, 0, 0));
                button.setPaddingRelative(horizontal / 2, vertical, horizontal, vertical);
            }
        }

        ViewGroup.LayoutParams params = row.getLayoutParams();
        if (params instanceof ViewGroup.MarginLayoutParams margins) {
            margins.width = ViewGroup.LayoutParams.MATCH_PARENT;
            if (grouped) {
                margins.topMargin = gapAbove
                        ? context.getResources().getDimensionPixelSize(R.dimen.setup_card_gap)
                        : 0;
                margins.bottomMargin = 0;
            }
            margins.leftMargin = 0;
            margins.rightMargin = 0;
            margins.setMarginStart(0);
            margins.setMarginEnd(0);
            row.setLayoutParams(margins);
        }
    }

    private static ColorStateList highlight(Context context) {
        TypedArray a = context.obtainStyledAttributes(
                new int[] {android.R.attr.colorControlHighlight});
        ColorStateList color = a.getColorStateList(0);
        a.recycle();
        return color != null ? color : ColorStateList.valueOf(0x1F000000);
    }
}
