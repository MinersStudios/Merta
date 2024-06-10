package com.minersstudios.genero.lib.ui.button;

import static com.minersstudios.genero.lib.ui.button.ActionButton.Icon.builder;

import static java.lang.Float.floatToIntBits;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import androidx.annotation.*;

import com.google.android.material.shape.ShapeAppearanceModel;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

/**
 * A holder class for {@link ActionButton} parameters
 *
 * @see ActionButton
 * @noinspection unused, UnusedReturnValue
 */
public class ButtonParamHolder {
    private Context context;
    private ButtonHandler handler;
    private @Dimension float size;
    private ColorStateList color;
    private ColorStateList rippleColor;
    private @Dimension float borderWidth;
    private ColorStateList borderColor;
    private ActionButton.Icon icon;
    private int animDuration;
    private Interpolator interpolator;
    private ShapeAppearanceModel shapeAppearanceModel;

    public static final int INVALID = -1;
    public static final ShapeAppearanceModel EMPTY_SHAPE_APPEARANCE_MODEL = ShapeAppearanceModel.builder().build();

    private ButtonParamHolder(final @NonNull Context context) {
        this.context = context;
        this.size = INVALID;
        this.borderWidth = INVALID;
        this.animDuration = INVALID;
    }

    /**
     * Returns the context
     *
     * @return The context
     */
    public @NonNull Context context() {
        return this.context;
    }

    /**
     * Sets the context
     *
     * @param context New context
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder context(final @NonNull Context context) {
        this.context = context;

        return this;
    }

    /**
     * Returns the handler
     *
     * @return The handler, or null if not set
     */
    public @Nullable ButtonHandler handler() {
        return this.handler;
    }

    /**
     * Returns the handler, or the {@code defaultHandler} if not set
     *
     * @param defaultHandler Default handler
     * @return The handler, or the {@code defaultHandler} if not set
     */
    public @UnknownNullability ButtonHandler handlerOr(final @Nullable ButtonHandler defaultHandler) {
        return this.handler == null ? defaultHandler : this.handler;
    }

    /**
     * Sets the handler.
     * <p>
     * If provided class name is null or empty, the handler will be set to null.
     *
     * @param className Button handler class name
     * @param initArgs  Array of objects to be passed as arguments to the
     *                  constructor call
     * @return This instance, for chaining
     * @throws RuntimeException If the class could not be found
     * @see #handler(Class, Object...)
     */
    @Contract("_, _ -> this")
    public @NonNull ButtonParamHolder handler(
            final @Nullable String className,
            final Object... initArgs
    ) throws RuntimeException {
        if (
                className == null
                || className.isEmpty()
        ) {
            return this.handler(null);
        }

        final String packageStr = this.context.getPackageName() + className;

        try {
            final Class<?> clazz = this.context.getClassLoader().loadClass(packageStr);

            return this.handler(clazz.asSubclass(ButtonHandler.class), initArgs);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("Could not find class: " + packageStr);
        }
    }

    /**
     * Sets the handler.
     * <p>
     * If provided class is null, the handler will be set to null.
     *
     * @param clazz    Button handler class
     * @param initArgs Array of objects to be passed as arguments to the
     *                 constructor call
     * @return This instance, for chaining
     * @throws RuntimeException If the class could not be instantiated
     * @see #handler(ButtonHandler)
     */
    @Contract("_, _ -> this")
    public @NonNull ButtonParamHolder handler(
            final @Nullable Class<? extends ButtonHandler> clazz,
            final Object... initArgs
    ) throws RuntimeException {
        if (clazz == null) {
            return this.handler(null);
        }

        try {
            return this.handler(clazz.getDeclaredConstructor().newInstance(initArgs));
        } catch (final Exception e) {
            throw new RuntimeException("Could not instantiate class: " + clazz.getName());
        }
    }

    /**
     * Sets the handler
     *
     * @param handler New handler
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder handler(final @Nullable ButtonHandler handler) {
        this.handler = handler;

        return this;
    }

    /**
     * Returns the size of the button
     *
     * @return The size of the button, or {@link #INVALID} if not set
     */
    public @Dimension float size() {
        return this.size;
    }

    /**
     * Returns the size of the button, or the {@code defaultSize} if not set
     *
     * @param defaultSize Default size
     * @return The size of the button, or the {@code defaultSize} if not set
     */
    public @Dimension float sizeOr(final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float defaultSize) {
        return this.size == INVALID ? defaultSize : this.size;
    }

