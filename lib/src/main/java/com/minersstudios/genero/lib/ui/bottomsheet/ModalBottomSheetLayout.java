package com.minersstudios.genero.lib.ui.bottomsheet;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.*;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import androidx.annotation.*;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import com.minersstudios.genero.lib.R;

/**
 * Represents a layout that can be used as the root of a
 * {@link ModalBottomSheetFragment}
 *
 * @see ModalBottomSheetFragment
 * @noinspection unused
 */
public class ModalBottomSheetLayout extends LinearLayout {
    private @StableState int mState;
    private @SaveFlags int mSaveFlags;
    private int mPeekHeight;
    private int mExpandedOffset;
    private int mSignificantVelocityThreshold;
    private float mHalfExpandedRatio;
    private float mHideFriction;
    private boolean mHideable;
    private boolean mDraggable;
    private boolean mEnableEdgeToEdge;
    private boolean mFitToContents;
    private boolean mSkipCollapsed;
    private boolean mGestureInsetBottomIgnored;
    private boolean mShouldRemoveExpandedCorners;
    private boolean mUpdateImportantForAccessibilityOnSiblings;

    //<editor-fold desc="Default attribute values" defaultstate="collapsed">
    public static final int DEFAULT_EXPANDED_OFFSET = 0;             // from BottomSheetBehavior
    public static final int DEFAULT_SIGNIFICANT_VEL_THRESHOLD = 500; // BottomSheetBehavior.DEFAULT_SIGNIFICANT_VEL_THRESHOLD

    public static final float DEFAULT_HALF_EXPANDED_RATIO = 0.5f; // from BottomSheetBehavior
    public static final float HIDE_FRICTION = 0.1f;               // BottomSheetBehavior.HIDE_FRICTION

    public static final boolean DEFAULT_HIDEABLE = false;                                       // from BottomSheetBehavior
    public static final boolean DEFAULT_DRAGGABLE = true;                                       // from BottomSheetBehavior
    public static final boolean DEFAULT_EDGE_TO_EDGE = false;
    public static final boolean DEFAULT_FIT_TO_CONTENTS = true;                                 // from BottomSheetBehavior
    public static final boolean DEFAULT_SKIP_COLLAPSED = false;                                 // from BottomSheetBehavior
    public static final boolean DEFAULT_GESTURE_INSET_BOTTOM_IGNORED = false;                   // from BottomSheetBehavior
    public static final boolean DEFAULT_SHOULD_REMOVE_EXPANDED_CORNERS = true;                  // from BottomSheetBehavior
    public static final boolean DEFAULT_UPDATE_IMPORTANT_FOR_ACCESSIBILITY_ON_SIBLINGS = false; // from BottomSheetBehavior
    //</editor-fold>

    private static final int DEFAULT_STYLE = R.style.Base_Widget_Genero_ModalBottomSheet;

    /**
     * Creates a new {@code ModalBottomSheetLayout}
     *
     * @param context The context
     * @see #ModalBottomSheetLayout(Context, AttributeSet)
     */
    public ModalBottomSheetLayout(final @NonNull Context context) {
        this(context, null);
    }

    /**
     * Creates a new {@code ModalBottomSheetLayout}
     *
     * @param context The context
     * @param attrSet The attribute set
     * @see #ModalBottomSheetLayout(Context, AttributeSet, int)
     */
    public ModalBottomSheetLayout(
            final @NonNull Context context,
            final @Nullable AttributeSet attrSet
    ) {
        this(context, attrSet, 0);
    }

