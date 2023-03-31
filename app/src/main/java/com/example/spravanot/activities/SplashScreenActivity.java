package com.example.spravanot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.spravanot.R;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // immersive mode - hide bars
        Objects.requireNonNull(getSupportActionBar()).hide(); // if theme changes to ..something.NoActionBar, then delete this
        WindowInsetsControllerCompat wicc = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        wicc.hide(WindowInsetsCompat.Type.systemBars());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 900);
    }
}