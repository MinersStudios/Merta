package com.minersstudios.genero.lib.util;

import static android.graphics.Color.TRANSPARENT;
import static com.google.android.material.color.MaterialColors.isColorLight;

import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;

import com.google.android.material.color.MaterialColors;

import org.jetbrains.annotations.Contract;

public class EdgeToEdgeUtil {

    @Contract(" -> fail")
    private EdgeToEdgeUtil() throws AssertionError {
        throw new AssertionError("Util class");
    }

    public static void applyEdgeToEdge(
            final @NonNull Window window,
            final boolean edgeToEdgeEnabled
    ) {
        applyEdgeToEdge(window, edgeToEdgeEnabled, null, null);
    }

    public static void applyEdgeToEdge(
            final @NonNull Window window,
            final boolean edgeToEdgeEnabled,
            @Nullable @ColorInt Integer statusBarOverlapBackgroundColor,
            @Nullable @ColorInt Integer navigationBarOverlapBackgroundColor
    ) {
        final boolean useDefaultBackgroundColorForStatusBar =
                statusBarOverlapBackgroundColor == null
                || statusBarOverlapBackgroundColor == 0;
        final boolean useDefaultBackgroundColorForNavigationBar =
                navigationBarOverlapBackgroundColor == null
                || navigationBarOverlapBackgroundColor == 0;

        if (useDefaultBackgroundColorForStatusBar || useDefaultBackgroundColorForNavigationBar) {
            final int defaultBackgroundColor =
                    MaterialColors.getColor(
                            window.getContext(),
                            android.R.attr.colorBackground,
                            Color.BLACK
                    );

            if (useDefaultBackgroundColorForStatusBar) {
                statusBarOverlapBackgroundColor = defaultBackgroundColor;
            }

            if (useDefaultBackgroundColorForNavigationBar) {
                navigationBarOverlapBackgroundColor = defaultBackgroundColor;
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, !edgeToEdgeEnabled);

        final int statusBarColor = getStatusBarColor(window.getContext(), edgeToEdgeEnabled);
        final int navigationBarColor = getNavigationBarColor(window.getContext(), edgeToEdgeEnabled);

        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(navigationBarColor);

        setLightStatusBar(
                window,
                isUsingLightSystemBar(
                        statusBarColor, isColorLight(statusBarOverlapBackgroundColor)
                )
        );
        setLightNavigationBar(
                window,
                isUsingLightSystemBar(
                        navigationBarColor, isColorLight(navigationBarOverlapBackgroundColor)
                )
        );
    }

    public static void setLightStatusBar(
            final @NonNull Window window,
            final boolean isLight
    ) {
        WindowCompat
        .getInsetsController(window, window.getDecorView())
        .setAppearanceLightStatusBars(isLight);
    }

    public static void setLightNavigationBar(
            final @NonNull Window window,
            final boolean isLight
    ) {
        WindowCompat
        .getInsetsController(window, window.getDecorView())
        .setAppearanceLightNavigationBars(isLight);
    }

    private static int getStatusBarColor(
            final Context context,
            final boolean isEdgeToEdgeEnabled
    ) {
        return isEdgeToEdgeEnabled
               ? TRANSPARENT
               : MaterialColors.getColor(context, android.R.attr.statusBarColor, Color.BLACK);
    }

    private static int getNavigationBarColor(
            final Context context,
            final boolean isEdgeToEdgeEnabled
    ) {
        return isEdgeToEdgeEnabled
               ? TRANSPARENT
               : MaterialColors.getColor(context, android.R.attr.navigationBarColor, Color.BLACK);
    }

    private static boolean isUsingLightSystemBar(
            final int systemBarColor,
            final boolean isLightBackground
    ) {
        return isColorLight(systemBarColor) || (systemBarColor == TRANSPARENT && isLightBackground);
    }
}