    /**
     * Creates a new {@code ModalBottomSheetLayout}
     *
     * @param context      The context
     * @param attrSet      The attribute set
     * @param defStyleAttr The default style attribute
     */
    public ModalBottomSheetLayout(
            final @NonNull Context context,
            final @Nullable AttributeSet attrSet,
            final int defStyleAttr
    ) {
        super(context, attrSet, defStyleAttr, DEFAULT_STYLE);

        final TypedArray typedArray =
                context.getTheme().obtainStyledAttributes(
                        attrSet,
                        R.styleable.ModalBottomSheetLayout,
                        defStyleAttr,
                        DEFAULT_STYLE
                );
        final TypedValue peekHeightValue =     typedArray.peekValue(R.styleable.ModalBottomSheetLayout_peekHeight);
        final TypedValue expandedOffsetValue = typedArray.peekValue(R.styleable.ModalBottomSheetLayout_expandedOffset);

        this.setPeekHeight(
                peekHeightValue != null
                && peekHeightValue.data == PEEK_HEIGHT_AUTO
                ? peekHeightValue.data
                : typedArray.getDimensionPixelSize(R.styleable.ModalBottomSheetLayout_peekHeight, PEEK_HEIGHT_AUTO)
        );
        this.setExpandedOffset(
                expandedOffsetValue != null
                && expandedOffsetValue.data == TypedValue.TYPE_FIRST_INT
                ? expandedOffsetValue.data
                : typedArray.getDimensionPixelSize(R.styleable.ModalBottomSheetLayout_expandedOffset, DEFAULT_EXPANDED_OFFSET)
        );
        this.setState(                                    typedArray.getInt(    R.styleable.ModalBottomSheetLayout_state,                                     STATE_COLLAPSED));
        this.setSaveFlags(                                typedArray.getInt(    R.styleable.ModalBottomSheetLayout_saveFlags,                                 SAVE_NONE));
        this.setSignificantVelocityThreshold(             typedArray.getInt(    R.styleable.ModalBottomSheetLayout_significantVelocityThreshold,              DEFAULT_SIGNIFICANT_VEL_THRESHOLD));
        this.setHalfExpandedRatio(                        typedArray.getFloat(  R.styleable.ModalBottomSheetLayout_halfExpandedRatio,                         DEFAULT_HALF_EXPANDED_RATIO));
        this.setHideFriction(                             typedArray.getFloat(  R.styleable.ModalBottomSheetLayout_hideFriction,                              HIDE_FRICTION));
        this.setHideable(                                 typedArray.getBoolean(R.styleable.ModalBottomSheetLayout_hideable,                                  DEFAULT_HIDEABLE));
        this.setDraggable(                                typedArray.getBoolean(R.styleable.ModalBottomSheetLayout_draggable,                                 DEFAULT_DRAGGABLE));
        this.setEdgeToEdgeEnabled(                        typedArray.getBoolean(R.styleable.ModalBottomSheetLayout_enableEdgeToEdge,                          DEFAULT_EDGE_TO_EDGE));
        this.setFitToContents(                            typedArray.getBoolean(R.styleable.ModalBottomSheetLayout_fitToContents,                             DEFAULT_FIT_TO_CONTENTS));
        this.setSkipCollapsed(                            typedArray.getBoolean(R.styleable.ModalBottomSheetLayout_skipCollapsed,                             DEFAULT_SKIP_COLLAPSED));
        this.setGestureInsetBottomIgnored(                typedArray.getBoolean(R.styleable.ModalBottomSheetLayout_gestureInsetBottomIgnored,                 DEFAULT_GESTURE_INSET_BOTTOM_IGNORED));
        this.setShouldRemoveExpandedCorners(              typedArray.getBoolean(R.styleable.ModalBottomSheetLayout_shouldRemoveExpandedCorners,               DEFAULT_SHOULD_REMOVE_EXPANDED_CORNERS));
        this.setUpdateImportantForAccessibilityOnSiblings(typedArray.getBoolean(R.styleable.ModalBottomSheetLayout_updateImportantForAccessibilityOnSiblings, DEFAULT_UPDATE_IMPORTANT_FOR_ACCESSIBILITY_ON_SIBLINGS));

        typedArray.recycle();
    }

    /**
     * Returns the state of the bottom sheet
     *
     * @return The state of the bottom sheet
     * @see BottomSheetBehavior.StableState
     */
    public @StableState int getState() {
        return this.mState;
    }

    /**
     * Sets the state of the bottom sheet
     *
     * @param state The state
     * @see BottomSheetBehavior.StableState
     */
    protected void setState(final @StableState int state) {
        this.mState = state;
    }

    /**
     * Returns save flags to be preserved in bottom sheet on configuration
     * change
     *
     * @return Save flags to be preserved in bottom sheet on configuration
     *         change
     * @see BottomSheetBehavior.SaveFlags
     */
    public @SaveFlags int getSaveFlags() {
        return this.mSaveFlags;
    }

    /**
     * Sets save flags to be preserved in bottom sheet on configuration change
     *
     * @param saveFlags The save flags
     * @see BottomSheetBehavior.SaveFlags
     */
    protected void setSaveFlags(final @SaveFlags int saveFlags) {
        this.mSaveFlags = saveFlags;
    }

    /**
     * Returns the height of the bottom sheet when it is collapsed
     *
     * @return The height of the bottom sheet when it is collapsed
     */
    public int getPeekHeight() {
        return this.mPeekHeight;
    }

    /**
     * Sets the height of the bottom sheet when it is collapsed
     *
     * @param peekHeight The height of the collapsed bottom sheet in pixels, or
     *                   {@link BottomSheetBehavior#PEEK_HEIGHT_AUTO} to
     *                   configure the sheet to peek automatically at
     *                   {@code 16:9} ratio key-line
     */
    protected void setPeekHeight(final int peekHeight) {
        this.mPeekHeight = peekHeight;
    }

