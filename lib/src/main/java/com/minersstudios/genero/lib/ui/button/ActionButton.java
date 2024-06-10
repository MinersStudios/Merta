package com.minersstudios.genero.lib.ui.button;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.animation.Animation.RELATIVE_TO_SELF;

import static com.minersstudios.genero.lib.ui.button.ButtonParamHolder.INVALID;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.*;
import androidx.core.content.ContextCompat;

import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.minersstudios.genero.lib.R;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents an action button
 *
 * @see ButtonHandler
 * @see ButtonParamHolder
 * @noinspection unused
 */
public class ActionButton extends View implements Shapeable {
    private final ButtonParamHolder paramsHolder;
    private final AtomicReference<Icon> iconRef;
    private MaterialShapeDrawable mShapeDrawable;
    private MaterialShapeDrawable mBorderDrawable;
    private RippleDrawable mRippleDrawable;

    private static final int DEFAULT_STYLE = R.style.Base_Genero_Widget_ActionButton;

    /**
     * Creates a new action button
     *
     * @param context The context
     * @see #ActionButton(Context, AttributeSet)
     */
    public ActionButton(final @NonNull Context context) {
        this(context, null);
    }

    /**
     * Creates a new action button
     *
     * @param context The context
     * @param attrSet The attribute set
     * @see #ActionButton(Context, AttributeSet, int)
     */
    public ActionButton(
            final @NonNull Context context,
            final @Nullable AttributeSet attrSet
    ) {
        this(context, attrSet, 0);
    }

    /**
     * Creates a new action button
     *
     * @param context      The context
     * @param attrSet      The attribute set
     * @param defStyleAttr The default style attribute
     */
    public ActionButton(
            final @NonNull Context context,
            final @Nullable AttributeSet attrSet,
            final @AttrRes int defStyleAttr
    ) {
        super(context, attrSet, defStyleAttr, DEFAULT_STYLE);

        this.paramsHolder = ButtonParamHolder.create(context);
        this.iconRef = new AtomicReference<>();
        final TypedArray attrArray =
                context.getTheme().obtainStyledAttributes(
                        attrSet,
                        R.styleable.ActionButton,
                        defStyleAttr,
                        DEFAULT_STYLE
                );

        this.setButtonHandler(attrArray.getString(        R.styleable.ActionButton_handler));
        this.setSize(         attrArray.getDimension(     R.styleable.ActionButton_size,         0.0f));
        this.setColor(        attrArray.getColorStateList(R.styleable.ActionButton_color));
        this.setRippleColor(  attrArray.getColorStateList(R.styleable.ActionButton_rippleColor));
        this.setBorderWidth(  attrArray.getDimension(     R.styleable.ActionButton_borderWidth,  0.0f));
        this.setBorderColor(  attrArray.getColorStateList(R.styleable.ActionButton_borderColor));
        this.setAnimDuration( attrArray.getInteger(       R.styleable.ActionButton_animDuration, 0));
        this.setInterpolator( attrArray.getResourceId(    R.styleable.ActionButton_interpolator, INVALID));
        this.setIcon(
                attrArray.getResourceId(    R.styleable.ActionButton_icon,     INVALID),
                attrArray.getDimension(     R.styleable.ActionButton_iconSize, 0.0f),
                attrArray.getColorStateList(R.styleable.ActionButton_iconTint)
        );
        attrArray.recycle();

        this.setupShapeDrawable();
        this.setupBorderDrawable();
        this.setupRippleDrawable();

        this.setShapeAppearanceModel(
                ShapeAppearanceModel.builder(
                        context, attrSet, defStyleAttr,
                        DEFAULT_STYLE,
                        ShapeAppearanceModel.PILL
                ).build()
        );
        this.setClickable(true);
        this.invalidate();
    }

    /**
     * Returns the button handler
     *
     * @return The button handler
     */
    public @Nullable ButtonHandler getButtonHandler() {
        return this.paramsHolder.handler();
    }

