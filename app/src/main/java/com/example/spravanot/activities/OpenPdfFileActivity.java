package com.example.spravanot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.spravanot.R;
import com.example.spravanot.utils.Mp3Player;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;
import java.util.ArrayList;

public class OpenPdfFileActivity extends AppCompatActivity {

    String path;
    PDFView pdfView;

    Context context;

    // mp3
    ImageButton optionsButton;
    ImageButton pause_play_button;
    SeekBar seekBar;
    MediaPlayer mediaPlayer = Mp3Player.getInstance();
    Uri mp3Uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf);
        init();
        getSheetmusicAndSetIntentData();
    }

    void init(){
        setContentView(R.layout.activity_open_pdf);
        context = this;
        //init
        pause_play_button = findViewById(R.id.open_pdf_pause_button);
        seekBar = findViewById(R.id.open_pdf_bar);
        optionsButton = findViewById(R.id.open_pdf_options);

        setOnClickListeners();
    }

    void getSheetmusicAndSetIntentData(){
        if(getIntent().hasExtra("path")){
            // get mp3 file
            if(getIntent().getExtras().containsKey("mp3") && getIntent().getExtras().getString("mp3") != null){
                String strUri = getIntent().getExtras().getString("mp3");
                mp3Uri = Uri.parse(strUri);
                Mp3Player.getInstance().reset();
            }

            // pdf
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

    void setOnClickListeners(){
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> options = new ArrayList<>();
                options.add(getString(R.string.dialog_hide));
                options.add(getString(R.string.dialog_show_mp3));
                options.add(getString(R.string.dialog_draw));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.options)
                        .setItems(options.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:
                                        hideEverything();
                                        break;
                                    case 1:
                                        if(mp3Uri != null){
                                            hideEverything();
                                            showMP3();
                                            playMusic();
                                            break;
                                        }else{
                                            Toast.makeText(context, R.string.no_mp3, Toast.LENGTH_SHORT).show();
                                        }

                                    case 2:
                                        hideEverything();
                                        showDraw();
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        pause_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausePlay();
            }
        });
    }

   // Visibility -------------------------------------------------
    void hideEverything(){
        // hide mp3
        if(mediaPlayer.isPlaying()) mediaPlayer.stop();
        pause_play_button.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);

        // hide draw
        //TODO
    }

    void showMP3(){
        pause_play_button.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
    }

    void showDraw(){
        //TODO
    }

// MP3 help -----------------------------------------------------

    void playMusic() {
        OpenPdfFileActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
                new Handler().postDelayed(this, 100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer != null && b) mediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this, mp3Uri);
            mediaPlayer.prepare();

            mediaPlayer.start();
            pause_play_button.setImageResource(R.drawable.ic_pause);
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    pause_play_button.setImageResource(R.drawable.ic_play);
                }
            });
        }catch (IOException e){
            Toast.makeText(context, R.string.no_mp3, Toast.LENGTH_SHORT).show();
        }
    }

    void pausePlay(){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            pause_play_button.setImageResource(R.drawable.ic_play);
        }else {
            mediaPlayer.start();
            pause_play_button.setImageResource(R.drawable.ic_pause);
        }
    }
}