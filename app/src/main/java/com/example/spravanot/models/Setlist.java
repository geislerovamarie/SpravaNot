package com.example.spravanot.models;

import java.util.ArrayList;

public class Setlist {

    private int id;
    private String name, notes;
    private ArrayList<String> tags;
    private ArrayList<Sheetmusic> sheetmusic;

    public Setlist(int id) {
        this.id = id;
        tags = new ArrayList<>();
        sheetmusic = new ArrayList<>();
    }

    // Getters and setters ------------------------------------------------------------

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

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

    public ArrayList<Sheetmusic> getSheetmusic() {
        return sheetmusic;
    }

    public void setSheetmusic(ArrayList<Sheetmusic> sheetmusic) {
        this.sheetmusic = sheetmusic;
    }
}
