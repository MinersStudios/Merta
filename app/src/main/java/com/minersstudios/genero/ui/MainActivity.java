package com.minersstudios.genero.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.minersstudios.genero.R;
import com.minersstudios.genero.databinding.ActivityMainBinding;
import com.minersstudios.genero.lib.ui.button.ButtonParamHolder;
import com.minersstudios.genero.lib.ui.navigation.BottomNavBar;
import com.minersstudios.genero.lib.ui.navigation.ItemSelectedListener;

public final class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityMainBinding.inflate(this.getLayoutInflater());

        this.setContentView(this.binding.getRoot());
        this.setupBottomBar();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(this.navController, this.appBarConfiguration);
    }

    public void normalizeBottomBarPadding() {
        final ConstraintLayout container = this.binding.bottomAppBarContainer;
        final Resources resources = this.getResources();

        this.binding.bottomAppBarContainer.setPadding(
                container.getPaddingLeft(),
                container.getPaddingTop(),
                container.getPaddingRight(),
                this.isNavBarVisible()
                ? resources.getDimensionPixelSize(R.dimen.nav_bar_bottom_padding_small)
                : resources.getDimensionPixelSize(R.dimen.nav_bar_bottom_padding_large)
        );
    }

    private void setupBottomBar() {
        this.navController = ((NavHostFragment) this.binding.mainContainer.getFragment()).getNavController();
        this.appBarConfiguration = new AppBarConfiguration.Builder(this.navController.getGraph()).build();

        this.normalizeBottomBarPadding();
        this.binding.bottomNavBar.setupWithNavController(this.navController);

        final ItemSelectedListener listener = index -> {
            final BottomNavBar.Item item = this.binding.bottomNavBar.getItems().get(index);

            for (final Object obj : item.getArgs()) {
                if (obj instanceof ButtonParamHolder) {
                    final ButtonParamHolder holder = (ButtonParamHolder) obj;

                    this.binding.bottomButton.setIconWithAnim(
                            holder.iconResIdOr(this.binding.bottomButton.getIconResId()),
                            holder.iconSizeOr(this.binding.bottomButton.getIconSize()),
                            holder.iconTintOr(this.binding.bottomButton.getIconTint())
                    );
                    this.binding.bottomButton.setButtonHandler(holder.handler());

                    break;
                }
            }

            final Menu menu = this.binding.bottomNavBar.getMenu();

            if (menu != null) {
                NavigationUI.onNavDestinationSelected(
                        menu.getItem(index),
                        this.navController
                );
            }
        };

        this.binding.bottomNavBar.setItemSelectedListener(listener);
        listener.onItemSelect(this.binding.bottomNavBar.getActiveItemIndex());
    }

    private int getNavBarHeight() {
        final Resources resources = this.getResources();
        @SuppressWarnings({"InternalInsetResource", "DiscouragedApi"})
        final int resourceId = resources.getIdentifier(
                "navigation_bar_height",
                "dimen",
                "android"
        );

        if (resourceId > 0) {
            try {
                return resources.getDimensionPixelSize(resourceId);
            } catch (final Resources.NotFoundException e) {
                Log.e(
                        this.getClass().getSimpleName(),
                        "Failed to get navigation bar height, defaulting to 0",
                        e
                );
            }
        }

        return 0;
    }

    private boolean isNavBarVisible() {
        return this.getNavBarHeight() > 0;
    }
}
