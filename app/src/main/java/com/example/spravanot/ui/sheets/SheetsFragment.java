package com.example.spravanot.ui.sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spravanot.databinding.FragmentSheetsBinding;

public class SheetsFragment extends Fragment {

    private FragmentSheetsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SheetsViewModel sheetsViewModel =
                new ViewModelProvider(this).get(SheetsViewModel.class);

        binding = FragmentSheetsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSheets;
        sheetsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}