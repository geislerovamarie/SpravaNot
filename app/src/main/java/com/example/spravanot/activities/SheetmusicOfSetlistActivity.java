package com.example.spravanot.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.spravanot.R;
import com.example.spravanot.adapters.SheetmusicAdapter;
import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.models.Setlist;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.utils.DatabaseHelper;

import java.util.ArrayList;

public class SheetmusicOfSetlistActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLaunch;
    PassInfoSheetmusic info;
    SheetmusicAdapter sheetmusicAdapter;
    RecyclerView recView;
    ImageButton add_sheetmusic_button, filter_button;

    DatabaseHelper db;
    Setlist setlist;
    ArrayList<Sheetmusic> sheetmusicsOfSetlist;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sheets);

        setUpLauncher();
        setUpInfo();
        initElements();

        setOnClickListeners();
    }

    void initElements(){
        db = new DatabaseHelper(this);
        setlist = (Setlist) getIntent().getSerializableExtra("setlist");

        recView = findViewById(R.id.recyclerViewSheets);
        add_sheetmusic_button = findViewById(R.id.buttonSheetsAdd);
        filter_button = findViewById(R.id.buttonSheetsFilter);

        recView.setLayoutManager(new LinearLayoutManager(this));
        sheetmusicAdapter = prepareAdapter();
        recView.setAdapter(sheetmusicAdapter);
    }

    void setOnClickListeners(){
        add_sheetmusic_button.setOnClickListener(view12 -> {
            Intent intent = new Intent(this, AddSheetsToSetlistActivity.class);
            intent.putExtra("setlist",setlist);
            activityResultLaunch.launch(intent);    // code 16
        });
        filter_button.setOnClickListener(view1 -> {
            Toast.makeText(this, "Filterrr", Toast.LENGTH_SHORT).show();
            // todo filter
        });
    }

    SheetmusicAdapter prepareAdapter(){
        db = new DatabaseHelper(this);
        sheetmusicsOfSetlist = db.selectSheetmusicForSetlist(setlist.getId());
        setlist.setSheetmusic(sheetmusicsOfSetlist);

        // TODO modify for various sort and filter
        //sortSheetsArrayAlphabetically();

        //get favorite
        ArrayList<Boolean> isFavorite = new ArrayList<>();
        int favSetlistID = db.getIdOfFavorite();
        for (int i = 0; i < sheetmusicsOfSetlist.size(); i++) {
            Boolean f = db.isInSetlist(sheetmusicsOfSetlist.get(i).getId(), favSetlistID);
            isFavorite.add(f);
        }
        boolean isThisSetlistFavorite = (setlist.getId()==favSetlistID);
        sheetmusicAdapter = new SheetmusicAdapter(this, this, sheetmusicsOfSetlist, info, isFavorite, isThisSetlistFavorite);

        return sheetmusicAdapter;
    }

    public void sortSheetsArrayAlphabetically(){
        sheetmusicsOfSetlist.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
    }

    void setUpLauncher() {
        // Launcher to help when an activity finishes
        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 9) {  // added
                sheetmusicAdapter = prepareAdapter();
                recView.setAdapter(sheetmusicAdapter);
                sheetmusicAdapter.notifyDataSetChanged();
            } else if (result.getResultCode() == 2) {   // updated
                sheetmusicAdapter = prepareAdapter();
                recView.setAdapter(sheetmusicAdapter);
                sheetmusicAdapter.notifyDataSetChanged();
            } else if (result.getResultCode() == 16){   // update sheetmusic in setlist via addsheetstosetlistactivity
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
                db.deleteOneSheetmusicFromSetlist(idSh, setlist.getId());
                sheetmusicsOfSetlist.remove(position);
                sheetmusicAdapter.notifyItemRemoved(position);
            }

            @Override
            public void updateSheetmusic(int position) {
                Intent intent = new Intent(getApplicationContext(), EditSheetmusicActivity.class);
                intent.putExtra("sheetmusic", sheetmusicsOfSetlist.get(position));
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
            public void addSheetmusicToSetlist(Sheetmusic s, boolean add) {}

        };
    }
}