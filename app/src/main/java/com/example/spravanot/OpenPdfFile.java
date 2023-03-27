package com.example.spravanot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

public class OpenPdfFile extends AppCompatActivity {

    String path;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf);
        getSheetmusicAndSetIntentData();
    }

    void getSheetmusicAndSetIntentData(){
        if(getIntent().hasExtra("path")){

            // Get

            path = getIntent().getStringExtra("path");
            Toast.makeText(this, path + " is being shown", Toast.LENGTH_SHORT).show();
            // Set
            // example:
            // title_input.setText(title);

        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}