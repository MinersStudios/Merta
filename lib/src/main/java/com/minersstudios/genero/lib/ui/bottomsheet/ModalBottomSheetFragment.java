package com.minersstudios.genero.lib.ui.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.minersstudios.genero.lib.util.EdgeToEdgeUtil;

public class ModalBottomSheetFragment extends BottomSheetDialogFragment {
    private @LayoutRes int mLayoutResId;

    public static final String ARG_LAYOUT_RES_ID = "layoutResId";

    public ModalBottomSheetFragment(final @LayoutRes int layoutResId) {
        this();
        this.initLayout(layoutResId);
    }

    public ModalBottomSheetFragment() {
        super();
    }

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("ModalBottomSheetFragment", "onCreate");

        final Bundle arguments = this.getArguments();

        if (arguments != null) {
            this.mLayoutResId = arguments.getInt(ARG_LAYOUT_RES_ID);
        }
    }

    @Override
    public @NonNull Dialog onCreateDialog(final @Nullable Bundle savedInstanceState) {
        Log.d("ModalBottomSheetFragment", "onCreateDialog");

        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final BottomSheetBehavior<FrameLayout> behavior = dialog.getBehavior();

        behavior.addBottomSheetCallback(new Callback(dialog));

        return dialog;
    }

    @Override
    public @Nullable View onCreateView(
            final @NonNull LayoutInflater inflater,
            final @Nullable ViewGroup container,
            final @Nullable Bundle savedInstanceState
    ) {
        Log.d("ModalBottomSheetFragment", "onCreateView");

        if (this.mLayoutResId != 0) {
            final View view = inflater.inflate(
                    this.mLayoutResId,
                    container,
                    false
            );

            if (view instanceof ModalBottomSheetLayout) {
                final ModalBottomSheetLayout layout = (ModalBottomSheetLayout) view;
                final BottomSheetDialog dialog = (BottomSheetDialog) this.getDialog();

                if (dialog != null) {
                    final BottomSheetBehavior<FrameLayout> behavior = dialog.getBehavior();

                    behavior.setState(layout.getState());
                    behavior.setSaveFlags(layout.getSaveFlags());
                    behavior.setPeekHeight(layout.getPeekHeight());
                    behavior.setExpandedOffset(layout.getExpandedOffset());
                    behavior.setSignificantVelocityThreshold(layout.getSignificantVelocityThreshold());
                    behavior.setHalfExpandedRatio(layout.getHalfExpandedRatio());
                    behavior.setHideFriction(layout.getHideFriction());
                    behavior.setHideable(layout.isHideable());
                    behavior.setDraggable(layout.isDraggable());
                    behavior.setFitToContents(layout.isFitToContents());
                    behavior.setSkipCollapsed(layout.isSkipCollapsed());
                    behavior.setGestureInsetBottomIgnored(layout.isGestureInsetBottomIgnored());
                    behavior.setShouldRemoveExpandedCorners(layout.isShouldRemoveExpandedCorners());
                    behavior.setUpdateImportantForAccessibilityOnSiblings(layout.isUpdateImportantForAccessibilityOnSiblings());

                    final Window window = dialog.getWindow();

                    if (
                            window != null
                            && layout.isEdgeToEdgeEnabled()
                    ) {
                        EdgeToEdgeUtil.apply(
                                window,
                                dialog.getEdgeToEdgeEnabled()
                        );
                    }
                }

                return layout;
            }
        }

        throw new IllegalArgumentException(
                "The layout resource ID must be a ModalBottomSheetLayout"
        );
    }

    protected void initLayout(final @LayoutRes int layoutResId) {
        final Bundle arguments = new Bundle();

        arguments.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        this.setArguments(arguments);
    }

    private static final class Callback extends BottomSheetBehavior.BottomSheetCallback {
        private final BottomSheetDialog dialog;
        private float slideOffset;
        private int previousStableState;

        Callback(final @NonNull BottomSheetDialog dialog) {
            this.dialog = dialog;
            this.previousStableState = BottomSheetBehavior.STATE_COLLAPSED;
        }

        @Override
        public void onStateChanged(
                final @NonNull View bottomSheet,
                final int newState
        ) {
            switch (newState) {
                case BottomSheetBehavior.STATE_SETTLING:
                    final BottomSheetBehavior<FrameLayout> behavior = this.dialog.getBehavior();

                    if (
                            behavior.isFitToContents()
                            && this.previousStableState == BottomSheetBehavior.STATE_COLLAPSED
                            && this.slideOffset < -behavior.getHideFriction()
                    ) {
                        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }

                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                case BottomSheetBehavior.STATE_EXPANDED:
                case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    this.previousStableState = newState;
                    break;
                default:
                    // Do nothing
            }
        }

        @Override
        public void onSlide(
                final @NonNull View bottomSheet,
                final float slideOffset
        ) {
            this.slideOffset = slideOffset;

            if (
                    this.slideOffset == -1.0f
                    && this.dialog.getBehavior().isHideable()
            ) {
                this.dialog.dismiss();
            }
        }
    }
}