    /**
     * Sets the button handler
     *
     * @param className Handler class name
     * @param args      Handler arguments
     * @throws RuntimeException If the class is not found
     * @see #setButtonHandler(Class, Object...)
     */
    public void setButtonHandler(
            final @Nullable String className,
            final Object... args
    ) throws RuntimeException {
        this.paramsHolder.handler(className, args);
    }

    /**
     * Sets the button handler
     *
     * @param clazz Handler class
     * @param args  Handler arguments
     * @throws RuntimeException If the class cannot be instantiated
     * @see #setButtonHandler(ButtonHandler)
     */
    public void setButtonHandler(
            final @Nullable Class<? extends ButtonHandler> clazz,
            final Object... args
    ) throws RuntimeException {
        this.paramsHolder.handler(clazz, args);
    }

    /**
     * Sets the button handler
     *
     * @param handler New button handler
     */
    public void setButtonHandler(final @Nullable ButtonHandler handler) {
        this.paramsHolder.handler(handler);
    }

    /**
     * Returns the button size
     *
     * @return The button size
     */
    public @Dimension float getSize() {
        return this.paramsHolder.size();
    }

    /**
     * Sets the button size
     *
     * @param size New button size
     */
    public void setSize(final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float size) {
        this.paramsHolder.size(size);
    }

    /**
     * Returns the button color
     *
     * @return The button color
     */
    public @Nullable ColorStateList getColor() {
        return this.paramsHolder.color();
    }

    /**
     * Sets the button color
     *
     * @param color New button color
     * @see #setColor(ColorStateList)
     */
    public void setColor(final @ColorInt int color) {
        this.paramsHolder.color(color);
    }

    /**
     * Sets the button color
     *
     * @param color New button color
     */
    public void setColor(final @Nullable ColorStateList color) {
        this.paramsHolder.color(color);
    }

    /**
     * Returns the button ripple color
     *
     * @return The button ripple color
     */
    public @Nullable ColorStateList getRippleColor() {
        return this.paramsHolder.rippleColor();
    }

    /**
     * Sets the button ripple color
     *
     * @param rippleColor New button ripple color
     * @see #setRippleColor(ColorStateList)
     */
    public void setRippleColor(final @ColorInt int rippleColor) {
        this.paramsHolder.rippleColor(rippleColor);
    }

    /**
     * Sets the button ripple color
     *
     * @param rippleColor New button ripple color
     */
    public void setRippleColor(final @Nullable ColorStateList rippleColor) {
        this.paramsHolder.rippleColor(rippleColor);
    }

    /**
     * Returns the button border width
     *
     * @return The button border width
     */
    public @Dimension float getBorderWidth() {
        return this.paramsHolder.borderWidth();
    }

    /**
     * Sets the button border width
     *
     * @param borderWidth New button border width
     */
    public void setBorderWidth(final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float borderWidth) {
        this.paramsHolder.borderWidth(borderWidth);

        if (this.mBorderDrawable == null) {
            this.setupBorderDrawable();
        }
    }

    /**
     * Returns the button border color
     *
     * @return The button border color
     */
    public @Nullable ColorStateList getBorderColor() {
        return this.paramsHolder.borderColor();
    }

    /**
     * Sets the button border color
     *
     * @param borderColor New button border color
     * @see #setBorderColor(ColorStateList)
     */
    public void setBorderColor(final @ColorInt int borderColor) {
        this.paramsHolder.borderColor(borderColor);
    }

    /**
     * Sets the button border color
     *
     * @param borderColor New button border color
     */
    public void setBorderColor(final @Nullable ColorStateList borderColor) {
        this.paramsHolder.borderColor(borderColor);

        if (this.mBorderDrawable == null) {
            this.setupBorderDrawable();
        }
    }

    /**
     * Returns the button icon
     *
     * @return The button icon
     */
    public @NotNull Icon getIcon() {
        return this.paramsHolder.icon();
    }

