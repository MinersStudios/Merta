package com.minersstudios.genero.app.ui.preferences;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.minersstudios.genero.app.R;
import com.minersstudios.genero.app.ui.preferences.fragment.PreferencesFragment;

public final class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new PreferencesFragment())
                .commit();
        }

        this.setContentView(R.layout.activity_preferences);
    }
}
