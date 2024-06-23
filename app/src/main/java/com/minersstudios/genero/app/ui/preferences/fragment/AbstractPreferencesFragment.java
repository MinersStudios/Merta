package com.minersstudios.genero.app.ui.preferences.fragment;

import static com.google.android.material.transition.MaterialSharedAxis.Z;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.transition.MaterialSharedAxis;
import com.minersstudios.genero.app.R;
import com.minersstudios.genero.app.databinding.FragmentPreferencesBinding;

public abstract class AbstractPreferencesFragment extends Fragment {
    private FragmentPreferencesBinding binding;

    /**
     * Returns the preference fragment to display below the toolbar
     *
     * @return The preference fragment to display below the toolbar
     */
    public abstract @NonNull PreferenceFragmentCompat getPreferenceFragment();

    /**
     * Returns the title of the toolbar
     *
     * @return the title of the toolbar
     */
    public @NonNull CharSequence getTitle() {
        return this.binding.toolbar.getTitle();
    }

    /**
     * Sets the title of the toolbar
     *
     * @param title The title string resource to set
     */
    public void setTitle(final @StringRes int title) {
        this.binding.toolbar.setTitle(title);
    }

    /**
     * Sets the title of the toolbar
     *
     * @param title The title string to set
     */
    public void setTitle(final @NonNull CharSequence title) {
        this.binding.toolbar.setTitle(title);
    }

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setEnterTransition(  new MaterialSharedAxis(Z, true));
        this.setReturnTransition( new MaterialSharedAxis(Z, false));
        this.setExitTransition(   new MaterialSharedAxis(Z, true));
        this.setReenterTransition(new MaterialSharedAxis(Z, false));
    }

    @Override
    public @NonNull View onCreateView(
            final @NonNull LayoutInflater inflater,
            final @Nullable ViewGroup container,
            final @Nullable Bundle savedInstanceState
    ) {
        this.binding = FragmentPreferencesBinding.inflate(inflater, container, false);

        this.setupToolbar();
        this.getChildFragmentManager()
            .beginTransaction()
            .addToBackStack(String.valueOf(System.currentTimeMillis()))
            .replace(R.id.preferences_container, this.getPreferenceFragment())
            .commit();

        return this.binding.getRoot();
    }

    private void setupToolbar() {
        final Toolbar toolbar = this.binding.toolbar;

        toolbar.setNavigationOnClickListener(v -> {
            toolbar.performHapticFeedback(0);
            toolbar.playSoundEffect(SoundEffectConstants.CLICK);
            this.requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }
}
