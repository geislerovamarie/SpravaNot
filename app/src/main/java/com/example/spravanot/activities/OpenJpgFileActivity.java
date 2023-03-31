package com.example.spravanot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.spravanot.adapters.JpgImageAdapter;
import com.example.spravanot.R;

import java.util.ArrayList;

public class OpenJpgFileActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_jpg_file);

        viewPager = findViewById(R.id.open_jpg_viewpager);
        ArrayList<String> addresses = getIntent().getExtras().getStringArrayList("addresses"); // uris
        int position = getIntent().getExtras().getInt("position");

        JpgImageAdapter adapter = new JpgImageAdapter(this, addresses);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}