package com.minersstudios.genero.app.ui.button.handler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.minersstudios.genero.lib.ui.button.ActionButton;
import com.minersstudios.genero.lib.ui.button.ButtonHandler;

public class PeopleButtonHandler implements ButtonHandler {

    @Override
    public boolean onClick(final @NonNull ActionButton view) {
        Log.d("PeopleButtonHandler", "onClick: PeopleButtonHandler clicked!");

        return true;
    }

    @Override
    public boolean isClickable() {
        return true;
    }
}
