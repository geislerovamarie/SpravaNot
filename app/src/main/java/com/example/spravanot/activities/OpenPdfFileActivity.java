package com.example.spravanot.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spravanot.R;
import com.example.spravanot.utils.DrawView;
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

    // draw
    DrawView paint;
    ImageButton undo;

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

        //init mp3
        pause_play_button = findViewById(R.id.open_pdf_pause_button);
        seekBar = findViewById(R.id.open_pdf_bar);
        optionsButton = findViewById(R.id.open_pdf_options);

        //init draw
        paint = findViewById(R.id.open_pdf_draw_view);
        undo = findViewById(R.id.open_pdf_undo_button);
        drawInit();

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
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show(); // should not happen
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
        optionsButton.setOnClickListener(view -> {
            ArrayList<String> options = new ArrayList<>();
            options.add(getString(R.string.dialog_hide));
            options.add(getString(R.string.dialog_show_mp3));
            options.add(getString(R.string.dialog_draw));

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.options)
                    .setItems(options.toArray(new String[0]), (dialogInterface, i) -> {
                        switch (i){
                            case 1:
                                if(mp3Uri != null){
                                    hideEverything();
                                    showMP3();
                                    playMusic();
                                }else{
                                    Toast.makeText(context, R.string.no_mp3, Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case 2:
                                hideEverything();
                                showDraw();
                                break;
                            default:
                                hideEverything();
                                break;
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        pause_play_button.setOnClickListener(view -> pausePlay());

        // draw
        undo.setOnClickListener(view -> paint.undo());
    }

    // Draw -----------------------------------------------------
    void drawInit(){
        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int w = paint.getMeasuredWidth();
                int h = paint.getMeasuredHeight();
                paint.init(h, w);
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
        undo.setVisibility(View.GONE);
        paint.setVisibility(View.GONE);
    }

    void showMP3(){
        pause_play_button.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
    }

    void showDraw(){
        undo.setVisibility(View.VISIBLE);
        paint.setVisibility(View.VISIBLE);
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