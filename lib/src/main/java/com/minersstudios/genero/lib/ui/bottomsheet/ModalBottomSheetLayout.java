package com.minersstudios.genero.lib.ui.bottomsheet;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.*;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.*;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.minersstudios.genero.lib.R;
import com.minersstudios.genero.lib.util.EdgeToEdgeUtil;

/**
 * Represents a modal bottom sheet layout that should be used as the root of a
 * {@link ModalBottomSheetFragment}
 *
 * @see ModalBottomSheetFragment
 * @noinspection unused
 */
public class ModalBottomSheetLayout extends LinearLayout {
    private BottomLayout mBottomContainer;

    //<editor-fold desc="Styleable attributes" defaultstate="collapsed">
    private int mMaxWidth;
    private int mMaxHeight;
    private @StableState int mState;
    private @SaveFlags int mSaveFlags;
    private int mPeekHeight;
    private int mExpandedOffset;
    private int mSignificantVelocityThreshold;
    private float mHalfExpandedRatio;
    private float mHideFriction;
    private float mDismissOffset;
    private boolean mHideable;
    private boolean mDraggable;
    private boolean mEnableEdgeToEdge;
    private boolean mFitToContents;
    private boolean mSkipCollapsed;
    private boolean mGestureInsetBottomIgnored;
    private boolean mShouldRemoveExpandedCorners;
    private boolean mUpdateImportantForAccessibilityOnSiblings;
    private boolean mCancelOnTouchOutside;
    private @LayoutRes int mBottomViewResId;
    //</editor-fold>

    //<editor-fold desc="Default attribute values" defaultstate="collapsed">
    public static final int DEFAULT_SIGNIFICANT_VEL_THRESHOLD = 500; // BottomSheetBehavior.DEFAULT_SIGNIFICANT_VEL_THRESHOLD
    public static final float DEFAULT_HALF_EXPANDED_RATIO = 0.5f;    // from BottomSheetBehavior
    public static final float HIDE_FRICTION = 0.1f;                  // BottomSheetBehavior.HIDE_FRICTION
    public static final float DISMISS_OFFSET = -0.95f;
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
        TypedValue value;

        value = typedArray.peekValue(R.styleable.ModalBottomSheetLayout_widthMax);
        this.setMaxWidth(
                value != null
                && value.data == MATCH_PARENT
                ? value.data
                : typedArray.getDimensionPixelSize(R.styleable.ModalBottomSheetLayout_widthMax, MATCH_PARENT)
        );

        value = typedArray.peekValue(R.styleable.ModalBottomSheetLayout_heightMax);
        this.setMaxHeight(
                value != null
                && value.data == MATCH_PARENT
                ? value.data
                : typedArray.getDimensionPixelSize(R.styleable.ModalBottomSheetLayout_heightMax, MATCH_PARENT)
        );

        value = typedArray.peekValue(R.styleable.ModalBottomSheetLayout_peekHeight);
        this.setPeekHeight(
                value != null
                && value.data == PEEK_HEIGHT_AUTO
                ? value.data
                : typedArray.getDimensionPixelSize(R.styleable.ModalBottomSheetLayout_peekHeight, PEEK_HEIGHT_AUTO)
        );

        value = typedArray.peekValue(R.styleable.ModalBottomSheetLayout_expandedOffset);
        this.setExpandedOffset(
                value != null
                && value.data == TypedValue.TYPE_FIRST_INT
                ? value.data
                : typedArray.getDimensionPixelSize(R.styleable.ModalBottomSheetLayout_expandedOffset, 0)
        );

