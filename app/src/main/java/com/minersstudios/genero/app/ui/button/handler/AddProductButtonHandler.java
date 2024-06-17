package com.minersstudios.genero.app.ui.button.handler;

import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.minersstudios.genero.lib.ui.button.ActionButton;
import com.minersstudios.genero.lib.ui.button.ButtonHandler;

public class AddProductButtonHandler implements ButtonHandler {

    @Override
    public void onClick(final @NonNull ActionButton view) {
        Log.d("AddProductButtonHandler", "onClick: AddProductButtonHandler clicked!");
    }

    @Override
    public void onTouch(
            final @NonNull ActionButton view,
            final @NonNull MotionEvent event
    ) {
        Log.d("AddProductButtonHandler", "onTouch: AddProductButtonHandler touched!");
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public boolean isTouchable() {
        return true;
    }
}
