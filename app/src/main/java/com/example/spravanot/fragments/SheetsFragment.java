package com.example.spravanot.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.R;
import com.example.spravanot.activities.AddSheetmusicActivity;
import com.example.spravanot.activities.EditSheetmusicActivity;
import com.example.spravanot.adapters.SheetmusicAdapter;
import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.models.Setlist;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.utils.DatabaseHelper;

import java.util.ArrayList;


public class SheetsFragment extends Fragment {

    ActivityResultLauncher<Intent> activityResultLaunch;
    PassInfoSheetmusic info;
    SheetmusicAdapter sheetmusicAdapter;
    RecyclerView recView;
    ImageButton add_sheetmusic_button, filter_button;

    DatabaseHelper db;
    ArrayList<Sheetmusic> sheetmusics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(getContext());
        setUpLauncher();
        setUpInfo();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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

        add_sheetmusic_button = getView().findViewById(R.id.buttonSheetsAdd);
        add_sheetmusic_button.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), AddSheetmusicActivity.class);
            activityResultLaunch.launch(intent);
        });

        filter_button = getView().findViewById(R.id.buttonSheetsFilter);
        filter_button.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Filterrr", Toast.LENGTH_SHORT).show();
            // todo filter
        });
    }


    SheetmusicAdapter prepareAdapter(){
        db = new DatabaseHelper(getContext());
        sheetmusics = db.selectAllSheetmusic();

        // TODO modify for various sort and filter
        sortSheetsArrayAlphabetically();

        //get favorite
        ArrayList<Boolean> isFavorite = new ArrayList<>();
        int favSetlistID = db.getIdOfFavorite();
        for (int i = 0; i < sheetmusics.size(); i++) {
            Boolean f = db.isInSetlist(sheetmusics.get(i).getId(), favSetlistID);
            isFavorite.add(f);
        }

        sheetmusicAdapter = new SheetmusicAdapter(getActivity(), getContext(), sheetmusics, info, isFavorite, false);
        return sheetmusicAdapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void sortSheetsArrayAlphabetically(){
        sheetmusics.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
    }

    void setUpLauncher() {
        // Launcher to help when an activity finishes
        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 3) {
                sheetmusicAdapter = prepareAdapter();
                recView.setAdapter(sheetmusicAdapter);
                sheetmusicAdapter.notifyDataSetChanged();
            } else if (result.getResultCode() == 2) {
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
                db.deleteOneSheetmusic(idSh);
                sheetmusics.remove(position);
                sheetmusicAdapter.notifyItemRemoved(position);
            }

            @Override
            public void updateSheetmusic(int position) {
                Intent intent = new Intent(getContext(), EditSheetmusicActivity.class);
                intent.putExtra("sheetmusic", sheetmusics.get(position));
                activityResultLaunch.launch(intent);
            }

            @Override
            public void toggleFavorite(int position, int idSh) {
                // create setlist "Favorite" if it doesn't exist
                if(!db.doesFavoriteExist()){
                    Setlist f = new Setlist(-1);
                    f.setName("Favorite");
                    f.setNotes("Favorite / Oblíbené");
                    db.addSetlist(f);
                }
                // toggle
                Sheetmusic sh = db.selectOneSheetmusic(idSh);
                ArrayList<Sheetmusic> newFaveSheets = new ArrayList<>();

                int idFav = db.getIdOfFavorite();
                if(db.isInSetlist(sh.getId(), idFav)){
                    db.deleteOneSheetmusicFromSetlist(sh.getId(), idFav);
                }
                else {
                    newFaveSheets.add(sh);
                    db.addToSetlist(idFav, newFaveSheets);
                }
                sheetmusicAdapter.notifyItemChanged(position);
            }

            @Override
            public void deleteTag(int position, String name) {}

            @Override
            public void addSheetmusicToSetlist(Sheetmusic s, boolean add) { }
        };
    }
}