package com.minersstudios.genero.app.ui.fragment;

import androidx.annotation.LayoutRes;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.minersstudios.genero.app.R;
import com.minersstudios.genero.lib.ui.bottomsheet.ModalBottomSheetFragment;

public class AddProductBottomSheet extends ModalBottomSheetFragment {

    public AddProductBottomSheet() {
        this(R.layout.bottom_sheet_add_product);
    }

    private AddProductBottomSheet(final @LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    public void onStart() {
        super.onStart();

        this.requireModal()
            .requireBottomContainer()
            .requireViewById(R.id.cancel_button)
            .setOnClickListener(
                    view -> {
                        final BottomSheetDialog dialog = (BottomSheetDialog) this.requireDialog();
                        final BottomSheetBehavior<?> behavior = dialog.getBehavior();

                        behavior.handleBackInvoked();
                    }
            );
    }
}
