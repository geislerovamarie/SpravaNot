package com.example.spravanot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

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
            path = getIntent().getStringExtra("path");
            pdfView = (PDFView) findViewById(R.id.open_pdf_View);
            Uri uri = Uri.parse(path);
            openPdf(uri);
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