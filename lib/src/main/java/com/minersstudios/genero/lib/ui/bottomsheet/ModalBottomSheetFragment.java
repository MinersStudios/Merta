package com.minersstudios.genero.lib.ui.bottomsheet;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.*;
import static com.minersstudios.genero.lib.ui.bottomsheet.ModalBottomSheetLayout.DISMISS_OFFSET;

import android.app.Dialog;
import android.os.Bundle;
import android.view.*;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.ApiStatus;

/**
 * A dialog fragment that displays a modal bottom sheet.
 * <br>
 * The layout resource ID must be a {@link ModalBottomSheetLayout}.
 *
 * @see ModalBottomSheetLayout
 * @noinspection unused
 */
public class ModalBottomSheetFragment extends BottomSheetDialogFragment {
    private @LayoutRes int mLayoutResId;
    private Callback mCallback;

    public static final String ARG_LAYOUT_RES_ID = "layoutResId";

    /**
     * Creates a new instance of {@link ModalBottomSheetFragment} with the
     * specified layout resource ID
     *
     * @param layoutResId The modal bottom sheet layout resource ID
     */
    public ModalBottomSheetFragment(final @LayoutRes int layoutResId) {
        this();
        this.initLayout(layoutResId);
    }

    /**
     * Creates a new instance of {@link ModalBottomSheetFragment}
     */
    public ModalBottomSheetFragment() {
        super();
    }

    /**
     * Returns the modal bottom sheet layout
     *
     * @return The modal bottom sheet layout or null if this was called before
     *         {@link #onStart()}
     */
    public @Nullable ModalBottomSheetLayout getModal() {
        final View view = this.getView();

        if (view instanceof ModalBottomSheetLayout) {
            return (ModalBottomSheetLayout) view;
        }

        return null;
    }

    /**
     * Returns the modal bottom sheet layout
     *
     * @return The modal bottom sheet layout
     * @throws IllegalStateException If the modal layout is not set or this was
     *                               called before {@link #onStart()}
     */
    public @NonNull ModalBottomSheetLayout requireModal() {
        final ModalBottomSheetLayout modal = this.getModal();

        if (modal == null) {
            throw new IllegalStateException(
                    "Fragment " + this + " does not have a modal layout or this was called before onStart()"
            );
        }

        return modal;
    }

    /**
     * Returns the callback of the bottom sheet dialog
     *
     * @return The callback if it is set or null if this was called before
     *         {@link #onCreateDialog(Bundle)}
     */
    public @Nullable Callback getCallback() {
        return this.mCallback;
    }

    /**
     * Sets the callback of the bottom sheet dialog
     *
     * @param callback The callback
     */
    @ApiStatus.Internal
    protected void setCallback(final @Nullable Callback callback) {
        this.mCallback = callback;
    }

    /**
     * Returns the callback
     *
     * @return The callback
     * @throws IllegalStateException If the callback is not set or this was
     *                               called before {@link #onCreateDialog(Bundle)}
     */
    public @NonNull Callback requireCallback() {
        final Callback callback = this.getCallback();

        if (callback == null) {
            throw new IllegalStateException(
                    "Fragment " + this + " does not have a callback or this was called before onCreateDialog(Bundle)"
            );
        }

        return callback;
    }

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = this.getArguments();

