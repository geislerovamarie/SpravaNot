package com.example.spravanot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spravanot.R;

public class SetlistsFragment extends Fragment {

   // private FragmentSetlistsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sheets, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}