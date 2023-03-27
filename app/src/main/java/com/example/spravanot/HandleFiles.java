package com.example.spravanot;

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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HandleFiles extends AppCompatActivity {

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

        info = new PassInfoSheetmusic() {
            @Override
            public void deleteSheetmusic(int position, int idSh) {
                addresses.remove(position);
                filesAdapter.notifyItemRemoved(position);
            }

            @Override
            public void toggleFavorite(int position, int idSh) {
                // create setlist "Favorite" if it doesn't exist
            }
        };

        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri uri = data.getData();
                String address = uri.getPath();
                if(!addresses.contains(address))addresses.add(address);

                filesAdapter = new HandleFilesAdapter(this, this, addresses, info, sheetmusic);
                recView.setAdapter(filesAdapter);
                filesAdapter.notifyDataSetChanged();
            }
        });

        modify = getIntent().getExtras().getBoolean("modify");
        int visibility = modify ? View.VISIBLE : View.GONE;
        sheetmusic = (Sheetmusic) getIntent().getExtras().getSerializable("sheetmusic");

     //!!!!!!!!!!!!!
       // Toast.makeText(this, sheetmusic.getName(), Toast.LENGTH_SHORT).show();


        type = getIntent().getExtras().getString("type");
        if(type.equals("pdf")){
            addresses = getIntent().getExtras().getStringArrayList("pdfs");
        }else if(type.equals("jpg")){
            addresses = getIntent().getExtras().getStringArrayList("jpgs");
        }

        addButton = findViewById(R.id.add_files_button);
        addButton.setVisibility(visibility);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                if(type.equals("pdf")){
                    intent.setType("application/pdf");
                }else if(type.equals("jpg")){
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
                activityResultLaunch.launch(intent);
            }
        });

        saveButton = findViewById(R.id.save_files_button);
        saveButton.setVisibility(visibility);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        filesAdapter = new HandleFilesAdapter(this, this, addresses, info, sheetmusic);
        recView = findViewById(R.id.recyclerViewFiles);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(filesAdapter);
    }
}