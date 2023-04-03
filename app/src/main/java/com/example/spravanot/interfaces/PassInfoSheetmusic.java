package com.example.spravanot.interfaces;

import com.example.spravanot.models.Sheetmusic;

public interface PassInfoSheetmusic {
    void deleteSheetmusic(int position, int idSh);

    void updateSheetmusic(int position);

    void toggleFavorite(int position, int idSh);

    void deleteTag(int position, String name);

    void addSheetmusicToSetlist(Sheetmusic s, boolean add);
}