    /**
     * Returns the top offset of the bottom sheet in the
     * {@link BottomSheetBehavior#STATE_EXPANDED} state when
     * {@link #isFitToContents() fitsToContent} is {@code false}.
     *
     * @return The expanded offset
     */
    public @IntRange(from = 0) int getExpandedOffset() {
        return this.mExpandedOffset;
    }

    /**
     * Determines the top offset of the bottom sheet in the
     * {@link BottomSheetBehavior#STATE_EXPANDED} state when
     * {@link #isFitToContents() fitsToContent} is {@code false}.
     * <p>
     * The default value is {@code 0}, which results in the sheet matching the
     * parent's top.
     *
     * @param expandedOffset An integer value greater than equal to {@code 0},
     *                       representing the
     *                       {@link BottomSheetBehavior#STATE_EXPANDED} offset.
     *                       Value must not exceed the offset in the half
     *                       expanded state.
     */
    protected void setExpandedOffset(final @IntRange(from = 0) int expandedOffset) {
        this.mExpandedOffset = expandedOffset;
    }

    /**
     * Returns the significant velocity threshold
     *
     * @return The significant velocity threshold
     */
    public int getSignificantVelocityThreshold() {
        return this.mSignificantVelocityThreshold;
    }

    /**
     * Sets the significant velocity threshold
     *
     * @param significantVelocityThreshold The significant velocity threshold
     */
    protected void setSignificantVelocityThreshold(final int significantVelocityThreshold) {
        this.mSignificantVelocityThreshold = significantVelocityThreshold;
    }

    /**
     * Returns the height of the bottom sheet in the
     * {@link BottomSheetBehavior#STATE_HALF_EXPANDED} state.
     * <p>
     * The default value is {@code 0.5}.
     *
     * @return The height of the bottom sheet in the
     *         {@link BottomSheetBehavior#STATE_HALF_EXPANDED} state
     */
    public @FloatRange(from = 0.0f, to = 1.0f, fromInclusive = false, toInclusive = false) float getHalfExpandedRatio() {
        return this.mHalfExpandedRatio;
    }

    /**
     * Determines the height of the bottom sheet in the
     * {@link BottomSheetBehavior#STATE_HALF_EXPANDED} state.
     * <p>
     * The material guidelines recommended a value of {@code 0.5}, which results
     * in the sheet filling half of the parent. The height of the bottom sheet
     * will be smaller as this ratio is decreased and taller as it is increased.
     * <p>
     * The default value is {@code 0.5}.
     *
     * @param halfExpandedRatio The half expanded ratio between {@code 0} and
     *                          {@code 1}
     */
    protected void setHalfExpandedRatio(final @FloatRange(from = 0.0f, to = 1.0f, fromInclusive = false, toInclusive = false) float halfExpandedRatio) {
        this.mHalfExpandedRatio = halfExpandedRatio;
    }

    /**
     * Returns the friction coefficient to hide the bottom sheet, or set it to
     * the next closest expanded state
     *
     * @return The hide friction coefficient
     */
    public float getHideFriction() {
        return this.mHideFriction;
    }

    /**
     * Sets the friction coefficient to hide the bottom sheet, or set it to the
     * next closest expanded state
     *
     * @param hideFriction The friction coefficient that determines the swipe
     *                     velocity needed to hide or set the bottom sheet to
     *                     the closest expanded state
     */
    protected void setHideFriction(final float hideFriction) {
        this.mHideFriction = hideFriction;
    }

    /**
     * Returns whether the bottom sheet can be hidden
     *
     * @return Whether the bottom sheet can be hidden
     */
    public boolean isHideable() {
        return this.mHideable;
    }

    /**
     * Sets whether the bottom sheet can be hidden
     *
     * @param state Whether the bottom sheet can be hidden
     */
    protected void setHideable(final boolean state) {
        this.mHideable = state;
    }

    /**
     * Returns whether the bottom sheet is can be collapsed/expanded by dragging
     *
     * @return Whether the bottom sheet is can be collapsed/expanded by dragging
     */
    public boolean isDraggable() {
        return this.mDraggable;
    }

    /**
     * Sets whether this bottom sheet is can be collapsed/expanded by dragging.
     * <p>
     * <b>NOTE:</b> When disabling dragging, an app will require to implement a
     *              custom way to expand/collapse the bottom sheet
     *
     * @param state Whether the bottom sheet is draggable
     */
    protected void setDraggable(final boolean state) {
        this.mDraggable = state;
    }

    /**
     * Returns whether edge-to-edge is enabled
     *
     * @return Whether edge-to-edge is enabled
     */
    public boolean isEdgeToEdgeEnabled() {
        return this.mEnableEdgeToEdge;
    }

