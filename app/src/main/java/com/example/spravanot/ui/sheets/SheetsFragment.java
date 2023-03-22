package com.example.spravanot.ui.sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.DatabaseHelper;
import com.example.spravanot.MainActivity;
import com.example.spravanot.R;
import com.example.spravanot.Sheetmusic;
import com.example.spravanot.SheetmusicAdapter;
import com.example.spravanot.databinding.FragmentSheetsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class SheetsFragment extends Fragment {

    //private FragmentSheetsBinding binding;

    DatabaseHelper database;

    //View view;
    SheetmusicAdapter sheetmusicAdapter;
    RecyclerView recView;

    FloatingActionButton add_sheetmusic_button;
    Button filter_button;
    ArrayList<Sheetmusic> sheetmusics;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SheetsViewModel sheetsViewModel = new ViewModelProvider(this).get(SheetsViewModel.class);
        //binding = FragmentSheetsBinding.inflate(inflater, container, false);
        //root = binding.getRoot();
        View view = inflater.inflate(R.layout.fragment_sheets, container, false);

        recView = view.findViewById(R.id.recyclerViewSheets);
        recView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        sheetmusicAdapter = prepareAdapter();
        recView.setAdapter(sheetmusicAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        add_sheetmusic_button = getView().findViewById(R.id.floatButtonSheetsAdd);
        add_sheetmusic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Adding", Toast.LENGTH_SHORT).show();
                // TODO start add sheets activity
            }
        });

        filter_button = getView().findViewById(R.id.buttonSheetsFilter);
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Filterrr", Toast.LENGTH_SHORT).show();
                // todo filter
            }
        });
    }

    SheetmusicAdapter prepareAdapter(){
        database = new DatabaseHelper(getContext());
        sheetmusics = database.selectAllSheetmusic();

        // prepare data for adapter
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();

        // HERE I SHOULD BE ABLE TO SORT
        for (int i = 0; i < sheetmusics.size(); i++) {
            String name = sheetmusics.get(i).getName();
            String author = sheetmusics.get(i).getAuthor();
            names.add(name);
            authors.add(author);
        }
        sheetmusicAdapter = new SheetmusicAdapter(getActivity(), getContext(), names, authors);
        return sheetmusicAdapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  binding = null;
    }
}