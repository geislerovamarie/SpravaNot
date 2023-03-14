package com.example.spravanot.ui.setlists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spravanot.databinding.FragmentSetlistsBinding;

public class SetlistsFragment extends Fragment {

    private FragmentSetlistsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SetlistsViewModel setlistsViewModel =
                new ViewModelProvider(this).get(SetlistsViewModel.class);

        binding = FragmentSetlistsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSetlists;
        setlistsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}