package com.minersstudios.genero.app.ui.preferences.fragment;

import static com.minersstudios.genero.app.BuildConfig.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.minersstudios.genero.app.R;

public class PreferencesAboutFragment extends AbstractPreferencesFragment {

    @Override
    public @NonNull PreferenceFragmentCompat getPreferenceFragment() {
        this.setTitle(R.string.title_about);

        return new Preferences();
    }

    public static final class Preferences extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(
                final @Nullable Bundle savedInstanceState,
                final @Nullable String rootKey
        ) {
            this.setPreferencesFromResource(R.xml.preferences_about, rootKey);

            final Preference version = this.findPreference(this.getString(R.string.key_app_version));
            final Preference buildType = this.findPreference(this.getString(R.string.key_app_build_type));

            if (version != null) {
                version.setSummary(VERSION_NAME + " (" + VERSION_CODE + ")");
            }

            if (buildType != null) {
                buildType.setSummary(BUILD_TYPE);
            }
        }
    }
}
