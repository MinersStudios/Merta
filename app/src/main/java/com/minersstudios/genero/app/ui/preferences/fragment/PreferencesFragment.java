package com.minersstudios.genero.app.ui.preferences.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import com.minersstudios.genero.app.R;

public class PreferencesFragment extends AbstractPreferencesFragment {

    public PreferencesFragment() {
        super(R.string.title_preferences);
    }

    @Override
    public @NonNull PreferencesContainer initContainer() {
        return new Container();
    }

    public static final class Container extends PreferencesContainer {
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

            if (key == null) {
                return super.onPreferenceTreeClick(preference);
            }

            final Fragment fragment;

            if (this.keyAbout.equals(key)) {
                fragment = new PreferencesAboutFragment();
            } else {
                fragment = null;
            }

            if (fragment != null) {
                this.performTransaction(
                        R.id.main_container,
                        fragment
                );
            }

            return super.onPreferenceTreeClick(preference);
        }
    }
}
