package com.minersstudios.genero.app.ui.preferences;

import android.os.Bundle;
import android.view.SoundEffectConstants;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.minersstudios.genero.app.databinding.ActivityPreferencesBinding;

public final class PreferencesActivity extends AppCompatActivity {
    private ActivityPreferencesBinding binding;

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        this.binding = ActivityPreferencesBinding.inflate(this.getLayoutInflater());

        this.setupToolbar();
        this.setContentView(this.binding.getRoot());
    }

    private void setupToolbar() {
        this.binding.toolbar.setNavigationOnClickListener(v -> {
            this.binding.toolbar.performHapticFeedback(0);
            this.binding.toolbar.playSoundEffect(SoundEffectConstants.CLICK);
            this.getOnBackPressedDispatcher().onBackPressed();
        });
    }
}
