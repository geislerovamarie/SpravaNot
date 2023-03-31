package com.example.spravanot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.spravanot.R;
import com.example.spravanot.models.Sheetmusic;

public class EditSheetmusicActivity extends AppCompatActivity {

    Sheetmusic sheetmusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sheetmusic);
        getSheetmusicAndSetIntentData();
    }

    void getSheetmusicAndSetIntentData(){
        if(getIntent().hasExtra("sheetmusic")){

            // Get
            sheetmusic = (Sheetmusic) getIntent().getSerializableExtra("sheetmusic");
            Toast.makeText(this, sheetmusic.getName() + " is being edited", Toast.LENGTH_SHORT).show();

            // Set
            // example:
            // title_input.setText(title);
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}