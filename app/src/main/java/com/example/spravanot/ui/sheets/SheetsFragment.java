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


// The relevant text view was deleted, so... now, this is completely useless
public class SheetsFragment extends Fragment {

    private FragmentSheetsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SheetsViewModel sheetsViewModel =
                new ViewModelProvider(this).get(SheetsViewModel.class);

        binding = FragmentSheetsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}