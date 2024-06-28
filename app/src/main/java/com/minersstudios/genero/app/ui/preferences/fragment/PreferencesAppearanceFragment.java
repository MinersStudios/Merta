package com.minersstudios.genero.app.ui.preferences.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.minersstudios.genero.app.R;

public class PreferencesAppearanceFragment extends AbstractPreferencesFragment {

    public PreferencesAppearanceFragment() {
        super(R.string.title_appearance);
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
            this.setPreferencesFromResource(R.xml.preferences_appearance, rootKey);
        }
    }
}