        this.setState(                                    typedArray.getInt(       R.styleable.ModalBottomSheetLayout_state,                                     STATE_COLLAPSED));
        this.setSaveFlags(                                typedArray.getInt(       R.styleable.ModalBottomSheetLayout_saveFlags,                                 SAVE_NONE));
        this.setSignificantVelocityThreshold(             typedArray.getInt(       R.styleable.ModalBottomSheetLayout_significantVelocityThreshold,              DEFAULT_SIGNIFICANT_VEL_THRESHOLD));
        this.setHalfExpandedRatio(                        typedArray.getFloat(     R.styleable.ModalBottomSheetLayout_halfExpandedRatio,                         DEFAULT_HALF_EXPANDED_RATIO));
        this.setHideFriction(                             typedArray.getFloat(     R.styleable.ModalBottomSheetLayout_hideFriction,                              HIDE_FRICTION));
        this.setDismissOffset(                            typedArray.getFloat(     R.styleable.ModalBottomSheetLayout_dismissOffset,                             DISMISS_OFFSET));
        this.setHideable(                                 typedArray.getBoolean(   R.styleable.ModalBottomSheetLayout_hideable,                                  true));
        this.setDraggable(                                typedArray.getBoolean(   R.styleable.ModalBottomSheetLayout_draggable,                                 true));
        this.setEdgeToEdgeEnabled(                        typedArray.getBoolean(   R.styleable.ModalBottomSheetLayout_enableEdgeToEdge,                          true));
        this.setFitToContents(                            typedArray.getBoolean(   R.styleable.ModalBottomSheetLayout_fitToContents,                             true));
        this.setSkipCollapsed(                            typedArray.getBoolean(   R.styleable.ModalBottomSheetLayout_skipCollapsed,                             false));
        this.setGestureInsetBottomIgnored(                typedArray.getBoolean(   R.styleable.ModalBottomSheetLayout_gestureInsetBottomIgnored,                 false));
        this.setShouldRemoveExpandedCorners(              typedArray.getBoolean(   R.styleable.ModalBottomSheetLayout_shouldRemoveExpandedCorners,               false));
        this.setUpdateImportantForAccessibilityOnSiblings(typedArray.getBoolean(   R.styleable.ModalBottomSheetLayout_updateImportantForAccessibilityOnSiblings, false));
        this.setCancelOnTouchOutside(                     typedArray.getBoolean(   R.styleable.ModalBottomSheetLayout_cancelOnTouchOutside,                      true));
        this.setBottomViewResId(                          typedArray.getResourceId(R.styleable.ModalBottomSheetLayout_bottomView,                                NO_ID));
        typedArray.recycle();

