package com.example.spravanot.utils;

import android.media.MediaPlayer;

public class Mp3Player {

    static MediaPlayer instance;

    public static MediaPlayer getInstance(){
        if(instance == null) instance = new MediaPlayer();
        return instance;
    }
}
