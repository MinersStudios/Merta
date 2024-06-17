package com.minersstudios.genero.app.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.minersstudios.genero.app.R;
import com.minersstudios.genero.app.databinding.ActivityPreferencesBinding;

public final class PreferencesActivity extends AppCompatActivity {
    private ActivityPreferencesBinding binding;

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityPreferencesBinding.inflate(this.getLayoutInflater());

        this.setupMargins();
        this.setupToolbar();
        this.setContentView(this.binding.getRoot());
    }

    private void setupMargins() {
        final ViewGroup.LayoutParams toolbarParams = this.binding.toolbar.getLayoutParams();

        if (toolbarParams instanceof ViewGroup.MarginLayoutParams) {
            final int statusBarHeight = this.getStatusBarHeight();

            ((ViewGroup.MarginLayoutParams) toolbarParams).topMargin += statusBarHeight;
            this.binding.collapsingToolbarLayout.getLayoutParams().height += statusBarHeight;
            this.binding.collapsingToolbarLayout.setScrimVisibleHeightTrigger(
                    this.binding.collapsingToolbarLayout.getScrimVisibleHeightTrigger() + statusBarHeight
            );

            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void setupToolbar() {
        this.binding.toolbar.setNavigationOnClickListener(v -> {
            this.binding.toolbar.performHapticFeedback(0);
            this.binding.toolbar.playSoundEffect(SoundEffectConstants.CLICK);
            this.getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private int getStatusBarHeight() {
        final Resources resources = this.getResources();
        @SuppressWarnings({"InternalInsetResource", "DiscouragedApi"})
        final int resourceId = resources.getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
        );

        if (resourceId > 0) {
            try {
                return resources.getDimensionPixelSize(resourceId);
            } catch (final Resources.NotFoundException e) {
                Log.e(
                        this.getClass().getSimpleName(),
                        "Failed to get status bar height resource.",
                        e
                );
            }
        }

        return resources.getDimensionPixelSize(R.dimen.preferences_status_bar_default_top_padding);
    }

    public static class PreferencesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(
                final @Nullable Bundle savedInstanceState,
                final @Nullable String rootKey
        ) {
            this.setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }
}