        if (this.hasBottomView()) {
            this.mBottomContainer = this.initBottomContainer();
        }
    }

    /**
     * Returns the bottom container view
     *
     * @return The bottom container view if {@link #hasBottomView()} is
     *         {@code true} or null if it is called before
     *         {@link ModalBottomSheetFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    public @Nullable BottomLayout getBottomContainer() {
        return this.mBottomContainer;
    }

    /**
     * Returns the bottom container view
     *
     * @return The bottom container view
     * @throws IllegalStateException If the bottom container is not set or this
     *                               was called before
     *                               {@link ModalBottomSheetFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    public @NonNull BottomLayout requireBottomContainer() {
        final BottomLayout bottomContainer = this.getBottomContainer();

        if (bottomContainer == null) {
            throw new IllegalStateException(
                    "Fragment " + this + " did not have a specified bottom container or this was called before onStart()"
            );
        }

        return bottomContainer;
    }

    /**
     * Returns the maximum width of the bottom sheet
     *
     * @return The maximum width of the bottom sheet
     */
    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    /**
     * Sets the maximum width of the bottom sheet
     *
     * @param maxWidth The maximum width of the bottom sheet
     */
    protected void setMaxWidth(final int maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    /**
     * Returns the maximum height of the bottom sheet
     *
     * @return The maximum height of the bottom sheet
     */
    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    /**
     * Sets the maximum height of the bottom sheet
     *
     * @param maxHeight The maximum height of the bottom sheet
     */
    protected void setMaxHeight(final int maxHeight) {
        this.mMaxHeight = maxHeight;
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
     * Returns the offset to dismiss the bottom sheet
     *
     * @return The offset to dismiss the bottom sheet
     */
    public @FloatRange(from = -1.0f, to = 0.0f, fromInclusive = false, toInclusive = false) float getDismissOffset() {
        return this.mDismissOffset;
    }

    /**
     * Sets the offset to dismiss the bottom sheet
     *
     * @param dismissOffset The offset to dismiss the bottom sheet
     */
    protected void setDismissOffset(final @FloatRange(from = -1.0f, to = 0.0f, fromInclusive = false, toInclusive = false) float dismissOffset) {
        this.mDismissOffset = dismissOffset;
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

    /**
     * Returns whether the bottom sheet should be dismissed when touched outside
     * of the bottom sheet
     *
     * @return Whether the bottom sheet should be dismissed when touched outside
     *         of the bottom sheet
     */
    public boolean isCancelOnTouchOutside() {
        return this.mCancelOnTouchOutside;
    }

    /**
     * Sets whether the bottom sheet should be dismissed when touched outside of
     * the bottom sheet
     *
     * @param state Whether the bottom sheet should be dismissed when touched
     *              outside of the bottom sheet
     */
    protected void setCancelOnTouchOutside(final boolean state) {
        this.mCancelOnTouchOutside = state;
    }

    /**
     * Returns the view resource ID of the bottom sheet
     *
     * @return The view resource ID of the bottom sheet
     */
    public @LayoutRes int getBottomViewResId() {
        return this.mBottomViewResId;
    }

    /**
     * Sets the view resource ID of the bottom sheet
     *
     * @param bottomViewResId The view resource ID of the bottom sheet
     */
    protected void setBottomViewResId(final @LayoutRes int bottomViewResId) {
        this.mBottomViewResId = bottomViewResId;
    }

    /**
     * Returns whether the bottom sheet has a bottom view
     *
     * @return Whether the bottom sheet has a bottom view
     */
    public boolean hasBottomView() {
        return this.mBottomViewResId != NO_ID;
    }

    /**
     * Applies the attributes of the modal bottom sheet layout to the bottom
     * sheet dialog and its callback
     *
     * @param dialog   The bottom sheet dialog
     * @param callback The callback of the bottom sheet dialog
     */
    public void applyAttributes(
            final @NonNull BottomSheetDialog dialog,
            final @Nullable ModalBottomSheetFragment.Callback callback
    ) {
        final BottomSheetBehavior<?> behavior = dialog.getBehavior();

        behavior.setMaxWidth(this.getMaxWidth());
        behavior.setMaxHeight(this.getMaxHeight());
        behavior.setState(this.getState());
        behavior.setSaveFlags(this.getSaveFlags());
        behavior.setPeekHeight(this.getPeekHeight());
        behavior.setExpandedOffset(this.getExpandedOffset());
        behavior.setSignificantVelocityThreshold(this.getSignificantVelocityThreshold());
        behavior.setHalfExpandedRatio(this.getHalfExpandedRatio());
        behavior.setHideFriction(this.getHideFriction());
        behavior.setHideable(this.isHideable());
        behavior.setDraggable(this.isDraggable());
        behavior.setFitToContents(this.isFitToContents());
        behavior.setSkipCollapsed(this.isSkipCollapsed());
        behavior.setGestureInsetBottomIgnored(this.isGestureInsetBottomIgnored());
        behavior.setShouldRemoveExpandedCorners(this.isShouldRemoveExpandedCorners());
        behavior.setUpdateImportantForAccessibilityOnSiblings(this.isUpdateImportantForAccessibilityOnSiblings());
        dialog.setCanceledOnTouchOutside(this.isCancelOnTouchOutside());
        this.applyEdgeToEdge(dialog);

        if (callback != null) {
            callback.setDismissOffset(this.getDismissOffset());
        }
    }

    /**
     * Shows the bottom container in the bottom sheet dialog.
     * <br>
     * <b>NOTE:</b> Should be called in {@link Fragment#onStart()} or an
     * {@link IllegalStateException exception} will be thrown.
     *
     * @param dialog The bottom sheet dialog
     * @throws IllegalStateException If the bottom sheet dialog is missing
     *                               required views :
     *                               {@code com.google.android.material.R.id.container}.
     *                               <br>
     *                               Usually thrown when this is called before
     *                               {@link Fragment#onStart()}.
     */
    public void showBottomContainer(final @NonNull Dialog dialog) throws IllegalStateException {
        final BottomLayout bottomContainer = this.getBottomContainer();

        if (bottomContainer == null) {
            Log.w(
                    "ModalBottomSheetLayout",
                    "Attempted to show bottom container, but it is not set"
            );

            return;
        }

        final FrameLayout container = dialog.findViewById(com.google.android.material.R.id.container);

        if (container == null) {
            throw new IllegalStateException(
                    "BottomSheetDialog is missing required views (container)"
            );
        }

        if (bottomContainer.getParent() == null) {
            final View bottomView = bottomContainer.getChildAt(0);

            if (bottomView == null) {
                Log.w(
                        "ModalBottomSheetLayout",
                        "Bottom container does not have a child view, nothing to show"
                );
            } else {
                this.applyInsets(bottomView, dialog.getWindow());
                container.addView(bottomContainer);
            }
        }
    }

    private @NonNull BottomLayout initBottomContainer() {
        final BottomLayout container = this.createBottomContainer();
        final View bottomView =
                LayoutInflater
                .from(this.getContext())
                .inflate(this.getBottomViewResId(), container, false);

        container.addView(bottomView);
        bottomView.post(
                () -> this.setPadding(
                        this.getPaddingLeft(),
                        this.getPaddingTop(),
                        this.getPaddingRight(),
                        this.getPaddingBottom() + bottomView.getHeight()
                )
        );

        return container;
    }

    private @NonNull BottomLayout createBottomContainer() {
        final BottomLayout bottomContainer = new BottomLayout(this.getContext());
        final BottomLayout.LayoutParams layoutParams =
                new BottomLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;

        bottomContainer.setLayoutParams(layoutParams);
        bottomContainer.setMaxWidth(this.getMaxWidth());

        return bottomContainer;
    }

    private void applyEdgeToEdge(final @NonNull BottomSheetDialog dialog) {
        final Window window = dialog.getWindow();

        if (
                window != null
                && this.isEdgeToEdgeEnabled()
        ) {
            EdgeToEdgeUtil.apply(
                    window,
                    dialog.getEdgeToEdgeEnabled()
            );
        }
    }

    private void applyInsets(
            final @NonNull View view,
            final @Nullable Window window
    ) {
        if (window == null) {
            return;
        }

        view.post(
                () -> {
                    final WindowInsets insets = window.getDecorView().getRootWindowInsets();
                    final int left = insets.getSystemWindowInsetLeft();
                    final int right = insets.getSystemWindowInsetRight();
                    final int bottom = insets.getSystemWindowInsetBottom();

                    view.setPadding(
                            view.getPaddingLeft() + left,
                            view.getPaddingTop(),
                            view.getPaddingRight() + right,
                            view.getPaddingBottom() + bottom
                    );
                }
        );
    }

    public static final class BottomLayout extends FrameLayout {
        private int mMaxWidth;
        private int mMaxHeight;

        public BottomLayout(final @NonNull Context context) {
            super(context);
        }

        /**
         * Returns the maximum width of the bottom container
         *
         * @return The maximum width of the bottom container
         */
        public int getMaxWidth() {
            return this.mMaxWidth;
        }

        /**
         * Sets the maximum width of the bottom container
         *
         * @param maxWidth The maximum width of the bottom container
         */
        public void setMaxWidth(final int maxWidth) {
            this.mMaxWidth = maxWidth;
        }

        /**
         * Returns the maximum height of the bottom container
         *
         * @return The maximum height of the bottom container
         */
        public int getMaxHeight() {
            return this.mMaxHeight;
        }

        /**
         * Sets the maximum height of the bottom container
         *
         * @param maxHeight The maximum height of the bottom container
         */
        public void setMaxHeight(final int maxHeight) {
            this.mMaxHeight = maxHeight;
        }

        @Override
        protected void onMeasure(
                final int widthMeasureSpec,
                final int heightMeasureSpec
        ) {
            final int maxedWidthMeasureSpec;
            final int maxedHeightMeasureSpec;

            if (this.mMaxWidth > 0) {
                maxedWidthMeasureSpec =
                        MeasureSpec.makeMeasureSpec(
                                Math.min(
                                        MeasureSpec.getSize(widthMeasureSpec),
                                        this.mMaxWidth
                                ),
                                MeasureSpec.getMode(widthMeasureSpec)
                        );
            } else {
                maxedWidthMeasureSpec = widthMeasureSpec;
            }

            if (this.mMaxHeight > 0) {
                maxedHeightMeasureSpec =
                        MeasureSpec.makeMeasureSpec(
                                Math.min(
                                        MeasureSpec.getSize(heightMeasureSpec),
                                        this.mMaxHeight
                                ),
                                MeasureSpec.getMode(heightMeasureSpec)
                        );
            } else {
                maxedHeightMeasureSpec = heightMeasureSpec;
            }

            super.onMeasure(
                    maxedWidthMeasureSpec,
                    maxedHeightMeasureSpec
            );
        }
    }
}
