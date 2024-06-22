package com.minersstudios.genero.app.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.minersstudios.genero.app.R;
import com.minersstudios.genero.app.databinding.ActivityMainBinding;
import com.minersstudios.genero.lib.ui.button.ActionButton;
import com.minersstudios.genero.lib.ui.button.ButtonParamHolder;
import com.minersstudios.genero.lib.ui.navigation.BottomNavBar;
import com.minersstudios.genero.lib.ui.navigation.ItemSelectedListener;

public final class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(this.navController, this.appBarConfiguration);
    }

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        this.binding = ActivityMainBinding.inflate(this.getLayoutInflater());

        this.setupBottomBarInsets();
        this.setupNavigation();
        this.setupActionButton();
        this.setContentView(this.binding.getRoot());
    }

    private void setupBottomBarInsets() {
        final Resources resources = this.getResources();
        final int smallPadding = resources.getDimensionPixelSize(R.dimen.nav_bar_bottom_padding_small);
        final int largePadding = resources.getDimensionPixelSize(R.dimen.nav_bar_bottom_padding_large);

        ViewCompat.setOnApplyWindowInsetsListener(this.binding.bottomAppBarContainer, (view, windowInsets) -> {
            final Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

            layoutParams.leftMargin = insets.left;
            layoutParams.bottomMargin = insets.bottom;
            layoutParams.rightMargin = insets.right;

            view.setLayoutParams(layoutParams);
            view.setPadding(
                    view.getPaddingLeft(),
                    view.getPaddingTop(),
                    view.getPaddingRight(),
                    insets.bottom > 0
                    ? smallPadding
                    : largePadding
            );

            return WindowInsetsCompat.CONSUMED;
        });
    }

    private void setupNavigation() {
        this.navController = ((NavHostFragment) this.binding.mainContainer.getFragment()).getNavController();
        this.appBarConfiguration = new AppBarConfiguration.Builder(this.navController.getGraph()).build();

        this.binding.bottomNavBar.setupWithNavController(this.navController);
    }

    private void setupActionButton() {
        final BottomNavBar navBar = this.binding.bottomNavBar;
        final ActionButton button = this.binding.bottomButton;
        final ItemSelectedListener listener = index -> {
            final BottomNavBar.Item item = navBar.getItems().get(index);

            for (final Object obj : item.getArgs()) {
                if (obj instanceof ButtonParamHolder) {
                    final ButtonParamHolder holder = (ButtonParamHolder) obj;

                    button.setIconWithAnim(
                            holder.iconResIdOr(button.getIconResId()),
                            holder.iconSizeOr(button.getIconSize()),
                            holder.iconTintOr(button.getIconTint())
                    );
                    button.setButtonHandler(holder.handler());

                    break;
                }
            }

            final Menu menu = navBar.getMenu();

            if (menu != null) {
                NavigationUI.onNavDestinationSelected(
                        menu.getItem(index),
                        this.navController
                );
            }
        };

        navBar.setItemSelectedListener(listener);
        listener.onItemSelect(navBar.getActiveItemIndex());
    }
}
