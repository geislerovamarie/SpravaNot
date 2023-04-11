package com.example.spravanot.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.R;
import com.example.spravanot.adapters.AddSheetmusicToSetlistAdapter;
import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.models.Setlist;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.utils.DatabaseHelper;
import com.example.spravanot.utils.FilterOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddSheetsToSetlistActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLaunch;
    PassInfoSheetmusic info;
    AddSheetmusicToSetlistAdapter sheetmusicAdapter;
    RecyclerView recView;
    ImageButton add_sheetmusic_button, filter_button;
    FloatingActionButton sheetmusic_save_button;

    DatabaseHelper db;
    Setlist setlist;
    ArrayList<Sheetmusic> sheetmusicsOfSetlist;
    ArrayList<Sheetmusic> result;
    ArrayList<Sheetmusic> allSheetmusic;
    FilterOptions filterOption;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sheets_to_setlist);

        setUpLauncher();
        setUpInfo();
        initElements();
        setOnClickListeners();
    }

    void initElements(){
        setTitle(getResources().getString(R.string.title_add_sheetmusic));

        db = new DatabaseHelper(this);
        setlist = (Setlist) getIntent().getSerializableExtra("setlist");
        result = setlist.getSheetmusic();
        filterOption = FilterOptions.NAME;

        recView = findViewById(R.id.recyclerViewSheets);
        add_sheetmusic_button = findViewById(R.id.buttonSheetsAdd);
        filter_button = findViewById(R.id.home_icon);
        sheetmusic_save_button = findViewById(R.id.save_sheets_button);
        search = findViewById(R.id.searchViewSheets);
        search.setImeOptions(EditorInfo.IME_ACTION_DONE);

        recView.setLayoutManager(new LinearLayoutManager(this));
        sheetmusicAdapter = prepareAdapter();
        recView.setAdapter(sheetmusicAdapter);
    }

    void setOnClickListeners(){
        add_sheetmusic_button.setOnClickListener(view12 -> {
            Intent intent = new Intent(this, AddSheetmusicActivity.class);
            activityResultLaunch.launch(intent);    // code 3
        });
        filter_button.setOnClickListener(view1 -> {
            ArrayList<String> options = new ArrayList<>();
            options.add(getString(R.string.text_name));
            options.add(getString(R.string.text_tags));
            options.add(getString(R.string.text_author));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.filter)
                    .setItems(options.toArray(new String[0]), (dialogInterface, i) -> {
                        filterOption = FilterOptions.values()[i];
                        sheetmusicAdapter = prepareAdapter();
                        recView.setAdapter(sheetmusicAdapter);
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        sheetmusic_save_button.setOnClickListener(view -> {
            setlist.setSheetmusic(result);
            db.updateSetlist(setlist);

            //end
            Intent intent = new Intent();
            setResult(16, intent);
            finish();
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                sheetmusicAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    AddSheetmusicToSetlistAdapter prepareAdapter(){
        db = new DatabaseHelper(this);
        sheetmusicsOfSetlist = db.selectSheetmusicForSetlist(setlist.getId());
        allSheetmusic = db.selectAllSheetmusic();

        // SORT
        sortSheetsArrayAlphabetically();

        //get favorite
        ArrayList<Boolean> isFavorite = new ArrayList<>();
        int favSetlistID = db.getIdOfFavorite();
        for (int i = 0; i < allSheetmusic.size(); i++) {
            Boolean f = db.isInSetlist(allSheetmusic.get(i).getId(), favSetlistID);
            isFavorite.add(f);
        }
        boolean isThisSetlistFavorite = (setlist.getId()==favSetlistID);
        sheetmusicAdapter = new AddSheetmusicToSetlistAdapter(this, this, allSheetmusic, info, isFavorite, isThisSetlistFavorite, sheetmusicsOfSetlist, filterOption);

        return sheetmusicAdapter;
    }

    public void sortSheetsArrayAlphabetically(){
        sheetmusicsOfSetlist.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        allSheetmusic.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
    }

    void setUpLauncher() {
        // Launcher to help when an activity finishes
        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 3) {  // added
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
            public void addSheetmusicToSetlist(Sheetmusic s, boolean add) {
                if(add){
                    result.add(s);
                }else{
                    result.remove(s);
                    result = removeItemFromSetlist(result, s);
                }
            }
        };
    }

    public ArrayList<Sheetmusic> removeItemFromSetlist(ArrayList<Sheetmusic> arr, Sheetmusic item){
        int position = -1;
        for (int i = 0; i < arr.size(); i++) {
            if(arr.get(i).getId() == item.getId()){
                position = i;
                break;
            }
        }
        if(position != -1) arr.remove(position);
        return arr;
    }
}