    /**
     * Sets the size of the button
     *
     * @param size New size
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder size(final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float size) {
        this.size = size;

        return this;
    }

    /**
     * Returns the color of the button
     *
     * @return The color of the button, or null if not set
     */
    public @Nullable ColorStateList color() {
        return this.color;
    }

    /**
     * Returns the color of the button, or the {@code defaultColor} if not set
     *
     * @param defaultColor Default color
     * @return The color of the button, or the {@code defaultColor} if not set
     */
    public @UnknownNullability ColorStateList colorOr(final @Nullable ColorStateList defaultColor) {
        return this.color == null ? defaultColor : this.color;
    }

    /**
     * Sets the color of the button
     *
     * @param color New color
     * @return This instance, for chaining
     * @see #color(ColorStateList)
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder color(final @ColorInt int color) {
        return this.color(
                color == INVALID
                ? null
                : ColorStateList.valueOf(color)
        );
    }

    /**
     * Sets the color of the button
     *
     * @param color New color
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder color(final @Nullable ColorStateList color) {
        this.color = color;

        return this;
    }

    /**
     * Returns the ripple color of the button
     *
     * @return The ripple color of the button, or null if not set
     */
    public @Nullable ColorStateList rippleColor() {
        return this.rippleColor;
    }

    /**
     * Returns the ripple color of the button, or the {@code defaultColor} if
     * not set
     *
     * @param defaultColor Default color
     * @return The ripple color of the button, or the {@code defaultColor} if
     *         not set
     */
    public @UnknownNullability ColorStateList rippleColorOr(final @Nullable ColorStateList defaultColor) {
        return this.rippleColor == null ? defaultColor : this.rippleColor;
    }

    /**
     * Sets the ripple color of the button
     *
     * @param rippleColor New ripple color
     * @return This instance, for chaining
     * @see #rippleColor(ColorStateList)
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder rippleColor(final @ColorInt int rippleColor) {
        return this.rippleColor(
                rippleColor == INVALID
                ? null
                : ColorStateList.valueOf(rippleColor)
        );
    }

    /**
     * Sets the ripple color of the button
     *
     * @param rippleColor New ripple color
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder rippleColor(final @Nullable ColorStateList rippleColor) {
        this.rippleColor = rippleColor;

        return this;
    }

    /**
     * Returns the border width of the button
     *
     * @return The border width of the button, or {@link #INVALID} if not set
     */
    public @Dimension float borderWidth() {
        return this.borderWidth;
    }

    /**
     * Returns the border width of the button, or the {@code defaultWidth} if
     * not set
     *
     * @param defaultWidth Default width
     * @return The border width of the button, or the {@code defaultWidth} if
     *         not set
     */
    public @Dimension float borderWidthOr(
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float defaultWidth
    ) {
        return this.borderWidth == INVALID ? defaultWidth : this.borderWidth;
    }

    /**
     * Sets the border width of the button
     *
     * @param borderWidth New border width
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder borderWidth(
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float borderWidth
    ) {
        this.borderWidth = borderWidth;

        return this;
    }

    /**
     * Returns the border color of the button
     *
     * @return The border color of the button, or null if not set
     */
    public @Nullable ColorStateList borderColor() {
        return this.borderColor;
    }

    /**
     * Returns the border color of the button, or the {@code defaultColor} if
     * not set
     *
     * @param defaultColor Default color
     * @return The border color of the button, or the {@code defaultColor} if
     *         not set
     */
    public @UnknownNullability ColorStateList borderColorOr(final @Nullable ColorStateList defaultColor) {
        return this.borderColor == null ? defaultColor : this.borderColor;
    }

    /**
     * Sets the border color of the button
     *
     * @param borderColor New border color
     * @return This instance, for chaining
     * @see #borderColor(ColorStateList)
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder borderColor(final @ColorInt int borderColor) {
        return this.borderColor(
                borderColor == INVALID
                ? null
                : ColorStateList.valueOf(borderColor)
        );
    }

    /**
     * Sets the border color of the button
     *
     * @param borderColor New border color
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder borderColor(final @Nullable ColorStateList borderColor) {
        this.borderColor = borderColor;

        return this;
    }

    /**
     * Returns the icon of the button
     *
     * @return The icon of the button, or {@link ActionButton.Icon#empty() empty}
     *         if not set
     */
    public @NonNull ActionButton.Icon icon() {
        return this.iconOr(ActionButton.Icon.empty());
    }

