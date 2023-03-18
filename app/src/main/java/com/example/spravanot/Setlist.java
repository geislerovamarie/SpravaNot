package com.example.spravanot;

import java.util.ArrayList;

public class Setlist {

    private int id;
    private String name, notes;
    private ArrayList<String> tags;
    private ArrayList<Sheetmusic> setlists;

    public Setlist(int id) {
        this.id = id;
        tags = new ArrayList<>();
        setlists = new ArrayList<>();
    }

    // Getters and setters ------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<Sheetmusic> getSetlists() {
        return setlists;
    }

    public void setSetlists(ArrayList<Sheetmusic> setlists) {
        this.setlists = setlists;
    }
}