    /**
     * Sets the button icon
     *
     * @param icon     Icon resource
     * @param iconSize Icon size
     * @param iconTint Icon tint
     * @throws IllegalArgumentException If the icon resource is invalid
     * @see #setIcon(int, float, ColorStateList)
     */
    public void setIcon(
            final @DrawableRes int icon,
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize,
            final @ColorInt int iconTint
    ) throws IllegalArgumentException {
        this.paramsHolder.icon(icon, iconSize, iconTint);
    }

    /**
     * Sets the button icon
     *
     * @param icon     Icon resource
     * @param iconSize Icon size
     * @param iconTint Icon tint
     * @throws IllegalArgumentException If the icon resource is invalid
     * @see #setIcon(Icon)
     */
    public void setIcon(
            final @DrawableRes int icon,
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize,
            final @Nullable ColorStateList iconTint
    ) throws IllegalArgumentException {
        this.paramsHolder.icon(icon, iconSize, iconTint);
    }

    /**
     * Sets the button icon
     *
     * @param icon New button icon
     */
    public void setIcon(final @NonNull Icon icon) {
        this.paramsHolder.icon(icon);
    }

    /**
     * Sets the button icon with an animation.
     * <p>
     * If the new icon is the same as the current one, the animation will not
     * play.
     *
     * @param icon New button icon
     * @throws IllegalArgumentException If the icon resource is invalid
     * @see #playIconAnim()
     * @see #setIconWithAnim(Icon)
     */
    public void setIconWithAnim(
            final @DrawableRes int icon,
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize,
            final @ColorInt int iconTint
    ) throws IllegalArgumentException {
        this.setIconWithAnim(icon, iconSize, ColorStateList.valueOf(iconTint), false);
    }

    /**
     * Sets the button icon with an animation
     *
     * @param icon     Icon resource
     * @param iconSize Icon size
     * @param iconTint Icon tint
     * @param force    Whether to force the icon animation
     * @throws IllegalArgumentException If the icon resource is invalid
     * @see #playIconAnim()
     * @see #setIconWithAnim(Icon, boolean)
     */
    public void setIconWithAnim(
            final @DrawableRes int icon,
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize,
            final @ColorInt int iconTint,
            final boolean force
    ) throws IllegalArgumentException {
        this.setIconWithAnim(icon, iconSize, ColorStateList.valueOf(iconTint), force);
    }

    /**
     * Sets the button icon with an animation.
     * <p>
     * If the new icon is the same as the current one, the animation will not
     * play.
     *
     * @param icon     Icon resource
     * @param iconSize Icon size
     * @param iconTint Icon tint
     * @throws IllegalArgumentException If the icon resource is invalid
     * @see #playIconAnim()
     * @see #setIconWithAnim(Icon)
     */
    public void setIconWithAnim(
            final @DrawableRes int icon,
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize,
            final @Nullable ColorStateList iconTint
    ) throws IllegalArgumentException {
        this.setIconWithAnim(icon, iconSize, iconTint, false);
    }

    /**
     * Sets the button icon with an animation
     *
     * @param icon     Icon resource
     * @param iconSize Icon size
     * @param iconTint Icon tint
     * @param force    Whether to force the icon animation
     * @throws IllegalArgumentException If the icon resource is invalid
     * @see #playIconAnim()
     * @see #setIconWithAnim(Icon, boolean)
     */
    public void setIconWithAnim(
            final @DrawableRes int icon,
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize,
            final @Nullable ColorStateList iconTint,
            final boolean force
    ) throws IllegalArgumentException {
        this.setIconWithAnim(
                Icon.builder()
                .resId(icon)
                .size(iconSize)
                .tint(iconTint)
                .build(this.getContext()),
                force
        );
    }

    /**
     * Sets the button icon with an animation.
     * <p>
     * If the new icon is the same as the current one, the animation will not
     * play.
     *
     * @param icon New button icon
     * @see #playIconAnim()
     * @see #setIconWithAnim(Icon, boolean)
     */
    public void setIconWithAnim(final @NonNull Icon icon) {
        this.setIconWithAnim(icon, false);
    }