        if (arguments != null) {
            this.mLayoutResId = arguments.getInt(ARG_LAYOUT_RES_ID);
        }
    }

    @Override
    public @NonNull Dialog onCreateDialog(final @Nullable Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final Callback callback = this.createCallback(dialog);

        this.setCallback(callback);

        if (callback != null) {
            dialog.getBehavior().addBottomSheetCallback(callback);
        }

        return dialog;
    }

    @Override
    public @NonNull View onCreateView(
            final @NonNull LayoutInflater inflater,
            final @Nullable ViewGroup container,
            final @Nullable Bundle savedInstanceState
    ) {
        final ModalBottomSheetLayout modal = this.createModal(inflater, container);

        modal.applyAttributes(
                (BottomSheetDialog) this.requireDialog(),
                this.getCallback()
        );

        return modal;
    }

    @Override
    public void onStart() {
        super.onStart();

        final ModalBottomSheetLayout modal = this.requireModal();

        if (modal.hasBottomView()) {
            modal.showBottomContainer(this.requireDialog());
        }
    }

    /**
     * Initializes the layout resource ID
     *
     * @param layoutResId The layout resource ID
     */
    protected void initLayout(final @LayoutRes int layoutResId) {
        final Bundle arguments = new Bundle();

        arguments.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        this.setArguments(arguments);
    }

    /**
     * Creates a new instance of callback.
     * <br>
     * Can be overridden to provide a custom implementation of callback.
     *
     * @param dialog The bottom sheet dialog that the callback is attached to
     * @return The new instance of {@code Callback} or null if the callback is
     *         not needed
     */
    protected @Nullable Callback createCallback(final @NonNull BottomSheetDialog dialog) {
        return new Callback(dialog);
    }

    private @NonNull ModalBottomSheetLayout createModal(
            final @NonNull LayoutInflater inflater,
            final @Nullable ViewGroup container
    ) throws IllegalArgumentException {
        if (this.mLayoutResId != 0) {
            final View view = inflater.inflate(
                    this.mLayoutResId,
                    container,
                    false
            );

            if (view instanceof ModalBottomSheetLayout) {
                return (ModalBottomSheetLayout) view;
            }
        }

        throw new IllegalArgumentException(
                "The layout resource ID must be a ModalBottomSheetLayout"
        );
    }

    /**
     * Callback for monitoring events about modal bottom sheets
     */
    public static class Callback extends BottomSheetCallback {
        private final BottomSheetDialog dialog;
        private float mDismissOffset;
        private float mSlideOffset;
        private int mPreviousStableState;

        /**
         * Creates a new instance of {@link Callback}
         *
         * @param dialog The bottom sheet dialog that the callback is attached to
         */
        protected Callback(final @NonNull BottomSheetDialog dialog) {
            this.dialog = dialog;
            this.mDismissOffset = DISMISS_OFFSET;
            this.mPreviousStableState = STATE_COLLAPSED;
        }

        /**
         * Returns the bottom sheet dialog that the callback is attached to
         *
         * @return The bottom sheet dialog that the callback is attached to
         */
        public @NonNull BottomSheetDialog getDialog() {
            return this.dialog;
        }

        /**
         * Returns the offset that the bottom sheet should be dismissed.
         * <br>
         * The default value is {@link ModalBottomSheetLayout#DISMISS_OFFSET}.
         *
         * @return The offset that the bottom sheet should be dismissed
         */
        public float getDismissOffset() {
            return this.mDismissOffset;
        }

        /**
         * Sets the offset that the bottom sheet should be dismissed
         *
         * @param dismissOffset New offset
         */
        public void setDismissOffset(final float dismissOffset) {
            this.mDismissOffset = dismissOffset;
        }

        /**
         * Returns current slide offset
         *
         * @return Current slide offset
         */
        public float getSlideOffset() {
            return this.mSlideOffset;
        }

        /**
         * Sets the current slide offset
         *
         * @param slideOffset New slide offset
         */
        protected void setSlideOffset(final float slideOffset) {
            this.mSlideOffset = slideOffset;
        }

        /**
         * Returns the previous stable state
         *
         * @return The previous stable state
         */
        public int getPreviousStableState() {
            return this.mPreviousStableState;
        }

        /**
         * Sets the previous stable state
         *
         * @param previousStableState New previous stable state
         */
        protected void setPreviousStableState(final int previousStableState) {
            this.mPreviousStableState = previousStableState;
        }

        @Override
        public void onStateChanged(
                final @NonNull View bottomSheet,
                final int newState
        ) {
            switch (newState) {
                case STATE_SETTLING:
                    final BottomSheetBehavior<?> behavior = this.getDialog().getBehavior();

                    if (
                            behavior.isFitToContents()
                            && this.getPreviousStableState() == STATE_COLLAPSED
                            && this.getSlideOffset() < -behavior.getHideFriction()
                    ) {
                        behavior.setState(STATE_HIDDEN);
                    }

                    break;
                case STATE_COLLAPSED:
                case STATE_EXPANDED:
                case STATE_HALF_EXPANDED:
                    this.setPreviousStableState(newState);
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
            this.setSlideOffset(slideOffset);

            final BottomSheetDialog dialog = this.getDialog();
            final BottomSheetBehavior<?> behavior = dialog.getBehavior();

            if (
                    behavior.getState() != STATE_DRAGGING
                    && behavior.isHideable()
                    && slideOffset <= this.getDismissOffset()
            ) {
                dialog.dismiss();
            }
        }
    }
}
