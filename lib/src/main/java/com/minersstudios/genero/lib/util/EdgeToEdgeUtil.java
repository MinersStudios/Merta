package com.minersstudios.genero.lib.util;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.TRANSPARENT;
import static com.google.android.material.color.MaterialColors.getColor;
import static com.google.android.material.color.MaterialColors.isColorLight;

import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.core.view.WindowCompat;

import org.jetbrains.annotations.Contract;

public final class EdgeToEdgeUtil {

    @Contract(" -> fail")
    private EdgeToEdgeUtil() throws AssertionError {
        throw new AssertionError("Util class");
    }

    public static void apply(
            final @NonNull Window window,
            final boolean edgeToEdgeEnabled
    ) {
        final Context context = window.getContext();
        final int statusBarColor = getStatusBarColor(context, edgeToEdgeEnabled);
        final int navigationBarColor = getNavigationBarColor(context, edgeToEdgeEnabled);
        final boolean isLightBackground = isColorLight(getBackgroundColor(context));

        WindowCompat.setDecorFitsSystemWindows(window, !edgeToEdgeEnabled);
        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(navigationBarColor);

        setLightStatusBar(
                window,
                isUsingLightSystemBar(
                        statusBarColor, isLightBackground
                )
        );
        setLightNavigationBar(
                window,
                isUsingLightSystemBar(
                        navigationBarColor, isLightBackground
                )
        );
    }

    private static int getBackgroundColor(final Context context) {
        return getColor(context, android.R.attr.colorBackground, BLACK);
    }

    private static int getStatusBarColor(
            final Context context,
            final boolean isEdgeToEdgeEnabled
    ) {
        return isEdgeToEdgeEnabled
               ? TRANSPARENT
               : getColor(context, android.R.attr.statusBarColor, BLACK);
    }

    private static int getNavigationBarColor(
            final Context context,
            final boolean isEdgeToEdgeEnabled
    ) {
        return isEdgeToEdgeEnabled
               ? TRANSPARENT
               : getColor(context, android.R.attr.navigationBarColor, BLACK);
    }

    private static boolean isUsingLightSystemBar(
            final int systemBarColor,
            final boolean isLightBackground
    ) {
        return isColorLight(systemBarColor)
                || (
                        systemBarColor == TRANSPARENT
                        && isLightBackground
                );
    }

    private static void setLightStatusBar(
            final @NonNull Window window,
            final boolean isLight
    ) {
        WindowCompat
        .getInsetsController(window, window.getDecorView())
        .setAppearanceLightStatusBars(isLight);
    }

    private static void setLightNavigationBar(
            final @NonNull Window window,
            final boolean isLight
    ) {
        WindowCompat
        .getInsetsController(window, window.getDecorView())
        .setAppearanceLightNavigationBars(isLight);
    }
}
