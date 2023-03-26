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
    HandleFilesAdapter pdfsAdapter;
    FloatingActionButton addButton;
    FloatingActionButton saveButton;

    ArrayList<String> addresses;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_handle_pdfs);

        activityResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // todo
                Intent data = result.getData();
                Uri uri = data.getData();
                String address = uri.getPath();
                if(!addresses.contains(address))addresses.add(address);

                pdfsAdapter = new HandleFilesAdapter(this, this, addresses);
                recView.setAdapter(pdfsAdapter);
                pdfsAdapter.notifyDataSetChanged();
            }
        });

        addresses = getIntent().getExtras().getStringArrayList("pdfs");

        addButton = findViewById(R.id.add_pdfs_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                activityResultLaunch.launch(intent);
            }
        });

        saveButton = findViewById(R.id.save_pdfs_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent = new Intent();
                intent.putExtra("pdfs", addresses);
                setResult(5, intent);
                finish();
            }
        });

        pdfsAdapter = new HandleFilesAdapter(this, this, addresses);
        recView = findViewById(R.id.recyclerViewPdfs);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(pdfsAdapter);
    }
}