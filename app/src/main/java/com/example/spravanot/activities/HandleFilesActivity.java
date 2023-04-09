package com.example.spravanot.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.spravanot.utils.DatabaseHelper;
import com.example.spravanot.adapters.HandleFilesAdapter;
import com.example.spravanot.interfaces.PassInfoSheetmusic;
import com.example.spravanot.R;
import com.example.spravanot.models.Sheetmusic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HandleFilesActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLaunch;
    PassInfoSheetmusic info;
    RecyclerView recView;
    HandleFilesAdapter filesAdapter;
    FloatingActionButton addButton;
    FloatingActionButton saveButton;

    Sheetmusic sheetmusic;
    ArrayList<String> addresses;
    DatabaseHelper db;

    String type;
    boolean modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_handle_files);
        setTitle(getResources().getString(R.string.text_files));

        setUpInfo();
        setUpLauncher();
        getAndSetDataFromParent();

        int visibility = modify ? View.VISIBLE : View.GONE;

        // set up buttons
        addButton = findViewById(R.id.add_files_button);
        addButton.setVisibility(visibility);
        saveButton = findViewById(R.id.save_files_button);
        saveButton.setVisibility(visibility);

        setOnClickListeners();

        filesAdapter = new HandleFilesAdapter(this, this, addresses, info, sheetmusic, modify);
        recView = findViewById(R.id.recyclerViewFiles);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(filesAdapter);
    }

    void setUpInfo(){
        // PassInfo - child activiry adapter tells this activity, which files should be removed
        info = new PassInfoSheetmusic() {
            @Override
            public void deleteSheetmusic(int position, int idSh) {
                addresses.remove(position);
                filesAdapter.notifyItemRemoved(position);
            }
            @Override
            public void updateSheetmusic(int position) {}   // not needed here
            @Override
            public void toggleFavorite(int position, int idSh) {}   // not needed here
            @Override
            public void deleteTag(int position, String name) {} // not needed here

            @Override
            public void addSheetmusicToSetlist(Sheetmusic s, boolean add) {}
        };
    }

    void setUpLauncher(){
        // Launcher for child activities
        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri uri = data.getData();
                String address = uri.toString();

                if(!addresses.contains(address))addresses.add(address);
                filesAdapter = new HandleFilesAdapter(this, this, addresses, info, sheetmusic, modify);
                recView.setAdapter(filesAdapter);
                filesAdapter.notifyDataSetChanged();
            }
        });
    }

    void getAndSetDataFromParent(){
        // set arguments from parent activity
        modify = getIntent().getExtras().getBoolean("modify");
        sheetmusic = (Sheetmusic) getIntent().getExtras().getSerializable("sheetmusic");
        type = getIntent().getExtras().getString("type");
        if(type.equals("pdf")){
            addresses = getIntent().getExtras().getStringArrayList("pdfs");
        }else if(type.equals("jpg")){
            addresses = getIntent().getExtras().getStringArrayList("jpgs");
        }
    }

    void setOnClickListeners(){
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            if(type.equals("pdf")){
                intent.setType("application/pdf");
            }else if(type.equals("jpg")){
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
            activityResultLaunch.launch(intent);
        });

        saveButton.setOnClickListener(view -> {
            if(type.equals("pdf")){
                Intent intent = new Intent();
                intent.putExtra("pdfs", addresses);
                setResult(5, intent);
                finish();
            }else if(type.equals("jpg")){
                Intent intent = new Intent();
                intent.putExtra("jpgs", addresses);
                setResult(6, intent);
                finish();
            }
        });
    }
}