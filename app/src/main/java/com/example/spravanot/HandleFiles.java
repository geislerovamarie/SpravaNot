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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HandleFiles extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLaunch;
    RecyclerView recView;
    HandleFilesAdapter filesAdapter;
    FloatingActionButton addButton;
    FloatingActionButton saveButton;

    ArrayList<String> addresses;
    DatabaseHelper db;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_handle_files);

        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // todo
                Intent data = result.getData();
                Uri uri = data.getData();
                String address = uri.getPath();
                if(!addresses.contains(address))addresses.add(address);

                filesAdapter = new HandleFilesAdapter(this, this, addresses);
                recView.setAdapter(filesAdapter);
                filesAdapter.notifyDataSetChanged();
            }
        });

        type = getIntent().getExtras().getString("type");
        if(type.equals("pdf")){
            addresses = getIntent().getExtras().getStringArrayList("pdfs");
        }else if(type.equals("jpg")){
            addresses = getIntent().getExtras().getStringArrayList("jpgs");
        }

        addButton = findViewById(R.id.add_files_button);
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

        filesAdapter = new HandleFilesAdapter(this, this, addresses);
        recView = findViewById(R.id.recyclerViewFiles);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(filesAdapter);
    }
}