    /**
     * Returns the icon of the button, or the {@code defaultIcon} if not set
     *
     * @param defaultIcon Default icon
     * @return The icon of the button, or the {@code defaultIcon} if not set
     */
    public @NonNull ActionButton.Icon iconOr(final @NonNull ActionButton.Icon defaultIcon) {
        return this.icon == null ? defaultIcon : this.icon;
    }

    /**
     * Sets the icon of the button
     *
     * @param icon     Icon resource
     * @param iconSize Icon size
     * @param iconTint Icon tint
     * @return This instance, for chaining
     * @throws IllegalArgumentException If the icon resource is set and invalid
     * @see #icon(int, float, ColorStateList)
     */
    @Contract("_, _, _ -> this")
    public @NonNull ButtonParamHolder icon(
            final @DrawableRes int icon,
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize,
            final @ColorInt int iconTint
    ) throws IllegalArgumentException {
        return this.icon(
                icon,
                iconSize,
                iconTint == INVALID
                ? null
                : ColorStateList.valueOf(iconTint)
        );
    }

    /**
     * Sets the icon of the button
     *
     * @param icon     Icon resource
     * @param iconSize Icon size
     * @param iconTint Icon tint
     * @return This instance, for chaining
     * @throws IllegalArgumentException If the icon resource is set and invalid
     * @see #icon(ActionButton.Icon)
     */
    @Contract("_, _, _ -> this")
    public @NonNull ButtonParamHolder icon(
            final @DrawableRes int icon,
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize,
            final @Nullable ColorStateList iconTint
    ) throws IllegalArgumentException {
        return this.icon(
                builder()
                .resId(icon)
                .size(iconSize)
                .tint(iconTint)
                .build(this.context)
        );
    }

    /**
     * Sets the icon of the button
     *
     * @param icon New icon
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder icon(final @NonNull ActionButton.Icon icon) {
        this.icon = icon;

        return this;
    }

    /**
     * Returns the icon drawable
     *
     * @return The icon drawable, or null if not set
     */
    public @Nullable Drawable iconDrawable() {
        return this.iconDrawableOr(null);
    }

    /**
     * Returns the icon drawable, or the {@code defaultDrawable} if not set
     *
     * @param defaultDrawable Default drawable
     * @return The icon drawable, or the {@code defaultDrawable} if not set
     */
    public @UnknownNullability Drawable iconDrawableOr(final @Nullable Drawable defaultDrawable) {
        if (this.icon == null) {
            return defaultDrawable;
        }

        final Drawable drawable = this.icon.getDrawable();

        return drawable == null ? defaultDrawable : drawable;
    }

    /**
     * Returns the resource ID of the icon
     *
     * @return The resource ID of the icon, or {@link #INVALID} if not set
     */
    public @DrawableRes int iconResId() {
        return this.iconResIdOr(INVALID);
    }

    /**
     * Returns the resource ID of the icon, or a {@code defaultResId} if not set
     *
     * @param defaultResId Default resource ID
     * @return The resource ID of the icon, or a {@code defaultResId} if not set
     */
    public @DrawableRes int iconResIdOr(final @DrawableRes int defaultResId) {
        if (this.icon == null) {
            return defaultResId;
        }

        final int resId = this.icon.getResId();

        return resId == INVALID ? defaultResId : resId;
    }

    /**
     * Sets the resource ID of the icon
     *
     * @param iconRes New resource ID
     * @return This instance, for chaining
     * @throws IllegalArgumentException If the icon resource is invalid
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #icon(ActionButton.Icon)} method
     *                               instead
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder iconResId(
            final @DrawableRes int iconRes
    ) throws IllegalArgumentException, IllegalStateException {
        if (this.icon == null) {
            throw new IllegalStateException(
                    "Cannot set icon resource ID without an icon, use #icon(ActionButton.Icon) instead"
            );
        }

        this.icon =
                this.icon.toBuilder()
                .resId(iconRes)
                .build(this.context);

        return this;
    }

    /**
     * Returns the size of the icon
     *
     * @return The size of the icon, or {@link #INVALID} if not set
     */
    @SuppressLint("Range")
    public @Dimension float iconSize() {
        return this.iconSizeOr(INVALID);
    }

    /**
     * Returns the size of the icon, or a {@code defaultSize} if not set
     *
     * @param defaultSize Default size
     * @return The size of the icon, or a {@code defaultSize} if not set
     */
    public @Dimension float iconSizeOr(
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float defaultSize
    ) {
        if (this.icon == null) {
            return defaultSize;
        }

        final float size = this.icon.getSize();

        return size == INVALID ? defaultSize : size;
    }

