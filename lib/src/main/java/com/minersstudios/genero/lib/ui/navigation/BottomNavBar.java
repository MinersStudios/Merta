package com.minersstudios.genero.lib.ui.navigation;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static com.minersstudios.genero.lib.ui.button.ButtonParamHolder.INVALID;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import androidx.annotation.*;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.ui.NavigationUI;

import com.minersstudios.genero.lib.R;
import com.minersstudios.genero.lib.throwable.ParseAttributeException;
import com.minersstudios.genero.lib.ui.button.ButtonParamHolder;
import com.minersstudios.genero.lib.ui.corners.CornerAttribute;
import com.minersstudios.genero.lib.ui.corners.CornerType;
import com.minersstudios.genero.lib.xml.XmlParser;
import com.minersstudios.genero.lib.xml.tag.XmlTag;
import com.minersstudios.genero.lib.xml.tag.attribute.ResourceAttribute;
import com.minersstudios.genero.lib.xml.tag.attribute.reader.ResourceAttributeReader;

import org.jetbrains.annotations.UnmodifiableView;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

@SuppressWarnings("unused")
public class BottomNavBar extends View {
    private final ObjectList<Item> itemList;
    private final Paint barBackgroundPaint;
    private final Rect barRect;
    private final Paint indicatorPaint;
    private Menu menu;
    private ItemSelectedListener itemSelectedListener;
    private ItemReselectedListener itemReselectedListener;
    private @Dimension float scale;
    private @Dimension float indicatorPos;
    private int activeItemIndex;
    private int previousItemIndex;
    private @ColorInt int currentIconTint;
    private @ColorInt int previousIconTint;

    //<editor-fold desc="Styleable attributes" defaultstate="collapsed">
    private int menuRes;
    private @Dimension float itemSize;
    private @ColorInt int barColor;
    private @Dimension float barRadius;
    private @Dimension float barPadding;
    private @ColorInt int indicatorColor;
    private @Dimension float indicatorRadius;
    private @Dimension float iconSize;
    private @ColorInt int iconTint;
    private @ColorInt int iconTintActive;
    private int animDuration;
    private Interpolator interpolator;
    private CornerAttribute corners;
    //</editor-fold>

    //<editor-fold desc="Constants" defaultstate="collapsed">
    private static final String ITEM_TAG =                   "item";
    private static final String ICON_ATTRIBUTE =             "icon";
    private static final String BUTTON_ICON_ATTRIBUTE =      "buttonIcon";
    private static final String BUTTON_ICON_SIZE_ATTRIBUTE = "buttonIconSize";
    private static final String BUTTON_ICON_TINT_ATTRIBUTE = "buttonIconTint";
    private static final String BUTTON_HANDLER_ATTRIBUTE =   "buttonHandler";

    private static final int INVALID_RES = -1;
    private static final int DEFAULT_STYLE = R.style.Base_Widget_Genero_BottomNavBar;
    //</editor-fold>

    /**
     * Creates a new bottom navigation bar
     *
     * @param context Context
     */
    public BottomNavBar(final @NonNull Context context) {
        this(context, null);
    }

    /**
     * Creates a new bottom navigation bar
     *
     * @param context Context
     * @param attrSet Attribute set
     */
    public BottomNavBar(
            final @NonNull Context context,
            final @Nullable AttributeSet attrSet
    ) {
        this(context, attrSet, 0);
    }

