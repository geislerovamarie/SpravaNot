package com.example.spravanot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.spravanot.adapters.JpgImageAdapter;
import com.example.spravanot.R;
import com.example.spravanot.utils.DrawView;
import com.example.spravanot.utils.FilterOptions;
import com.example.spravanot.utils.Mp3Player;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class OpenJpgFileActivity extends AppCompatActivity {

    ViewPager viewPager;
    Context context;
    int position;
    ArrayList<String> addresses;

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
        setContentView(R.layout.activity_open_jpg_file);
        context = this;

        init();
        setOnClickListeners();

        // get mp3 file
        if(getIntent().getExtras().containsKey("mp3") && getIntent().getExtras().getString("mp3") != null){
            String strUri = getIntent().getExtras().getString("mp3");
            mp3Uri = Uri.parse(strUri);
            Mp3Player.getInstance().reset();
        }

        JpgImageAdapter adapter = new JpgImageAdapter(this, addresses);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    void init(){
        //init mp3
        pause_play_button = findViewById(R.id.open_jpg_pause_button);
        seekBar = findViewById(R.id.open_jpg_bar);

        //init draw
        paint = findViewById(R.id.open_jpg_draw_view);
        undo = findViewById(R.id.open_jpg_undo_button);

        viewPager = findViewById(R.id.open_jpg_viewpager);
        optionsButton = findViewById(R.id.open_jpg_options);
        addresses = getIntent().getExtras().getStringArrayList("addresses"); // uris
        position = getIntent().getExtras().getInt("position");

        drawInit();
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
        // mp3
        pause_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausePlay();
            }
        });

        // draw
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.undo();
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
        OpenJpgFileActivity.this.runOnUiThread(new Runnable() {
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
}