    /**
     * Sets the size of the icon
     *
     * @param iconSize New size
     * @return This instance, for chaining
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #icon(ActionButton.Icon)} method
     *                               instead
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder iconSize(
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize
    ) throws IllegalStateException {
        if (this.icon == null) {
            throw new IllegalStateException(
                    "Cannot set icon size without an icon, use #icon(ActionButton.Icon) instead"
            );
        }

        this.icon =
                this.icon.toBuilder()
                .size(iconSize)
                .build(this.context);

        return this;
    }

    /**
     * Returns the tint of the icon
     *
     * @return The tint of the icon, or null if not set
     */
    public @Nullable ColorStateList iconTint() {
        return this.iconTintOr(null);
    }

    /**
     * Returns the tint of the icon, or a {@code defaultTint} if not set
     *
     * @param defaultTint Default tint
     * @return The tint of the icon, or a {@code defaultTint} if not set
     */
    public @UnknownNullability ColorStateList iconTintOr(final @Nullable ColorStateList defaultTint) {
        if (this.icon == null) {
            return defaultTint;
        }

        final ColorStateList tint = this.icon.getTint();

        return tint == null ? defaultTint : tint;
    }

    /**
     * Sets the tint of the icon
     *
     * @param iconTint New tint
     * @return This instance, for chaining
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #icon(ActionButton.Icon)} method
     *                               instead
     * @see #iconTint(ColorStateList)
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder iconTint(final @ColorInt int iconTint) throws IllegalStateException {
        return this.iconTint(
                iconTint == INVALID
                ? null
                : ColorStateList.valueOf(iconTint)
        );
    }

    /**
     * Sets the tint of the icon
     *
     * @param iconTint New tint
     * @return This instance, for chaining
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #icon(ActionButton.Icon)} method
     *                               instead
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder iconTint(final @Nullable ColorStateList iconTint) throws IllegalStateException {
        if (this.icon == null) {
            throw new IllegalStateException(
                    "Cannot set icon tint without an icon, use #icon(ActionButton.Icon) instead"
            );
        }

        this.icon =
                this.icon.toBuilder()
                .tint(iconTint)
                .build(this.context);

        return this;
    }

    /**
     * Returns the animation duration
     *
     * @return The animation duration, or {@link #INVALID} if not set
     */
    public int animDuration() {
        return this.animDuration;
    }

    /**
     * Returns the animation duration, or the {@code defaultDuration} if not set
     *
     * @param defaultDuration Default duration
     * @return The animation duration, or the {@code defaultDuration} if not set
     */
    public int animDurationOr(final @IntRange(from = 0, to = Integer.MAX_VALUE) int defaultDuration) {
        return this.animDuration == INVALID ? defaultDuration : this.animDuration;
    }

    /**
     * Sets the animation duration
     *
     * @param animDuration New animation duration
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder animDuration(final @IntRange(from = 0, to = Integer.MAX_VALUE) int animDuration) {
        this.animDuration = animDuration;

        return this;
    }

    /**
     * Returns the icon animation interpolator
     *
     * @return The icon animation interpolator, or null if not set
     */
    public @Nullable Interpolator interpolator() {
        return this.interpolator;
    }

    /**
     * Returns the icon animation interpolator, or the
     * {@code defaultInterpolator} if not set
     *
     * @param defaultInterpolator Default interpolator
     * @return The icon animation interpolator,
     *         or the {@code defaultInterpolator} if not set
     */
    public @UnknownNullability Interpolator interpolatorOr(final @Nullable Interpolator defaultInterpolator) {
        return this.interpolator == null ? defaultInterpolator : this.interpolator;
    }

    /**
     * Sets the icon animation interpolator using a resource
     *
     * @param interpolatorRes New interpolator resource
     * @return This instance, for chaining
     * @see #interpolator(Interpolator)
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder interpolator(final @InterpolatorRes int interpolatorRes) {
        return this.interpolator(AnimationUtils.loadInterpolator(this.context, interpolatorRes));
    }

    /**
     * Sets the icon animation interpolator
     *
     * @param interpolator New interpolator
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder interpolator(final @Nullable Interpolator interpolator) {
        this.interpolator = interpolator;

        return this;
    }

    /**
     * Returns the shape appearance model
     *
     * @return The shape appearance model,
     *         or {@link #EMPTY_SHAPE_APPEARANCE_MODEL empty} if not set
     */
    public @NonNull ShapeAppearanceModel shapeAppearanceModel() {
        return this.shapeAppearanceModelOr(EMPTY_SHAPE_APPEARANCE_MODEL);
    }

