package com.minersstudios.genero.app.ui.button.handler;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.minersstudios.genero.app.ui.fragment.AddProductBottomSheet;
import com.minersstudios.genero.lib.ui.button.ActionButton;
import com.minersstudios.genero.lib.ui.button.ButtonHandler;

public class AddProductButtonHandler implements ButtonHandler {

    @Override
    public boolean onClick(final @NonNull ActionButton view) {
        final Context context = view.getContext();

        if (context instanceof FragmentActivity) {
            final FragmentActivity activity = (FragmentActivity) context;

            new AddProductBottomSheet()
            .show(
                    activity.getSupportFragmentManager(),
                    "AddProductBottomSheetFragment"
            );
        }

        return true;
    }

    @Override
    public boolean isClickable() {
        return true;
    }
}
