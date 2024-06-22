package com.minersstudios.genero.app.ui.preferences;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.minersstudios.genero.app.R;

public class PreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(
            final @Nullable Bundle savedInstanceState,
            final @Nullable String rootKey
    ) {
        this.setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
