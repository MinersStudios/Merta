package com.minersstudios.genero.app.ui.navigation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.minersstudios.genero.app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public @NonNull View onCreateView(
            final @NonNull LayoutInflater inflater,
            final @Nullable ViewGroup container,
            final @Nullable Bundle savedInstanceState
    ) {
        this.binding = FragmentHomeBinding.inflate(inflater, container, false);

        if (this.viewModel == null) {
            this.viewModel =
                    new ViewModelProvider(this)
                    .get(HomeViewModel.class);
        }

        return this.binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        this.binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.viewModel = null;
    }
}
