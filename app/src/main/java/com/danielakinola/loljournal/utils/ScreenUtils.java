package com.danielakinola.loljournal.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int item_width = 150;
        return (int) (dpWidth / item_width);
    }
}
