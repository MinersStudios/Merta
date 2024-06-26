package com.minersstudios.genero.app.ui.preferences.fragment;

import static com.minersstudios.genero.app.BuildConfig.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.minersstudios.genero.app.R;

public class PreferencesAboutFragment extends AbstractPreferencesFragment {

    public PreferencesAboutFragment() {
        super(R.string.title_about);
    }

    @Override
    public @NonNull PreferencesContainer initContainer() {
        return new Container();
    }

    public static final class Container extends PreferencesContainer {

        @Override
        public void onCreatePreferences(
                final @Nullable Bundle savedInstanceState,
                final @Nullable String rootKey
        ) {
            this.setPreferencesFromResource(R.xml.preferences_about, rootKey);

            this.runOnFind(
                    R.string.key_app_version,
                    preference -> preference.setSummary(VERSION_NAME + " (" + VERSION_CODE + ")")
            );
            this.runOnFind(
                    R.string.key_app_build_type,
                    preference -> preference.setSummary(BUILD_TYPE)
            );
        }
    }
}
