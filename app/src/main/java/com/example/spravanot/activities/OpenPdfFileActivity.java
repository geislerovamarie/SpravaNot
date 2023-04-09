package com.example.spravanot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.spravanot.R;
import com.github.barteksc.pdfviewer.PDFView;

public class OpenPdfFileActivity extends AppCompatActivity {

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
            path = getIntent().getStringExtra("path");
            pdfView = findViewById(R.id.open_pdf_View);
            Uri uri = Uri.parse(path);
            openPdf(uri);

            // store as last opened in shared preferences
            SharedPreferences sharedPref = getSharedPreferences("LastFilePref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("last_opened", path);
            editor.apply();
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    void openPdf(Uri uri){
        pdfView.fromUri(uri)
                .enableSwipe(true)
            //    .pageSnap(true)
                .pageFling(true)
                .autoSpacing(true)
                .swipeHorizontal(true)
                .enableAnnotationRendering(true)
                .enableDoubletap(true)
                .load();
    }
}