    /**
     * Returns the shape appearance model, or the {@code defaultModel} if not set
     *
     * @param defaultModel Default model
     * @return The shape appearance model, or the {@code defaultModel} if not set
     */
    public @NonNull ShapeAppearanceModel shapeAppearanceModelOr(final @NonNull ShapeAppearanceModel defaultModel) {
        return this.shapeAppearanceModel == null ? defaultModel : this.shapeAppearanceModel;
    }

    /**
     * Sets the shape appearance model
     *
     * @param shapeAppearanceModel New shape appearance model
     * @return This instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder shapeAppearanceModel(final @NonNull ShapeAppearanceModel shapeAppearanceModel) {
        this.shapeAppearanceModel = shapeAppearanceModel;

        return this;
    }

    /**
     * Modifies the current instance with the provided one
     *
     * @param that The button parameters holder to modify with
     * @return This modified instance, for chaining
     */
    @Contract("_ -> this")
    public @NonNull ButtonParamHolder modify(final @NonNull ButtonParamHolder that) {
        if (this == that) {
            return this;
        }

        return this
                .handler(that.handlerOr(this.handler))
                .size(that.sizeOr(this.size))
                .color(that.colorOr(this.color))
                .rippleColor(that.rippleColorOr(this.rippleColor))
                .borderWidth(that.borderWidthOr(this.borderWidth))
                .borderColor(that.borderColorOr(this.borderColor))
                .icon(that.iconOr(this.icon))
                .animDuration(that.animDurationOr(this.animDuration))
                .interpolator(that.interpolatorOr(this.interpolator))
                .shapeAppearanceModel(that.shapeAppearanceModelOr(this.shapeAppearanceModel));
    }


    /**
     * Returns the hash code value for this button parameters holder
     *
     * @return The hash code value for this button parameters holder
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.context.hashCode();
        result = prime * result + Objects.hashCode(this.handler);
        result = prime * result + floatToIntBits(this.size);
        result = prime * result + Objects.hashCode(this.color);
        result = prime * result + Objects.hashCode(this.rippleColor);
        result = prime * result + floatToIntBits(this.borderWidth);
        result = prime * result + Objects.hashCode(this.borderColor);
        result = prime * result + Objects.hashCode(this.icon);
        result = prime * result + this.animDuration;
        result = prime * result + Objects.hashCode(this.interpolator);
        result = prime * result + Objects.hashCode(this.shapeAppearanceModel);

        return result;
    }

    /**
     * Returns whether the given object is equal to this button parameters holder
     *
     * @param obj The attribute object to be compared
     * @return True if the given object is equal to this button parameters holder
     */
    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (
                obj == null
                || this.getClass() != obj.getClass()
        ) {
            return false;
        }

        final ButtonParamHolder that = (ButtonParamHolder) obj;

        return this.context.equals(that.context)
                && this.animDuration                == that.animDuration
                && floatToIntBits(this.size)        == floatToIntBits(that.size)
                && floatToIntBits(this.borderWidth) == floatToIntBits(that.borderWidth)
                && Objects.equals(this.handler,              that.handler)
                && Objects.equals(this.color,                that.color)
                && Objects.equals(this.rippleColor,          that.rippleColor)
                && Objects.equals(this.borderColor,          that.borderColor)
                && Objects.equals(this.icon,                 that.icon)
                && Objects.equals(this.interpolator,         that.interpolator)
                && Objects.equals(this.shapeAppearanceModel, that.shapeAppearanceModel);
    }

    /**
     * Returns a string representation of this button parameters holder
     *
     * @return A string representation of this button parameters holder
     */
    @Override
    public @NonNull String toString() {
        return "ButtonParamHolder{" +
                "context=" + this.context +
                ", handler=" + this.handler +
                ", size=" + this.size +
                ", color=" + this.color +
                ", rippleColor=" + this.rippleColor +
                ", borderWidth=" + this.borderWidth +
                ", borderColor=" + this.borderColor +
                ", icon=" + this.icon +
                ", animDuration=" + this.animDuration +
                ", interpolator=" + this.interpolator +
                ", shapeAppearanceModel=" + this.shapeAppearanceModel +
                '}';
    }

    /**
     * Creates a new instance of {@code ButtonParamHolder}
     *
     * @param context The context
     * @return A new instance of {@code ButtonParamHolder}
     */
    @Contract("_ -> new")
    public static @NonNull ButtonParamHolder create(final @NonNull Context context) {
        return new ButtonParamHolder(context);
    }
}
