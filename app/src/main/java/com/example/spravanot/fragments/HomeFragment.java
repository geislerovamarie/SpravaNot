package com.example.spravanot.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.R;
import com.example.spravanot.activities.EditSheetmusicActivity;
import com.example.spravanot.adapters.SheetmusicAdapter;
import com.example.spravanot.databinding.FragmentHomeBinding;
import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.models.Setlist;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.utils.DatabaseHelper;
import com.example.spravanot.utils.FilterOptions;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    DatabaseHelper db;
    ActivityResultLauncher<Intent> activityResultLaunch;
    PassInfoSheetmusic info;
    SheetmusicAdapter sheetmusicAdapter;
    RecyclerView recView;

    int idF;
    ArrayList<Sheetmusic> favorite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(getContext());
        setUpLauncher();
        setUpInfo();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recView = view.findViewById(R.id.recyclerViewSheets);
        recView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        sheetmusicAdapter = prepareAdapter();
        recView.setAdapter(sheetmusicAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    SheetmusicAdapter prepareAdapter(){
        db = new DatabaseHelper(getContext());
        idF = db.getIdOfFavorite();

        favorite = db.selectSheetmusicForSetlist(idF);

        // SORT
        //sortSheetsArrayAlphabetically();

        //get favorite
        ArrayList<Boolean> isFavorite = new ArrayList<>();
        for (int i = 0; i < favorite.size(); i++) { isFavorite.add(true); }

        boolean isThisSetlistFavorite = true;
        sheetmusicAdapter = new SheetmusicAdapter(getActivity(), getContext(), favorite, info, isFavorite, isThisSetlistFavorite, FilterOptions.NAME);

        return sheetmusicAdapter;
    }

    void setUpLauncher() {
        // Launcher to help when an activity finishes
        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 2) { // edit
                sheetmusicAdapter = prepareAdapter();
                recView.setAdapter(sheetmusicAdapter);
                sheetmusicAdapter.notifyDataSetChanged();
            }
        });
    }


    void setUpInfo(){
        // PassInfo - connection between this fragment and the adapter
        info = new PassInfoSheetmusic() {
            @Override
            public void deleteSheetmusic(int position, int idSh) {
                db.deleteOneSheetmusicFromSetlist(idSh, idF);
                favorite.remove(position);
                sheetmusicAdapter = prepareAdapter();
                recView.setAdapter(sheetmusicAdapter);
                sheetmusicAdapter.notifyDataSetChanged();
            }

            @Override
            public void updateSheetmusic(int position) {
                Intent intent = new Intent(getContext(), EditSheetmusicActivity.class);
                intent.putExtra("sheetmusic", favorite.get(position));
                activityResultLaunch.launch(intent);
            }

            @Override
            public void toggleFavorite(int position, int idSh) { }

            @Override
            public void deleteTag(int position, String name) {}

            @Override
            public void addSheetmusicToSetlist(Sheetmusic s, boolean add) {}

        };
    }
}