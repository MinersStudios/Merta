package com.minersstudios.genero.app.ui.preferences.fragment;

import static com.minersstudios.genero.app.BuildConfig.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.minersstudios.genero.app.R;
import com.minersstudios.genero.app.ui.fragment.BottomSheetLicenseFragment;
import com.minersstudios.genero.lib.util.EdgeToEdgeUtil;

public class PreferencesAboutFragment extends AbstractPreferencesFragment {

    public PreferencesAboutFragment() {
        super(R.string.title_about);
    }

    @Override
    public @NonNull PreferencesContainer initContainer() {
        return new Container();
    }

    public static final class Container extends PreferencesContainer {
        private String keyLicense;

        @Override
        public void onCreatePreferences(
                final @Nullable Bundle savedInstanceState,
                final @Nullable String rootKey
        ) {
            this.setPreferencesFromResource(R.xml.preferences_about, rootKey);

            this.keyLicense = this.getString(R.string.key_app_license);

            this.runOnFind(
                    R.string.key_app_version,
                    preference -> preference.setSummary(VERSION_NAME + " (" + VERSION_CODE + ")")
            );
            this.runOnFind(
                    R.string.key_app_build_type,
                    preference -> preference.setSummary(BUILD_TYPE)
            );
        }

        @Override
        public boolean onPreferenceTreeClick(final @NonNull Preference preference) {
            if (this.keyLicense.equals(preference.getKey())) {
                new BottomSheetLicenseFragment().show(
                        this.getParentFragmentManager(),
                        "LicenseDialogFragment"
                );
            }

            return super.onPreferenceTreeClick(preference);
        }
    }
}
