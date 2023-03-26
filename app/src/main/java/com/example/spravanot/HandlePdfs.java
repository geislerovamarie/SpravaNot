package com.example.spravanot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HandlePdfs extends AppCompatActivity {

    RecyclerView recView;
    HandlePdfsAdapter pdfsAdapter;
    FloatingActionButton addButton;
    FloatingActionButton saveButton;

    ArrayList<String> addresses;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_handle_pdfs);

        addresses = getIntent().getExtras().getStringArrayList("pdfs");

        addButton = findViewById(R.id.add_pdfs_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // modify addresses
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


        pdfsAdapter = new HandlePdfsAdapter(this, this, addresses);
        recView = findViewById(R.id.recyclerViewPdfs);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(pdfsAdapter);
    }
}