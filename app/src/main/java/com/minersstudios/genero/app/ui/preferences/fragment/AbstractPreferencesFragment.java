package com.minersstudios.genero.app.ui.preferences.fragment;

import static com.google.android.material.transition.MaterialSharedAxis.Z;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.transition.MaterialSharedAxis;
import com.minersstudios.genero.app.R;
import com.minersstudios.genero.app.databinding.FragmentPreferencesBinding;

import java.util.function.Consumer;

abstract class AbstractPreferencesFragment extends Fragment {
    private @StringRes int mTitle;
    private FragmentPreferencesBinding mBinding;

    /**
     * Initializes the preference fragment
     *
     * @param title The title of the toolbar
     */
    public AbstractPreferencesFragment(final @StringRes int title) {
        super();

        this.mTitle = title;
    }

    /**
     * Initializes the preferences container fragment to display below the
     * toolbar
     *
     * @return The preferences container fragment
     */
    public abstract @NonNull PreferencesContainer initContainer();

    /**
     * Returns the title string resource of the toolbar
     *
     * @return The title string resource of the toolbar
     */
    public @StringRes int getTitle() {
        return this.mTitle;
    }

    /**
     * Sets the title of the toolbar
     *
     * @param title The title string resource to set
     */
    public void setTitle(final @StringRes int title) {
        this.mTitle = title;

        if (this.mBinding != null) {
            this.mBinding.toolbar.setTitle(title);
        } else {
            Log.w(
                    this.getClass().getSimpleName(),
                    "Attempted to set title before preferences view was created"
            );
        }
    }

    /**
     * Performs a fragment transaction
     *
     * @param containerViewId The container view ID to replace
     * @param fragment        The fragment to replace the container view with
     */
    public void performTransaction(
            final @IdRes int containerViewId,
            final @NonNull Fragment fragment
    ) {
        this.getChildFragmentManager()
            .beginTransaction()
            .addToBackStack(String.valueOf(System.currentTimeMillis()))
            .replace(containerViewId, fragment)
            .commit();
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
        this.mBinding = FragmentPreferencesBinding.inflate(inflater, container, false);

        this.mBinding.toolbar.setNavigationOnClickListener(view -> {
            view.performHapticFeedback(0);
            view.playSoundEffect(SoundEffectConstants.CLICK);
            this.requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
        this.setTitle(this.getTitle());
        this.performTransaction(
                R.id.preferences_container,
                this.initContainer()
        );

        return this.mBinding.getRoot();
    }

    public static abstract class PreferencesContainer extends PreferenceFragmentCompat {

        /**
         * Performs a fragment transaction
         *
         * @param containerViewId The container view ID to replace
         * @param fragment        The fragment to replace the container view
         *                        with
         */
        public void performTransaction(
                final @IdRes int containerViewId,
                final @NonNull Fragment fragment
        ) {
            final FragmentActivity activity = this.getActivity();

            if (activity != null) {
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(String.valueOf(System.currentTimeMillis()))
                        .replace(containerViewId, fragment)
                        .commit();
            } else {
                Log.w(
                        this.getClass().getSimpleName(),
                        "Cannot perform transaction because activity is null"
                );
            }
        }

        /**
         * Runs a consumer on a preference if it is found
         *
         * @param key      The key of the preference to find
         * @param consumer The consumer to run on the preference
         */
        public void runOnFind(
                final @StringRes int key,
                final @NonNull Consumer<Preference> consumer
        ) {
            final Preference preference = this.findPreference(this.getString(key));

            if (preference != null) {
                consumer.accept(preference);
            }
        }
    }
}
