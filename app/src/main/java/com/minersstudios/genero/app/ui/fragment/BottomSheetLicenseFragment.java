package com.minersstudios.genero.app.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.minersstudios.genero.app.R;
import com.minersstudios.genero.app.databinding.BottomSheetAppLicenseBinding;
import com.minersstudios.genero.app.databinding.FragmentHomeBinding;
import com.minersstudios.genero.app.ui.navigation.home.HomeViewModel;
import com.minersstudios.genero.lib.util.EdgeToEdgeUtil;

public class BottomSheetLicenseFragment extends BottomSheetDialogFragment {

    public BottomSheetLicenseFragment() {
        super(R.layout.bottom_sheet_app_license);
    }

    @Override
    public @NonNull Dialog onCreateDialog(final @Nullable Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final Window window = dialog.getWindow();

        if (window != null) {
            EdgeToEdgeUtil.applyEdgeToEdge(
                    dialog.getWindow(),
                    true
            );
        }

        return dialog;
    }
}
