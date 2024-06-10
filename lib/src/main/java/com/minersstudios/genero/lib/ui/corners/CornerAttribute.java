package com.minersstudios.genero.lib.ui.corners;

import static com.minersstudios.genero.lib.ui.corners.CornerType.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import javax.annotation.concurrent.Immutable;

/**
 * Represents a corner attribute of a rectangle
 *
 * @see CornerType
 */
@Immutable
public final class CornerAttribute implements Comparable<CornerAttribute> {
    private final byte value;

    public static final byte NO_CORNERS =  0;
    public static final byte ALL_CORNERS = 15;

    private static final CornerAttribute NO_CORNERS_ATTRIBUTE =  new CornerAttribute(NO_CORNERS);
    private static final CornerAttribute ALL_CORNERS_ATTRIBUTE = new CornerAttribute(ALL_CORNERS);

    private CornerAttribute(final byte value) {
        this.value = value;
    }

    /**
     * Returns the byte value of this corner attribute
     *
     * @return The byte value of this corner attribute
     */
    public byte getValue() {
        return this.value;
    }

    /**
     * Adds the given corner types to the current corner attribute
     *
     * @param first The first corner type
     * @param rest  The rest of the corner types
     * @return A new corner attribute with added corner types to the current one
     */
    @Contract("_, _ -> new")
    public @NonNull CornerAttribute with(
            final @Nullable CornerType first,
            final @Nullable CornerType... rest
    ) {
        byte value = addCorner(this.value, first);

        if (rest != null) {
            for (final CornerType type : rest) {
                value = addCorner(value, type);
            }
        }

        return new CornerAttribute(value);
    }

    /**
     * Removes the given corner types from the current corner attribute
     *
     * @param first The first corner type
     * @param rest  The rest of the corner types
     * @return A new corner attribute with removed corner types from the current
     *         one
     */
    @Contract("_, _ -> new")
    public @NonNull CornerAttribute without(
            final @Nullable CornerType first,
            final @Nullable CornerType... rest
    ) {
        byte value = removeCorner(this.value, first);

        if (rest != null) {
            for (final CornerType type : rest) {
                value = removeCorner(value, type);
            }
        }

        return new CornerAttribute(value);
    }

    /**
     * Returns whether the current corner attribute has the given corner type
     *
     * @param type The corner type
     * @return True if the current corner attribute has the given corner type
     * @see #has(byte)
     */
    public boolean has(final @NonNull CornerType type) {
        return this.has(type.getValue());
    }

    /**
     * Returns whether the current corner attribute has the given corner type
     * value
     *
     * @param value The byte value of the {@link CornerType corner type}
     * @return True if the current corner attribute has the given corner type
     *         value
     */
    public boolean has(final byte value) {
        return hasCorner(this.value, value);
    }

