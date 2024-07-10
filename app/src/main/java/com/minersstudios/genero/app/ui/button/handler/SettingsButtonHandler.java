package com.minersstudios.genero.app.ui.button.handler;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.minersstudios.genero.lib.ui.button.ActionButton;
import com.minersstudios.genero.lib.ui.button.ButtonHandler;
import com.minersstudios.genero.app.ui.preferences.PreferencesActivity;

public class SettingsButtonHandler implements ButtonHandler {

    @Override
    public boolean onClick(final @NonNull ActionButton button) {
        final Context context = button.getContext();

        if (context != null) {
            context.startActivity(
                    new Intent(
                            context,
                            PreferencesActivity.class
                    )
            );
        }

        return true;
    }

    @Override
    public boolean isClickable() {
        return true;
    }
}
