package com.minersstudios.genero.lib.ui.button;

import android.view.MotionEvent;

import androidx.annotation.NonNull;

/**
 * Interface for handling button
 */
public interface ButtonHandler {

    /**
     * Called when a button has been clicked.
     * <p>
     * <b>NOTE:</b> Override {@link #isClickable()} to return {@code true} to
     *              enable this method.
     *
     * @param button The button that was clicked
     */
    default void onClick(final @NonNull ActionButton button) {}

    /**
     * Called when a button has been long clicked.
     * <p>
     * <b>NOTE:</b> Override {@link #isTouchable()} to return {@code true} to
     *              enable this method.
     *
     * @param button The button that was long clicked
     * @param event  The motion event that triggered this method
     */
    default void onTouch(
            final @NonNull ActionButton button,
            final @NonNull MotionEvent event
    ) {}

    /**
     * Returns whether the button is clickable and should trigger the
     * {@link #onClick(ActionButton)} method
     *
     * @return True if the button is clickable and should trigger the
     *         {@link #onClick(ActionButton)} method
     */
    default boolean isClickable() {
        return false;
    }

    /**
     * Returns whether the button is touchable and should trigger the
     * {@link #onTouch(ActionButton, MotionEvent)} method
     *
     * @return True if the button is touchable and should trigger the
     *         {@link #onTouch(ActionButton, MotionEvent)} method
     */
    default boolean isTouchable() {
        return false;
    }
}
