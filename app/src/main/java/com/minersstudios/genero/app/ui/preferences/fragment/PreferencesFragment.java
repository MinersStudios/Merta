package com.minersstudios.genero.app.ui.preferences.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.minersstudios.genero.app.R;

public class PreferencesFragment extends AbstractPreferencesFragment {

    @Override
    public @NonNull PreferenceFragmentCompat getPreferenceFragment() {
        this.setTitle(R.string.title_preferences);

        return new Preferences();
    }

    public static final class Preferences extends PreferenceFragmentCompat {
        private String keyAbout;

        @Override
        public void onCreatePreferences(
                final @Nullable Bundle savedInstanceState,
                final @Nullable String rootKey
        ) {
            this.setPreferencesFromResource(R.xml.preferences, rootKey);

            this.keyAbout = this.getString(R.string.key_about);
        }

        @Override
        public boolean onPreferenceTreeClick(final @NonNull Preference preference) {
            final String key = preference.getKey();
            final Fragment fragment;

            if (key.equals(this.keyAbout)) {
                fragment = new PreferencesAboutFragment();
            } else {
                fragment = null;
            }

            if (fragment != null) {
                this.requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(String.valueOf(System.currentTimeMillis()))
                .replace(R.id.main_container, new PreferencesAboutFragment())
                .commit();
            }

            return super.onPreferenceTreeClick(preference);
        }
    }
}
