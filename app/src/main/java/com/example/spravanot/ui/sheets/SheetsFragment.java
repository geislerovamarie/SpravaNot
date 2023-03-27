package com.example.spravanot.ui.sheets;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.AddSheetmusic;
import com.example.spravanot.DatabaseHelper;
import com.example.spravanot.PassInfoSheetmusic;
import com.example.spravanot.R;
import com.example.spravanot.Setlist;
import com.example.spravanot.Sheetmusic;
import com.example.spravanot.SheetmusicAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


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

        // PassInfo - connection between this fragment and the adapter
        info = new PassInfoSheetmusic() {
            @Override
            public void deleteSheetmusic(int position, int idSh) {
                db.deleteOneSheetmusic(idSh);
                sheetmusics.remove(position);
                sheetmusicAdapter.notifyItemRemoved(position);
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
                    // change color
                }
                else {
                    newFaveSheets.add(sh);
                    db.addToSetlist(idFav, newFaveSheets);
                    // change color
                }
                sheetmusicAdapter.notifyItemChanged(position);
            }
        };


        // Launcher to help when an activity finishes
        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 3) {
                sheetmusicAdapter = prepareAdapter();
                recView.setAdapter(sheetmusicAdapter);
                sheetmusicAdapter.notifyDataSetChanged();
            }
        });
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
        add_sheetmusic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddSheetmusic.class);
                activityResultLaunch.launch(intent);
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
        db = new DatabaseHelper(getContext());
        sheetmusics = db.selectAllSheetmusic();

        // TODO modify for various sort and filter
        sortSheetsArrayAlphabetically();

        sheetmusicAdapter = new SheetmusicAdapter(getActivity(), getContext(), sheetmusics, info);
        return sheetmusicAdapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void sortSheetsArrayAlphabetically(){
        Collections.sort(sheetmusics, new Comparator<Sheetmusic>(){
            public int compare(Sheetmusic s1, Sheetmusic s2){
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });
    }
}