    /**
     * Sets whether edge-to-edge is enabled
     *
     * @param state Whether edge-to-edge is enabled
     */
    protected void setEdgeToEdgeEnabled(final boolean state) {
        this.mEnableEdgeToEdge = state;
    }

    /**
     * Returns whether the height of the expanded sheet is determined by the
     * height of its contents, or if it is expanded in two stages (half the
     * height of the parent container, full height of parent container).
     * <p>
     * The default value is {@code true}.
     *
     * @return Whether the bottom sheet should fit to contents
     */
    public boolean isFitToContents() {
        return this.mFitToContents;
    }

    /**
     * Sets whether the height of the expanded sheet is determined by the height
     * of its contents, or if it is expanded in two stages (half the height of
     * the parent container, full height of parent container).
     * <p>
     * The default value is {@code true}.
     *
     * @param state Whether the bottom sheet should fit to contents
     */
    protected void setFitToContents(final boolean state) {
        this.mFitToContents = state;
    }

    /**
     * Returns whether this bottom sheet should skip the collapsed state when it
     * is being hidden after it is expanded once.
     * <p>
     * This flag has no effect unless the sheet is hideable.
     *
     * @return Whether the bottom sheet should skip collapsed state
     */
    public boolean isSkipCollapsed() {
        return this.mSkipCollapsed;
    }

    /**
     * Sets whether this bottom sheet should skip the collapsed state when it is
     * being hidden after it is expanded once.
     * <p>
     * Setting this to true has no effect unless the sheet is hideable.
     *
     * @param state Whether the bottom sheet should skip collapsed state
     */
    protected void setSkipCollapsed(final boolean state) {
        this.mSkipCollapsed = state;
    }

    /**
     * Returns whether this bottom sheet should adjust it's position based on
     * the system gesture area on Android Q and above.
     * <p>
     * <b>NOTE:</b> the bottom sheet will only adjust it's position if it would
     *              be unable to be scrolled upwards because the
     *              {@link #getPeekHeight() peek height} is less than the
     *              gesture inset margins, (because that would cause a gesture
     *              conflict), gesture navigation is enabled, and this flag is
     *              false.
     *
     * @return Whether the gesture inset bottom is ignored
     */
    public boolean isGestureInsetBottomIgnored() {
        return this.mGestureInsetBottomIgnored;
    }

    /**
     * Sets whether this bottom sheet should adjust it's position based on the
     * system gesture area on Android Q and above.
     * <p>
     * <b>NOTE:</b> the bottom sheet will only adjust it's position if it would
     *              be unable to be scrolled upwards because the
     *              {@link #getPeekHeight() peek height} is less than the
     *              gesture inset margins, (because that would cause a gesture
     *              conflict), gesture navigation is enabled, and this flag is
     *              false.
     *
     * @param state Whether the gesture inset bottom is ignored
     */
    protected void setGestureInsetBottomIgnored(final boolean state) {
        this.mGestureInsetBottomIgnored = state;
    }

    /**
     * Returns whether the bottom sheet should remove its corners when it
     * reaches the expanded state.
     * <p>
     * If false, the bottom sheet will only remove its corners if it is expanded
     * and reaches the top of the screen.
     *
     * @return Whether the expanded corners should be removed
     */
    public boolean isShouldRemoveExpandedCorners() {
        return this.mShouldRemoveExpandedCorners;
    }

    /**
     * Sets whether the bottom sheet should remove its corners when it reaches
     * the expanded state.
     * <p>
     * If false, the bottom sheet will only remove its corners if it is expanded
     * and reaches the top of the screen.
     *
     * @param state Whether the expanded corners should be removed
     */
    protected void setShouldRemoveExpandedCorners(final boolean state) {
        this.mShouldRemoveExpandedCorners = state;
    }

    /**
     * Returns whether the bottom sheet should update the accessibility status
     * of its {@link CoordinatorLayout} siblings when expanded
     *
     * @return Whether the important for accessibility should be updated on
     *         siblings
     */
    public boolean isUpdateImportantForAccessibilityOnSiblings() {
        return this.mUpdateImportantForAccessibilityOnSiblings;
    }

    /**
     * Sets whether the bottom sheet should update the accessibility status of
     * its {@link CoordinatorLayout} siblings when expanded.
     * <p>
     * Set this to true if the expanded state of the sheet blocks access to
     * siblings (e.g., when the sheet expands over the full screen).
     *
     * @param state Whether the important for accessibility should be updated on
     *              siblings
     */
    protected void setUpdateImportantForAccessibilityOnSiblings(final boolean state) {
        this.mUpdateImportantForAccessibilityOnSiblings = state;
    }
}
