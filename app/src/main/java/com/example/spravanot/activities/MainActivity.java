package com.example.spravanot.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.spravanot.R;
import com.example.spravanot.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_sheets, R.id.navigation_setlists)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        setSettings();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSettings();
    }


// MENU ---------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.start_settings){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

// SETTINGS -----------------------------------------------------------------------------------

    void setSettings(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        // display
        boolean display_on_pref = sp.getBoolean("display_on_off", false);
        displaySetting(display_on_pref);

        // language
        String language = sp.getString("language","");
        setLanguage(language);

        // dark mode
        boolean dark_mode = sp.getBoolean("dark_mode", false);
        setMode(dark_mode);
    }

    void displaySetting(boolean display_on_pref){
        if(display_on_pref){
            // Toast.makeText(this, "The display should stay on", Toast.LENGTH_SHORT).show();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            // Toast.makeText(this, "The display should turn off eventually", Toast.LENGTH_SHORT).show();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    void setLanguage(String language){
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(language);
        AppCompatDelegate.setApplicationLocales(appLocale);
        // Toast.makeText(this, "language: " + language, Toast.LENGTH_SHORT).show();
    }

    void setMode(boolean dark_mode){
        if(dark_mode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

}