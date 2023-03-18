package com.example.spravanot;

/*
* Todo:
*
*
* */

import java.util.ArrayList;

public class Sheetmusic {

    private int id;
    private String name, author, genre, key, instument, notes;
    private String mp3; // address or .mp3 or something else?
    private ArrayList<String> files; // files with sheet music (pdf/jpg)
    private ArrayList<String> tags;

    public Sheetmusic(int id) {
        this.id = id;
        files = new ArrayList<>();
        tags = new ArrayList<>();
    }




    // Getters and setters ------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInstument() {
        return instument;
    }

    public void setInstument(String instument) {
        this.instument = instument;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