    /**
     * Creates a new bottom navigation bar
     *
     * @param context      Context
     * @param attrSet      Attribute set
     * @param defStyleAttr Default style attribute
     */
    public BottomNavBar(
            final @NonNull Context context,
            final @Nullable AttributeSet attrSet,
            final @AttrRes int defStyleAttr
    ) {
        super(context, attrSet, defStyleAttr, DEFAULT_STYLE);

        this.itemList = new ObjectArrayList<>();
        this.barBackgroundPaint = new Paint();
        this.barRect = new Rect();
        this.indicatorPaint = new Paint();
        final TypedArray attrArray =
                context.getTheme().obtainStyledAttributes(
                        attrSet,
                        R.styleable.BottomNavBar,
                        defStyleAttr,
                        DEFAULT_STYLE
                );

        this.setMenuRes(        attrArray.getResourceId(        R.styleable.BottomNavBar_menu,            INVALID_RES));
        this.setItemSize(       attrArray.getDimensionPixelSize(R.styleable.BottomNavBar_itemSize,        0));
        this.setBarColor(       attrArray.getColor(             R.styleable.BottomNavBar_barColor,        0));
        this.setBarRadius(      attrArray.getDimensionPixelSize(R.styleable.BottomNavBar_barRadius,       0));
        this.setBarPadding(     attrArray.getDimensionPixelSize(R.styleable.BottomNavBar_barPadding,      0));
        this.setIndicatorColor( attrArray.getColor(             R.styleable.BottomNavBar_indicatorColor,  0));
        this.setIndicatorRadius(attrArray.getDimensionPixelSize(R.styleable.BottomNavBar_indicatorRadius, 0));
        this.setIconSize(       attrArray.getDimensionPixelSize(R.styleable.BottomNavBar_iconSize,        0));
        this.setIconTint(       attrArray.getColor(             R.styleable.BottomNavBar_iconTint,        this.indicatorColor));
        this.setIconTintActive( attrArray.getColor(             R.styleable.BottomNavBar_iconTintActive,  this.barColor));
        this.setAnimDuration(   attrArray.getInteger(           R.styleable.BottomNavBar_animDuration,    0));
        this.setInterpolator(   attrArray.getResourceId(        R.styleable.BottomNavBar_interpolator,    0));
        this.setCorners((byte)  attrArray.getInteger(           R.styleable.BottomNavBar_corners,         CornerAttribute.NO_CORNERS));
        attrArray.recycle();

        this.currentIconTint = this.iconTintActive;
        this.previousIconTint = this.iconTint;

        this.barBackgroundPaint.setAntiAlias(true);
        this.barBackgroundPaint.setStyle(Paint.Style.FILL);
        this.barBackgroundPaint.setColor(this.barColor);
        this.indicatorPaint.setAntiAlias(true);
        this.indicatorPaint.setStyle(Paint.Style.FILL);
        this.indicatorPaint.setColor(this.indicatorColor);
        this.invalidate();
    }

    /**
     * Returns the menu
     *
     * @return The menu
     */
    public @Nullable Menu getMenu() {
        return this.menu;
    }

    /**
     * Returns the menu resource ID
     *
     * @return The menu resource ID
     */
    public @MenuRes int getMenuRes() {
        return this.menuRes;
    }

    /**
     * Sets the menu resource ID and parses new menu items
     *
     * @param menuRes New menu resource ID
     */
    public void setMenuRes(final @MenuRes int menuRes) {
        this.menuRes = menuRes;

        final PopupMenu popupMenu = new PopupMenu(this.getContext(), this);

        popupMenu.inflate(menuRes);

        this.menu = popupMenu.getMenu();

        if (menuRes != INVALID_RES) {
            this.parseMenuItems();
        }
    }

    /**
     * Returns the item size
     *
     * @return The item size
     */
    public @Dimension float getItemSize() {
        return this.itemSize;
    }

    /**
     * Sets the item size
     *
     * @param itemSize New item size
     */
    public void setItemSize(final @Dimension float itemSize) {
        this.itemSize = itemSize;
    }

    /**
     * Returns the bar background color
     *
     * @return The bar background color
     */
    public @ColorInt int getBarColor() {
        return this.barColor;
    }

    /**
     * Sets the bar background color
     *
     * @param backgroundColor New bar background color
     */
    public void setBarColor(final @ColorInt int backgroundColor) {
        this.barColor = backgroundColor;
    }

    /**
     * Returns the bar radius
     *
     * @return The bar radius
     */
    public @Dimension float getBarRadius() {
        return this.barRadius;
    }

    /**
     * Sets the bar radius
     *
     * @param barRadius New bar radius
     */
    public void setBarRadius(final @Dimension float barRadius) {
        this.barRadius = barRadius;
    }

    /**
     * Returns the bar padding
     *
     * @return The bar padding
     */
    public @Dimension float getBarPadding() {
        return this.barPadding;
    }

    /**
     * Sets the bar padding
     *
     * @param value New bar padding
     */
    public void setBarPadding(final @Dimension float value) {
        this.barPadding = value;
    }

    /**
     * Returns the indicator color
     *
     * @return The indicator color
     */
    public @ColorInt int getIndicatorColor() {
        return this.indicatorColor;
    }

