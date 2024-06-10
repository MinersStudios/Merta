package com.minersstudios.genero.lib.ui.corners;

import static com.minersstudios.genero.lib.ui.corners.CornerAttribute.*;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

/**
 * Represents a corner type of a rectangle
 * <p>
 * Available corner types with their byte values :
 * <ul>
 *     <li>{@link #TOP_LEFT} - 1</li>
 *     <li>{@link #TOP_RIGHT} - 2</li>
 *     <li>{@link #BOTTOM_LEFT} - 4</li>
 *     <li>{@link #BOTTOM_RIGHT} - 8</li>
 * </ul>
 *
 * @see CornerAttribute
 */
public enum CornerType {
    TOP_LEFT((byte) 1),
    TOP_RIGHT((byte) 2),
    BOTTOM_LEFT((byte) 4),
    BOTTOM_RIGHT((byte) 8);

    private final byte value;

    private static final CornerType[] VALUES = values();

    CornerType(final byte value) {
        this.value = value;
    }

    /**
     * Returns the byte value of this corner type
     *
     * @return The byte value of this corner type
     */
    public byte getValue() {
        return this.value;
    }

    /**
     * Returns a string representation of this corner type
     *
     * @return A string representation of this corner type
     */
    @Override
    public @NonNull String toString() {
        return this.name() + '(' + this.value + ')';
    }

    /**
     * Returns the corner type of the given value
     *
     * @param value The byte value of the corner type
     * @return The corner type of the given value, or null if the value is
     *         invalid
     */
    public static @Nullable CornerType of(final byte value) {
        return value == TOP_LEFT.value     ? TOP_LEFT
             : value == TOP_RIGHT.value    ? TOP_RIGHT
             : value == BOTTOM_LEFT.value  ? BOTTOM_LEFT
             : value == BOTTOM_RIGHT.value ? BOTTOM_RIGHT
             : null;
    }

    /**
     * Returns an array of corner types that are represented by the given value
     *
     * @param value The byte value of the corner types
     * @return An array of corner types that are represented by the given value
     *         or an empty array if the value is invalid
     */
    @Contract("_ -> new")
    public static @NonNull CornerType[] allOf(final byte value) {
        if (value == ALL_CORNERS) {
            return VALUES.clone();
        }

        CornerType[] array = new CornerType[0];

        if (value == NO_CORNERS) {
            return array;
        } else if (
                value < NO_CORNERS
                || value > ALL_CORNERS
        ) {
            Log.w("CornerType", "Trying to get all corner types of an invalid value: " + value);

            return array;
        }

        for (final CornerType type : VALUES) {
            if ((value & type.value) == type.value) {
                final CornerType[] newArray = new CornerType[array.length + 1];

                System.arraycopy(array, 0, newArray, 0, array.length);

                newArray[array.length] = type;
                array = newArray;
            }
        }

        return array;
    }
}