    /**
     * Sets the button icon with an animation
     *
     * @param icon  New button icon
     * @param force Whether to force the icon animation
     * @see #playIconAnim()
     */
    public void setIconWithAnim(
            final @NonNull Icon icon,
            final boolean force
    ) {
        if (
                !force
                && icon.equals(this.getActualIcon())
        ) {
            return;
        }

        this.setActualIcon(icon);

        if (this.hasAnimation()) {
            this.playIconAnim();
            new Handler(Looper.getMainLooper())
            .postDelayed(
                    () -> {
                        this.setIcon(icon);
                        this.invalidate();
                    },
                    this.getAnimDuration() / 2
            );
        } else {
            this.setIcon(icon);
            this.invalidate();
        }
    }

    /**
     * Returns the button icon resource ID
     *
     * @return The button icon resource ID
     */
    public @DrawableRes int getIconResId() {
        return this.paramsHolder.iconResId();
    }

    /**
     * Sets the button icon resource ID
     *
     * @param iconResId New button icon resource ID
     * @throws IllegalArgumentException If the icon resource is invalid
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #setIcon(Icon)} method instead
     */
    public void setIconResId(
            final @DrawableRes int iconResId
    ) throws IllegalArgumentException, IllegalStateException {
        this.paramsHolder.iconResId(iconResId);
    }

    /**
     * Sets the button icon resource ID with an animation.
     * <p>
     * If the new icon resource ID is the same as the current one, the
     * animation will not play.
     *
     * @param iconResId New button icon resource ID
     * @throws IllegalArgumentException If the icon resource is invalid
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #setIcon(Icon)} method instead
     * @see #setIconResIdWithAnim(int, boolean)
     */
    public void setIconResIdWithAnim(
            final @DrawableRes int iconResId
    ) throws IllegalArgumentException, IllegalStateException {
        this.setIconResIdWithAnim(iconResId, false);
    }

    /**
     * Sets the button icon resource ID with an animation
     *
     * @param iconResId New button icon resource ID
     * @param force     Whether to force the icon animation
     * @throws IllegalArgumentException If the icon resource is invalid
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #setIcon(Icon)} method instead
     */
    public void setIconResIdWithAnim(
            final @DrawableRes int iconResId,
            final boolean force
    ) throws IllegalArgumentException, IllegalStateException {
        if (this.getIcon().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot set icon resource ID without an icon, use #setIcon(ActionButton.Icon) instead"
            );
        }

