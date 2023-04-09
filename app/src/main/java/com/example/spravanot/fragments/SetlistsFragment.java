package com.example.spravanot.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spravanot.R;
import com.example.spravanot.activities.AddSetlistActivity;
import com.example.spravanot.activities.AddSheetmusicActivity;
import com.example.spravanot.activities.EditSetlistActivity;
import com.example.spravanot.activities.EditSheetmusicActivity;
import com.example.spravanot.adapters.SetlistAdapter;
import com.example.spravanot.adapters.SheetmusicAdapter;
import com.example.spravanot.interfaces.PassInfoSetlist;
import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.models.Setlist;
import com.example.spravanot.models.Sheetmusic;
import com.example.spravanot.utils.DatabaseHelper;
import com.example.spravanot.utils.FilterOptions;

import java.util.ArrayList;

public class SetlistsFragment extends Fragment {

    ActivityResultLauncher<Intent> activityResultLaunch;
    PassInfoSetlist info;
    SetlistAdapter setlistAdapter;
    RecyclerView recView;
    ImageButton add_setlist_button, filter_button;

    DatabaseHelper db;
    ArrayList<Setlist> setlists;

    SearchView search;
    FilterOptions filterOption;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterOption = FilterOptions.NAME;

        db = new DatabaseHelper(getContext());
        setUpInfo();
        setUpLauncher();

        // set up Favorite if doesnt exist
        if(!db.doesFavoriteExist()){
            Setlist f = new Setlist(-1);
            f.setName("Favorite");
            f.setNotes("Favorite / Oblíbené");
            db.addSetlist(f);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setlists, container, false);

        recView = view.findViewById(R.id.recyclerViewSetlist);
        recView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        setlistAdapter = prepareAdapter();
        recView.setAdapter(setlistAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        add_setlist_button = getView().findViewById(R.id.buttonSetlistAdd);
        add_setlist_button.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), AddSetlistActivity.class);
            activityResultLaunch.launch(intent);
        });

        search = view.findViewById(R.id.searchViewSetlist);
        search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false;  }

            @Override
            public boolean onQueryTextChange(String newText) {
                setlistAdapter.getFilter().filter(newText);
                return false;
            }
        });

        filter_button = getView().findViewById(R.id.buttonSetlistFilter);
        filter_button.setOnClickListener(view1 -> {
            ArrayList<String> options = new ArrayList<>();
            options.add(getString(R.string.text_name));
            options.add(getString(R.string.text_tags));

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.filter)
                    .setItems(options.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            filterOption = FilterOptions.values()[i];
                            setlistAdapter = prepareAdapter();
                            recView.setAdapter(setlistAdapter);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    SetlistAdapter prepareAdapter(){
        db = new DatabaseHelper(getContext());
        setlists = db.selectAllSetlists();

        // TODO modify for various sort and filter
        sortSetlistsArrayAlphabetically();

        setlistAdapter = new SetlistAdapter(getContext(), getActivity(), setlists, info, filterOption);
        return setlistAdapter;
    }

    public void sortSetlistsArrayAlphabetically(){
        setlists.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
    }

    void setUpInfo(){
        // PassInfo - connection between this fragment and the adapter
        info = new PassInfoSetlist() {

            @Override
            public void deleteFromSetlist(int position, int idSh, int idSe) {

            }

            @Override
            public void deleteSetlist(int position, int idSe) {
                db.deleteOneSetlist(idSe);
                setlists.remove(position);
                setlistAdapter = prepareAdapter();
                recView.setAdapter(setlistAdapter);
                setlistAdapter.notifyDataSetChanged();
            }

            @Override
            public void deleteTag(int position, String name) {

            }

            @Override
            public void updateSetlist(int position) {
                Intent intent = new Intent(getContext(), EditSetlistActivity.class);
                intent.putExtra("setlist", setlists.get(position));
                activityResultLaunch.launch(intent);
            }
        };
    }

    void setUpLauncher(){
        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 3) {  // new setlist added
                setlistAdapter = prepareAdapter();
                recView.setAdapter(setlistAdapter);
                setlistAdapter.notifyDataSetChanged();
            } else if (result.getResultCode() == 2) {   // setlist updated
                setlistAdapter = prepareAdapter();
                recView.setAdapter(setlistAdapter);
                setlistAdapter.notifyDataSetChanged();
            }
        });
    }
}