    /**
     * Returns whether the current corner attribute has all the given corner
     * types
     *
     * @param first The first corner type
     * @param rest  The rest of the corner types
     * @return True if the current corner attribute has all the given corner
     *         types
     * @see #has(CornerType)
     */
    public boolean hasAll(
            final @NonNull CornerType first,
            final @NonNull CornerType... rest
    ) {
        if (!this.has(first)) {
            return false;
        }

        for (final CornerType type : rest) {
            if (!this.has(type)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the current corner attribute has any of the given corner
     * types
     *
     * @param first The first corner type
     * @param rest  The rest of the corner types
     * @return True if the current corner attribute has any of the given corner
     *         types
     * @see #has(CornerType)
     */
    public boolean hasAny(
            final @NonNull CornerType first,
            final @NonNull CornerType... rest
    ) {
        if (this.has(first)) {
            return true;
        }

        for (final CornerType type : rest) {
            if (this.has(type)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether the current corner attribute has no corners
     *
     * @return True if the current corner attribute has no corners
     */
    public boolean hasNoCorners() {
        return this.value == NO_CORNERS;
    }

    /**
     * Returns whether the current corner attribute has all corners
     *
     * @return True if the current corner attribute has all corners
     */
    public boolean hasAllCorners() {
        return this.value == ALL_CORNERS;
    }

    /**
     * Returns the number of corners in the current corner attribute
     *
     * @return The number of corners in the current corner attribute
     */
    public int count() {
        return this.toArray().length;
    }

    /**
     * Returns the hash code of this corner attribute
     *
     * @return The hash code of this corner attribute
     */
    @Override
    public int hashCode() {
        return Byte.hashCode(this.value);
    }

    /**
     * Compares this corner attribute with the given one.
     * <p>
     * <b>NOTE:</b> The comparison is based on the {@link #getValue() value} of
     *              the corner attribute.
     *
     * @param attribute The attribute to be compared
     * @return The value {@code 0} if {@code this == attribute};
     *         a value less than {@code 0} if {@code this < attribute}; and
     *         a value greater than {@code 0} if {@code this > attribute}
     *         or {@code 1} if {@code attribute} is null
     */
    @Override
    public int compareTo(final @Nullable CornerAttribute attribute) {
        return attribute == null
                ? 1
                : Byte.compare(this.value, attribute.value);
    }

    /**
     * Returns whether the given object is equal to this corner attribute
     *
     * @param obj The attribute object to be compared
     * @return True if the given object is equal to this corner attribute
     */
    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj != null
                        && this.getClass() == obj.getClass()
                        && this.value == ((CornerAttribute) obj).value
                );
    }

    /**
     * Returns a string representation of this corner attribute
     *
     * @return A string representation of this corner attribute
     */
    @Override
    public @NonNull String toString() {
        final String className = this.getClass().getSimpleName();

        if (this.hasNoCorners()) {
            return className + "[0]{}";
        }

        final CornerType[] array = this.toArray();
        final int lastIndex = array.length - 1;
        final StringBuilder builder =
                new StringBuilder(className)
                .append('[')
                .append(this.value)
                .append("]{");

        for (int i = 0; i < array.length; ++i) {
            builder
            .append(array[i])
            .append(i == lastIndex ? '}' : ", ");
        }

        return builder.toString();
    }

    /**
     * Returns an array of corner types that are represented by the current
     * corner attribute
     *
     * @return An array of corner types that are represented by the current
     *         corner attribute
     * @see CornerType#allOf(byte)
     */
    public @NonNull CornerType[] toArray() {
        return CornerType.allOf(this.value);
    }

    /**
     * Returns a corner attribute with no corners
     *
     * @return A corner attribute with no corners
     */
    public static @NonNull CornerAttribute none() {
        return NO_CORNERS_ATTRIBUTE;
    }

    /**
     * Returns a corner attribute with all corners
     *
     * @return A corner attribute with all corners
     */
    public static @NonNull CornerAttribute all() {
        return ALL_CORNERS_ATTRIBUTE;
    }

    /**
     * Returns a corner attribute with the given corner types
     *
     * @param topLeft     True if the top-left corner is present
     * @param topRight    True if the top-right corner is present
     * @param bottomLeft  True if the bottom-left corner is present
     * @param bottomRight True if the bottom-right corner is present
     * @return A corner attribute with the given corner types
     */
    @Contract("_, _, _, _ -> new")
    public static @NonNull CornerAttribute create(
            final boolean topLeft,
            final boolean topRight,
            final boolean bottomLeft,
            final boolean bottomRight
    ) {
        return NO_CORNERS_ATTRIBUTE.with(
                topLeft     ? TOP_LEFT     : null,
                topRight    ? TOP_RIGHT    : null,
                bottomLeft  ? BOTTOM_LEFT  : null,
                bottomRight ? BOTTOM_RIGHT : null
        );
    }

    /**
     * Returns a new corner attribute with the given byte value
     *
     * @param value The byte value
     * @return A new corner attribute with the given byte value
     * @throws IllegalArgumentException If the value is invalid
     * @see #validateValue(byte)
     */
    @Contract("_ -> new")
    public static @NonNull CornerAttribute create(final byte value) throws IllegalArgumentException {
        validateValue(value);

        return new CornerAttribute(value);
    }

    /**
     * Validates the given corner value.
     * <p>
     * If {@code value} < {@link #NO_CORNERS}
     * or {@code value} > {@link #ALL_CORNERS},
     * an {@link IllegalArgumentException} is thrown.
     *
     * @param value The corner value
     * @throws IllegalArgumentException If the value is invalid
     */
    public static void validateValue(final byte value) throws IllegalArgumentException {
        if (
                value < NO_CORNERS
                || value > ALL_CORNERS
        ) {
            throw new IllegalArgumentException("Invalid corner value: " + value);
        }
    }

    private static byte addCorner(
            final byte value,
            final @Nullable CornerType type
    ) {
        if (type == null) {
            return value;
        }

        final byte typeValue = type.getValue();

        return hasCorner(value, typeValue)
                ? value
                : (byte) (value | typeValue);
    }

    private static byte removeCorner(
            final byte value,
            final @Nullable CornerType type
    ) {
        if (type == null) {
            return value;
        }

        final byte typeValue = type.getValue();

        return hasCorner(value, typeValue)
                ? (byte) (value & ~typeValue)
                : value;
    }

    private static boolean hasCorner(
            final byte value,
            final byte typeValue
    ) {
        return (value & typeValue) == typeValue;
    }
}