        this.setIconWithAnim(
                iconResId,
                this.getIconSize(),
                this.getIconTint(),
                force
        );
    }

    /**
     * Returns the button icon size
     *
     * @return The button icon size
     */
    public @Dimension float getIconSize() {
        return this.paramsHolder.iconSize();
    }

    /**
     * Sets the button icon size
     *
     * @param iconSize New button icon size
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #setIcon(Icon)} method instead
     */
    public void setIconSize(
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize
    ) throws IllegalStateException {
        this.paramsHolder.iconSize(iconSize);
    }

    /**
     * Sets the button icon size with an animation.
     * <p>
     * If the new icon size is the same as the current one, the animation will
     * not play.
     *
     * @param iconSize New button icon size
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #setIcon(Icon)} method instead
     * @see #setIconSizeWithAnim(float, boolean)
     */
    public void setIconSizeWithAnim(
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize
    ) throws IllegalStateException {
        this.setIconSizeWithAnim(iconSize, false);
    }

    /**
     * Sets the button icon size with an animation
     *
     * @param iconSize New button icon size
     * @param force    Whether to force the icon animation
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #setIcon(Icon)} method instead
     */
    public void setIconSizeWithAnim(
            final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize,
            final boolean force
    ) throws IllegalStateException {
        if (this.getIcon().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot set icon size without an icon, use #setIcon(ActionButton.Icon) instead"
            );
        }

        this.setIconWithAnim(
                this.getIconResId(),
                iconSize,
                this.getIconTint(),
                force
        );
    }

    /**
     * Returns the button icon tint
     *
     * @return The button icon tint
     */
    public @Nullable ColorStateList getIconTint() {
        return this.paramsHolder.iconTint();
    }

    /**
     * Sets the button icon tint
     *
     * @param iconTint New button icon tint
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #setIcon(Icon)} method instead
     */
    public void setIconTint(final @ColorInt int iconTint) throws IllegalStateException {
        this.paramsHolder.iconTint(iconTint);
    }

    /**
     * Sets the button icon tint with an animation.
     * <p>
     * If the new icon tint is the same as the current one, the animation will
     * not play.
     *
     * @param iconTint New button icon tint
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #setIcon(Icon)} method instead
     * @see #setIconTintWithAnim(int, boolean)
     */
    public void setIconTintWithAnim(final @ColorInt int iconTint) throws IllegalStateException {
        this.setIconTintWithAnim(iconTint, false);
    }

    /**
     * Sets the button icon tint with an animation
     *
     * @param iconTint New button icon tint
     * @param force    Whether to force the icon animation
     * @throws IllegalStateException If the icon is not set,
     *                               use {@link #setIcon(Icon)} method instead
     */
    public void setIconTintWithAnim(
            final @ColorInt int iconTint,
            final boolean force
    ) throws IllegalStateException {
        if (this.getIcon().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot set icon tint without an icon, use #setIcon(ActionButton.Icon) instead"
            );
        }

        this.setIconWithAnim(
                this.getIconResId(),
                this.getIconSize(),
                ColorStateList.valueOf(iconTint),
                force
        );
    }

    /**
     * Returns the button icon change animation duration
     *
     * @return The button icon change animation duration
     */
    public int getAnimDuration() {
        return this.paramsHolder.animDuration();
    }

    /**
     * Sets the button icon change animation duration
     *
     * @param animDuration New button icon change animation duration
     */
    public void setAnimDuration(final int animDuration) {
        this.paramsHolder.animDuration(animDuration);
    }

    /**
     * Returns the button icon change animation interpolator
     *
     * @return The button icon change animation interpolator
     */
    public @Nullable Interpolator getInterpolator() {
        return this.paramsHolder.interpolator();
    }

    /**
     * Sets the button icon change animation interpolator
     *
     * @param interpolator New button icon change animation interpolator
     * @see #setInterpolator(Interpolator)
     */
    public void setInterpolator(final @InterpolatorRes int interpolator) {
        if (interpolator == INVALID) {
            return;
        }

        this.paramsHolder.interpolator(interpolator);
    }

    /**
     * Sets the button icon change animation interpolator
     *
     * @param interpolator New button icon change animation interpolator
     */
    public void setInterpolator(final @Nullable Interpolator interpolator) {
        this.paramsHolder.interpolator(interpolator);
    }

    /**
     * Returns the shape appearance model
     *
     * @return The shape appearance model,
     *         or {@link ButtonParamHolder#EMPTY_SHAPE_APPEARANCE_MODEL empty}
     *         if not set
     */
    @Override
    public @NonNull ShapeAppearanceModel getShapeAppearanceModel() {
        return this.paramsHolder.shapeAppearanceModel();
    }

    /**
     * Sets the shape appearance model
     *
     * @param shapeAppearance New shape appearance model
     */
    @Override
    public void setShapeAppearanceModel(final @NonNull ShapeAppearanceModel shapeAppearance) {
        this.paramsHolder.shapeAppearanceModel(shapeAppearance);

        if (this.mShapeDrawable != null) {
            this.mShapeDrawable.setShapeAppearanceModel(shapeAppearance);
        }

        if (this.mBorderDrawable != null) {
            this.mBorderDrawable.setShapeAppearanceModel(shapeAppearance);
        }

        if (this.mRippleDrawable instanceof Shapeable) {
            ((Shapeable) this.mRippleDrawable).setShapeAppearanceModel(shapeAppearance);
        }
    }

    /**
     * Returns whether the button border is visible
     *
     * @return Whether the button border is visible
     */
    public boolean isBorderVisible() {
        if (this.getBorderWidth() <= 0) {
            return false;
        }

        final ColorStateList borderColor = this.getBorderColor();

        return borderColor != null
                && borderColor.isOpaque();
    }

    /**
     * Returns whether the button has an animation
     *
     * @return Whether the button has an animation
     */
    public boolean hasAnimation() {
        return this.getAnimDuration() > 0
                && this.getInterpolator() != null;
    }

    /**
     * Plays the button icon rotation animation
     */
    public void playIconAnim() {
        if (this.hasAnimation()) {
            final RotateAnimation rotateAnimation = new RotateAnimation(
                    0, 360,
                    RELATIVE_TO_SELF, 0.5f,
                    RELATIVE_TO_SELF, 0.5f
            );

            rotateAnimation.setDuration(this.getAnimDuration());
            rotateAnimation.setInterpolator(this.getInterpolator());
            this.startAnimation(rotateAnimation);
        }
    }

    @Override
    public boolean performClick() {
        final ButtonHandler handler = this.getButtonHandler();

        if (
                handler != null
                && handler.isClickable()
        ) {
            this.playSoundEffect(SoundEffectConstants.CLICK);
            this.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
            handler.onClick(this);

            return true;
        }

        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(final @NonNull MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_DOWN:
                this.performHapticFeedback(0);
                break;
            case ACTION_UP:
                this.performClick();
                break;
        }

        final ButtonHandler handler = this.getButtonHandler();

        if (
                handler != null
                && handler.isTouchable()
        ) {
            handler.onTouch(this, event);
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(
            final int widthMeasureSpec,
            final int heightMeasureSpec
    ) {
        final int size = (int) this.getSize();
        final int diameter =
                Math.min(
                        View.resolveSize(size, widthMeasureSpec),
                        View.resolveSize(size, heightMeasureSpec)
                );

        this.setMeasuredDimension(diameter, diameter);
    }

    @Override
    protected void onDraw(final @NonNull Canvas canvas) {
        this.drawIcon(canvas);
        this.drawBorder(canvas);
    }

    @Override
    protected @Nullable Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();

        if (superState == null) {
            return null;
        }

        final SavedState savedState = new SavedState(superState);
        final Icon icon = this.getActualIcon();

        savedState.iconResId = icon.getResId();
        savedState.iconSize = icon.getSize();
        savedState.iconTint = icon.getTint();

        return savedState;
    }

    /**
     * Returns the button icon at that moment in animation.
     * <p>
     * Used in {@link #setIconWithAnim(Icon, boolean)} method and related.
     *
     * @return The button icon at that moment in animation
     */
    protected @NonNull Icon getActualIcon() {
        final Icon icon = this.iconRef.get();

        return icon == null
               ? this.getIcon()
               : icon;
    }

    /**
     * Sets the button icon at that moment in animation.
     * <p>
     * Used in {@link #setIconWithAnim(Icon, boolean)} method and related.
     *
     * @param icon New button icon at that moment in animation
     */
    protected void setActualIcon(final @NonNull Icon icon) {
        this.iconRef.set(icon);
    }

    private void drawIcon(final @NonNull Canvas canvas) {
        final Icon icon = this.getIcon();

        if (icon.isEmpty()) {
            return;
        }

        final Drawable drawable = icon.getDrawable();
        assert drawable != null
                : "Icon drawable is null, should not happen as it is checked in ActionButton.Icon#isEmpty()";

        final int h = this.getHeight();
        final int w = this.getWidth();
        final int d = Math.min(h, w);

        final int halfSize = (int) (icon.getSize() / 2 * (d / this.getSize()));
        final int horOffset = h / 2 - halfSize;
        final int verOffset = w / 2 - halfSize;

        drawable.mutate();
        drawable.setBounds(
                verOffset,
                horOffset,
                w - verOffset,
                h - horOffset
        );
        drawable.draw(canvas);
    }

    private void drawBorder(final @NonNull Canvas canvas) {
        if (this.isBorderVisible()) {
            this.mBorderDrawable.setBounds(0, 0, this.getWidth(), this.getHeight());
            this.mBorderDrawable.draw(canvas);
        }
    }

    private void setupShapeDrawable() {
        this.mShapeDrawable = new MaterialShapeDrawable(this.getShapeAppearanceModel());

        this.mShapeDrawable.setTintList(this.getColor());
    }

    private void setupBorderDrawable() {
        if (this.isBorderVisible()) {
            this.mBorderDrawable = new MaterialShapeDrawable(this.getShapeAppearanceModel());

            this.mBorderDrawable.setTint(Color.TRANSPARENT);
            this.mBorderDrawable.setStroke(this.getBorderWidth(), this.getBorderColor());
        }
    }

    private void setupRippleDrawable() {
        this.mRippleDrawable = new RippleDrawable(
                this.getRippleColor() == null
                ? ColorStateList.valueOf(Color.TRANSPARENT)
                : this.getRippleColor(),
                this.mShapeDrawable,
                null
        );

        this.setBackground(this.mRippleDrawable);
    }

    /**
     * Represents an {@link ActionButton action button} icon
     */
    public static class Icon {
        private final @DrawableRes int resId;
        private final Drawable drawable;
        private final @Dimension float size;
        private final ColorStateList tint;

        private static final Icon EMPTY = new Icon(INVALID, null, INVALID, null);

        private Icon(
                final @DrawableRes int resId,
                final @Nullable Drawable drawable,
                final @Dimension float size,
                final @Nullable ColorStateList tint
        ) {
            this.resId = resId;
            this.drawable = drawable;
            this.size = size;
            this.tint = tint;
        }

        /**
         * Returns the icon resource ID
         *
         * @return The icon resource ID
         */
        public @DrawableRes int getResId() {
            return this.resId;
        }

        /**
         * Returns the icon drawable
         *
         * @return The icon drawable
         */
        public @Nullable Drawable getDrawable() {
            return this.drawable;
        }

        /**
         * Returns the icon size
         *
         * @return The icon size
         */
        public @Dimension float getSize() {
            return this.size;
        }

        /**
         * Returns the icon tint
         *
         * @return The icon tint
         */
        public @Nullable ColorStateList getTint() {
            return this.tint;
        }

        /**
         * Returns a hash code value for this icon
         *
         * @return A hash code value for this icon
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;

            result = prime * result + this.resId;
            result = prime * result + Float.floatToIntBits(this.size);
            result = prime * result + Objects.hashCode(this.tint);

            return result;
        }

        /**
         * Returns whether the icon drawable is null or the size is less than
         * or equal to 0
         *
         * @return Whether the icon is empty
         */
        public boolean isEmpty() {
            return this.resId == INVALID
                    || this.drawable == null
                    || this.size <= 0;
        }

        /**
         * Indicates whether some other object is "equal to" this one
         *
         * @param obj The reference object with which to compare
         * @return True if this object is the same as the obj argument
         */
        @Contract("null -> false")
        @Override
        public boolean equals(final @Nullable Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Icon)) {
                return false;
            }

            final Icon that = (Icon) obj;

            if (
                    this.resId != that.resId
                    || this.size != that.size
            ) {
                return false;
            }

            final int thisIconTint =
                    this.tint == null
                    ? 0
                    : this.tint.getDefaultColor();
            final int thatIconTint =
                    that.tint == null
                    ? 0
                    : that.tint.getDefaultColor();

            return thisIconTint == thatIconTint;
        }

        /**
         * Returns a string representation of this icon
         *
         * @return A string representation of this icon
         */
        @Override
        public @NonNull String toString() {
            return "ActionButton.Icon{" +
                    "resId=" + this.resId +
                    ", iconSize=" + this.size +
                    ", iconTint=" + (this.tint == null ? "null" : this.tint.getDefaultColor()) +
                    '}';
        }

        /**
         * Returns a new icon builder with the same properties as this icon
         *
         * @return A new icon builder with the same properties as this icon
         */
        @Contract(" -> new")
        public @NonNull Builder toBuilder() {
            return builder()
                    .resId(this.resId)
                    .size(this.size)
                    .tint(this.tint);
        }

        /**
         * Returns an empty icon
         *
         * @return An empty icon
         */
        public static @NotNull Icon empty() {
            return EMPTY;
        }

        /**
         * Creates a new icon builder
         *
         * @return A new icon builder
         */
        @Contract(" -> new")
        public static @NonNull Builder builder() {
            return new Builder();
        }

        /**
         * Represents a builder for an {@link Icon action button icon}
         */
        public static final class Builder {
            private @DrawableRes int resId;
            private @Dimension float size;
            private ColorStateList tint;

            private Builder() {
                this.resId = INVALID;
                this.size = 0.0f;
                this.tint = null;
            }

            /**
             * Returns the icon resource ID
             *
             * @return The icon resource ID
             */
            public @DrawableRes int resId() {
                return this.resId;
            }

            /**
             * Sets the icon resource ID
             *
             * @param resId New icon resource ID
             * @return This icon builder
             */
            @Contract("_ -> this")
            public @NonNull Builder resId(final @DrawableRes int resId) {
                this.resId = resId;

                return this;
            }

            /**
             * Returns the icon size
             *
             * @return The icon size
             */
            public @Dimension float size() {
                return this.size;
            }

            /**
             * Sets the icon size
             *
             * @param iconSize New icon size (from 0 to {@link Float#MAX_VALUE})
             * @return This icon builder
             */
            @Contract("_ -> this")
            public @NonNull Builder size(
                    final @FloatRange(from = 0.0f, to = Float.MAX_VALUE) @Dimension float iconSize
            ) {
                this.size = iconSize;

                return this;
            }

            /**
             * Returns the icon tint
             *
             * @return The icon tint
             */
            public @UnknownNullability ColorStateList tint() {
                return this.tint;
            }

            /**
             * Sets the icon tint
             *
             * @param iconTint New icon tint
             * @return This icon builder
             */
            @Contract("_ -> this")
            public @NonNull Builder tint(final @ColorInt int iconTint) {
                return this.tint(ColorStateList.valueOf(iconTint));
            }

            /**
             * Sets the icon tint
             *
             * @param iconTint New icon tint
             * @return This icon builder
             */
            @Contract("_ -> this")
            public @NonNull Builder tint(final @Nullable ColorStateList iconTint) {
                this.tint = iconTint;

                return this;
            }

            /**
             * Builds a new icon with the properties set in this icon builder
             *
             * @return A new icon with the properties set in this icon builder
             * @throws IllegalArgumentException If the icon resource is set and
             *                                  invalid
             */
            @Contract("_ -> new")
            public @NonNull Icon build(final @NonNull Context context) throws IllegalArgumentException {
                if (this.resId == INVALID) {
                    return new Icon(this.resId, null, this.size, this.tint);
                }

                final Drawable drawable = ContextCompat.getDrawable(context, this.resId);

                if (drawable == null) {
                    throw new IllegalArgumentException("Invalid resource id");
                }

                if (this.tint != null) {
                    drawable.setTintList(this.tint);
                }

                return new Icon(this.resId, drawable, this.size, this.tint);
            }

            /**
             * Returns a string representation of this icon builder
             *
             * @return A string representation of this icon builder
             */
            @Override
            public @NonNull String toString() {
                return "ActionButton.IconBuilder{" +
                        "resId=" + this.resId +
                        ", size=" + this.size +
                        ", tint=" + (this.tint == null ? "null" : this.tint.getDefaultColor()) +
                        '}';
            }
        }
    }

    private static class SavedState extends AbsSavedState {
        private @DrawableRes int iconResId;
        private @Dimension float iconSize;
        private ColorStateList iconTint;

        public SavedState(final @NonNull Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(
                final @NonNull Parcel out,
                final int flags
        ) {
            super.writeToParcel(out, flags);

            out.writeInt(this.iconResId);
            out.writeFloat(this.iconSize);
            out.writeParcelable(this.iconTint, flags);
        }
    }
}