    /**
     * Sets the indicator color
     *
     * @param indicatorColor New indicator color
     */
    public void setIndicatorColor(final @ColorInt int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    /**
     * Returns the indicator radius
     *
     * @return The indicator radius
     */
    public @Dimension float getIndicatorRadius() {
        return this.indicatorRadius;
    }

    /**
     * Sets the indicator radius
     *
     * @param indicatorRadius New indicator radius
     */
    public void setIndicatorRadius(final @Dimension float indicatorRadius) {
        this.indicatorRadius = indicatorRadius;
    }

    /**
     * Returns the icon size
     *
     * @return The icon size
     */
    public @Dimension float getIconSize() {
        return this.iconSize;
    }

    /**
     * Sets the icon size
     *
     * @param iconSize New icon size
     */
    public void setIconSize(final @Dimension float iconSize) {
        this.iconSize = iconSize;
    }

    /**
     * Returns the icon tint
     *
     * @return The icon tint
     */
    public @ColorInt int getIconTint() {
        return this.iconTint;
    }

    /**
     * Sets the icon tint
     *
     * @param iconTint New icon tint
     */
    public void setIconTint(final @ColorInt int iconTint) {
        this.iconTint = iconTint;
    }

    /**
     * Returns the active icon tint
     *
     * @return The active icon tint
     */
    public @ColorInt int getIconTintActive() {
        return this.iconTintActive;
    }

    /**
     * Sets the active icon tint
     *
     * @param iconTintActive New active icon tint
     */
    public void setIconTintActive(final @ColorInt int iconTintActive) {
        this.iconTintActive = iconTintActive;
    }

    /**
     * Returns the animation duration
     *
     * @return The animation duration
     */
    public int getAnimDuration() {
        return this.animDuration;
    }

    /**
     * Sets the animation duration
     *
     * @param duration New animation duration
     */
    public void setAnimDuration(final int duration) {
        this.animDuration = duration;
    }

    /**
     * Returns the interpolator
     *
     * @return The interpolator
     */
    public @Nullable Interpolator getInterpolator() {
        return this.interpolator;
    }

    /**
     * Sets the interpolator
     *
     * @param interpolatorRes New interpolator resource
     */
    public void setInterpolator(final @InterpolatorRes int interpolatorRes) {
        this.setInterpolator(AnimationUtils.loadInterpolator(this.getContext(), interpolatorRes));
    }

    /**
     * Sets the interpolator
     *
     * @param interpolator New interpolator
     */
    public void setInterpolator(final @Nullable Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * Returns the corners of the bar background
     *
     * @return The corners of the bar background
     */
    public @NonNull CornerAttribute getCorners() {
        return this.corners;
    }

    /**
     * Sets the corners of the bar background
     *
     * @param value New corners value
     */
    public void setCorners(final byte value) {
        this.setCorners(CornerAttribute.create(value));
    }

    /**
     * Sets the corners of the bar background
     *
     * @param attribute New corners attribute
     */
    public void setCorners(final @NonNull CornerAttribute attribute) {
        this.corners = attribute;
    }

    /**
     * Returns an unmodifiable view of the menu items list
     *
     * @return An unmodifiable view of the menu items list
     */
    public @NonNull @UnmodifiableView List<Item> getItems() {
        return ObjectLists.unmodifiable(this.itemList);
    }

    /**
     * Returns an unmodifiable view of the actual menu items list, in case the
     * layout direction is RTL the list will be reversed
     *
     * @return An unmodifiable view of the actual menu items list
     */
    public @NonNull @UnmodifiableView List<Item> getActualItems() {
        if (this.getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            final ObjectList<Item> items = new ObjectArrayList<>(this.itemList);

            Collections.reverse(items);

            return ObjectLists.unmodifiable(items);
        }

        return ObjectLists.unmodifiable(this.itemList);
    }

    /**
     * Returns the item selected listener
     *
     * @return The item selected listener
     */
    public @Nullable ItemSelectedListener getItemSelectedListener() {
        return this.itemSelectedListener;
    }

    /**
     * Sets the item selected listener
     *
     * @param listener New listener
     */
    public void setItemSelectedListener(final @Nullable ItemSelectedListener listener) {
        this.itemSelectedListener = listener;
    }

    /**
     * Returns the item reselected listener
     *
     * @return The item reselected listener
     */
    public @Nullable ItemReselectedListener getItemReselectedListener() {
        return this.itemReselectedListener;
    }

    /**
     * Sets the item reselected listener
     *
     * @param listener New listener
     */
    public void setItemReselectedListener(final @Nullable ItemReselectedListener listener) {
        this.itemReselectedListener = listener;
    }

    /**
     * Returns the scale of the bar elements
     *
     * @return The scale of the bar elements
     */
    public @Dimension float getScale() {
        return this.scale;
    }

    /**
     * Returns the active item index
     *
     * @return The active item index
     */
    public int getActiveItemIndex() {
        return this.activeItemIndex;
    }

    /**
     * Returns the actual active item index, in case the layout direction is RTL
     * the index will be reversed
     *
     * @return The actual active item index
     */
    public int getActualActiveItemIndex() {
        return this.getActualIndex(this.activeItemIndex);
    }

    /**
     * Returns the previous item index
     *
     * @return The previous item index
     */
    public int getPreviousItemIndex() {
        return this.previousItemIndex;
    }

    /**
     * Returns the actual previous item index, in case the layout direction is
     * RTL the index will be reversed
     *
     * @return The actual previous item index
     */
    public int getActualPreviousItemIndex() {
        return this.getActualIndex(this.previousItemIndex);
    }

    /**
     * Sets the active and previous item indexes, also updates the view
     *
     * @param index New index
     */
    public void setActiveItemIndex(final int index) {
        if (this.activeItemIndex == index) {
            return;
        }

        this.previousItemIndex = this.activeItemIndex;
        this.activeItemIndex = index;

        this.applyItemActiveIndex(this.animDuration);
    }

    /**
     * Returns whether the navigation bar has an animation
     *
     * @return Whether the navigation bar has an animation
     */
    public boolean hasAnimation() {
        return this.getAnimDuration() > 0
                && this.getInterpolator() != null;
    }

    /**
     * Selects an item by its index
     *
     * @param index Index of the item
     */
    public void selectItem(final int index) {
        if (index != this.activeItemIndex) {
            this.setActiveItemIndex(index);

            if (this.itemSelectedListener != null) {
                this.itemSelectedListener.onItemSelect(index);
            }
        } else if (this.itemReselectedListener != null) {
            this.itemReselectedListener.onItemReselect(index);
        }
    }

    /**
     * Sets up the bottom navigation bar with a provided navigation controller
     *
     * @param controller Navigation controller
     * @throws IllegalStateException If the menu resource is not set
     */
    public void setupWithNavController(final @NonNull NavController controller) throws IllegalStateException {
        if (this.menu == null) {
            throw new IllegalStateException("Menu resource not set");
        }

        this.setupWithNavController(this.menu, controller);
    }

    /**
     * Sets up the bottom navigation bar with a provided menu and navigation
     * controller
     *
     * @param menu       Menu
     * @param controller Navigation controller
     */
    public void setupWithNavController(
            final @NonNull Menu menu,
            final @NonNull NavController controller
    ) {
        final WeakReference<BottomNavBar> weakReference = new WeakReference<>(this);

        this.setItemSelectedListener(pos -> NavigationUI.onNavDestinationSelected(menu.getItem(pos), controller));
        controller.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {

            @Override
            public void onDestinationChanged(
                    final @NonNull NavController controller,
                    final @NonNull NavDestination destination,
                    final @Nullable Bundle arguments
            ) {
                final BottomNavBar view = weakReference.get();

                if (view == null) {
                    controller.removeOnDestinationChangedListener(this);

                    return;
                }

                for (int i = 0; i < menu.size(); ++i) {
                    final MenuItem menuItem = menu.getItem(i);

                    if (matchDestination(destination, menuItem.getItemId())) {
                        menuItem.setChecked(true);
                        view.setActiveItemIndex(i);
                    }
                }
            }
        });
    }

    @SuppressWarnings("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(final @NonNull MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        if (this.barRect.contains(x, y)) {
            switch (event.getAction()) {
                case ACTION_DOWN:
                    this.performHapticFeedback(0);

                    return true;
                case ACTION_UP:
                    for (int i = 0; i < this.itemList.size(); ++i) {
                        if (this.itemList.get(i).contains(x, y)) {
                            this.selectItem(i);
                            this.playSoundEffect(SoundEffectConstants.CLICK);

                            break;
                        }
                    }
                    // Fall through
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(
            final int widthMeasureSpec,
            final int heightMeasureSpec
    ) {
        if (
                widthMeasureSpec < 0
                && heightMeasureSpec < 0
        ) {
            super.onMeasure(
                    -widthMeasureSpec,
                    -heightMeasureSpec
            );

            return;
        }

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final float expectedWidth = this.itemList.size() * this.itemSize;

        this.scale =
                expectedWidth > width
                ? width / expectedWidth
                : 1.0f;

        this.setMeasuredDimension(
                (int) (width * this.scale),
                (int) (this.itemSize * this.scale)
        );
    }

    @Override
    protected void onSizeChanged(
            final int width,
            final int height,
            final int oldWidth,
            final int oldHeight
    ) {
        this.measure(
                -MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                -MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );

        int lastX = 0;

        for (final Item item : this.getActualItems()) {
            item.setRectangle(
                    lastX,
                    0,
                    lastX + height,
                    height
            );

            lastX = lastX + height;
        }

        this.initBarRect();
        this.applyItemActiveIndex(0);
    }

    @Override
    protected void onDraw(final @NonNull Canvas canvas) {
        this.drawBarBackground(canvas);
        this.drawIndicator(canvas);
        this.drawIcons(canvas);
    }

    @Override
    protected @Nullable Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();

        if (superState == null) {
            return null;
        }

        final SavedState savedState = new SavedState(superState);

        savedState.menuRes = this.getMenuRes();
        savedState.indicatorPos = this.indicatorPos;
        savedState.activeItemIndex = this.getActiveItemIndex();
        savedState.previousItemIndex = this.getPreviousItemIndex();

        return savedState;
    }

    private int getActualIndex(final int index) {
        return this.getLayoutDirection() == LAYOUT_DIRECTION_RTL
               ? this.itemList.size() - 1 - index
               : index;
    }

    private void initBarRect() {
        final int height = this.getMeasuredHeight();

        this.barRect.set(
                0,
                0,
                height * this.itemList.size(),
                height
        );
    }

    private void drawBarBackground(final @NonNull Canvas canvas) {
        this.initBarRect();

        if (
                this.barRadius <= 0.0f
                || this.corners.hasNoCorners()
        ) {
            canvas.drawRect(
                    this.barRect,
                    this.barBackgroundPaint
            );
        } else {
            canvas.drawRoundRect(
                    this.barRect.left,
                    this.barRect.top,
                    this.barRect.right,
                    this.barRect.bottom,
                    this.barRadius,
                    this.barRadius,
                    this.barBackgroundPaint
            );

            if (!this.corners.hasAllCorners()) {
                final float width = this.barRect.width();
                final float height = this.barRect.height();

                if (!this.corners.has(CornerType.TOP_LEFT)) {
                    canvas.drawRect(
                            0.0f, 0.0f,
                            width / 2.0f, height / 2.0f,
                            this.barBackgroundPaint
                    );
                }

                if (!this.corners.has(CornerType.TOP_RIGHT)) {
                    canvas.drawRect(
                            width / 2.0f, 0.0f,
                            width, height / 2.0f,
                            this.barBackgroundPaint
                    );
                }

                if (!this.corners.has(CornerType.BOTTOM_LEFT)) {
                    canvas.drawRect(
                            0.0f, height / 2.0f,
                            width / 2.0f, height,
                            this.barBackgroundPaint
                    );
                }

                if (!this.corners.has(CornerType.BOTTOM_RIGHT)) {
                    canvas.drawRect(
                            width / 2.0f, height / 2.0f,
                            width, height,
                            this.barBackgroundPaint
                    );
                }
            }
        }
    }

    private void drawIndicator(final @NonNull Canvas canvas) {
        if (
                this.activeItemIndex < 0
                || this.activeItemIndex >= this.itemList.size()
        ) {
            return;
        }

        final float leftMargin = this.barRect.left + this.indicatorPos;
        final float paddedDiameter = this.barRect.height() - this.barPadding * this.scale;

        canvas.drawRoundRect(
                leftMargin + this.barPadding * this.scale,
                this.barRect.top + this.barPadding * this.scale,
                leftMargin + paddedDiameter,
                this.barRect.top + paddedDiameter,
                this.indicatorRadius,
                this.indicatorRadius,
                this.indicatorPaint
        );
    }

    private void drawIcons(final @NonNull Canvas canvas) {
        final int radius = this.barRect.height() / 2;
        final int halfIconSize = (int) (this.iconSize / 2 * this.scale);
        final int offset = halfIconSize - radius;

        for (int i = 0; i < this.itemList.size(); ++i) {
            final Item item = this.itemList.get(i);
            final Drawable icon = item.getIcon();
            final Rect rect = item.getRectangle();

            icon.mutate();
            icon.setBounds(
                    rect.left - offset,
                    rect.top - offset,
                    rect.right + offset,
                    rect.bottom + offset
            );
            icon.setTint(
                    i == this.activeItemIndex ? this.currentIconTint
                    : i == this.previousItemIndex ? this.previousIconTint
                    : this.iconTint
            );
            icon.draw(canvas);
        }
    }

    private void applyItemActiveIndex(final int animDuration) {
        if (
                this.itemList.isEmpty()
                || this.barRect.height() <= 0
        ) {
            return;
        }

        this.animateIndicator(
                animDuration,
                this.indicatorPos,
                this.getActualActiveItemIndex() * this.barRect.height(),
                animation -> {
                    this.indicatorPos = (float) animation.getAnimatedValue();
                    this.invalidate();
                }
        );
        this.animateIcon(
                animDuration,
                this.iconTint,
                this.iconTintActive,
                animation -> {
                    this.currentIconTint = (int) animation.getAnimatedValue();
                    this.invalidate();
                }
        );
        this.animateIcon(
                animDuration,
                this.iconTintActive,
                this.iconTint,
                animation -> {
                    this.previousIconTint = (int) animation.getAnimatedValue();
                    this.invalidate();
                }
        );
    }

    private void parseMenuItems() {
        this.itemList.clear();

        XmlParser
        .parseResource(this.getContext().getResources().getXml(this.menuRes))
        .filter(tag -> tag.getType() == START_TAG)
        .map(XmlTag::toStartTag)
        .filter(tag -> tag.getName().equals(ITEM_TAG))
        .map(tag -> tag.getAttributeReader(ResourceAttributeReader.class))
        .forEach(reader -> this.itemList.add(this.parseItem(reader)));
    }

    private @NonNull Item parseItem(final @NonNull ResourceAttributeReader reader) throws ParseAttributeException {
        Drawable icon = null;
        @DrawableRes int iconResId = INVALID;
        @Dimension float iconSize = INVALID;
        @ColorInt int iconTint = INVALID;
        String buttonHandler = null;

        for (final ResourceAttribute attribute : reader) {
            switch (attribute.getName()) {
                case ICON_ATTRIBUTE:
                    icon = ContextCompat.getDrawable(
                            this.getContext(),
                            attribute.getResourceValue(0)
                    );

                    break;
                case BUTTON_ICON_ATTRIBUTE:
                    iconResId = attribute.getResourceValue(INVALID);

                    break;
                case BUTTON_ICON_SIZE_ATTRIBUTE:
                    iconSize = attribute.getFloatValue(INVALID);

                    break;
                case BUTTON_ICON_TINT_ATTRIBUTE:
                    iconTint = attribute.getIntValue(INVALID);

                    break;
                case BUTTON_HANDLER_ATTRIBUTE:
                    buttonHandler = attribute.getValue();

                    break;
            }
        }

        if (icon == null) {
            throw new IllegalStateException("Item icon not found");
        }

        return new Item(
                icon,
                ButtonParamHolder
                .create(this.getContext())
                .icon(
                        iconResId,
                        iconSize,
                        iconTint
                )
                .handler(buttonHandler)
        );
    }

    private void animateIndicator(
            final int animDuration,
            final float startPos,
            final float endPos,
            final @Nullable ValueAnimator.AnimatorUpdateListener listener
    ) {
        final ValueAnimator animator = ValueAnimator.ofFloat(startPos, endPos);

        if (listener != null) {
            animator.addUpdateListener(listener);
        }

        animator.setDuration(animDuration);
        animator.setInterpolator(this.getInterpolator());
        animator.start();
    }

    private void animateIcon(
            final int animDuration,
            final @ColorInt int startColor,
            final @ColorInt int endColor,
            final @Nullable ValueAnimator.AnimatorUpdateListener listener
    ) {
        final ValueAnimator animator = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                startColor, endColor
        );

        if (listener != null) {
            animator.addUpdateListener(listener);
        }

        animator.setDuration(animDuration);
        animator.setInterpolator(this.getInterpolator());
        animator.start();
    }

    private static boolean matchDestination(
            final @NonNull NavDestination destination,
            final int destId
    ) {
        NavDestination currentDestination = destination;

        while (
                currentDestination.getId() != destId
                && currentDestination.getParent() != null
        ) {
            currentDestination = currentDestination.getParent();
        }

        return currentDestination.getId() == destId;
    }

    /**
     * Navigation bar item
     */
    public final class Item {
        private final Rect rectangle;
        private final Drawable icon;
        private final Object[] args;

        /**
         * Creates a new item with an empty rectangle and icon
         *
         * @param icon Icon of the item
         */
        public Item(
                final @NonNull Drawable icon,
                final Object... args
        ) {
            this(new Rect(), icon, args);
        }

        /**
         * Creates a new item with the given rectangle and icon
         *
         * @param rectangle Rectangle of the item
         * @param icon      Icon of the item
         */
        public Item(
                final @NonNull Rect rectangle,
                final @NonNull Drawable icon,
                final Object... args
        ) {
            this.rectangle = rectangle;
            this.icon = icon;
            this.args = args;
        }

        /**
         * Returns the index of the item
         *
         * @return The index of the item
         */
        public int getIndex() {
            return BottomNavBar.this.itemList.indexOf(this);
        }

        /**
         * Returns the rectangle of the item
         *
         * @return The rectangle of the item
         */
        public @NonNull Rect getRectangle() {
            return this.rectangle;
        }

        /**
         * Sets the rectangle of the item
         *
         * @param left   New left position
         * @param top    New top position
         * @param right  New right position
         * @param bottom New bottom position
         */
        public void setRectangle(
                final int left,
                final int top,
                final int right,
                final int bottom
        ) {
            this.rectangle.set(left, top, right, bottom);
        }

        /**
         * Sets the rectangle of the item
         *
         * @param rectangle New rectangle
         */
        public void setRectangle(final @NonNull Rect rectangle) {
            this.rectangle.set(rectangle);
        }

        /**
         * Returns the icon of the item
         *
         * @return The icon of the item
         */
        public @NonNull Drawable getIcon() {
            return this.icon;
        }

        /**
         * Returns the arguments of the item
         *
         * @return The arguments of the item
         */
        public Object[] getArgs() {
            return this.args;
        }

        /**
         * Returns whether the item contains the given coordinates
         *
         * @param x X coordinate
         * @param y Y coordinate
         * @return Whether the item contains the given coordinates
         */
        public boolean contains(
                final float x,
                final float y
        ) {
            return this.contains((int) x, (int) y);
        }

        /**
         * Returns whether the item contains the given coordinates
         *
         * @param x X coordinate
         * @param y Y coordinate
         * @return Whether the item contains the given coordinates
         */
        public boolean contains(
                final int x,
                final int y
        ) {
            return this.rectangle.contains(x, y);
        }

        /**
         * Returns a string representation of the item
         *
         * @return A string representation of the item
         */
        @Override
        public @NonNull String toString() {
            return "BottomNavBar.Item{" +
                    "index=" + BottomNavBar.this.itemList.indexOf(this) +
                    ", rectangle=" + this.rectangle +
                    ", icon=" + this.icon +
                    '}';
        }
    }

    private static class SavedState extends AbsSavedState {
        private @MenuRes int menuRes;
        private @Dimension float indicatorPos;
        private int activeItemIndex;
        private int previousItemIndex;

        public SavedState(final @NonNull Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(
                final @NonNull Parcel out,
                final int flags
        ) {
            super.writeToParcel(out, flags);

            out.writeInt(this.menuRes);
            out.writeFloat(this.indicatorPos);
            out.writeInt(this.activeItemIndex);
            out.writeInt(this.previousItemIndex);
        }
    }
}
